/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.io.legacy_200;

import com.silverminer.shrines.packages.io.DirectoryStructureAccessor;
import net.minecraft.FileUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;

public class Legacy200DirectoryStructureAccessor implements DirectoryStructureAccessor {
   @Override
   public Path getMinecraftPath() {
      return FMLPaths.GAMEDIR.get();
   }

   @Override
   public Path getBasePath() {
      return this.getMinecraftPath().resolve("shrines-saves");
   }

   @Override
   public Path getCachePath() {
      return this.getBasePath().resolve("Cache");
   }

   @Override
   public Path getImagesCachePath() {
      return this.getCachePath().resolve("Images");
   }

   @Override
   public Path getImportCachePath() {
      return this.getCachePath().resolve("Import");
   }

   @Override
   public Path getExportCachePath() {
      return this.getCachePath().resolve("Export");
   }

   @Override
   public Path getPackagesPath() {
      return this.getBasePath().resolve("Packets");
   }

   @Override
   public Path getTemplatePath(@Nonnull ResourceLocation location, @Nonnull Path packagePath, boolean extension) {
      return packagePath.resolve("data").resolve(location.getNamespace()).resolve("structures").resolve(location.getPath() + (extension ? ".nbt" : ""));
   }

   @Override
   public Path getTemplatePoolPath(@Nonnull ResourceLocation location, @Nonnull Path packagePath, boolean extension) {
      return packagePath.resolve("data").resolve(location.getNamespace()).resolve("worldgen").resolve("template_pool").resolve(location.getPath() + (extension ? ".json" : ""));
   }

   @Override
   public Path getTemplatePoolPath(@Nonnull ResourceLocation location, @Nonnull String packageName, boolean extension) {
      return this.getTemplatePoolPath(location, this.getPackagesPath().resolve(packageName), extension);
   }

   @Override
   public Path getStructureIconPath(@Nonnull ResourceLocation location, @Nonnull Path packagePath, boolean extension) {
      return packagePath.resolve("assets").resolve(location.getNamespace()).resolve("textures").resolve("structures").resolve(location.getPath() + (extension ? ".png" : ""));
   }

   @Override
   public Path getStructureIconPath(@NotNull ResourceLocation location, @NotNull String packageName, boolean extension) {
      return this.getStructureIconPath(location, this.getPackagesPath().resolve(packageName), extension);
   }

   @Override
   public Path getStructuresPath(@NotNull ResourceLocation location, Path packagePath, boolean extension) {
      return null;
   }

   @Override
   public Path getStructuresPath(@NotNull ResourceLocation location, String packageName, boolean extension) {
      return null;
   }

   @Override
   public void clearCache() {
      try {
         FileUtils.deleteDirectory(this.getCachePath().toFile());
      } catch (IOException e) {
      }
   }

   @Override
   public String findUnusedPackageFilename(String enteredName, String defaultName) {
      String resultName = enteredName.trim();
      if (resultName.isEmpty()) {
         resultName = defaultName;
      }
      try {
         resultName = FileUtil.findAvailableName(this.getPackagesPath(), resultName, "");
      } catch (Exception exception1) {
         resultName = defaultName;

         try {
            resultName = FileUtil.findAvailableName(this.getPackagesPath(), resultName, "");
         } catch (Exception exception) {
            throw new RuntimeException("Failed to find an unused directory name to save structure package", exception);
         }
      }
      return resultName;
   }
}
