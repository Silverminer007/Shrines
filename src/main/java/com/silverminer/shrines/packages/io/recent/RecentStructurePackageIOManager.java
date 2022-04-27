/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.io.recent;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.packages.container.*;
import com.silverminer.shrines.packages.datacontainer.*;
import com.silverminer.shrines.packages.io.DirectoryStructureAccessor;
import com.silverminer.shrines.packages.io.PackageIOException;
import com.silverminer.shrines.packages.io.StructurePackageLoader;
import com.silverminer.shrines.packages.io.StructurePackageSaver;
import com.silverminer.shrines.utils.CalculationError;
import com.silverminer.shrines.utils.ZIPUtils;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class RecentStructurePackageIOManager implements StructurePackageLoader, StructurePackageSaver {
   private static final HashMap<UUID, String> packageID_SaveDirectory = new HashMap<>();
   private static final HashMap<UUID, List<StructureTemplate>> templateID_SaveName = new HashMap<>();
   private final RecentDirectoryStructureAccessor directoryStructureAccessor = new RecentDirectoryStructureAccessor();
   // Make these two above static, so multiple threads don't matter. -> Instances don't need to be stable

   @Override
   public void savePackages(StructurePackageContainer structurePackages) throws PackageIOException {
      // What to do with removed or renamed elements?
      // Option one: Compare saved data with modified data and only remove changed files and then save everything -> Huge bunch of work, Lesser IO stressing, No extra data is deleted
      // Option two: Remove the whole Packages directory and then save everything. -> More easy, needs extra attention to templates, because we do not load the bunch of data
      // Option three: compare package differences (just package directories) and delete all directory that doesn't belong to a package -> stable directory ids to identify packages
      // Clear structures and pools directory and save the ones that our runtime data has. Compare templates and delete removed ones. Rename is requires extra attention!
      // Probably I'm going to choose option three, because it combines option one and two and their advantages

      // Before anything else, we should create the base directory. That is important to mark that we've already used this format and don't want to fall back to legacy formats
      if (Files.notExists(this.getDirectoryStructureAccessor().getPackagesPath())) {
         try {
            Files.createDirectories(this.getDirectoryStructureAccessor().getPackagesPath());
         } catch (IOException e) {
            throw new PackageIOException(new CalculationError("Failed to save packages", "Unable to create packages directory. Caused by: %s", e));
         }
      }
      // We need one file to identify that we have already read packages in this format one time, so let's create a README with link to our wiki
      try {
         Files.writeString(this.getDirectoryStructureAccessor().getPackagesPath().resolve("README.txt"), ShrinesMod.WIKI_LINK);
      } catch (IOException e) {
         throw new PackageIOException(new CalculationError("Failed to write README.txt", "Unable to write File. Caused by: %s", e));
      }
      // First delete All removed packages
      try {
         // These packages existed while last loading
         List<UUID> packageIDsLoad = new ArrayList<>(this.getPackageID_SaveDirectory().keySet());// We need to prevent changes on the original map, so we're forced to copy the set
         List<UUID> packagesIDsSave = structurePackages.getAsStream().map(StructuresPackageWrapper::getPackageID).toList();// These packages want to be saved. These and only these
         packageIDsLoad.removeAll(packagesIDsSave);// If we remove all packages here that still exists, only the packages that were removed persist
         for (UUID deletedPackageID : packageIDsLoad) {
            Path packagePath = this.getDirectoryStructureAccessor().getPackagesPath().resolve(this.getPackageID_SaveDirectory().get(deletedPackageID));
            if (Files.isDirectory(packagePath)) {
               // Deletes the directory recursively
               this.deleteDirectoryRecursively(packagePath);
            }
            // Mark that package as removed, no matter if the package truly needed to be removed, because we don't need to try to remove it all the time if it doesn't exist anymore
            this.getPackageID_SaveDirectory().remove(deletedPackageID);
         }
      } catch (IOException e) {
         throw new PackageIOException(new CalculationError("Failed to save packages", "Failed to delete packages, that we're removed by the user. Caused by: %s", e));
      }

      // Now save remaining packages
      for (StructuresPackageWrapper structuresPackageWrapper : structurePackages.getAsIterable()) {
         if (structuresPackageWrapper != null) {
            this.savePackage(structuresPackageWrapper);
         }
      }
   }

   @Override
   public byte[] exportPackage(StructuresPackageWrapper structuresPackageWrapper) throws PackageIOException {
      if (this.getPackageID_SaveDirectory().containsKey(structuresPackageWrapper.getPackageID())) {
         Path packagesPath = this.getDirectoryStructureAccessor().getPackagesPath().resolve(this.getPackageID_SaveDirectory().get(structuresPackageWrapper.getPackageID()));
         try {
            return ZIPUtils.zipDirectory(packagesPath, this.getDirectoryStructureAccessor().getExportCachePath());
         } catch (IOException e) {
            throw new PackageIOException(new CalculationError("Failed to export package", "Failed to create zip archive. Caused by: %s", e));
         }
      } else {
         throw new PackageIOException(new CalculationError("Failed to export package", "The package wasn't saved before"));
      }
   }

   protected HashMap<UUID, String> getPackageID_SaveDirectory() {
      return RecentStructurePackageIOManager.packageID_SaveDirectory;
   }

   @Override
   public DirectoryStructureAccessor getDirectoryStructureAccessor() {
      return this.directoryStructureAccessor;
   }

   @Override
   public boolean matchesFormat() {
      return Files.exists(this.getDirectoryStructureAccessor().getPackagesPath().resolve("README.txt"));
   }

   @Override
   public StructurePackageContainer loadPackages() throws PackageIOException {
      return this.loadPackages(false);
   }

   @Override
   public StructurePackageContainer earlyLoadPackages() throws PackageIOException {
      return this.loadPackages(true);
   }

   @Override
   public boolean canLoadStructureIcons() {
      return true;
   }

   @Override
   public StructureIconContainer loadStructureIcons(StructurePackageContainer packageContainer) throws PackageIOException {
      StructureIconContainer structureIconContainer = new StructureIconContainer();
      for (StructuresPackageWrapper structuresPackageWrapper : packageContainer.getAsIterable()) {
         for (StructureData d : structuresPackageWrapper.getStructures().getAsIterable()) {
            ResourceLocation key = new ResourceLocation(d.getKey() + ".png");
            if (!this.getPackageID_SaveDirectory().containsKey(structuresPackageWrapper.getPackageID())) {
               continue;
            }
            Path iconPath = this.getDirectoryStructureAccessor().getStructureIconPath(key, this.getPackageID_SaveDirectory().get(structuresPackageWrapper.getPackageID()), true);
            if (Files.exists(iconPath) && Files.isRegularFile(iconPath)) {
               try {
                  structureIconContainer.add(key, Files.readAllBytes(iconPath));
               } catch (IOException e) {
                  throw new PackageIOException(new CalculationError("Failed to read structure icon", "Caused by: %s", e));
               }
            }
         }
      }
      return structureIconContainer;
   }

   @Override
   public StructuresPackageWrapper tryImportPackage(Path source) {
      try {
         StructuresPackageWrapper structuresPackageWrapper = this.loadStructurePackage(source, false);
         Path packagePath = this.getDirectoryStructureAccessor().getBasePath()
               .resolve(this.getDirectoryStructureAccessor().findUnusedPackageFilename(structuresPackageWrapper.getStructuresPacketInfo().getDisplayName(), "structures package"));
         Files.copy(source, packagePath);
         this.getPackageID_SaveDirectory().put(structuresPackageWrapper.getPackageID(), packagePath.getFileName().toString());
         return structuresPackageWrapper;
      } catch (PackageIOException | IOException e) {
         return null;
      }
   }

   protected StructurePackageContainer loadPackages(boolean earlyLoad) throws PackageIOException {
      this.getPackageID_SaveDirectory().clear();
      StructurePackageContainer structurePackageContainer = new StructurePackageContainer();

      if (Files.isDirectory(this.getDirectoryStructureAccessor().getPackagesPath())) {
         try {
            for (Path path : Files.find(this.getDirectoryStructureAccessor().getPackagesPath(), 1, (path, basicFileAttributes) -> Files.isDirectory(path)).toList()) {
               try {
                  if (Files.isSameFile(path, this.getDirectoryStructureAccessor().getPackagesPath())) {
                     continue;
                  }
               } catch (IOException e) {
                  throw new PackageIOException(new CalculationError("Failed to load structure packages", "Failed to compare current path with directory structure. Caused by: %s", e));
               }
               StructuresPackageWrapper structuresPackageWrapper = this.loadStructurePackage(path, earlyLoad);
               if (structuresPackageWrapper != null) {
                  this.getPackageID_SaveDirectory().put(structuresPackageWrapper.getPackageID(), path.getFileName().toString());
                  structurePackageContainer.add(structuresPackageWrapper);
               }
            }
         } catch (IOException e) {
            throw new PackageIOException(new CalculationError("Unable to load structure packages from disk", "Caused by: %s", e));
         }
         return structurePackageContainer;
      }
      throw new PackageIOException(new CalculationError("Unable to load structure packages from disk", "Package Save Directory doesn't exist"));
   }

   protected StructuresPackageWrapper loadStructurePackage(Path source, boolean earlyLoad) throws PackageIOException {
      UUID packageID = UUID.randomUUID();
      StructuresPackageInfo structuresPackageInfo = this.loadStructurePackageInfo(source);
      StructureDataContainer structures = this.loadStructures(source);
      StructureTemplateContainer structureTemplates = earlyLoad ? new StructureTemplateContainer() : this.loadStructureTemplates(source, packageID);
      TemplatePoolContainer templatePools = earlyLoad ? new TemplatePoolContainer() : this.loadStructureTemplatePools(source);
      if (Objects.isNull(structuresPackageInfo) || Objects.isNull(structureTemplates) || Objects.isNull(templatePools)) {
         return null;
      }
      return new StructuresPackageWrapper(structuresPackageInfo, structures, structureTemplates, templatePools, packageID);
   }

   protected StructuresPackageInfo loadStructurePackageInfo(Path packagePath) throws PackageIOException {
      Path jsonPackageInfoPath = packagePath.resolve("package-info.json");
      if (Files.isRegularFile(jsonPackageInfoPath)) {
         JsonElement jsonPackageInfoElement;
         try {
            jsonPackageInfoElement = JsonParser.parseString(Files.readString(jsonPackageInfoPath));
         } catch (IOException e) {
            throw new PackageIOException(new CalculationError("Failed to load structure package info", "Failed to read JSOn file. Caused by: %s", e));
         }
         if (jsonPackageInfoElement != null) {
            DataResult<Pair<StructuresPackageInfo, JsonElement>> dataResult = StructuresPackageInfo.CODEC.decode(JsonOps.INSTANCE, jsonPackageInfoElement);
            Optional<Pair<StructuresPackageInfo, JsonElement>> pairOptional = dataResult.result();
            if (pairOptional.isPresent()) {
               return pairOptional.get().getFirst();
            } else {
               throw new PackageIOException(new CalculationError("Failed to parse structure package info", "Caused by: %s", dataResult.error().map(DataResult.PartialResult::message)));
            }
         }
      }
      throw new PackageIOException(new CalculationError("Failed to load structure package info", "Package info file doesn't exist"));
   }

   protected StructureDataContainer loadStructures(Path packagePath) throws PackageIOException {
      Path data = packagePath.resolve("data");
      StructureDataContainer structures = new StructureDataContainer();
      // It's a valid case that no structure exist, so we just return an empty list
      if (Files.isDirectory(data)) {
         try {
            for (Path namespacePath : this.findDataNamespaces(packagePath)) {
               try {
                  // Create the template pools base directory for the current namespace
                  Path poolsPath = namespacePath.resolve("shrines_structures");
                  if (Files.isDirectory(poolsPath)) {
                     for (Path poolPath : Files.find(poolsPath, Integer.MAX_VALUE, (p, basicFileAttributes) -> Files.isRegularFile(p) && p.toString().endsWith(".json")).toList()) {
                        try {
                           String structureFileString = Files.readString(poolPath);
                           JsonElement structureFileObject = JsonParser.parseString(structureFileString);
                           StructureData structureData = StructureData.loadFromJSON(structureFileObject);
                           if (structureData != null) {
                              structureData.setKey(new ResourceLocation(namespacePath.getFileName().toString(), poolsPath.relativize(poolPath).toString().replaceAll(".json", "")));
                              structures.add(structureData);
                           }
                        } catch (IOException e) {
                           throw new PackageIOException(new CalculationError("Unable to load structure", "Phase: Load and parse structure file. Caused by: %S", e));
                        }
                     }
                  }
               } catch (IOException e) {
                  throw new PackageIOException(new CalculationError("Unable to load structure", "Phase: Gather structures path. Caused by: %s", e));
               }
            }
         } catch (IOException e) {
            throw new PackageIOException(new CalculationError("Unable to load structure", "Phase: Gather namespaces. Caused by: %s", e));
         }
      }
      return structures;
   }

   protected StructureTemplateContainer loadStructureTemplates(Path packagePath, UUID packageID) throws PackageIOException {
      Path data = packagePath.resolve("data");
      StructureTemplateContainer templates = new StructureTemplateContainer();
      // For structure templates it's a valid case that no template exists. Just return an empty list if the path doesn't exist
      if (Files.isDirectory(data)) {
         try {
            for (Path namespacePath : this.findDataNamespaces(packagePath)) {
               try {
                  // Create the templates base directory for the current namespace
                  Path poolsPath = namespacePath.resolve("structures");
                  if (Files.isDirectory(poolsPath)) {
                     for (Path currentPath : Files.find(poolsPath, Integer.MAX_VALUE, (p, basicFileAttributes) -> Files.isRegularFile(p) && p.toString().endsWith(".nbt")).toList()) {
                        // ResourceLocations use relative paths, so we make it relative to the data main directory
                        Path relativeTemplate = data.relativize(currentPath).normalize();
                        // Template paths must have at least a subdirectory depth of 3 (<namespace>/structures/<filename>)
                        if (relativeTemplate.getNameCount() >= 3) {
                           // Get the namespace of the current template. namespace is the first directory in data, so we grant that element by index 0
                           String namespace = relativeTemplate.getName(0).toString();
                           // Now get the relative path of the structure template based on the general template directory of that namespace
                           String locationPath = data.relativize(poolsPath).relativize(relativeTemplate).toString();
                           // Create the ResourceLocation to the structure template based on the relative path above. resourceLocations shouldn't have extensions, so we remove the '.nbt' extension
                           ResourceLocation template = new ResourceLocation(namespace, locationPath.replace(".nbt", ""));
                           StructureTemplate structureTemplate = new StructureTemplate(template);
                           templates.add(structureTemplate);
                           this.getTemplateID_SaveName().computeIfAbsent(packageID, key -> new ArrayList<>()).add(structureTemplate);
                        }
                     }
                  }
               } catch (IOException e) {
                  throw new PackageIOException(new CalculationError("Unable to load template", "Phase: Gather templates path. Caused by: %s", e));
               }
            }
         } catch (IOException e) {
            throw new PackageIOException(new CalculationError("Unable to read structure templates", "Caused by: %s", e));
         }
      }
      return templates;
   }

   protected TemplatePoolContainer loadStructureTemplatePools(Path packagePath) throws PackageIOException {
      Path data = packagePath.resolve("data");
      TemplatePoolContainer templates = new TemplatePoolContainer();
      // It's a valid case that no structure template pools exist, so we just return an empty list
      if (Files.isDirectory(data)) {
         try {
            for (Path namespacePath : this.findDataNamespaces(packagePath)) {
               try {
                  // Create the template pools base directory for the current namespace
                  Path poolsPath = namespacePath.resolve("worldgen").resolve("template_pool");
                  if (Files.isDirectory(poolsPath)) {
                     for (Path poolPath : Files.find(poolsPath, Integer.MAX_VALUE, (p, basicFileAttributes) -> Files.isRegularFile(p) && p.toString().endsWith(".json")).toList()) {
                        try {
                           TemplatePool pool = TemplatePool.fromString(Files.readString(poolPath));
                           pool.setSaveName(new ResourceLocation(namespacePath.getFileName().toString(), poolsPath.relativize(poolPath).toString().replaceAll(".json", "")));
                           templates.add(pool);
                        } catch (IOException e) {
                           throw new PackageIOException(new CalculationError("Unable to load template pools", "Phase: Load and parse template pool. Caused by: %s", e));
                        }
                     }
                  }
               } catch (IOException e) {
                  throw new PackageIOException(new CalculationError("Unable to load template pools", "Phase: Gather pools path. Caused by: %s", e));
               }
            }
         } catch (IOException e) {
            throw new PackageIOException(new CalculationError("Unable to load template pools", "Phase: Gather namespaces. Caused by: %s", e));
         }
      }
      return templates;
   }

   protected List<Path> findDataNamespaces(Path packagePath) throws IOException {
      Path dataPath = packagePath.resolve("data");
      if (!Files.exists(dataPath)) {
         return new ArrayList<>();
      }
      return Files.find(dataPath, 1, ((path1, basicFileAttributes) -> {
         try {
            return Files.isDirectory(path1) && !Files.isSameFile(dataPath, path1);
         } catch (IOException e) {
            return false;
         }
      })).toList();
   }

   protected HashMap<UUID, List<StructureTemplate>> getTemplateID_SaveName() {
      return RecentStructurePackageIOManager.templateID_SaveName;
   }

   protected void deleteDirectoryRecursively(Path directory) throws IOException {
      if (Files.exists(directory)) {
         Files.walkFileTree(directory, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
               Files.delete(file);
               return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
               Files.delete(dir);
               return FileVisitResult.CONTINUE;
            }
         });
      }
   }

   protected void savePackage(@NotNull StructuresPackageWrapper structuresPackageWrapper) throws PackageIOException {
      Path packagePath = this.getDirectoryStructureAccessor().getPackagesPath();
      if (this.getPackageID_SaveDirectory().containsKey(structuresPackageWrapper.getPackageID())) {
         packagePath = packagePath.resolve(this.getPackageID_SaveDirectory().get(structuresPackageWrapper.getPackageID()));
      } else {
         packagePath = packagePath.resolve(this.getDirectoryStructureAccessor().findUnusedPackageFilename(structuresPackageWrapper.getStructuresPacketInfo().getDisplayName(), "structures package"));
      }
      try {
         if (!Files.isDirectory(packagePath)) {
            Files.createDirectories(packagePath);
         }
      } catch (IOException e) {
         throw new PackageIOException(new CalculationError("Failed to save structure packages", "Can't create package's directory. Caused by: %s"));
      }
      // Before any data is saved, we should save the package's info
      this.saveStructurePackageInfo(structuresPackageWrapper.getStructuresPacketInfo(), packagePath);
      // Save structures and pools afterwards. They have the smallest effort
      // First delete the structures and pools directories and then recreate it. That makes it much easier to delete removes structures/pools, and we don't need to do any expensive Comparators
      this.clearStructuresAndPoolsDirectories(packagePath);
      this.saveStructuresContainer(structuresPackageWrapper.getStructures(), packagePath);
      this.saveTemplatePoolContainer(structuresPackageWrapper.getPools(), packagePath);
      // Then save templates. We should save this in the end, because here is the hugest potential of errors, and we need to make sure that most things are already saved and aren't lost
      this.saveStructureTemplates(structuresPackageWrapper.getTemplates(), packagePath, structuresPackageWrapper.getPackageID());
      // Finally, add the new package's name to the map
      this.getPackageID_SaveDirectory().put(structuresPackageWrapper.getPackageID(), packagePath.getFileName().toString());
   }

   protected void saveStructurePackageInfo(StructuresPackageInfo structuresPackageInfo, @NotNull Path packagePath) throws PackageIOException {
      Path packageInfoPath = packagePath.resolve("package-info.json");
      DataResult<JsonElement> dataResult = StructuresPackageInfo.CODEC.encode(structuresPackageInfo, JsonOps.INSTANCE, new JsonObject());
      Optional<JsonElement> optionalJsonElement = dataResult.result();
      if (optionalJsonElement.isPresent()) {
         JsonElement jsonPackageInfo = optionalJsonElement.get();
         try {
            Files.writeString(packageInfoPath, jsonPackageInfo.toString());
         } catch (IOException e) {
            throw new PackageIOException(new CalculationError("Unable to save structure package info", "Phase: Save json package info. Caused by: %s", e));
         }
      } else {
         Optional<String> error = dataResult.error().map(DataResult.PartialResult::message);
         if (error.isPresent()) {
            throw new PackageIOException(new CalculationError("Unable to save structure package info", "Failed to encode data. Caused by: %s", error.get()));
         } else {
            throw new PackageIOException(new CalculationError("Unable to save structure package info", "Failed to encode data for unknown reason"));
         }
      }
      this.saveStructurePackageMeta(packagePath);
   }

   protected void clearStructuresAndPoolsDirectories(Path packagePath) throws PackageIOException {
      try {
         for (Path namespace : this.findDataNamespaces(packagePath)) {
            Path structureDirectoryPath = this.getDirectoryStructureAccessor().getStructuresPath(new ResourceLocation(namespace.getFileName().toString(), ""), packagePath, false);
            this.deleteDirectoryRecursively(structureDirectoryPath);
            Path poolsDirectoryPath = this.getDirectoryStructureAccessor().getTemplatePoolPath(new ResourceLocation(namespace.getFileName().toString(), ""), packagePath, false);
            this.deleteDirectoryRecursively(poolsDirectoryPath);
         }
      } catch (IOException e) {
         throw new PackageIOException(new CalculationError("Failed to save Packages", "Failed to clear structures and pools directories. Caused by: %s", e));
      }
   }

   protected void saveStructuresContainer(@NotNull StructureDataContainer structureDataContainer, Path packagePath) throws PackageIOException {
      for (StructureData structureData : structureDataContainer.getAsIterable()) {
         this.saveStructure(structureData, packagePath);
      }
   }

   protected void saveTemplatePoolContainer(@NotNull TemplatePoolContainer templatePoolContainer, Path packagePath) throws PackageIOException {
      for (TemplatePool templatePool : templatePoolContainer.getAsIterable()) {
         this.saveTemplatePool(templatePool, packagePath);
      }
   }

   protected void saveStructureTemplates(StructureTemplateContainer templates, Path packagePath, UUID packageID) throws PackageIOException {
      // First rename/remove templates. That prevents deletions of new templates because of errors
      List<StructureTemplate> templatesFromLoad = this.getTemplateID_SaveName().getOrDefault(packageID, new ArrayList<>());
      List<UUID> templateIDsOnSave = templates.getKeysAsList();
      for (StructureTemplate structureTemplate : templatesFromLoad) {
         if (templateIDsOnSave.contains(structureTemplate.getTemplateID())) {
            StructureTemplate saveTemplate = templates.getElementByKey(structureTemplate.getTemplateID());
            // If the locations are equals, we don't need to do anything, because nothing changed
            if (!saveTemplate.getTemplateLocation().equals(structureTemplate.getTemplateLocation())) {
               // Different locations -> Move template to the new path
               try {
                  Path oldPath = this.getDirectoryStructureAccessor().getTemplatePath(structureTemplate.getTemplateLocation(), packagePath, true);
                  Path newPath = this.getDirectoryStructureAccessor().getTemplatePath(saveTemplate.getTemplateLocation(), packagePath, true);
                  Files.move(oldPath, newPath);
               } catch (IOException e) {
                  throw new PackageIOException(new CalculationError("Failed to save packages", "Failed to rename templates. Caused by: %s", e));
               }
            }
         } else {
            // The template doesn't exist anymore -> Delete the file
            try {
               Path templatePath = this.getDirectoryStructureAccessor().getTemplatePath(structureTemplate.getTemplateLocation(), packagePath, true);
               Files.delete(templatePath);
            } catch (IOException e) {
               throw new PackageIOException(new CalculationError("Failed to save packages", "Failed to delete removed templates. Caused by: %s", e));
            }
         }
      }
      // Then save added templates
      try {
         // Get all templates with stored files and save them
         for (FilledStructureTemplate newTemplate : templates.getElementsAsList().stream().filter(template -> template instanceof FilledStructureTemplate).map(template -> (FilledStructureTemplate) template).toList()) {
            Path newTemplatePath = this.getDirectoryStructureAccessor().getTemplatePath(newTemplate.getTemplateLocation(), packagePath, true);
            if(!Files.exists(newTemplatePath.getParent())){
               Files.createDirectories(newTemplatePath.getParent());
            }
            NbtIo.writeCompressed(newTemplate.getTemplateData(), newTemplatePath.toFile());
            // We have saved the templates data and don't need it anymore, so we replace the template with one without data
            templates.remove(newTemplate.getTemplateID());
            StructureTemplate templateWithoutData = newTemplate.withoutTemplateData();
            templates.add(templateWithoutData);
            this.getTemplateID_SaveName().computeIfAbsent(packageID, key -> new ArrayList<>()).add(templateWithoutData);
         }
      } catch (IOException e) {
         throw new PackageIOException(new CalculationError("Failed to save packages", "Failed to save new templates. Caused by: %s", e));
      }
   }

   protected void saveStructurePackageMeta(@NotNull Path packagePath) throws PackageIOException {
      // Create the data pack metadata for minecraft data packs that loads our templates and template pools on runtime
      Path metadataPath = packagePath.resolve("pack.mcmeta");
      JsonObject metadataObject = new JsonObject();
      JsonObject metadataPackObject = new JsonObject();
      metadataObject.add("pack_format", new JsonPrimitive(8));// Version 8 since 21w37
      metadataObject.add("description", new JsonPrimitive(""));
      metadataPackObject.add("pack", metadataObject);
      try {
         Files.writeString(metadataPath, metadataPackObject.toString());
      } catch (IOException e) {
         throw new PackageIOException(new CalculationError("Unable to create metadata file for structure package", "Might fail to load templates and template pools on runtime. Caused by: %s", e));
      }
   }

   protected void saveStructure(@NotNull StructureData structureData, Path packagePath) throws PackageIOException {
      Path structuresPath = this.getDirectoryStructureAccessor().getStructuresPath(structureData.getKey(), packagePath, true);
      try {
         Files.createDirectories(structuresPath.getParent());
         Files.writeString(structuresPath, StructureData.saveToJSON(structureData).toString());
      } catch (IOException e) {
         throw new PackageIOException(new CalculationError("Unable to save structure", "Caused By: %s", e));
      }
   }

   protected void saveTemplatePool(@NotNull TemplatePool templatePool, Path packagePath) throws PackageIOException {
      Path poolPath = this.getDirectoryStructureAccessor().getTemplatePoolPath(templatePool.getSaveName(), packagePath, true);
      try {
         Files.createDirectories(poolPath.getParent());
         Files.writeString(poolPath, templatePool.toString());
      } catch (IOException e) {
         throw new PackageIOException(new CalculationError("Unable to save structure template pool", "Caused By: %s", e));
      }
   }
}
