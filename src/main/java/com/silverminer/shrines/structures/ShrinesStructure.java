/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ShrinesStructure extends StructureFeature<ShrinesConfiguration> {
   protected static final Logger LOGGER = LogManager.getLogger(ShrinesStructure.class);
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

         Optional<PieceGenerator<JigsawConfiguration>> pieceGenerator =
               JigsawPlacement.addPieces(convertSupplierContext(context), PoolElementStructurePiece::new, position,
                     false, context.config().getPlacementCalculator().isRelativeToWorldHeight());

         return pieceGenerator.isEmpty() ? Optional.empty() :
               Optional.of((structurePieceBuilder, pieceGeneratorContext) ->
                     pieceGenerator.get().generatePieces(structurePieceBuilder, convertContext(pieceGeneratorContext)));
      }
   }

   // Java generics are so stupid. I can't even pass a shrines configuration to a jigsaw configuration even if it's a subclass. So lets just create a new object. Not clean, but works
   private static PieceGeneratorSupplier.Context<JigsawConfiguration> convertSupplierContext(PieceGeneratorSupplier.Context<ShrinesConfiguration> shrinesConfigurationContext) {
      return new PieceGeneratorSupplier.Context<>(
            shrinesConfigurationContext.chunkGenerator(),
            shrinesConfigurationContext.biomeSource(),
            shrinesConfigurationContext.seed(),
            shrinesConfigurationContext.chunkPos(),
            new JigsawConfiguration(
                  shrinesConfigurationContext.config().startPool(),
                  shrinesConfigurationContext.config().maxDepth()
            ),
            shrinesConfigurationContext.heightAccessor(),
            shrinesConfigurationContext.validBiome(),
            shrinesConfigurationContext.structureManager(),
            shrinesConfigurationContext.registryAccess()
      );
   }

   // Java generics are so stupid. I can't even pass a shrines configuration to a jigsaw configuration even if it's a subclass. So lets just create a new object. Not clean, but works
   private static PieceGenerator.Context<JigsawConfiguration> convertContext(PieceGenerator.Context<ShrinesConfiguration> shrinesConfigurationContext) {
      return new PieceGenerator.Context<>(
            new JigsawConfiguration(
                  shrinesConfigurationContext.config().startPool(),
                  shrinesConfigurationContext.config().maxDepth()
            ),
            shrinesConfigurationContext.chunkGenerator(),
            shrinesConfigurationContext.structureManager(),
            shrinesConfigurationContext.chunkPos(),
            shrinesConfigurationContext.heightAccessor(),
            shrinesConfigurationContext.random(),
            shrinesConfigurationContext.seed()
      );
   }

   @NotNull
   @Override
   public GenerationStep.Decoration step() {
      return this.step;
   }
}