/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.data;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.data.generators.ShrinesBiomeTagsProvider;
import com.silverminer.shrines.registries.StructureInit;
import com.silverminer.shrines.worldgen.structures.ShrinesConfiguration;
import com.silverminer.shrines.worldgen.structures.placement_types.SimplePlacementCalculator;
import com.silverminer.shrines.worldgen.structures.spawn_criteria.HeightSpawnCriteria;
import com.silverminer.shrines.worldgen.structures.spawn_criteria.RandomChanceSpawnCriteria;
import com.silverminer.shrines.worldgen.structures.spawn_criteria.SpawnCriteria;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfiguredStructureFeatures {
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> ABANDONED_VILLA = makeConfigured("abandoned_villa", ShrinesBiomeTagsProvider.ABANDONED_VILLA, true, new RandomChanceSpawnCriteria(0.3), new HeightSpawnCriteria(64, Integer.MAX_VALUE, 32));
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> ABANDONED_WITCH_HOUSE = makeConfigured("abandoned_witch_house", ShrinesBiomeTagsProvider.ABANDONED_WITCH_HOUSE, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> BALLOON = makeConfigured("balloon", ShrinesBiomeTagsProvider.BALLOON, false);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> BEES = makeConfigured("bees", ShrinesBiomeTagsProvider.BEES, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> END_TEMPLE = makeConfigured("end_temple", ShrinesBiomeTagsProvider.END_TEMPLE, false);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> FLOODED_TEMPLE = makeConfigured("flooded_temple", ShrinesBiomeTagsProvider.FLOODED_TEMPLE, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> GUARDIANS_MEETING = makeConfigured("guardian_meeting", ShrinesBiomeTagsProvider.GUARDIANS_MEETING, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> HARBOUR = makeConfigured("harbour", ShrinesBiomeTagsProvider.HARBOUR, false);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> HIGH_TEMPLE = makeConfigured("high_temple", ShrinesBiomeTagsProvider.HIGH_TEMPLE, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> JUNGLE_TOWER = makeConfigured("jungle_tower", ShrinesBiomeTagsProvider.JUNGLE_TOWER, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> MINERAL_TEMPLE = makeConfigured("mineral_temple", ShrinesBiomeTagsProvider.MINERAL_TEMPLE, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> MODERN_VILLA = makeConfigured("modern_villa", ShrinesBiomeTagsProvider.MODERN_VILLA, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> NETHER_PYRAMID_OVERWORLD = makeConfigured("nether_pyramid", ShrinesBiomeTagsProvider.NETHER_PYRAMID_OVERWORLD, true, "nether_pyramid_overworld");
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> NETHER_SHRINE_OVERWORLD = makeConfigured("nether_shrine", ShrinesBiomeTagsProvider.NETHER_SHRINE_OVERWORLD, true, "nether_shrine_overworld");
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> NETHER_PYRAMID_NETHER = makeConfigured("nether_pyramid", ShrinesBiomeTagsProvider.NETHER_PYRAMID_NETHER, true, "nether_pyramid_nether");
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> NETHER_SHRINE_NETHER = makeConfigured("nether_shrine", ShrinesBiomeTagsProvider.NETHER_SHRINE_NETHER, true, "nether_shrine_nether");
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> OASIS_SHRINE = makeConfigured("oasis_shrine", ShrinesBiomeTagsProvider.OASIS_SHRINE, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> ORIENTAL_SANCTUARY = makeConfigured("oriental_sanctuary", ShrinesBiomeTagsProvider.ORIENTAL_SANCTUARY, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> PLAYER_HOUSE = makeConfigured("player_house", ShrinesBiomeTagsProvider.PLAYER_HOUSE, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> INFESTED_PRISON = makeConfigured("infested_prison", ShrinesBiomeTagsProvider.INFESTED_PRISON, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> SHRINE_OF_SAVANNA = makeConfigured("shrine_of_savanna", ShrinesBiomeTagsProvider.SHRINE_OF_SAVANNA, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> SMALL_TEMPLE = makeConfigured("small_temple", ShrinesBiomeTagsProvider.SMALL_TEMPLE, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> TRADER_HOUSE = makeConfigured("trader_house", ShrinesBiomeTagsProvider.TRADER_HOUSE, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> WATCH_TOWER = makeConfigured("watch_tower", ShrinesBiomeTagsProvider.WATCH_TOWER, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> WATER_SHRINE = makeConfigured("water_shrine", ShrinesBiomeTagsProvider.WATER_SHRINE, true);
   public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> WORLD_TREE_MANOR = makeConfigured("world_tree_manor", ShrinesBiomeTagsProvider.WORLD_TREE_MANOR, true);

   private static ConfiguredStructureFeature<ShrinesConfiguration, ?> makeConfigured(String structureName, TagKey<Biome> biomes, boolean adaptNoise, SpawnCriteria... spawnCriteriaReplacement) {
      return makeConfigured(structureName, biomes, adaptNoise, structureName, spawnCriteriaReplacement);
   }

   private static ConfiguredStructureFeature<ShrinesConfiguration, ?> makeConfigured(String structureName, TagKey<Biome> biomes, boolean adaptNoise, String configuredName, SpawnCriteria... spawnCriteriaReplacement) {
      ResourceKey<StructureTemplatePool> startPoolKey = ResourceKey.create(Registry.TEMPLATE_POOL_REGISTRY,
            ShrinesMod.location(structureName + "/start_pool"));
      Holder.Reference<StructureTemplatePool> holder = Holder.Reference.createStandAlone(BuiltinRegistries.TEMPLATE_POOL,
            startPoolKey);
      List<SpawnCriteria> spawnCriteriaList = new ArrayList<>(List.of(
            new HeightSpawnCriteria(64, Integer.MAX_VALUE, 32),
            new RandomChanceSpawnCriteria(0.6)));
      if (spawnCriteriaReplacement != null) {
         spawnCriteriaList = Arrays.asList(spawnCriteriaReplacement);
      }
      ConfiguredStructureFeature<ShrinesConfiguration, ?> element = StructureInit.SURFACE.get()
            .configured(new ShrinesConfiguration(holder, 7,
                  spawnCriteriaList,
                  new SimplePlacementCalculator()), biomes, adaptNoise);
      ResourceKey<ConfiguredStructureFeature<?, ?>> structureKey =
            ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, ShrinesMod.location(configuredName));
      BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, structureKey, element);
      return element;
   }

   public static void bootstrap() {
   }
}