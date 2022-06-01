/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.structures.placement_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

public class RelativePlacementCalculator extends PlacementCalculator {
   public static final Codec<RelativePlacementCalculator> CODEC = RecordCodecBuilder.create(relativePlacementCalculatorInstance ->
         relativePlacementCalculatorInstance.group(
               Codec.INT.fieldOf("offset").forGetter(RelativePlacementCalculator::getOffset)
         ).apply(relativePlacementCalculatorInstance, RelativePlacementCalculator::new));
   private final int offset;

   public RelativePlacementCalculator(int offset) {
      this.offset = offset;
   }

   @Override
   public PlacementCalculatorType getType() {
      return PlacementCalculatorTypeRegistry.RELATIVE.get();
   }

   @Override
   public int calculate(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome, StructureManager structureManager, RegistryAccess registryAccess) {
      return this.getOffset();
   }

   @Override
   public boolean isRelativeToWorldHeight() {
      return true;
   }

   private int getOffset() {
      return offset;
   }
}