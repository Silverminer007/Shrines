/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ShrinesStructure extends StructureFeature<ShrinesConfiguration> {
   private final GenerationStep.Decoration step;

   public ShrinesStructure(GenerationStep.Decoration step) {
      super(ShrinesConfiguration.CODEC, ShrinesStructure::place);
      this.step = step;
   }

   private static @NotNull Optional<PieceGenerator<ShrinesConfiguration>> place(PieceGeneratorSupplier.Context<ShrinesConfiguration> context) {
      if (context.config().getSpawnCriteria().stream()
            .anyMatch(spawnCriteria ->
                  !spawnCriteria.test(context.chunkGenerator(), context.biomeSource(), context.seed(),
                        context.chunkPos(), context.heightAccessor(), context.validBiome(),
                        context.structureManager(), context.registryAccess()))) {
         return Optional.empty();
      } else {
         Pools.bootstrap();

         BlockPos position = context.chunkPos().getMiddleBlockPosition(context.config().getPlacementCalculator().calculate(
               context.chunkGenerator(), context.biomeSource(), context.seed(), context.chunkPos(), context.heightAccessor(),
               context.validBiome(), context.structureManager(), context.registryAccess()));

         return ShrinesJigsawPlacement.addPieces(context, PoolElementStructurePiece::new, position,
               false, context.config().getPlacementCalculator().isRelativeToWorldHeight());
      }
   }

   @NotNull
   @Override
   public GenerationStep.Decoration step() {
      return this.step;
   }
}