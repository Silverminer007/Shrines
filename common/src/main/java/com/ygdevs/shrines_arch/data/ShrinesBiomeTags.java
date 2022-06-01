/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.data;

import com.ygdevs.shrines_arch.Shrines;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ShrinesBiomeTags {
   public static final TagKey<Biome> EMPTY = createShrines("empty");
   public static final TagKey<Biome> IS_PLAINS = createMinecraft("is_plains");
   public static final TagKey<Biome> IS_MESA = createMinecraft("is_mesa");
   public static final TagKey<Biome> IS_SAVANNA = createMinecraft("is_savanna");
   public static final TagKey<Biome> IS_ICY = createMinecraft("is_icy");
   public static final TagKey<Biome> IS_THEEND = createMinecraft("is_theend");
   public static final TagKey<Biome> IS_DESERT = createMinecraft("is_desert");
   public static final TagKey<Biome> IS_SWAMP = createMinecraft("is_swamp");
   public static final TagKey<Biome> IS_MUSHROOM = createMinecraft("is_mushroom");
   public static final TagKey<Biome> IS_NETHER = createMinecraft("is_nether");
   public static final TagKey<Biome> ABANDONED_VILLA = createShrines("abandoned_villa");
   public static final TagKey<Biome> ABANDONED_WITCH_HOUSE = createShrines("abandoned_witch_house");
   public static final TagKey<Biome> AZALEA_PAVILION = createShrines("azalea_pavilion");
   public static final TagKey<Biome> BALLOON = createShrines("balloon");
   public static final TagKey<Biome> BEES = createShrines("bees");
   public static final TagKey<Biome> END_TEMPLE = createShrines("end_temple");
   public static final TagKey<Biome> FLOODED_TEMPLE = createShrines("flooded_temple");
   public static final TagKey<Biome> GUARDIANS_MEETING = createShrines("guardians_meeting");
   public static final TagKey<Biome> HARBOUR = createShrines("harbour");
   public static final TagKey<Biome> HIGH_TEMPLE = createShrines("high_temple");
   public static final TagKey<Biome> JUNGLE_TOWER = createShrines("jungle_tower");
   public static final TagKey<Biome> LUXURY_VILLA = createShrines("luxury_villa");
   public static final TagKey<Biome> MINERAL_TEMPLE = createShrines("mineral_temple");
   public static final TagKey<Biome> MODERN_VILLA = createShrines("modern_villa");
   public static final TagKey<Biome> NETHER_PYRAMID_OVERWORLD = createShrines("nether_pyramid_overworld");
   public static final TagKey<Biome> NETHER_SHRINE_OVERWORLD = createShrines("nether_shrine_overworld");
   public static final TagKey<Biome> NETHER_PYRAMID_NETHER = createShrines("nether_pyramid_nether");
   public static final TagKey<Biome> NETHER_SHRINE_NETHER = createShrines("nether_shrine_nether");
   public static final TagKey<Biome> OASIS_SHRINE = createShrines("oasis_shrine");
   public static final TagKey<Biome> ORIENTAL_HUT = createShrines("oriental_hut");
   public static final TagKey<Biome> ORIENTAL_SANCTUARY = createShrines("oriental_sanctuary");
   public static final TagKey<Biome> PLAYER_HOUSE = createShrines("player_house");
   public static final TagKey<Biome> INFESTED_PRISON = createShrines("infested_prison");
   public static final TagKey<Biome> SHRINE_OF_SAVANNA = createShrines("shrine_of_savanna");
   public static final TagKey<Biome> SMALL_TEMPLE = createShrines("small_temple");
   public static final TagKey<Biome> TRADER_HOUSE = createShrines("trader_house");
   public static final TagKey<Biome> WATCH_TOWER = createShrines("watch_tower");
   public static final TagKey<Biome> WATER_SHRINE = createShrines("water_shrine");
   public static final TagKey<Biome> WORLD_TREE_MANOR = createShrines("world_tree_manor");

   private static TagKey<Biome> createShrines(String name) {
      return TagKey.create(Registry.BIOME_REGISTRY, Shrines.location("has_structure/" + name));
   }

   private static TagKey<Biome> createMinecraft(String name) {
      return TagKey.create(Registry.BIOME_REGISTRY, Shrines.location(name));
   }
}