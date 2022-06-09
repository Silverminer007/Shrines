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
import com.silverminer.shrines.structures.ShrinesStructure;
import com.silverminer.shrines.structures.spawn_criteria.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class StructureRegistry {
   public static final DeferredRegister<Structure> REGISTRY = DeferredRegister.create(Registry.STRUCTURE_REGISTRY, Shrines.MODID);

   public static final RegistryObject<Structure> DELETED_STRUCTURE = REGISTRY.register("deleted_structure",
         new Builder().setTerrainAdjustment(TerrainAdjustment.NONE).setSpawnCriteria(List.of()).build());

   public static final RegistryObject<Structure> ABANDONED_VILLA = REGISTRY.register("abandoned_villa",
         new Builder().setTemplatePool(TemplatePoolRegistry.ABANDONED_VILLA).setBiomeTagKey(ShrinesBiomeTagsProvider.ABANDONED_VILLA).build());

   public static final RegistryObject<Structure> ABANDONED_WITCH_HOUSE = REGISTRY.register("abandoned_witch_house",
         new Builder().setTemplatePool(TemplatePoolRegistry.ABANDONED_WITCH_HOUSE).setBiomeTagKey(ShrinesBiomeTagsProvider.ABANDONED_WITCH_HOUSE).build());

   public static final RegistryObject<Structure> AZALEA_PAVILION = REGISTRY.register("azalea_pavilion",
         new Builder().setTemplatePool(TemplatePoolRegistry.AZALEA_PAVILION).setBiomeTagKey(ShrinesBiomeTagsProvider.AZALEA_PAVILION).build());

   public static final RegistryObject<Structure> BALLOON = REGISTRY.register("balloon",
         new Builder().setTemplatePool(TemplatePoolRegistry.BALLOON).setBiomeTagKey(ShrinesBiomeTagsProvider.BALLOON).setTerrainAdjustment(TerrainAdjustment.NONE).build());

   public static final RegistryObject<Structure> BEES = REGISTRY.register("bees",
         new Builder().setTemplatePool(TemplatePoolRegistry.BEES).setBiomeTagKey(ShrinesBiomeTagsProvider.BEES).build());

   public static final RegistryObject<Structure> END_TEMPLE = REGISTRY.register("end_temple",
         new Builder().setTemplatePool(TemplatePoolRegistry.END_TEMPLE).setBiomeTagKey(ShrinesBiomeTagsProvider.END_TEMPLE)
               .setSpawnCriteria(List.of(new HeightSpawnCriteria(50, Integer.MAX_VALUE, 16),
                     new RandomChanceSpawnCriteria(0.6), new MinStructureDistanceSpawnCriteria(3))).build());

   public static final RegistryObject<Structure> FLOODED_TEMPLE = REGISTRY.register("flooded_temple",
         new Builder().setTemplatePool(TemplatePoolRegistry.FLOODED_TEMPLE).setBiomeTagKey(ShrinesBiomeTagsProvider.FLOODED_TEMPLE).build());

   public static final RegistryObject<Structure> GUARDIAN_MEETING = REGISTRY.register("guardian_meeting",
         new Builder().setTemplatePool(TemplatePoolRegistry.GUARDIANS_MEETING).setBiomeTagKey(ShrinesBiomeTagsProvider.GUARDIANS_MEETING).build());

   public static final RegistryObject<Structure> HARBOUR = REGISTRY.register("harbour",
         new Builder().setTemplatePool(TemplatePoolRegistry.HARBOUR).setBiomeTagKey(ShrinesBiomeTagsProvider.HARBOUR).setTerrainAdjustment(TerrainAdjustment.NONE)
               .setSpawnCriteria(List.of(
                     new HeightSpawnCriteria(50, Integer.MAX_VALUE, 100),
                     new RandomChanceSpawnCriteria(0.6),
                     new GroundLevelDeltaSpawnCriteria(1.0D, 100),
                     new MinStructureDistanceSpawnCriteria(-1))).build());

   public static final RegistryObject<Structure> HIGH_TEMPLE = REGISTRY.register("high_temple",
         new Builder().setTemplatePool(TemplatePoolRegistry.HIGH_TEMPLE).setBiomeTagKey(ShrinesBiomeTagsProvider.HIGH_TEMPLE).build());

   public static final RegistryObject<Structure> INFESTED_PRISON = REGISTRY.register("infested_prison",
         new Builder().setTemplatePool(TemplatePoolRegistry.INFESTED_PRISON).setBiomeTagKey(ShrinesBiomeTagsProvider.INFESTED_PRISON).build());

   public static final RegistryObject<Structure> JUNGLE_TOWER = REGISTRY.register("jungle_tower",
         new Builder().setTemplatePool(TemplatePoolRegistry.JUNGLE_TOWER).setBiomeTagKey(ShrinesBiomeTagsProvider.JUNGLE_TOWER).build());

   public static final RegistryObject<Structure> LUXURY_VILLA = REGISTRY.register("luxury_villa",
         new Builder().setTemplatePool(TemplatePoolRegistry.LUXURY_VILLA).setBiomeTagKey(ShrinesBiomeTagsProvider.LUXURY_VILLA).build());

   public static final RegistryObject<Structure> MAYAN_TEMPLE = REGISTRY.register("mayan_temple",
         new Builder().setTemplatePool(TemplatePoolRegistry.MAYAN_TEMPLE).setBiomeTagKey(ShrinesBiomeTagsProvider.MAYAN_TEMPLE).build());

   public static final RegistryObject<Structure> MINERAL_TEMPLE = REGISTRY.register("mineral_temple",
         new Builder().setTemplatePool(TemplatePoolRegistry.MINERAL_TEMPLE).setBiomeTagKey(ShrinesBiomeTagsProvider.MINERAL_TEMPLE).build());

   public static final RegistryObject<Structure> MODERN_VILLA = REGISTRY.register("modern_villa",
         new Builder().setTemplatePool(TemplatePoolRegistry.MODERN_VILLA).setBiomeTagKey(ShrinesBiomeTagsProvider.MODERN_VILLA).build());

   public static final RegistryObject<Structure> NETHER_PYRAMID_NETHER = REGISTRY.register("nether_pyramid_nether",
         new Builder().setTemplatePool(TemplatePoolRegistry.NETHER_PYRAMID).setBiomeTagKey(ShrinesBiomeTagsProvider.NETHER_PYRAMID_NETHER)
               .setHeightProvider(ConstantHeight.of(VerticalAnchor.absolute(33))).setProjectStartToHeightMap(null)
               .setSpawnCriteria(List.of(new RandomChanceSpawnCriteria(0.6), new MinStructureDistanceSpawnCriteria(-1))).build());

   public static final RegistryObject<Structure> NETHER_PYRAMID_OVERWORLD = REGISTRY.register("nether_pyramid_overworld",
         new Builder().setTemplatePool(TemplatePoolRegistry.NETHER_PYRAMID).setBiomeTagKey(ShrinesBiomeTagsProvider.NETHER_PYRAMID_OVERWORLD).build());

   public static final RegistryObject<Structure> NETHER_SHRINE_NETHER = REGISTRY.register("nether_shrine_nether",
         new Builder().setTemplatePool(TemplatePoolRegistry.NETHER_SHRINE).setBiomeTagKey(ShrinesBiomeTagsProvider.NETHER_SHRINE_NETHER)
               .setHeightProvider(ConstantHeight.of(VerticalAnchor.absolute(33))).setProjectStartToHeightMap(null)
               .setSpawnCriteria(List.of(new RandomChanceSpawnCriteria(0.6), new MinStructureDistanceSpawnCriteria(-1))).build());

   public static final RegistryObject<Structure> NETHER_SHRINE_OVERWORLD = REGISTRY.register("nether_shrine_overworld",
         new Builder().setTemplatePool(TemplatePoolRegistry.NETHER_SHRINE).setBiomeTagKey(ShrinesBiomeTagsProvider.NETHER_SHRINE_OVERWORLD).build());

   public static final RegistryObject<Structure> OASIS_SHRINE = REGISTRY.register("oasis_shrine",
         new Builder().setTemplatePool(TemplatePoolRegistry.OASIS_SHRINE).setBiomeTagKey(ShrinesBiomeTagsProvider.OASIS_SHRINE).build());

   public static final RegistryObject<Structure> ORIENTAL_HUT = REGISTRY.register("oriental_hut",
         new Builder().setTemplatePool(TemplatePoolRegistry.ORIENTAL_HUT).setBiomeTagKey(ShrinesBiomeTagsProvider.ORIENTAL_HUT).build());

   public static final RegistryObject<Structure> ORIENTAL_SANCTUARY = REGISTRY.register("oriental_sanctuary",
         new Builder().setTemplatePool(TemplatePoolRegistry.ORIENTAL_SANCTUARY).setBiomeTagKey(ShrinesBiomeTagsProvider.ORIENTAL_SANCTUARY).build());

   public static final RegistryObject<Structure> SHRINE_OF_SAVANNA = REGISTRY.register("shrine_of_savanna",
         new Builder().setTemplatePool(TemplatePoolRegistry.SHRINE_OF_SAVANNA).setBiomeTagKey(ShrinesBiomeTagsProvider.SHRINE_OF_SAVANNA).build());

   public static final RegistryObject<Structure> SMALL_PLAYER_HOUSE = REGISTRY.register("small_player_house",
         new Builder().setTemplatePool(TemplatePoolRegistry.SMALL_PLAYER_HOUSE).setBiomeTagKey(ShrinesBiomeTagsProvider.PLAYER_HOUSE).build());

   public static final RegistryObject<Structure> SMALL_TEMPLE = REGISTRY.register("small_temple",
         new Builder().setTemplatePool(TemplatePoolRegistry.SMALL_TEMPLE).setBiomeTagKey(ShrinesBiomeTagsProvider.SMALL_TEMPLE).build());

   public static final RegistryObject<Structure> TALL_PLAYER_HOUSE = REGISTRY.register("tall_player_house",
         new Builder().setTemplatePool(TemplatePoolRegistry.TALL_PLAYER_HOUSE).setBiomeTagKey(ShrinesBiomeTagsProvider.PLAYER_HOUSE).build());

   public static final RegistryObject<Structure> TRADER_HOUSE = REGISTRY.register("trader_house",
         new Builder().setTemplatePool(TemplatePoolRegistry.TRADER_HOUSE).setBiomeTagKey(ShrinesBiomeTagsProvider.TRADER_HOUSE).build());

   public static final RegistryObject<Structure> WATCH_TOWER = REGISTRY.register("watch_tower",
         new Builder().setTemplatePool(TemplatePoolRegistry.WATCH_TOWER).setBiomeTagKey(ShrinesBiomeTagsProvider.WATCH_TOWER).build());

   public static final RegistryObject<Structure> WATER_SHRINE = REGISTRY.register("water_shrine",
         new Builder().setTemplatePool(TemplatePoolRegistry.WATER_SHRINE).setBiomeTagKey(ShrinesBiomeTagsProvider.WATER_SHRINE).build());

   public static final RegistryObject<Structure> WORLD_TREE_MANOR = REGISTRY.register("world_tree_manor",
         new Builder().setTemplatePool(TemplatePoolRegistry.WORLD_TREE_MANOR).setBiomeTagKey(ShrinesBiomeTagsProvider.WORLD_TREE_MANOR).build());

   private static class Builder {
      private Holder<StructureTemplatePool> structureTemplatePoolHolder = Holder.Reference.createStandAlone(BuiltinRegistries.TEMPLATE_POOL, ResourceKey.create(Registry.TEMPLATE_POOL_REGISTRY, new ResourceLocation("empty")));
      private ResourceLocation templatePoolID = null;
      private TagKey<Biome> biomeTagKey = ShrinesBiomeTagsProvider.EMPTY;
      private TerrainAdjustment terrainAdjustment = TerrainAdjustment.BEARD_THIN;
      private List<SpawnCriteria> spawnCriteria = List.of(
            new HeightSpawnCriteria(64, Integer.MAX_VALUE, 32),
            new RandomChanceSpawnCriteria(0.6),
            new GroundLevelDeltaSpawnCriteria(2.0D, 32),
            new MinStructureDistanceSpawnCriteria(-1));
      private int maxDepth = 7;
      private GenerationStep.Decoration generationStep = GenerationStep.Decoration.SURFACE_STRUCTURES;
      private HeightProvider heightProvider = ConstantHeight.of(VerticalAnchor.absolute(0));
      private boolean useExpansionHack = false;
      private Heightmap.Types projectStartToHeightMap = Heightmap.Types.WORLD_SURFACE_WG;
      private int maxDistanceFromCenter = 64;

      public Builder setTemplatePool(Holder<StructureTemplatePool> templatePool) {
         this.structureTemplatePoolHolder = templatePool;
         return this;
      }

      @Contract(value = "_ -> this", mutates = "this")
      public Builder setTemplatePool(@NotNull RegistryObject<StructureTemplatePool> templatePool) {
         return this.setTemplatePool(templatePool.getHolder().orElseThrow());
      }

      public Builder setTerrainAdjustment(TerrainAdjustment terrainAdjustment) {
         this.terrainAdjustment = terrainAdjustment;
         return this;
      }

      public Builder addSpawnCriteria(SpawnCriteria spawnCriteria) {
         this.spawnCriteria.add(spawnCriteria);
         return this;
      }

      public Builder setSpawnCriteria(List<SpawnCriteria> spawnCriteria) {
         this.spawnCriteria = new ArrayList<>(spawnCriteria);
         return this;
      }

      public Builder setMaxDepth(int size) {
         this.maxDepth = size;
         return this;
      }

      public Builder setTemplatePoolID(ResourceLocation templatePoolID) {
         this.templatePoolID = templatePoolID;
         return this;
      }

      public Builder setBiomeTagKey(TagKey<Biome> biomeTagKey) {
         this.biomeTagKey = biomeTagKey;
         return this;
      }

      public Builder setGenerationStep(GenerationStep.Decoration generationStep) {
         this.generationStep = generationStep;
         return this;
      }

      public Builder setHeightProvider(HeightProvider heightProvider) {
         this.heightProvider = heightProvider;
         return this;
      }

      public Builder setUseExpansionHack(boolean useExpansionHack) {
         this.useExpansionHack = useExpansionHack;
         return this;
      }

      public Builder setProjectStartToHeightMap(@Nullable Heightmap.Types projectStartToHeightMap) {
         this.projectStartToHeightMap = projectStartToHeightMap;
         return this;
      }

      public Builder setMaxDistanceFromCenter(int maxDistanceFromCenter) {
         this.maxDistanceFromCenter = maxDistanceFromCenter;
         return this;
      }

      @Contract(pure = true)
      public @NotNull Supplier<Structure> build() {
         return () -> new ShrinesStructure(
               new Structure.StructureSettings(BuiltinRegistries.BIOME.getOrCreateTag(this.biomeTagKey), Map.of(), this.generationStep, this.terrainAdjustment),
               this.structureTemplatePoolHolder,
               this.templatePoolID,
               this.maxDepth,
               this.heightProvider,
               this.useExpansionHack,
               this.projectStartToHeightMap,
               this.maxDistanceFromCenter,
               this.spawnCriteria
         );
      }
   }
}