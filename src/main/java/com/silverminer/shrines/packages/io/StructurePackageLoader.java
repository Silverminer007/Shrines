/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.io;

import com.silverminer.shrines.packages.container.StructureIconContainer;
import com.silverminer.shrines.packages.container.StructurePackageContainer;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;

import java.nio.file.Path;

public interface StructurePackageLoader {
   DirectoryStructureAccessor getDirectoryStructureAccessor();

   boolean matchesFormat();

   StructurePackageContainer loadPackages() throws PackageIOException;

   StructurePackageContainer earlyLoadPackages() throws PackageIOException;

   boolean canLoadStructureIcons();

   StructureIconContainer loadStructureIcons(StructurePackageContainer packageContainer) throws PackageIOException;

   /**
    * @param source the path to the base directory of the package
    * @return the imported package or null if loading failed
    */
   StructuresPackageWrapper tryImportPackage(Path source);
}