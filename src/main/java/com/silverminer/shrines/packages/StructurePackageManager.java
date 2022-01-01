package com.silverminer.shrines.packages;

import com.silverminer.shrines.packages.container.NovelDataContainer;
import com.silverminer.shrines.packages.container.StructurePackageContainer;
import com.silverminer.shrines.utils.CalculationError;

public abstract class StructurePackageManager {
   protected StructurePackageContainer packages = new StructurePackageContainer();

   public StructurePackageContainer getPackages() {
      return this.packages;
   }

   public abstract NovelDataContainer getNovels();

   public abstract void setNovels(NovelDataContainer novels);

   public abstract void onError(CalculationError error);
}
