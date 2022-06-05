/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.structures;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.init.StructureRegistryHolder;
import com.silverminer.shrines.structures.load.StructureData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public class ShrinesStructure extends Structure<NoFeatureConfig> {
   protected static final Logger LOGGER = LogManager.getLogger(ShrinesStructure.class);

   public final String name;
   public StructureData structureConfig;

   public ShrinesStructure(String nameIn, StructureData config) {
      super(NoFeatureConfig.CODEC);
      this.name = nameIn;
      this.structureConfig = config;
      this.setRegistryName(this.getFeatureName());
   }

   public int getDistance() {
      return (int) (this.getConfig().getDistance() * Config.SETTINGS.DISTANCE_FACTOR.get());
   }

   public StructureData getConfig() {
      return this.structureConfig;
   }

   @Override
   public GenerationStage.@NotNull Decoration step() {
      return GenerationStage.Decoration.SURFACE_STRUCTURES;
   }

   public int getSeparation() {
      return (int) (this.getConfig().getSeperation() * Config.SETTINGS.SEPARATION_FACTOR.get());
   }

   /**
    * @return the seed modifier for this structure configured in {@link StructureData}
    * @deprecated use {@link StructureData#getSeed_modifier()} directly
    */
   @Deprecated
   public int getSeedModifier() {
      return this.getConfig().getSeed_modifier();
   }

   @Override
   public @NotNull String getFeatureName() {
      return this.name;
   }

   /**
    * @deprecated use {@link StructureData#getDimension_whitelist()} and {@link ShrinesStructure#getConfig()}
    */
   @Deprecated
   public List<? extends String> getDimensions() {
      return this.getConfig().getDimension_whitelist();
   }

   public int getMaxDepth() {
      return 7;
   }

   /**
    * @deprecated use {@link ShrinesStructure#getConfig()} and {@link StructureData#getSpawn_chance()} instead
    */
   @Deprecated
   public double getSpawnChance() {
      return this.getConfig().getSpawn_chance();
   }

   @Override
   protected boolean isFeatureChunk(@NotNull ChunkGenerator generator, @NotNull BiomeProvider provider, long seed, @NotNull SharedSeedRandom rand,
                                    int chunkX, int chunkZ, @NotNull Biome biome, @NotNull ChunkPos pos, @NotNull NoFeatureConfig config) {
      rand.setLargeFeatureSeed(seed, chunkX, chunkZ);
      List<Structure<?>> structures = new ArrayList<>();

      for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
         Structure<?> structure = holder.getStructure();

         if (structure.step() == this.step()) {
            structures.add(structure);
         }
      }
      if (this.step() == Structure.VILLAGE.step()) {
         structures.add(Structure.VILLAGE);
      }
      if (!this.checkForOtherStructures(this, generator, seed, rand, chunkX, chunkZ, structures)) {
         return false;
      }
      if (generator.getFirstFreeHeight(chunkX, chunkZ, Heightmap.Type.WORLD_SURFACE_WG) < 60
            && biome.getBiomeCategory() != Biome.Category.NETHER ||
            this.structureConfig.getBiomeCategoryWhitelist().stream().noneMatch(cat ->
                  cat.equals(biome.getBiomeCategory().toString())) ||
            this.structureConfig.getBiomeCategoryWhitelist().stream().anyMatch(biomeString ->
                  biome.getRegistryName() == null || biomeString.equals(biome.getRegistryName().toString()))) {
         return false;
      }
      return rand.nextDouble() < this.getConfig().getSpawn_chance();
   }

   protected boolean checkForOtherStructures(Structure<?> structure, ChunkGenerator generator, long seed,
                                             SharedSeedRandom rand, int chunkX, int chunkZ, @NotNull List<Structure<?>> structures) {
      for (Structure<?> structure1 : structures) {
         StructureSeparationSettings separationSettings = generator.getSettings().getConfig(structure1);

         if (separationSettings == null || structure == structure1) {
            continue;
         }

         int distance = Config.SETTINGS.STRUCTURE_MIN_DISTANCE.get();
         for (int x = chunkX - distance; x <= chunkX + distance; x++) {
            for (int z = chunkZ - distance; z <= chunkZ + distance; z++) {
               if (separationSettings.separation() == 0 || separationSettings.spacing() == 0) {
                  return false;
               }
               if (separationSettings.salt() == 0) {
                  LOGGER.error("Structure [{}] has invalid separation settings. Salt can be 0", structure1.getFeatureName());
                  return false;
               }
               ChunkPos structurePos = structure1.getPotentialFeatureChunk(separationSettings, seed, rand, x, z);

               if (x == structurePos.x && z == structurePos.z) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public Structure.@NotNull IStartFactory<NoFeatureConfig> getStartFactory() {
      return (structure, chunkX, chunkZ, mbb, references, seed) -> new Start(this, chunkX, chunkZ, mbb, references, seed);
   }

   public static class Start extends MarginedStructureStart<NoFeatureConfig> {
      private final ShrinesStructure feature;

      public Start(ShrinesStructure structure, int chunkX, int chunkZ, MutableBoundingBox mbb, int references,
                   long seed) {
         super(structure, chunkX, chunkZ, mbb, references, seed);
         this.feature = structure;
      }

      @SuppressWarnings("deprecation")
      @Override
      public void generatePieces(@NotNull DynamicRegistries registries, @NotNull ChunkGenerator chunkGenerator,
                                 @NotNull TemplateManager templateManager, int chunkX, int chunkZ, @NotNull Biome biome, @NotNull NoFeatureConfig no_config) {
         @SuppressWarnings("unused")
         JigsawPattern pattern = JigsawPatternRegistry.bootstrap();

         String location = this.feature.structureConfig.getStart_pool();
         LocalDate localdate = LocalDate.now();
         if (this.feature.getFeatureName().contains("balloon") && localdate.get(ChronoField.MONTH_OF_YEAR) == 6) {
            location += "_rainbow";
         }
         ResourceLocation pool = new ResourceLocation(location);
         VillageConfig config = new VillageConfig(
               () -> registries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(pool),
               this.feature.getMaxDepth());

         BlockPos blockpos;
         // Check if the actual Dimension has ceiling. If so we don't want it to be
         // generated over it, so we need to search for another place
         if (biome.getBiomeCategory().equals(Biome.Category.NETHER)) {
            blockpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
            IBlockReader blockReader = chunkGenerator.getBaseColumn(blockpos.getX(), blockpos.getZ());
            while (!blockReader.getBlockState(blockpos).isAir(blockReader, blockpos)) {
               blockpos = blockpos.above();
            }
            JigsawManager.addPieces(registries, config, AbstractVillagePiece::new, chunkGenerator, templateManager,
                  blockpos, this.pieces, this.random, false, false);
         } else {
            int x = chunkX * 16;
            int z = chunkZ * 16;
            int y = chunkGenerator.getFirstFreeHeight(x, z, this.feature.structureConfig.getHeightmapType())
                  + this.feature.structureConfig.getHeight_offset();
            blockpos = new BlockPos(x, y, z);
            JigsawManager.addPieces(registries, config, AbstractVillagePiece::new, chunkGenerator, templateManager,
                  blockpos, this.pieces, this.random, false, false);
         }

         Vector3i structureCenter = this.pieces.get(0).getBoundingBox().getCenter();
         int xOffset = blockpos.getX() - structureCenter.getX();
         int zOffset = blockpos.getZ() - structureCenter.getZ();
         for (StructurePiece structurePiece : this.pieces) {
            structurePiece.move(xOffset, 0, zOffset);
         }

         this.calculateBoundingBox();
      }
   }


}