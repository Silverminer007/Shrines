package com.silverminer.shrines.packages.io;

import com.silverminer.shrines.packages.container.StructurePackageContainer;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;

public interface StructurePackageSaver {
   void savePackages(StructurePackageContainer structurePackages) throws PackageIOException;

   DirectoryStructureAccessor getDirectoryStructureAccessor();

   byte[] exportPackage(StructuresPackageWrapper structuresPackageWrapper) throws PackageIOException;
}
