/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShrinesConfig {
   public static final ForgeConfigSpec GENERAL_SPEC;

   static {
      ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
      setupConfig(configBuilder);
      GENERAL_SPEC = configBuilder.build();
   }

   public static ForgeConfigSpec.ConfigValue<List<String>> removedStructures;
   public static ForgeConfigSpec.ConfigValue<List<String>> disabledStructures;
   public static ForgeConfigSpec.BooleanValue runStructureUpdater;
   public static ForgeConfigSpec.IntValue min_structure_distance;

   private static void setupConfig(ForgeConfigSpec.@NotNull Builder builder) {
      removedStructures = builder.comment("You should only need to use this option in very edge case scenarios. This option transforms all structures in this list to shrines:deleted_structures when Minecraft updates your world from 1.18.1 or below to 1.18.2 or above").define("removed_structures", new ArrayList<>());
      disabledStructures = builder.comment("All structures in this list won't spawn in worlds anymore. Already generated structures won't disappear and toggling this option won't cause issues").define("disabled_structures", new ArrayList<>());
      runStructureUpdater = builder.comment("You should only need to change this if you want to rerun the updater task on next Minecraft start. In fact it does exactly that. See here: https://silverminer007.github.io/ShrinesWiki/wiki/en_us/users/structureUpdater1.18.2.html").define("run_structure_updater", true);
      min_structure_distance = builder.comment("An additional spawn requirement for shrines structures. This only affects structures which use the shrines:min_structure_distance spawn criteria and they're is set to a negative value. This is the case for all shrines default structures in default configuration. Set this to a negative value to disable min structure distance entirely. \nIf you activate this option, all buildings will appear much less frequently. A tenfold increase is typical").defineInRange("min_structure_distance", -1, -1, 100);
   }
}