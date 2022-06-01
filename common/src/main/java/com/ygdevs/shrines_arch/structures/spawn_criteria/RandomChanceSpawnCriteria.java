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
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.function.Predicate;

public class RandomChanceSpawnCriteria extends SpawnCriteria {
   public static final Codec<RandomChanceSpawnCriteria> CODEC = RecordCodecBuilder.create(randomChanceSpawnCriteriaInstance ->
         randomChanceSpawnCriteriaInstance.group(
               Codec.doubleRange(0.0, 1.0).fieldOf("spawn_chance").forGetter(RandomChanceSpawnCriteria::getSpawnChance)
         ).apply(randomChanceSpawnCriteriaInstance, RandomChanceSpawnCriteria::new));
   private final double spawnChance;

   public RandomChanceSpawnCriteria(double spawnChance) {
      this.spawnChance = spawnChance;
   }

   @Override
   public SpawnCriteriaType getType() {
      return SpawnCriteriaTypeRegistry.RANDOM_CHANCE.get();
   }

   @Override
   public boolean test(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome, StructureManager structureManager, RegistryAccess registryAccess) {
      WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(seed));
      worldgenrandom.setLargeFeatureSeed(seed, chunkPos.x, chunkPos.z);
      return worldgenrandom.nextDouble() < this.getSpawnChance();
   }

   protected double getSpawnChance() {
      return spawnChance;
   }
}