/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.config;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ShrinesSettingsConfig {

   public final ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLISTED_BIOMES;

   public final ForgeConfigSpec.DoubleValue DISTANCE_FACTOR;
   public final ForgeConfigSpec.DoubleValue SEPARATION_FACTOR;
   public final ForgeConfigSpec.IntValue STRUCTURE_MIN_DISTANCE;
   public final ForgeConfigSpec.BooleanValue ADVANCED_LOGGING;

   public final ForgeConfigSpec.ConfigValue<List<? extends String>> BANNED_BLOCKS;
   public final ForgeConfigSpec.ConfigValue<List<? extends String>> BANNED_ENTITIES;

   public ShrinesSettingsConfig(final ForgeConfigSpec.Builder SERVER_BUILDER) {
      BLACKLISTED_BIOMES = SERVER_BUILDER
            .comment("Biomes where NO Structure (of this mod) can generate in. Custom structures too")
            .translation("config.shrines.blacklist").worldRestart().defineList("structures.blacklisted_biomes",
                  Lists.newArrayList(), ShrinesSettingsConfig::validateBiome);
      DISTANCE_FACTOR = SERVER_BUILDER.comment(
                  "Distance Factor (Default 1.0) Is multiplied on the structures distance. Allows changing distance of every structure at once. See 'Structure Min Distance' too")
            .translation("config.shrines.distance_factor")
            .defineInRange("structures.distance_factor", 1.0, 0.0, 100.0);
      SEPARATION_FACTOR = SERVER_BUILDER.comment(
                  "Separation Factor (Default 1.0) Is multiplied on the structures separation. Allows changing separation of every structure at once. See 'Structure Min Distance' too")
            .translation("config.shrines.separation_factor")
            .defineInRange("structures.separation_factor", 1.0, 0.0, 100.0);
      STRUCTURE_MIN_DISTANCE = SERVER_BUILDER.comment(
                  "The structures min. distance is the smallest possible distance between two structures (of this mod). You should set this to an lower value if you set the distance and separation to lower values, because to high distances here can prevent any structure from spawning")
            .translation("config.shrines.structure_min_distance")
            .defineInRange("structures.structures_min_distance", 10, 1, 100);
      ADVANCED_LOGGING = SERVER_BUILDER.comment(
                  "Use advanced logging. Gives more help by finding issues. Please enable this before reporting a bug")
            .translation("config.shrines.advanced_logging").define("structures.advanced_logging", true);
      BANNED_BLOCKS = SERVER_BUILDER.comment(
                  "All blocks in this list will not be placed with the structures. So it is possible to make blocks that are considered too valuable not appear in the world")
            .translation("config.shrines.banned_blocks")
            .defineList("structures.banned_blocks", Lists.newArrayList(), ShrinesSettingsConfig::validateBlock);
      BANNED_ENTITIES = SERVER_BUILDER.comment("All entities in this list will not be placed with the structures")
            .translation("config.shrines.banned_entities")
            .defineList("structures.banned_entities", Lists.newArrayList(), ShrinesSettingsConfig::validateEntity);
   }

   private static boolean validateBiome(Object o) {
      return o != null && ForgeRegistries.BIOMES.containsKey(new ResourceLocation((String) o));
   }

   private static boolean validateBlock(Object o) {
      return o != null && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation((String) o));
   }

   private static boolean validateEntity(Object o) {
      return o != null && ForgeRegistries.ENTITIES.containsKey(new ResourceLocation((String) o));
   }
}