/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.structures.spawn_criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.ygdevs.shrines_arch.registries.SpawnCriteriaTypeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.Optional;
import java.util.function.Predicate;

public abstract class SpawnCriteria {
   public static final Codec<SpawnCriteria> CODEC = ResourceLocation.CODEC.flatXmap(
         resourceLocation ->
               Optional.ofNullable(SpawnCriteriaTypeRegistry.REGISTRY.getRegistrar().get(resourceLocation))
                     .map(DataResult::success).orElse(DataResult.error(resourceLocation + " is no valid SpawnCriteria Type")),
         spawnCriteria -> Optional.ofNullable(SpawnCriteriaTypeRegistry.REGISTRY.getRegistrar().getId(spawnCriteria))
               .map(DataResult::success).orElse(DataResult.error("This spawn criteria type isn't registered"))).dispatch("type", SpawnCriteria::getType, SpawnCriteriaType::codec);

   public abstract SpawnCriteriaType getType();

   public abstract boolean test(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome, StructureManager structureManager, RegistryAccess registryAccess);
}