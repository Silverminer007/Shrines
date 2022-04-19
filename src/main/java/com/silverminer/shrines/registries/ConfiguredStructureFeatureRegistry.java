/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.registries;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.generators.ShrinesBiomeTagsProvider;
import com.silverminer.shrines.structures.ShrinesConfiguration;
import com.silverminer.shrines.structures.placement_types.PlacementCalculator;
import com.silverminer.shrines.structures.placement_types.SimplePlacementCalculator;
import com.silverminer.shrines.structures.spawn_criteria.GroundLevelDeltaSpawnCriteria;
import com.silverminer.shrines.structures.spawn_criteria.HeightSpawnCriteria;
import com.silverminer.shrines.structures.spawn_criteria.RandomChanceSpawnCriteria;
import com.silverminer.shrines.structures.spawn_criteria.SpawnCriteria;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ConfiguredStructureFeatureRegistry {
   public static final DeferredRegister<ConfiguredStructureFeature<?, ?>> REGISTRY = DeferredRegister.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, Shrines.MODID);

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> DELETED_STRUCTURE = REGISTRY.register("deleted_structure", () ->
         makeConfigured(new ResourceLocation("empty"), ShrinesBiomeTagsProvider.EMPTY, false, List.of()));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> ABANDONED_VILLA = REGISTRY.register("abandoned_villa", () ->
         makeConfigured(TemplatePoolRegistry.ABANDONED_VILLA.getId(), ShrinesBiomeTagsProvider.ABANDONED_VILLA, true, List.of(
               new RandomChanceSpawnCriteria(0.3),
               new HeightSpawnCriteria(64, Integer.MAX_VALUE, 32),
               new GroundLevelDeltaSpawnCriteria(2.0D, 32))));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> ABANDONED_WITCH_HOUSE = REGISTRY.register("abandoned_witch_house", () ->
         makeConfigured(TemplatePoolRegistry.ABANDONED_WITCH_HOUSE.getId(), ShrinesBiomeTagsProvider.AZALEA_PAVILION, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> AZALEA_PAVILION = REGISTRY.register("azalea_pavilion", () ->
         makeConfigured(TemplatePoolRegistry.AZALEA_PAVILION.getId(), ShrinesBiomeTagsProvider.AZALEA_PAVILION, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> BALLOON = REGISTRY.register("balloon", () ->
         makeConfigured(TemplatePoolRegistry.BALLOON.getId(), ShrinesBiomeTagsProvider.BALLOON, false));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> BEES = REGISTRY.register("bees", () ->
         makeConfigured(TemplatePoolRegistry.BEES.getId(), ShrinesBiomeTagsProvider.BEES, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> END_TEMPLE = REGISTRY.register("end_temple", () ->
         makeConfigured(TemplatePoolRegistry.END_TEMPLE.getId(), ShrinesBiomeTagsProvider.END_TEMPLE, false));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> FLOODED_TEMPLE = REGISTRY.register("flooded_temple", () ->
         makeConfigured(TemplatePoolRegistry.FLOODED_TEMPLE.getId(), ShrinesBiomeTagsProvider.FLOODED_TEMPLE, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> GUARDIAN_MEETING = REGISTRY.register("guardian_meeting", () ->
         makeConfigured(TemplatePoolRegistry.GUARDIANS_MEETING.getId(), ShrinesBiomeTagsProvider.GUARDIANS_MEETING, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> HARBOUR = REGISTRY.register("harbour", () ->
         makeConfigured(TemplatePoolRegistry.HARBOUR.getId(), ShrinesBiomeTagsProvider.HARBOUR, false, List.of(
               new HeightSpawnCriteria(50, Integer.MAX_VALUE, 100),
               new RandomChanceSpawnCriteria(0.6),
               new GroundLevelDeltaSpawnCriteria(1.0D, 100))));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> HIGH_TEMPLE = REGISTRY.register("high_temple", () ->
         makeConfigured(TemplatePoolRegistry.HIGH_TEMPLE.getId(), ShrinesBiomeTagsProvider.HIGH_TEMPLE, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> JUNGLE_TOWER = REGISTRY.register("jungle_tower", () ->
         makeConfigured(TemplatePoolRegistry.JUNGLE_TOWER.getId(), ShrinesBiomeTagsProvider.JUNGLE_TOWER, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> LUXURY_VILLA = REGISTRY.register("luxury_villa", () ->
         makeConfigured(TemplatePoolRegistry.LUXURY_VILLA.getId(), ShrinesBiomeTagsProvider.LUXURY_VILLA, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> MINERAL_TEMPLE = REGISTRY.register("mineral_temple", () ->
         makeConfigured(TemplatePoolRegistry.MINERAL_TEMPLE.getId(), ShrinesBiomeTagsProvider.MINERAL_TEMPLE, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> MODERN_VILLA = REGISTRY.register("modern_villa", () ->
         makeConfigured(TemplatePoolRegistry.MODERN_VILLA.getId(), ShrinesBiomeTagsProvider.MODERN_VILLA, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> NETHER_PYRAMID_NETHER = REGISTRY.register("nether_pyramid_nether", () ->
         makeConfigured(TemplatePoolRegistry.NETHER_PYRAMID.getId(), ShrinesBiomeTagsProvider.NETHER_PYRAMID_NETHER, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> NETHER_PYRAMID_OVERWORLD = REGISTRY.register("nether_pyramid_overworld", () ->
         makeConfigured(TemplatePoolRegistry.NETHER_PYRAMID.getId(), ShrinesBiomeTagsProvider.NETHER_PYRAMID_OVERWORLD, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> NETHER_SHRINE_NETHER = REGISTRY.register("nether_shrine_nether", () ->
         makeConfigured(TemplatePoolRegistry.NETHER_PYRAMID.getId(), ShrinesBiomeTagsProvider.NETHER_SHRINE_NETHER, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> NETHER_SHRINE_OVERWORLD = REGISTRY.register("nether_shrine_overworld", () ->
         makeConfigured(TemplatePoolRegistry.NETHER_SHRINE.getId(), ShrinesBiomeTagsProvider.NETHER_SHRINE_OVERWORLD, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> OASIS_SHRINE = REGISTRY.register("oasis_shrine", () ->
         makeConfigured(TemplatePoolRegistry.OASIS_SHRINE.getId(), ShrinesBiomeTagsProvider.OASIS_SHRINE, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> ORIENTAL_SANCTUARY = REGISTRY.register("oriental_sanctuary", () ->
         makeConfigured(TemplatePoolRegistry.ORIENTAL_SANCTUARY.getId(), ShrinesBiomeTagsProvider.ORIENTAL_SANCTUARY, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> SMALL_PLAYER_HOUSE = REGISTRY.register("small_player_house", () ->
         makeConfigured(TemplatePoolRegistry.SMALL_PLAYER_HOUSE.getId(), ShrinesBiomeTagsProvider.PLAYER_HOUSE, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> INFESTED_PRISON = REGISTRY.register("infested_prison", () ->
         makeConfigured(TemplatePoolRegistry.INFESTED_PRISON.getId(), ShrinesBiomeTagsProvider.INFESTED_PRISON, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> SHRINE_OF_SAVANNA = REGISTRY.register("shrine_of_savanna", () ->
         makeConfigured(TemplatePoolRegistry.SHRINE_OF_SAVANNA.getId(), ShrinesBiomeTagsProvider.SHRINE_OF_SAVANNA, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> SMALL_TEMPLE = REGISTRY.register("small_temple", () ->
         makeConfigured(TemplatePoolRegistry.SMALL_TEMPLE.getId(), ShrinesBiomeTagsProvider.SMALL_TEMPLE, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> TALL_PLAYER_HOUSE = REGISTRY.register("tall_player_house", () ->
         makeConfigured(TemplatePoolRegistry.TALL_PLAYER_HOUSE.getId(), ShrinesBiomeTagsProvider.PLAYER_HOUSE, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> TRADER_HOUSE = REGISTRY.register("trader_house", () ->
         makeConfigured(TemplatePoolRegistry.TRADER_HOUSE.getId(), ShrinesBiomeTagsProvider.TRADER_HOUSE, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> WATCH_TOWER = REGISTRY.register("watch_tower", () ->
         makeConfigured(TemplatePoolRegistry.WATCH_TOWER.getId(), ShrinesBiomeTagsProvider.WATCH_TOWER, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> WATER_SHRINE = REGISTRY.register("water_shrine", () ->
         makeConfigured(TemplatePoolRegistry.WATER_SHRINE.getId(), ShrinesBiomeTagsProvider.WATER_SHRINE, true));

   public static final RegistryObject<ConfiguredStructureFeature<?, ?>> WORLD_TREE_MANOR = REGISTRY.register("world_tree_manor", () ->
         makeConfigured(TemplatePoolRegistry.WORLD_TREE_MANOR.getId(), ShrinesBiomeTagsProvider.WORLD_TREE_MANOR, true));

   private static ConfiguredStructureFeature<ShrinesConfiguration, ?> makeConfigured(ResourceLocation pool, TagKey<Biome> biomes, boolean adaptNoise) {
      return makeConfigured(pool, biomes, adaptNoise, List.of(
            new HeightSpawnCriteria(64, Integer.MAX_VALUE, 32),
            new RandomChanceSpawnCriteria(0.6),
            new GroundLevelDeltaSpawnCriteria(2.0D, 32)));
   }

   private static ConfiguredStructureFeature<ShrinesConfiguration, ?> makeConfigured(ResourceLocation pool, TagKey<Biome> biomes, boolean adaptNoise, PlacementCalculator placementCalculator) {
      return makeConfigured(pool, biomes, adaptNoise, List.of(
            new HeightSpawnCriteria(64, Integer.MAX_VALUE, 32),
            new RandomChanceSpawnCriteria(0.6),
            new GroundLevelDeltaSpawnCriteria(2.0D, 32)), placementCalculator);
   }

   private static ConfiguredStructureFeature<ShrinesConfiguration, ?> makeConfigured(ResourceLocation pool, TagKey<Biome> biomes, boolean adaptNoise, List<SpawnCriteria> spawnCriteria) {
      return makeConfigured(pool, biomes, adaptNoise, spawnCriteria, new SimplePlacementCalculator());
   }

   private static ConfiguredStructureFeature<ShrinesConfiguration, ?> makeConfigured(ResourceLocation pool, TagKey<Biome> biomes, boolean adaptNoise, List<SpawnCriteria> spawnCriteria, PlacementCalculator placementCalculator) {
      ResourceKey<StructureTemplatePool> startPoolKey = ResourceKey.create(Registry.TEMPLATE_POOL_REGISTRY, pool);
      Holder.Reference<StructureTemplatePool> holder = Holder.Reference.createStandAlone(BuiltinRegistries.TEMPLATE_POOL, startPoolKey);
      return StructureInit.SURFACE.get().configured(new ShrinesConfiguration(holder, 7, spawnCriteria,
            placementCalculator), biomes, adaptNoise);
   }
}