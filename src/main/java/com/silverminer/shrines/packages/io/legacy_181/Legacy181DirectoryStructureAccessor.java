/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.io.legacy_181;

import com.silverminer.shrines.packages.io.DirectoryStructureAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class Legacy181DirectoryStructureAccessor implements DirectoryStructureAccessor {
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
      return null;
   }

   @Override
   public Path getImagesCachePath() {
      return null;
   }

   @Override
   public Path getImportCachePath() {
      return null;
   }

   @Override
   public Path getExportCachePath() {
      return null;
   }

   @Override
   public Path getPackagesPath() {
      return this.getBasePath();
   }

   @Override
   public Path getTemplatePath(@NotNull ResourceLocation location, @NotNull Path packagePath, boolean extension) {
      return null;
   }

   @Override
   public Path getTemplatePoolPath(@NotNull ResourceLocation location, @NotNull Path packagePath, boolean extension) {
      return null;
   }

   @Override
   public Path getTemplatePoolPath(@NotNull ResourceLocation location, @NotNull String packageName, boolean extension) {
      return null;
   }

   @Override
   public Path getStructureIconPath(@NotNull ResourceLocation location, @NotNull Path packagePath, boolean extension) {
      return null;
   }

   @Override
   public Path getStructureIconPath(@NotNull ResourceLocation location, @NotNull String packageName, boolean extension) {
      return null;
   }

   @Override
   public Path getStructuresPath(@NotNull ResourceLocation location, String packageName, boolean extension) {
      return null;
   }

   @Override
   public Path getStructuresPath(@NotNull ResourceLocation key, Path packagePath, boolean extension) {
      return null;
   }

   @Override
   public void clearCache() {
   }

   @Override
   public String findUnusedPackageFilename(String enteredName, String defaultName) {
      return null;
   }
}
