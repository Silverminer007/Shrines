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

   private static void setupConfig(ForgeConfigSpec.@NotNull Builder builder) {
      removedStructures = builder.define("removed_structures", new ArrayList<>());
   }
}