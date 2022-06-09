/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.structures.spawn_criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.config.ShrinesConfig;
import com.silverminer.shrines.registries.SpawnCriteriaTypeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;

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
   public boolean test(Structure.GenerationContext generationContext) {
      int range = this.getRange() >= 0 ? this.getRange() : ShrinesConfig.min_structure_distance.get();
      if (range < 0)
         return true;
      Registry<StructureSet> structureSetRegistry = generationContext.registryAccess().registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);
      return structureSetRegistry.holders().filter((structureSet) ->
                  generationContext.chunkGenerator().hasStructureChunkInRange(structureSet, generationContext.randomState(), generationContext.seed(), generationContext.chunkPos().x, generationContext.chunkPos().z, range))
            .count() <= range;
   }

   protected int getRange() {
      return range;
   }
}