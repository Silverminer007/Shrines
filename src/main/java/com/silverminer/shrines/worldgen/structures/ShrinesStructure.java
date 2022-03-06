/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.worldgen.structures;

import com.silverminer.shrines.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;

public class ShrinesStructure extends StructureFeature<ShrinesConfiguration> {
    protected static final Logger LOGGER = LogManager.getLogger(ShrinesStructure.class);

    public ShrinesStructure() {
        super(ShrinesConfiguration.CODEC, ShrinesStructure::place);
    }

    private static @NotNull Optional<PieceGenerator<ShrinesConfiguration>> place(PieceGeneratorSupplier.Context<ShrinesConfiguration> context) {
        if (!ShrinesStructure.checkLocation(context)) {
            return Optional.empty();
        } else {
            Pools.bootstrap();
            BlockPos position = context.chunkPos().getMiddleBlockPosition(0);
            Optional<PieceGenerator<JigsawConfiguration>> pieceGenerator;
            if (Biome.getBiomeCategory(context.chunkGenerator().getNoiseBiome(position.getX(), position.getY(), position.getZ())).equals(Biome.BiomeCategory.NETHER)) {
                NoiseColumn blockReader = context.chunkGenerator().getBaseColumn(position.getX(), position.getZ(), context.heightAccessor());
                int i = 0;
                while (!blockReader.getBlock(i).isAir()) {
                    i++;
                }
                position = new BlockPos(position.getX(), i, position.getZ());
                pieceGenerator = JigsawPlacement.addPieces(convertSupplierContext(context), PoolElementStructurePiece::new, position, false, false);
            } else {
                pieceGenerator = JigsawPlacement.addPieces(convertSupplierContext(context), PoolElementStructurePiece::new, position, false, true);
            }
            return pieceGenerator.isEmpty() ? Optional.empty() :
                    Optional.of((structurePieceBuilder, pieceGeneratorContext) ->
                            pieceGenerator.get().generatePieces(structurePieceBuilder, convertContext(pieceGeneratorContext)));
        }
    }

    // TODO Try to use casts instead
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
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureSeed(shrinesConfigurationContext.seed(), shrinesConfigurationContext.chunkPos().x, shrinesConfigurationContext.chunkPos().z);
        return new PieceGenerator.Context<>(
                new JigsawConfiguration(
                        shrinesConfigurationContext.config().startPool(),
                        shrinesConfigurationContext.config().maxDepth()
                ),
                shrinesConfigurationContext.chunkGenerator(),
                shrinesConfigurationContext.structureManager(),
                shrinesConfigurationContext.chunkPos(),
                shrinesConfigurationContext.heightAccessor(),
                worldgenrandom,
                shrinesConfigurationContext.seed()
        );
    }

    private static boolean checkLocation(PieceGeneratorSupplier.Context<ShrinesConfiguration> context) {
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(context.seed()));
        worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        if (!ShrinesStructure.checkForOtherStructures(context)) {
            return false;
        }
        if (getYPositionForFeature(context.chunkPos(), context.chunkGenerator(), context.heightAccessor(), context.seed()) < 60) {
            return false;
        }
        return worldgenrandom.nextDouble() < context.config().getSpawnChance();
    }

    private static boolean checkForOtherStructures(PieceGeneratorSupplier.Context<ShrinesConfiguration> context) {
        for (StructureSet structureSet : context.registryAccess().registryOrThrow(Registry.STRUCTURE_SET_REGISTRY)) {
            if (structureSet.placement() instanceof RandomSpreadStructurePlacement randomSpreadStructurePlacement) {
                ChunkPos chunkPos = context.chunkPos();

                int distance = Config.SETTINGS.STRUCTURE_MIN_DISTANCE.get();
                for (int x = chunkPos.x - distance; x <= chunkPos.x + distance; x++) {
                    for (int z = chunkPos.z - distance; z <= chunkPos.z + distance; z++) {
                        BlockPos structurePos = StructureFeature.getLocatePos(randomSpreadStructurePlacement, new ChunkPos(x, z));

                        // Check if the current structure set has a placement closer than 16 blocks near to the center of this chunk
                        if (structurePos.closerThan(new ChunkPos(x, z).getMiddleBlockPosition(0), 16.0)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private static int getYPositionForFeature(ChunkPos chunkPos, ChunkGenerator chunkGenerator, LevelHeightAccessor levelHeightAccessor, long seed) {
        Random random = new Random(chunkPos.x + (long) chunkPos.z * seed);
        Rotation rotation = Rotation.getRandom(random);
        int checkRadius = 20;
        int i = checkRadius;
        int j = checkRadius;
        if (rotation == Rotation.CLOCKWISE_90) {
            i = -checkRadius;
        } else if (rotation == Rotation.CLOCKWISE_180) {
            i = -checkRadius;
            j = -checkRadius;
        } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
            j = -checkRadius;
        }

        int k = chunkPos.getBlockX(7);
        int l = chunkPos.getBlockZ(7);
        int i1 = chunkGenerator.getFirstOccupiedHeight(k, l, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
        int j1 = chunkGenerator.getFirstOccupiedHeight(k, l + j, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
        int k1 = chunkGenerator.getFirstOccupiedHeight(k + i, l, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
        int l1 = chunkGenerator.getFirstOccupiedHeight(k + i, l + j, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
        return Math.min(Math.min(i1, j1), Math.min(k1, l1));
    }

    @NotNull
    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }
}