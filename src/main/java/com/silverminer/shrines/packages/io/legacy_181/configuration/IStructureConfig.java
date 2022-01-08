/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.packages.io.legacy_181.configuration;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Locale;

/**
 * @author Silverminer
 */
public interface IStructureConfig extends Comparable<IStructureConfig> {
   String getName();

   boolean getGenerate();

   double getSpawnChance();

   boolean getNeedsGround();

   int getDistance();

   int getSeparation();

   int getSeed();

   List<? extends Biome.BiomeCategory> getWhitelist();

   List<? extends String> getBlacklist();

   List<? extends String> getDimensions();

   boolean getUseRandomVarianting();

   double getLootChance();

   boolean getSpawnVillagers();

   boolean isBuiltIn();

   default String getDataName() {
      return this.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_");
   }

   boolean getActive();

   void setActive(boolean value);

   List<? extends IConfigOption<?>> getAllOptions();

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
      return new OptionParsingResult(false, new TextComponent("There is no such option as provided"));
   }
}