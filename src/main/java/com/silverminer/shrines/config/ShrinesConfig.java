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

   private static void setupConfig(ForgeConfigSpec.@NotNull Builder builder) {
      removedStructures = builder.define("removed_structures", new ArrayList<>());
      disabledStructures = builder.define("disabled_structures", new ArrayList<>());
      runStructureUpdater = builder.define("run_structure_updater", true);
   }
}