/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.io;

import com.silverminer.shrines.packages.container.StructurePackageContainer;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;

public interface StructurePackageSaver {
   void savePackages(StructurePackageContainer structurePackages) throws PackageIOException;

   DirectoryStructureAccessor getDirectoryStructureAccessor();

   byte[] exportPackage(StructuresPackageWrapper structuresPackageWrapper) throws PackageIOException;
}
