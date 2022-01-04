package com.silverminer.shrines.packages.io.legacy_200;

import com.silverminer.shrines.packages.configuration.ConfigOptions;
import com.silverminer.shrines.packages.container.*;
import com.silverminer.shrines.packages.datacontainer.*;
import com.silverminer.shrines.packages.io.DirectoryStructureAccessor;
import com.silverminer.shrines.packages.io.PackageIOException;
import com.silverminer.shrines.packages.io.StructurePackageLoader;
import com.silverminer.shrines.utils.CalculationError;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Legacy200StructurePackageLoader implements StructurePackageLoader {
   private final Legacy200DirectoryStructureAccessor directoryStructureAccessor = new Legacy200DirectoryStructureAccessor();

   @Override
   public DirectoryStructureAccessor getDirectoryStructureAccessor() {
      return this.directoryStructureAccessor;
   }

   @Override
   public boolean matchesFormat() {
      return Files.isDirectory(this.getDirectoryStructureAccessor().getPackagesPath());
   }

   @Override
   public StructurePackageContainer loadPackages() throws PackageIOException {
      return this.loadPackages(false);
   }

   @Override
   public StructurePackageContainer earlyLoadPackages() throws PackageIOException {
      return this.loadPackages(true);
   }

   protected StructurePackageContainer loadPackages(boolean earlyLoad) throws PackageIOException {
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
      Path packageInfoPath = source.resolve("structures.nbt");
      CompoundTag packageInfoTag;
      try {
         packageInfoTag = NbtIo.read(packageInfoPath.toFile());
      } catch (IOException e) {
         throw new PackageIOException(new CalculationError("Failed to load legacy 2.0.0 structures package", "Failed to read structure package info. Caused by: %s", e));
      }
      if (packageInfoTag == null) {
         throw new PackageIOException(new CalculationError("Failed to load legacy 2.0.0 structures package", "Loaded structure package info was null"));
      }
      StructuresPackageInfo structuresPackageInfo = this.loadStructurePackageInfo(packageInfoTag);
      StructureDataContainer structures = this.loadStructures(packageInfoTag.getList("Structures", 10));
      StructureTemplateContainer structureTemplates = earlyLoad ? new StructureTemplateContainer() : this.loadStructureTemplates(source);
      TemplatePoolContainer templatePools = earlyLoad ? new TemplatePoolContainer() : this.loadStructureTemplatePools(source);
      if (Objects.isNull(structuresPackageInfo) || Objects.isNull(structureTemplates) || Objects.isNull(templatePools)) {
         return null;
      }
      return new StructuresPackageWrapper(structuresPackageInfo, structures, structureTemplates, templatePools, packageID);
   }

   protected StructuresPackageInfo loadStructurePackageInfo(CompoundTag packageInfoTag) {
      String author = packageInfoTag.getString("Author");
      String displayName = packageInfoTag.getString("Packet Name");
      return new StructuresPackageInfo(displayName, author);
   }

   protected StructureDataContainer loadStructures(ListTag structuresTag) {
      StructureDataContainer structures = new StructureDataContainer();
      for (Tag tag : structuresTag) {
         if (tag instanceof CompoundTag structureCompound) {
            StructureData structureData = new StructureData(
                  structureCompound.getCompound(ConfigOptions.LEGACY_2xx.name()).getString("Value"),
                  new ResourceLocation(structureCompound.getCompound(ConfigOptions.LEGACY_2xx.key()).getString("Value")),
                  new SpawnConfiguration(
                        structureCompound.getCompound(ConfigOptions.LEGACY_2xx.transformLand()).getBoolean("Value"),
                        structureCompound.getCompound(ConfigOptions.LEGACY_2xx.generate()).getBoolean("Value"),
                        structureCompound.getCompound(ConfigOptions.LEGACY_2xx.spawnChance()).getDouble("Value"),
                        structureCompound.getCompound(ConfigOptions.LEGACY_2xx.useRandomVarianting()).getBoolean("Value"),
                        structureCompound.getCompound(ConfigOptions.LEGACY_2xx.distance()).getInt("Value"),
                        structureCompound.getCompound(ConfigOptions.LEGACY_2xx.separation()).getInt("Value"),
                        structureCompound.getCompound(ConfigOptions.LEGACY_2xx.seedModifier()).getInt("Value"),
                        structureCompound.getCompound(ConfigOptions.LEGACY_2xx.heightOffset()).getInt("Value"),
                        structureCompound.getCompound(ConfigOptions.LEGACY_2xx.biomeBlacklist()).getList("Value", 8).stream().map(Tag::getAsString).collect(Collectors.toList()),
                        structureCompound.getCompound(ConfigOptions.LEGACY_2xx.biomeCategoryWhitelist()).getList("Value", 8).stream().map(Tag::getAsString).collect(Collectors.toList()),
                        structureCompound.getCompound(ConfigOptions.LEGACY_2xx.dimensionWhitelist()).getList("Value", 8).stream().map(Tag::getAsString).collect(Collectors.toList()),
                        structureCompound.getCompound(ConfigOptions.LEGACY_2xx.startPool()).getString("Value"),
                        7),
                  null,
                  null
            );
            structures.add(structureData);
         }
      }
      return structures;
   }

   protected StructureTemplateContainer loadStructureTemplates(Path packagePath) throws PackageIOException {
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
                           CompoundTag templateData = NbtIo.read(currentPath.toFile());
                           templates.add(new FilledStructureTemplate(template, templateData));
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

   @Override
   public boolean canLoadStructureIcons() {
      return false;
   }

   @Override
   public StructureIconContainer loadStructureIcons(StructurePackageContainer packageContainer) {
      return null;
   }

   @Override
   public StructuresPackageWrapper tryImportPackage(Path source) {
      try {
         return this.loadStructurePackage(source, false);
      } catch (PackageIOException e) {
         return null;
      }
   }
}
