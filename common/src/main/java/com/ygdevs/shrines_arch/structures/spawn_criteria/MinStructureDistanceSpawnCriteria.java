/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.structures.spawn_criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ygdevs.shrines_arch.config.Config;
import com.ygdevs.shrines_arch.registries.SpawnCriteriaTypeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.function.Predicate;

public class MinStructureDistanceSpawnCriteria extends SpawnCriteria {
   public static final Codec<MinStructureDistanceSpawnCriteria> CODEC = RecordCodecBuilder.create(minStructureDistanceSpawnCriteriaInstance ->
         minStructureDistanceSpawnCriteriaInstance.group(
               Codec.intRange(-1, 10).fieldOf("range").forGetter(MinStructureDistanceSpawnCriteria::getRange)
         ).apply(minStructureDistanceSpawnCriteriaInstance, MinStructureDistanceSpawnCriteria::new));
   private final int range;

   public MinStructureDistanceSpawnCriteria(int range) {
      this.range = range;
   }

   @Override
   public SpawnCriteriaType getType() {
      return SpawnCriteriaTypeRegistry.MIN_STRUCTURE_DISTANCE.get();
   }

   @Override
   public boolean test(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome, StructureManager structureManager, RegistryAccess registryAccess) {
      int range = this.getRange() >= 0 ? this.getRange() : Config.minStructureDistance();
      if (range < 0)
         return true;
      return registryAccess.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY).entrySet().stream().filter((structureSet) ->
                  chunkGenerator.hasFeatureChunkInRange(structureSet.getKey(), seed, chunkPos.x, chunkPos.z, range))
            .count() <= range;
   }

   protected int getRange() {
      return range;
   }
}