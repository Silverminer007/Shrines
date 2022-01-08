/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.configuration;

public record ConfigOptions(String name, String key, String transformLand, String generate, String spawnChance, String useRandomVarianting, String distance, String separation,
                            String seedModifier, String heightOffset, String biomeBlacklist, String biomeCategoryWhitelist, String dimensionWhitelist,
                            String startPool, String novel, String jigsawMaxDepth, String iconPath, String comments_prefix, String comments_suffix) {
   public static final ConfigOptions LATEST = new ConfigOptions(
         "name", "key", "transform_land", "generate", "spawn_chance", "use_random_varianting", "distance",
         "separation", "seed_modifier", "height_offset", "biome_blacklist", "biome_category_whitelist", "dimension_whitelist",
         "start_pool", "novel", "jigsaw_max_depth", "icon_path","options.shrines.", ".comment");
   public static final ConfigOptions LEGACY_2xx = new ConfigOptions(
         "name", "key", "transform_land", "generate", "spawn_chance", "use_random_varianting", "distance",
         "separation", "seed_modifier", "height_offset", "biome_blacklist", "biome_category_whitelist", "dimension_whitelist",
         "start_pool", "novel", "", "","options.shrines.", ".comment");
}
