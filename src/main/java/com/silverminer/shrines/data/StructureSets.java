/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.data;

import com.mojang.datafixers.util.Pair;
import com.silverminer.shrines.ShrinesMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StructureSets {
   public static final StructureSet ABANDONED_WITCH_HOUSE = create("abandoned_witch_house", 60, 23, 1721882513);
   public static final StructureSet BALLOON = create("balloon", 50, 8, 143665);
   public static final StructureSet AZALEA_PAVILION = create("azalea_pavilion", 40, 16, 635166686);
   public static final StructureSet BEES = create("bees", 70, 12, 779806245);
   public static final StructureSet END_TEMPLE = create("end_temple", 60, 11, 32 ^ 478392);
   public static final StructureSet GUARDIAN_MEETING = create("guardian_meeting", 70, 17, 1498473232);
   public static final StructureSet HARBOUR = create("harbour", 50, 8, 651398043);
   public static final StructureSet TEMPLES = create("temples", 56, 32, 536987987, List.of(Pair.of("small_temple", 3), Pair.of("high_temple", 3), Pair.of("flooded_temple", 4), Pair.of("mineral_temple", 4)));
   public static final StructureSet JUNGLE_TOWER = create("jungle_tower", 60, 11, 987531843);
   public static final StructureSet MODERN_VILLA = create("modern_villa", 55, 30, 688286800, List.of(Pair.of("modern_villa", 5), Pair.of("abandoned_villa", 3)));
   public static final StructureSet NETHER_PYRAMID = create("nether_pyramid", 150, 50, 7428394, "nether_pyramid_overworld", "nether_pyramid_nether");
   public static final StructureSet NETHER_SHRINE = create("nether_shrine", 80, 15, 653267, "nether_shrine_overworld", "nether_shrine_nether");
   public static final StructureSet OASIS_SHRINE = create("oasis_shrine", 40, 32, 2056047070);
   public static final StructureSet ORIENTAL_SANCTUARY = create("oriental_sanctuary", 50, 21, 143665);
   public static final StructureSet PLAYER_HOUSE = create("player_house", 80, 27, 751963298, "small_player_house", "tall_player_house");
   public static final StructureSet INFESTED_PRISON = create("infested_prison", 60, 22, 567483014);
   public static final StructureSet SHRINE_OF_SAVANNA = create("shrine_of_savanna", 67, 22, 432333099);
   public static final StructureSet TRADER_HOUSE = create("trader_house", 60, 18, 760055678);
   public static final StructureSet WATCH_TOWER = create("watch_tower", 77, 16, 432189012);
   public static final StructureSet WATER_SHRINE = create("water_shrine", 80, 15, 643168754);
   public static final StructureSet WORLD_TREE_MANOR = create("world_tree_manor", 55, 45, 14944438);

   private static StructureSet create(String structureName, int spacing, int separation, int seed) {
      return create(structureName, spacing, separation, seed, structureName);
   }

   private static StructureSet create(String structureName, int spacing, int separation, int seed, String... configuredStructureFeatures) {
      List<Pair<String, Integer>> structureFeatures = new ArrayList<>(configuredStructureFeatures.length);
      for(String s : configuredStructureFeatures) {
         structureFeatures.add(Pair.of(s, 1));
      }
      return create(structureName, spacing, separation, seed, structureFeatures);
   }

   private static StructureSet create(String setName, int spacing, int separation, int seed, List<Pair<String, Integer>> configuredStructureFeatures) {
      StructureSet element = new StructureSet(configuredStructureFeatures.stream().map(configuredStructureFeature ->
            new StructureSet.StructureSelectionEntry(Holder.Reference.createStandAlone(
                  BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
                  ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
                        ShrinesMod.location(configuredStructureFeature.getFirst()))), configuredStructureFeature.getSecond())).toList(),
            new RandomSpreadStructurePlacement(spacing, separation, RandomSpreadType.LINEAR, seed));
      BuiltinRegistries.register(BuiltinRegistries.STRUCTURE_SETS, ShrinesMod.location(setName), element);
      return element;
   }

   public static void bootstrap() {
   }
}