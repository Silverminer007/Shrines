/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.worldgen.structures.spawn_criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.registries.SpawnCriteriaTypeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.function.Predicate;

public class NotCloseToStructureSpawnCriteria extends SpawnCriteria {
   public static final Codec<NotCloseToStructureSpawnCriteria> CODEC = RecordCodecBuilder.create(closeToStructureSpawnCriteriaInstance ->
         closeToStructureSpawnCriteriaInstance.group(
               StructureSet.CODEC.fieldOf("structure_set").forGetter(NotCloseToStructureSpawnCriteria::getStructureSetHolder),
               Codec.intRange(0, 32).fieldOf("min_chunk_distance").forGetter(NotCloseToStructureSpawnCriteria::getMinChunkDistance)
         ).apply(closeToStructureSpawnCriteriaInstance, NotCloseToStructureSpawnCriteria::new));
   private final Holder<StructureSet> structureSetHolder;
   private final int minChunkDistance;

   public NotCloseToStructureSpawnCriteria(Holder<StructureSet> structureSetHolder, int minChunkDistance) {
      this.structureSetHolder = structureSetHolder;
      this.minChunkDistance = minChunkDistance;
   }

   private static boolean hasFeatureChunkInRange(StructureSet structureSet, int chunkX, int chunkZ, int range, ChunkGenerator chunkGenerator) {
      if (structureSet != null) {
         StructurePlacement structureplacement = structureSet.placement();

         for (int i = chunkX - range; i <= chunkX + range; ++i) {
            for (int j = chunkZ - range; j <= chunkZ + range; ++j) {
               if (structureplacement.isFeatureChunk(chunkGenerator, range, i, j)) {
                  return true;
               }
            }
         }

      }
      return false;
   }

   @Override
   public SpawnCriteriaType getType() {
      return SpawnCriteriaTypeRegistry.NOT_CLOSE_TO_STRUCTURE.get();
   }

   @Override
   public boolean test(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome, StructureManager structureManager, RegistryAccess registryAccess) {
      return !hasFeatureChunkInRange(this.getStructureSetHolder().value(), chunkPos.x, chunkPos.z, this.getMinChunkDistance(), chunkGenerator);
   }

   protected Holder<StructureSet> getStructureSetHolder() {
      return structureSetHolder;
   }

   protected int getMinChunkDistance() {
      return minChunkDistance;
   }
}