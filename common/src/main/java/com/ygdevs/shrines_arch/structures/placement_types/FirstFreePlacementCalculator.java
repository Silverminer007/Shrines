/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.structures.placement_types;

import com.mojang.serialization.Codec;
import com.ygdevs.shrines_arch.registries.PlacementCalculatorTypeRegistry;
import net.minecraft.core.BlockPos;
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

public class FirstFreePlacementCalculator extends PlacementCalculator {
   public static final Codec<SimplePlacementCalculator> CODEC = Codec.unit(SimplePlacementCalculator::new);

   @Override
   public PlacementCalculatorType getType() {
      return PlacementCalculatorTypeRegistry.FIRST_FREE.get();
   }

   @Override
   public int calculate(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome, StructureManager structureManager, RegistryAccess registryAccess) {
      BlockPos position = chunkPos.getMiddleBlockPosition(0);
      return chunkGenerator.getFirstFreeHeight(position.getX(), position.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
   }

   @Override
   public boolean isRelativeToWorldHeight() {
      return false;
   }
}