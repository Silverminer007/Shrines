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
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.function.Predicate;

public class SimplePlacementCalculator extends PlacementCalculator {
   public static final Codec<SimplePlacementCalculator> CODEC = Codec.unit(SimplePlacementCalculator::new);

   @Override
   public PlacementCalculatorType getType() {
      return PlacementCalculatorTypeRegistry.SIMPLE.get();
   }

   @Override
   public int calculate(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome, StructureManager structureManager, RegistryAccess registryAccess) {
      return 0;
   }

   @Override
   public boolean isRelativeToWorldHeight() {
      return true;
   }
}