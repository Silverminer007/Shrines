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
import com.silverminer.shrines.registries.SpawnCriteriaTypeRegistry;
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

public class HeightSpawnCriteria extends SpawnCriteria {
   public static final Codec<HeightSpawnCriteria> CODEC = RecordCodecBuilder.create(heightSpawnCriteriaInstance ->
         heightSpawnCriteriaInstance.group(
               Codec.INT.fieldOf("min_height").forGetter(HeightSpawnCriteria::getMinHeight),
               Codec.INT.fieldOf("max_height").forGetter(HeightSpawnCriteria::getMaxHeight),
               Codec.INT.optionalFieldOf("check_size", 16).forGetter(HeightSpawnCriteria::getCheckSize)
         ).apply(heightSpawnCriteriaInstance, HeightSpawnCriteria::new));
   private final int minHeight;
   private final int maxHeight;
   private final int checkSize;

   public HeightSpawnCriteria(int minHeight, int maxHeight, int checkSize) {
      this.minHeight = minHeight;
      this.maxHeight = maxHeight;
      this.checkSize = checkSize;
   }

   @Override
   public SpawnCriteriaType getType() {
      return SpawnCriteriaTypeRegistry.HEIGHT.get();
   }

   @Override
   public boolean test(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome, StructureManager structureManager, RegistryAccess registryAccess) {
      int lowestY = this.getLowestY(chunkPos, chunkGenerator, heightAccessor);
      return lowestY > this.minHeight && lowestY < this.maxHeight;
   }

   private int getLowestY(ChunkPos chunkPos, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor) {
      int i = chunkPos.getMinBlockX();
      int j = chunkPos.getMinBlockZ();
      int[] aint = this.getCornerHeights(i - (this.getCheckSize() / 2), i + (this.getCheckSize() / 2),
            j - (this.getCheckSize() / 2), j + (this.getCheckSize() / 2), chunkGenerator, heightAccessor);
      return Math.min(Math.min(aint[0], aint[1]), Math.min(aint[2], aint[3]));
   }

   private int[] getCornerHeights(int x0, int z0, int x1, int z1, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor) {
      return new int[]{
            chunkGenerator.getFirstOccupiedHeight(x0, x1, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor),
            chunkGenerator.getFirstOccupiedHeight(x0, x1 + z1, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor),
            chunkGenerator.getFirstOccupiedHeight(x0 + z0, x1, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor),
            chunkGenerator.getFirstOccupiedHeight(x0 + z0, x1 + z1, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor)};
   }

   protected int getMinHeight() {
      return minHeight;
   }

   protected int getMaxHeight() {
      return maxHeight;
   }

   protected int getCheckSize() {
      return this.checkSize;
   }
}