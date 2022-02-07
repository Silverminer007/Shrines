/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

public class ShrinesSettingsConfig {

    public final ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLISTED_BIOMES;

    public final ForgeConfigSpec.DoubleValue DISTANCE_FACTOR;
    public final ForgeConfigSpec.DoubleValue SEPERATION_FACTOR;

    public ShrinesSettingsConfig(final ForgeConfigSpec.Builder SERVER_BUILDER) {
        BLACKLISTED_BIOMES = SERVER_BUILDER
                .comment("Biomes where NO Structure (of this mod) can generate in. Custom structures too")
                .translation("config.shrines.blacklist").worldRestart().defineList("structures.blacklisted_biomes",
                        Lists.newArrayList(), ShrinesSettingsConfig::validateBiome);
        DISTANCE_FACTOR = SERVER_BUILDER.comment(
                        "Distance Factor (Default 1.0) Is multiplied on the structures distance. Allows changing distance of every structure at once")
                .translation("config.shrines.distance_factor").worldRestart()
                .defineInRange("structures.distance_factor", 1.0, 0.0, 100.0);
        SEPERATION_FACTOR = SERVER_BUILDER.comment(
                        "Seperation Factor (Default 1.0) Is multiplied on the structures seperation. Allows changing seperation of every structure at once")
                .translation("config.shrines.seperation_factor").worldRestart()
                .defineInRange("structures.seperation_factor", 1.0, 0.0, 100.0);
    }

    private static boolean validateBiome(Object o) {
        return o == null || ForgeRegistries.BIOMES.containsKey(new ResourceLocation((String) o));
    }
}