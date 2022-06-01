/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.registries;

import com.ygdevs.shrines_arch.Shrines;
import com.ygdevs.shrines_arch.data.ShrinesBiomeTags;
import com.ygdevs.shrines_arch.structures.placement_types.PlacementCalculator;
import com.ygdevs.shrines_arch.structures.placement_types.SimplePlacementCalculator;
import com.ygdevs.shrines_arch.structures.ShrinesConfiguration;
import com.ygdevs.shrines_arch.structures.spawn_criteria.*;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ConfiguredStructureFeatureRegistry {
   public static final DeferredRegister<ConfiguredStructureFeature<?, ?>> REGISTRY =
         DeferredRegister.create(Shrines.MODID, Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> DELETED_STRUCTURE = REGISTRY.register("deleted_structure",
         new Builder().setAdaptNoise(false).setSpawnCriteria(List.of()).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> ABANDONED_VILLA = REGISTRY.register("abandoned_villa",
         new Builder().setTemplatePool(TemplatePoolRegistry.ABANDONED_VILLA).setBiomes(ShrinesBiomeTags.ABANDONED_VILLA).setSpawnCriteria(List.of(
               new RandomChanceSpawnCriteria(0.3),
               new HeightSpawnCriteria(64, Integer.MAX_VALUE, 32),
               new GroundLevelDeltaSpawnCriteria(2.0D, 32),
               new MinStructureDistanceSpawnCriteria(-1))).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> ABANDONED_WITCH_HOUSE = REGISTRY.register("abandoned_witch_house",
         new Builder().setTemplatePool(TemplatePoolRegistry.ABANDONED_WITCH_HOUSE).setBiomes(ShrinesBiomeTags.ABANDONED_WITCH_HOUSE).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> AZALEA_PAVILION = REGISTRY.register("azalea_pavilion",
         new Builder().setTemplatePool(TemplatePoolRegistry.AZALEA_PAVILION).setBiomes(ShrinesBiomeTags.AZALEA_PAVILION).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> BALLOON = REGISTRY.register("balloon",
         new Builder().setTemplatePool(TemplatePoolRegistry.BALLOON).setBiomes(ShrinesBiomeTags.BALLOON).setAdaptNoise(false).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> BEES = REGISTRY.register("bees",
         new Builder().setTemplatePool(TemplatePoolRegistry.BEES).setBiomes(ShrinesBiomeTags.BEES).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> END_TEMPLE = REGISTRY.register("end_temple",
         new Builder().setTemplatePool(TemplatePoolRegistry.END_TEMPLE).setBiomes(ShrinesBiomeTags.END_TEMPLE).setAdaptNoise(false).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> FLOODED_TEMPLE = REGISTRY.register("flooded_temple",
         new Builder().setTemplatePool(TemplatePoolRegistry.FLOODED_TEMPLE).setBiomes(ShrinesBiomeTags.FLOODED_TEMPLE).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> GUARDIAN_MEETING = REGISTRY.register("guardian_meeting",
         new Builder().setTemplatePool(TemplatePoolRegistry.GUARDIANS_MEETING).setBiomes(ShrinesBiomeTags.GUARDIANS_MEETING).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> HARBOUR = REGISTRY.register("harbour",
         new Builder().setTemplatePool(TemplatePoolRegistry.HARBOUR).setBiomes(ShrinesBiomeTags.HARBOUR).setAdaptNoise(false)
               .setSpawnCriteria(List.of(
                     new HeightSpawnCriteria(50, Integer.MAX_VALUE, 100),
                     new RandomChanceSpawnCriteria(0.6),
                     new GroundLevelDeltaSpawnCriteria(1.0D, 100),
                     new MinStructureDistanceSpawnCriteria(-1))).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> HIGH_TEMPLE = REGISTRY.register("high_temple",
         new Builder().setTemplatePool(TemplatePoolRegistry.HIGH_TEMPLE).setBiomes(ShrinesBiomeTags.HIGH_TEMPLE).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> JUNGLE_TOWER = REGISTRY.register("jungle_tower",
         new Builder().setTemplatePool(TemplatePoolRegistry.JUNGLE_TOWER).setBiomes(ShrinesBiomeTags.JUNGLE_TOWER).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> LUXURY_VILLA = REGISTRY.register("luxury_villa",
         new Builder().setTemplatePool(TemplatePoolRegistry.LUXURY_VILLA).setBiomes(ShrinesBiomeTags.LUXURY_VILLA).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> MINERAL_TEMPLE = REGISTRY.register("mineral_temple",
         new Builder().setTemplatePool(TemplatePoolRegistry.MINERAL_TEMPLE).setBiomes(ShrinesBiomeTags.MINERAL_TEMPLE).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> MODERN_VILLA = REGISTRY.register("modern_villa",
         new Builder().setTemplatePool(TemplatePoolRegistry.MODERN_VILLA).setBiomes(ShrinesBiomeTags.MODERN_VILLA).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> NETHER_PYRAMID_NETHER = REGISTRY.register("nether_pyramid_nether",
         new Builder().setTemplatePool(TemplatePoolRegistry.NETHER_PYRAMID).setBiomes(ShrinesBiomeTags.NETHER_PYRAMID_NETHER).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> NETHER_PYRAMID_OVERWORLD = REGISTRY.register("nether_pyramid_overworld",
         new Builder().setTemplatePool(TemplatePoolRegistry.NETHER_PYRAMID).setBiomes(ShrinesBiomeTags.NETHER_PYRAMID_OVERWORLD).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> NETHER_SHRINE_NETHER = REGISTRY.register("nether_shrine_nether",
         new Builder().setTemplatePool(TemplatePoolRegistry.NETHER_SHRINE).setBiomes(ShrinesBiomeTags.NETHER_SHRINE_NETHER).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> NETHER_SHRINE_OVERWORLD = REGISTRY.register("nether_shrine_overworld",
         new Builder().setTemplatePool(TemplatePoolRegistry.NETHER_SHRINE).setBiomes(ShrinesBiomeTags.NETHER_SHRINE_OVERWORLD).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> OASIS_SHRINE = REGISTRY.register("oasis_shrine",
         new Builder().setTemplatePool(TemplatePoolRegistry.OASIS_SHRINE).setBiomes(ShrinesBiomeTags.OASIS_SHRINE).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> ORIENTAL_HUT = REGISTRY.register("oriental_hut",
         new Builder().setTemplatePool(TemplatePoolRegistry.ORIENTAL_HUT).setBiomes(ShrinesBiomeTags.ORIENTAL_HUT).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> ORIENTAL_SANCTUARY = REGISTRY.register("oriental_sanctuary",
         new Builder().setTemplatePool(TemplatePoolRegistry.ORIENTAL_SANCTUARY).setBiomes(ShrinesBiomeTags.ORIENTAL_SANCTUARY).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> INFESTED_PRISON = REGISTRY.register("infested_prison",
         new Builder().setTemplatePool(TemplatePoolRegistry.INFESTED_PRISON).setBiomes(ShrinesBiomeTags.INFESTED_PRISON).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> SHRINE_OF_SAVANNA = REGISTRY.register("shrine_of_savanna",
         new Builder().setTemplatePool(TemplatePoolRegistry.SHRINE_OF_SAVANNA).setBiomes(ShrinesBiomeTags.SHRINE_OF_SAVANNA).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> SMALL_PLAYER_HOUSE = REGISTRY.register("small_player_house",
         new Builder().setTemplatePool(TemplatePoolRegistry.SMALL_PLAYER_HOUSE).setBiomes(ShrinesBiomeTags.PLAYER_HOUSE).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> SMALL_TEMPLE = REGISTRY.register("small_temple",
         new Builder().setTemplatePool(TemplatePoolRegistry.SMALL_TEMPLE).setBiomes(ShrinesBiomeTags.SMALL_TEMPLE).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> TALL_PLAYER_HOUSE = REGISTRY.register("tall_player_house",
         new Builder().setTemplatePool(TemplatePoolRegistry.TALL_PLAYER_HOUSE).setBiomes(ShrinesBiomeTags.PLAYER_HOUSE).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> TRADER_HOUSE = REGISTRY.register("trader_house",
         new Builder().setTemplatePool(TemplatePoolRegistry.TRADER_HOUSE).setBiomes(ShrinesBiomeTags.TRADER_HOUSE).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> WATCH_TOWER = REGISTRY.register("watch_tower",
         new Builder().setTemplatePool(TemplatePoolRegistry.WATCH_TOWER).setBiomes(ShrinesBiomeTags.WATCH_TOWER).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> WATER_SHRINE = REGISTRY.register("water_shrine",
         new Builder().setTemplatePool(TemplatePoolRegistry.WATER_SHRINE).setBiomes(ShrinesBiomeTags.WATER_SHRINE).build());

   public static final RegistrySupplier<ConfiguredStructureFeature<?, ?>> WORLD_TREE_MANOR = REGISTRY.register("world_tree_manor",
         new Builder().setTemplatePool(TemplatePoolRegistry.WORLD_TREE_MANOR).setBiomes(ShrinesBiomeTags.WORLD_TREE_MANOR).build());

   private static class Builder {
      private ResourceLocation templatePoolID = new ResourceLocation("empty");
      private TagKey<Biome> biomeTagKey = ShrinesBiomeTags.EMPTY;
      private boolean adaptNoise = true;
      private PlacementCalculator placementCalculator = new SimplePlacementCalculator();
      private List<SpawnCriteria> spawnCriteria = new ArrayList<>(List.of(
            new HeightSpawnCriteria(64, Integer.MAX_VALUE, 32),
            new RandomChanceSpawnCriteria(0.6),
            new GroundLevelDeltaSpawnCriteria(2.0D, 32),
            new MinStructureDistanceSpawnCriteria(-1)));
      private int jigsawSize = 7;
      private RegistrySupplier<? extends StructureFeature<ShrinesConfiguration>> baseStructure = StructureRegistry.SURFACE;

      public Builder setTemplatePool(ResourceLocation templatePoolID) {
         this.templatePoolID = templatePoolID;
         return this;
      }

      @Contract(value = "_ -> this", mutates = "this")
      public Builder setTemplatePool(@NotNull RegistrySupplier<StructureTemplatePool> templatePool) {
         this.templatePoolID = templatePool.getId();
         return this;
      }

      public Builder setBiomes(TagKey<Biome> biomeTagKey) {
         this.biomeTagKey = biomeTagKey;
         return this;
      }

      public Builder setAdaptNoise(boolean adaptNoise) {
         this.adaptNoise = adaptNoise;
         return this;
      }

      public Builder setPlacementCalculator(PlacementCalculator placementCalculator) {
         this.placementCalculator = placementCalculator;
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

      public Builder setJigsawSize(int size) {
         this.jigsawSize = size;
         return this;
      }

      public Builder setBaseStructure(RegistrySupplier<StructureFeature<ShrinesConfiguration>> baseStructure) {
         this.baseStructure = baseStructure;
         return this;
      }

      @Contract(pure = true)
      public @NotNull Supplier<ConfiguredStructureFeature<?, ?>> build() {
         return () -> {
            ResourceKey<StructureTemplatePool> startPoolKey = ResourceKey.create(Registry.TEMPLATE_POOL_REGISTRY, this.templatePoolID);
            Holder.Reference<StructureTemplatePool> holder = Holder.Reference.createStandAlone(BuiltinRegistries.TEMPLATE_POOL, startPoolKey);
            return this.baseStructure.get().configured(new ShrinesConfiguration(holder, this.jigsawSize, spawnCriteria,
                  placementCalculator), this.biomeTagKey, adaptNoise);
         };
      }
   }
}