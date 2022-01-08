/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.io;

import com.silverminer.shrines.config.DefaultStructureConfig;
import com.silverminer.shrines.packages.container.*;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageInfo;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import com.silverminer.shrines.packages.io.recent.RecentDirectoryStructureAccessor;

import java.nio.file.Path;

public class DefaultStructurePackageLoader implements StructurePackageLoader {
   private final RecentDirectoryStructureAccessor directoryStructureAccessor = new RecentDirectoryStructureAccessor();

   @Override
   public DirectoryStructureAccessor getDirectoryStructureAccessor() {
      return this.directoryStructureAccessor;
   }

   @Override
   public boolean matchesFormat() {
      return true;
   }

   @Override
   public StructurePackageContainer loadPackages() {
      StructurePackageContainer packageContainer = new StructurePackageContainer();
      StructuresPackageInfo info = new StructuresPackageInfo("Included Structures", "Silverm7ner");
      StructureDataContainer structureDataContainer = this.getIncludedStructures();
      StructuresPackageWrapper structuresPackageWrapper = new StructuresPackageWrapper(info, structureDataContainer, new StructureTemplateContainer(), new TemplatePoolContainer());
      packageContainer.add(structuresPackageWrapper);
      return packageContainer;
   }

   @Override
   public StructurePackageContainer earlyLoadPackages() throws PackageIOException {
      return this.loadPackages();
   }

   private StructureDataContainer getIncludedStructures() {
      StructureDataContainer structures = new StructureDataContainer();
      structures.add(DefaultStructureConfig.ABANDONEDWITCHHOUSE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.BALLON_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.BEES_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.ENDTEMPLE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.FLOODEDTEMPLE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.GUARDIANMEETING_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.HARBOUR_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.HIGHTEMPLE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.INFESTEDPRISON_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.JUNGLETOWER_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.MINERALTEMPLE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.NETHERPYRAMID_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.NETHERSHRINE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.ORIENTALSANCTUARY_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.PLAYERHOUSE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.SHRINEOFSAVANNA_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.SMALLTEMPLE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.TRADER_HOUSE_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.WATCHTOWER_CONFIG.toStructureData());
      structures.add(DefaultStructureConfig.WATERSHRINE_CONFIG.toStructureData());
      return structures;
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
      return null;
   }
}
