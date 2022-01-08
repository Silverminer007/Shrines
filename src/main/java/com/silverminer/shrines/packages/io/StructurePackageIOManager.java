/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.io;

import com.silverminer.shrines.packages.container.StructureIconContainer;
import com.silverminer.shrines.packages.container.StructurePackageContainer;
import com.silverminer.shrines.packages.datacontainer.StructureData;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import com.silverminer.shrines.packages.io.legacy_181.Legacy181StructurePackageLoader;
import com.silverminer.shrines.packages.io.legacy_200.Legacy200StructurePackageLoader;
import com.silverminer.shrines.packages.io.recent.RecentStructurePackageIOManager;
import com.silverminer.shrines.utils.CalculationError;
import com.silverminer.shrines.utils.ZIPUtils;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class StructurePackageIOManager {
   public static final StructurePackageIOManager INSTANCE = new StructurePackageIOManager();
   private final StructurePackageSaver packageSaver = new RecentStructurePackageIOManager();
   private final StructurePackageLoader[] packageLoaders = new StructurePackageLoader[]{
         new RecentStructurePackageIOManager(),
         new Legacy200StructurePackageLoader(),
         new Legacy181StructurePackageLoader()
   };// Add all possible package loaders here. They are tested from first to last element, so put the latest one at place 0
   private final StructurePackageLoader fallbackPackageLoader = new DefaultStructurePackageLoader();

   public void savePackages(StructurePackageContainer structurePackages) throws PackageIOException {
      this.packageSaver.savePackages(structurePackages);
   }

   public StructurePackageContainer earlyLoadPackages() throws PackageIOException {
      StructurePackageContainer structurePackageContainer = this.earlyLoadPackageFromLoaders();
      this.makeStructureKeysAndSeedUnique(structurePackageContainer);
      return structurePackageContainer;
   }

   private StructurePackageContainer earlyLoadPackageFromLoaders() throws PackageIOException {
      for (StructurePackageLoader packageLoader : this.packageLoaders) {
         if (packageLoader.matchesFormat()) {
            return packageLoader.earlyLoadPackages();
         }
      }
      return this.fallbackPackageLoader.loadPackages();
   }

   public StructurePackageContainer loadPackages() throws PackageIOException {
      StructurePackageContainer structurePackageContainer = this.loadPackageFromLoaders();
      this.makeStructureKeysAndSeedUnique(structurePackageContainer);
      // Save the packages to make sure we have all changes in the latest save format. Especially important for default structure data this is important
      this.savePackages(structurePackageContainer);
      return structurePackageContainer;
   }

   private StructurePackageContainer loadPackageFromLoaders() throws PackageIOException {
      for (StructurePackageLoader packageLoader : this.packageLoaders) {
         if (packageLoader.matchesFormat()) {
            return packageLoader.loadPackages();
         }
      }
      return this.fallbackPackageLoader.loadPackages();
   }

   protected void makeStructureKeysAndSeedUnique(StructurePackageContainer structurePackageContainer) {
      Random random = new Random();
      for (StructuresPackageWrapper structuresPackageWrapper : structurePackageContainer.getAsIterable()) {
         structuresPackageWrapper.getStructures().getAsList()
               .forEach(structureData -> this.changeStructureKeyAndSeed(structureData.getKey(), () -> random.nextInt(Integer.MAX_VALUE), structurePackageContainer));
      }
   }


   protected void changeStructureKeyAndSeed(ResourceLocation oldKey, Supplier<Integer> seedCalculator, StructurePackageContainer structurePackageContainer) {
      for (StructuresPackageWrapper structuresPackageWrapper : structurePackageContainer.getAsIterable()) {
         ArrayList<StructureData> structureDataList = new ArrayList<>(structuresPackageWrapper.getStructures().getAsList().stream()
               .filter(structure -> structure.getKey().equals(oldKey)).toList());
         if (structureDataList.size() > 0) {
            structureDataList.remove(0);
         }
         for (StructureData structureData : structureDataList) {
            if (structureData.getSeed_modifier() == 0) {
               structureData.setSeed_modifier(seedCalculator.get());
            }
            structureData.setKey(this.calculateNewStructureKey(oldKey, structurePackageContainer));
         }
      }
   }

   protected ResourceLocation calculateNewStructureKey(ResourceLocation oldStructureKey, StructurePackageContainer structurePackageContainer) {
      List<ResourceLocation> takenStructureKeys = new ArrayList<>();
      for (StructuresPackageWrapper structuresPackageWrapper : structurePackageContainer.getAsIterable()) {
         takenStructureKeys.addAll(structuresPackageWrapper.getStructures().getAsList().stream().map(StructureData::getKey).toList());
      }
      String keyPath = oldStructureKey.getPath();
      ResourceLocation newStructureKey = new ResourceLocation(oldStructureKey.getNamespace(), keyPath);
      int i = 0;
      while (takenStructureKeys.contains(newStructureKey)) {
         keyPath = oldStructureKey.getPath() + i++;
         newStructureKey = new ResourceLocation(oldStructureKey.getNamespace(), keyPath);
      }
      return newStructureKey;
   }

   public StructureIconContainer loadStructureIcons(StructurePackageContainer packageContainer) throws PackageIOException {
      for (StructurePackageLoader packageLoader : this.packageLoaders) {
         if (packageLoader.canLoadStructureIcons()) {
            return packageLoader.loadStructureIcons(packageContainer);
         }
      }
      throw new PackageIOException(new CalculationError("Failed to load structure icons", "No package Loader was able to load structure icons"));
   }

   @Nonnull
   public StructuresPackageWrapper importPackage(byte[] packageFile) throws PackageIOException {
      Path importPath = DirectoryStructureAccessor.RECENT.getImportCachePath().resolve("temp");
      try {
         // Make sure the directory is empty
         if (Files.exists(importPath)) {
            Files.delete(importPath);
         }
         Files.createDirectories(importPath);
         ZIPUtils.unzipFile(packageFile, DirectoryStructureAccessor.RECENT.getImportCachePath(), importPath);
         for (StructurePackageLoader structurePackageLoader : this.packageLoaders) {
            StructuresPackageWrapper structuresPackageWrapper = structurePackageLoader.tryImportPackage(importPath);
            if (structuresPackageWrapper != null) {
               try {
                  Files.delete(importPath);
               } catch (IOException ignored) {
               }
               return structuresPackageWrapper;
            }
         }
         try {
            Files.delete(importPath);
         } catch (IOException ignored) {
         }
         throw new PackageIOException(new CalculationError("Failed to import package", "No package loader was able to import package"));
      } catch (IOException e) {
         throw new PackageIOException(new CalculationError("Failed to import package", "Failed to unzip File. Caused by: %s", e));
      }
   }

   @Nonnull
   public byte[] exportPackage(StructuresPackageWrapper structuresPackageWrapper) throws PackageIOException {
      return this.packageSaver.exportPackage(structuresPackageWrapper);
   }
}