/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.structures.placement_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.ygdevs.shrines_arch.registries.PlacementCalculatorTypeRegistry;
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

public abstract class PlacementCalculator {
   public static final Codec<PlacementCalculator> CODEC = ResourceLocation.CODEC.flatXmap(
         resourceLocation ->
               Optional.ofNullable(PlacementCalculatorTypeRegistry.REGISTRY.getRegistrar().get(resourceLocation))
                     .map(DataResult::success).orElse(DataResult.error(resourceLocation + " is no valid SpawnCriteria Type")),
         spawnCriteria -> Optional.ofNullable(PlacementCalculatorTypeRegistry.REGISTRY.getRegistrar().getId(spawnCriteria))
               .map(DataResult::success).orElse(DataResult.error("This spawn criteria type isn't registered"))).dispatch("type", PlacementCalculator::getType, PlacementCalculatorType::codec);

   public abstract PlacementCalculatorType getType();

   public abstract int calculate(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome, StructureManager structureManager, RegistryAccess registryAccess);

   public abstract boolean isRelativeToWorldHeight();
}