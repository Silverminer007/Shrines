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
import com.ygdevs.shrines_arch.registries.SpawnCriteriaTypeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.function.Predicate;

public class GroundLevelDeltaSpawnCriteria extends SpawnCriteria {
   public static final Codec<GroundLevelDeltaSpawnCriteria> CODEC = RecordCodecBuilder.create(groundLevelDeltaSpawnCriteriaInstance ->
         groundLevelDeltaSpawnCriteriaInstance.group(
               Codec.DOUBLE.fieldOf("delta").forGetter(GroundLevelDeltaSpawnCriteria::getDelta),
               Codec.INT.fieldOf("check_size").forGetter(GroundLevelDeltaSpawnCriteria::getCheckSize)
         ).apply(groundLevelDeltaSpawnCriteriaInstance, GroundLevelDeltaSpawnCriteria::new));
   private final double delta;
   private final int checkSize;

   public GroundLevelDeltaSpawnCriteria(double delta, int checkSize) {
      this.delta = delta;
      this.checkSize = checkSize;
   }

   @Override
   public SpawnCriteriaType getType() {
      return SpawnCriteriaTypeRegistry.GROUND_LEVEL_DELTA.get();
   }

   @Override
   public boolean test(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome, StructureManager structureManager, RegistryAccess registryAccess) {
      int[] corners = this.getCornerHeights(chunkPos.getMinBlockX() - (this.getCheckSize() / 2),
      chunkPos.getMinBlockZ() - (this.getCheckSize() / 2),
      chunkPos.getMinBlockX() + (this.getCheckSize() / 2),
            chunkPos.getMinBlockZ() + (this.getCheckSize() / 2),
      chunkGenerator, heightAccessor);
      int sum = 0;
      for(int i : corners) {
         sum += i;
      }
      double average = sum / (double)corners.length;
      double delta = average - Math.min(Math.min(corners[0], corners[1]), Math.min(corners[2], corners[3]));
      return delta < this.getDelta();
   }

   private int[] getCornerHeights(int x0, int z0, int x1, int z1, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor) {
      return new int[]{
            chunkGenerator.getFirstOccupiedHeight(x0, x1, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor),
            chunkGenerator.getFirstOccupiedHeight(x0, x1 + z1, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor),
            chunkGenerator.getFirstOccupiedHeight(x0 + z0, x1, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor),
            chunkGenerator.getFirstOccupiedHeight(x0 + z0, x1 + z1, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor)};
   }

   protected double getDelta() {
      return delta;
   }

   protected int getCheckSize() {
      return checkSize;
   }
}