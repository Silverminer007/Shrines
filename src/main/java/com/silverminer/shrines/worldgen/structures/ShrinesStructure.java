/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.worldgen.structures;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.StructureInit;
import com.silverminer.shrines.init.StructureRegistryHolder;
import com.silverminer.shrines.packages.datacontainer.StructureData;
import com.silverminer.shrines.worldgen.structures.variation.RandomVariantsProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.NoiseAffectingStructureFeature;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ShrinesStructure extends NoiseAffectingStructureFeature<JigsawConfiguration> {
   protected static final Logger LOGGER = LogManager.getLogger(ShrinesStructure.class);

   public final ResourceLocation name;
   public StructureData structureConfig;

   public ShrinesStructure(ResourceLocation nameIn, StructureData config) {
      super(JigsawConfiguration.CODEC, (context) -> ShrinesStructure.place(context, config), new RandomVariantsProcessor(config.getVariationConfiguration()));
      this.name = nameIn;
      this.structureConfig = config;
      this.setRegistryName(this.getFeatureName());
   }

   private static @NotNull Optional<PieceGenerator<JigsawConfiguration>> place(PieceGeneratorSupplier.Context<JigsawConfiguration> context, StructureData structureConfig) {
      if (!ShrinesStructure.checkLocation(context, structureConfig)) {
         return Optional.empty();
      } else {
         JigsawConfiguration newConfig = new JigsawConfiguration(
               () -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                     .get(new ResourceLocation(structureConfig.getSpawnConfiguration().getStart_pool())),
               structureConfig.getSpawnConfiguration().getJigsawMaxDepth()
         );
         PieceGeneratorSupplier.Context<JigsawConfiguration> newContext = new PieceGeneratorSupplier.Context<>(
               context.chunkGenerator(),
               context.biomeSource(),
               context.seed(),
               context.chunkPos(),
               newConfig,
               context.heightAccessor(),
               context.validBiome(),
               context.structureManager(),
               context.registryAccess()
         );
         Pools.bootstrap();

         BlockPos position = context.chunkPos().getMiddleBlockPosition(0);
         Optional<PieceGenerator<JigsawConfiguration>> pieceGenerator;
         try {
            if (context.chunkGenerator().getNoiseBiome(position.getX(), position.getY(), position.getZ()).getBiomeCategory().equals(Biome.BiomeCategory.NETHER)) {
               NoiseColumn blockReader = context.chunkGenerator().getBaseColumn(position.getX(), position.getZ(), context.heightAccessor());
               int i = 0;
               while (!blockReader.getBlock(i).isAir()) {
                  i++;
               }
               position = new BlockPos(position.getX(), i, position.getZ());
               pieceGenerator = JigsawPlacement.addPieces(newContext, PoolElementStructurePiece::new, position, false, false);
            } else {
               pieceGenerator = JigsawPlacement.addPieces(newContext, PoolElementStructurePiece::new, position, false, true);
            }
         } catch (NullPointerException e) {// Catch this when the supplied start pool isn't found. An error message is more friendly than a crash
            pieceGenerator = Optional.empty();
         }
         return pieceGenerator;
      }
   }

   private static boolean checkLocation(PieceGeneratorSupplier.Context<JigsawConfiguration> context, StructureData structureConfig) {
      WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(context.seed()));
      worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
      List<StructureFeature<?>> structures = new ArrayList<>();

      for (StructureRegistryHolder holder : StructureInit.STRUCTURES) {
         structures.add(holder.getStructure());
      }
      structures.add(StructureFeature.VILLAGE);
      if (!ShrinesStructure.checkForOtherStructures(structureConfig, context.chunkGenerator(), context.seed(), context.chunkPos(), structures)) {
         return false;
      }
      if (getYPositionForFeature(context.chunkPos(), context.chunkGenerator(), context.heightAccessor(), structureConfig.getSpawnConfiguration().getSeed_modifier()) < 60) {
         return false;
      }
      return worldgenrandom.nextDouble() < structureConfig.getSpawnConfiguration().getSpawn_chance();
   }

   private static boolean checkForOtherStructures(StructureData structureConfig, ChunkGenerator generator, long seed,
                                                  ChunkPos chunkPos, List<StructureFeature<?>> structures) {
      for (StructureFeature<?> structure1 : structures) {
         StructureFeatureConfiguration separationSettings = generator.getSettings().getConfig(structure1);

         if (separationSettings == null || (structure1 instanceof ShrinesStructure && ((ShrinesStructure) structure1).structureConfig.getKey().equals(structureConfig.getKey()))) {
            continue;
         }

         int distance = Config.SETTINGS.STRUCTURE_MIN_DISTANCE.get();
         for (int x = chunkPos.x - distance; x <= chunkPos.x + distance; x++) {
            for (int z = chunkPos.z - distance; z <= chunkPos.z + distance; z++) {
               ChunkPos structurePos = structure1.getPotentialFeatureChunk(separationSettings, seed, x, z);

               if (x == structurePos.x && z == structurePos.z) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   private static int getYPositionForFeature(ChunkPos chunkPos, ChunkGenerator chunkGenerator, LevelHeightAccessor levelHeightAccessor, int seed) {
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

   @Override
   public @NotNull
   String getFeatureName() {
      return this.name.toString();
   }

   public int getDistance() {
      return (int) (this.getConfig().getSpawnConfiguration().getDistance() * Config.SETTINGS.DISTANCE_FACTOR.get());
   }

   public StructureData getConfig() {
      return this.structureConfig;
   }

   public int getSeparation() {
      return (int) (this.getConfig().getSpawnConfiguration().getSeparation() * Config.SETTINGS.SEPARATION_FACTOR.get());
   }
}