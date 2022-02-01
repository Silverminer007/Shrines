/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.config;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.DefaultedStructureData;
import net.minecraft.world.biome.Biome.Category;

import java.util.ArrayList;

public class DefaultStructureConfig {
   public static final DefaultedStructureData CUSTOM = new DefaultedStructureData("", "", 0).setStartPool("").setDistance(50)
         .setSeperation(8);

   public static final DefaultedStructureData ABANDONEDWITCHHOUSE_CONFIG = new DefaultedStructureData(
         "Abandoned Witch House", "abandoned_witch_house", 1721882513)
         .setBiomeCategoryWhitelist(Category.SWAMP, Category.FOREST)
         .setBiomeBlacklist("minecraft:flower_forest", "minecraft:tall_birch_forest", "minecraft:forest",
               "minecraft:birch_forest", "minecraft:birch_forest_hills")
         .setDistance(60).setSeperation(11);

   public static final DefaultedStructureData BALLON_CONFIG = new DefaultedStructureData("Balloon", "balloon", 143665)
         .setDistance(50).setSeperation(8).setTransformLand(false);

   public static final DefaultedStructureData BEES_CONFIG = new DefaultedStructureData("Bees", "bees", 779806245)
         .setDistance(70).setSeperation(12).setUseRandomVarianting(false);

   public static final DefaultedStructureData ENDTEMPLE_CONFIG = new DefaultedStructureData("End Temple", "end_temple",
         32 ^ 478392).setDistance(60).setSeperation(11).setBiomeCategoryWhitelist(Category.THEEND)
         .setBiomeBlacklist("minecraft:the_end", "minecraft:the_void", "minecraft:small_end_islands")
         .setDimensionWhitelist(Lists.newArrayList("minecraft:the_end"));

   public static final DefaultedStructureData FLOODEDTEMPLE_CONFIG = new DefaultedStructureData("Flooded Temple",
         "flooded_temple", 54315143).setDistance(50).setSeperation(10).setUseRandomVarianting(false)
         .removeFromBiomeCategoryWhitelist(Category.DESERT, Category.MESA);

   public static final DefaultedStructureData GUARDIANMEETING_CONFIG = new DefaultedStructureData("Guardian Meeting",
         "guardian_meeting", 1498473232).setDistance(70).setSeperation(17).setUseRandomVarianting(false);

   public static final DefaultedStructureData HARBOUR_CONFIG = new DefaultedStructureData("Harbour", "harbour",
         651398043).setDistance(50).setSeperation(8).setTransformLand(false).setBiomeCategoryWhitelist(Category.OCEAN);

   public static final DefaultedStructureData HIGHTEMPLE_CONFIG = new DefaultedStructureData("High Temple",
         "high_temple", 536987987).setDistance(85).setSeperation(18).setNovel(Novels.HIGH_TEMPLE);

   public static final DefaultedStructureData JUNGLETOWER_CONFIG = new DefaultedStructureData("Jungle Tower",
         "jungle_tower", 987531843).setDistance(60).setSeperation(11).setBiomeCategoryWhitelist(Category.JUNGLE);

   public static final DefaultedStructureData MINERALTEMPLE_CONFIG = new DefaultedStructureData("Mineral Temple",
         "mineral_temple", 576143753).setDistance(50).setSeperation(10).setUseRandomVarianting(false);

   public static final DefaultedStructureData NETHERPYRAMID_CONFIG = new DefaultedStructureData("Nether Pyramid",
         "nether_pyramid", 7428394).setDistance(150).setSeperation(50)
         .addDimensionToWhitelist("minecraft:the_nether").addToBiomeCategoryWhitelist(Category.NETHER).setNovel(Novels.NETHER_PYRAMID);

   public static final DefaultedStructureData NETHERSHRINE_CONFIG = new DefaultedStructureData("Nether Shrine",
         "nether_shrine", 653267).setDistance(80).setSeperation(15).addDimensionToWhitelist("minecraft:the_nether")
         .addToBiomeCategoryWhitelist(Category.NETHER);

   public static final DefaultedStructureData ORIENTALSANCTUARY_CONFIG = new DefaultedStructureData(
         "Oriental Sanctuary", "oriental_sanctuary", 143665).setDistance(50).setSeperation(14);

   public static final DefaultedStructureData PLAYERHOUSE_CONFIG = new DefaultedStructureData("Player House",
         "player_house", 751963298).setDistance(80).setSeperation(15);

   public static final DefaultedStructureData INFESTEDPRISON_CONFIG = new DefaultedStructureData("Infested Prison",
         "infested_prison", 567483014).setDistance(60).setSeperation(11);

   public static final DefaultedStructureData SHRINEOFSAVANNA_CONFIG = new DefaultedStructureData("Shrine of savanna",
         "shrine_of_savanna", 432333099).setBiomeCategoryWhitelist(Category.SAVANNA).setDistance(67).setSeperation(11).setNovel(Novels.SHRINE_OF_SAVANNA);

   public static final DefaultedStructureData SMALLTEMPLE_CONFIG = new DefaultedStructureData("Small Temple",
         "small_temple", 4765321).setDistance(75).setSeperation(13);

   public static final DefaultedStructureData TRADER_HOUSE_CONFIG = new DefaultedStructureData("Trader House",
         "trader_house", 760055678).setDistance(60).setSeperation(11);

   public static final DefaultedStructureData WATCHTOWER_CONFIG = new DefaultedStructureData("Watchtower",
         "watchtower", 432189012).setDistance(77).setSeperation(16);

   public static final DefaultedStructureData WATERSHRINE_CONFIG = new DefaultedStructureData("Water Shrine",
         "water_shrine", 643168754).setDistance(80).setSeperation(15).setNovel("");

   public static final DefaultedStructureData DELETED_STRUCTURE_CONFIG = new DefaultedStructureData("___ Deleted Structure ___",
         "deleted_structure",// Key will probably change. We use this config to allow smooth update from 1.8.1 and change it's key to the old ones to tell minecraft they're still
         // there
         0).setDistance(0).setSeperation(0).setGenerate(false).setTransformLand(false).setSpawnChance(0.0D)
         .setUseRandomVarianting(false).setDimensionWhitelist(new ArrayList<>()).setBiomeCategoryWhitelist().setBiomeBlacklist();
}