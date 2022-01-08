/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.io;

import com.silverminer.shrines.packages.io.recent.RecentDirectoryStructureAccessor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public interface DirectoryStructureAccessor {
   RecentDirectoryStructureAccessor RECENT = new RecentDirectoryStructureAccessor();

   Path getMinecraftPath();

   /**
    *
    * @return The Base path of shrines saved data should be unique for that version, because the saver/loader is identified with that path
    */
   Path getBasePath();

   Path getCachePath();

   /**
    * @return the path to the image cache
    */
   Path getImagesCachePath();

   @SuppressWarnings("unused")
   Path getImportCachePath();

   @SuppressWarnings("unused")
   Path getExportCachePath();

   /**
    * @return the path to packages base
    */
   Path getPackagesPath();

   /**
    * @return the path to template in the given package
    */
   Path getTemplatePath(@Nonnull ResourceLocation location, @Nonnull Path packagePath, boolean extension);

   /**
    * @return the path to template pool in the given package
    */
   Path getTemplatePoolPath(@Nonnull ResourceLocation location, @Nonnull Path packagePath, boolean extension);

   /**
    * @return the path to template pool in the given package
    */
   @SuppressWarnings("unused")
   Path getTemplatePoolPath(@Nonnull ResourceLocation location, @Nonnull String packageName, boolean extension);

   /**
    * @return the path to the structure icon in the given package
    */
   Path getStructureIconPath(@Nonnull ResourceLocation location, @Nonnull Path packagePath, boolean extension);

   /**
    * @return the path to the structure icon in the given package
    */
   Path getStructureIconPath(@Nonnull ResourceLocation location, @Nonnull String packageName, boolean extension);

   @SuppressWarnings("unused")
   Path getStructuresPath(@NotNull ResourceLocation location, String packageName, boolean extension);

   Path getStructuresPath(@Nonnull ResourceLocation key, Path packagePath, boolean extension);

   void clearCache() throws PackageIOException;

   String findUnusedPackageFilename(String enteredName, String defaultName);
}
