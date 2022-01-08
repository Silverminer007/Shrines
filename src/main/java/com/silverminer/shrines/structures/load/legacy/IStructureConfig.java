/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.structures.load.legacy;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.biome.Biome.Category;

import java.util.List;
import java.util.Locale;

/**
 * @author Silverminer
 */
public interface IStructureConfig extends Comparable<IStructureConfig> {
   boolean getGenerate();

   double getSpawnChance();

   boolean getNeedsGround();

   int getDistance();

   int getSeparation();

   int getSeed();

   List<? extends Category> getWhitelist();

   List<? extends String> getBlacklist();

   List<? extends String> getDimensions();

   boolean getUseRandomVarianting();

   double getLootChance();

   boolean getSpawnVillagers();

   boolean isBuiltIn();

   default String getDataName() {
      return this.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_");
   }

   String getName();

   boolean getActive();

   void setActive(boolean value);

   /**
    * @param option
    * @param value
    */
   default OptionParsingResult fromString(String option, String value) {
      for (IConfigOption<?> co : this.getAllOptions()) {
         if (co.getName().equals(option)) {
            OptionParsingResult res = co.fromString(value, this);
            return res;
         }
      }
      return new OptionParsingResult(false, new StringTextComponent("There is no such option as provided"));
   }

   List<? extends IConfigOption<?>> getAllOptions();
}