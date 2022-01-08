/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.structures;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.config.ConfigBuilder;
import com.silverminer.shrines.config.IStructureConfig;
import com.silverminer.shrines.init.NewStructureInit;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.common.ForgeConfigSpec;

public abstract class AbstractStructure<C extends IFeatureConfig> extends Structure<C> {
   protected static final Logger LOGGER = LogManager.getLogger(AbstractStructure.class);
   public final int size;

   public final String name;
   public IStructureConfig structureConfig;

   public AbstractStructure(Codec<C> codec, int sizeIn, String nameIn, IStructureConfig config) {
      super(codec);
      this.size = sizeIn;
      this.name = nameIn;
      structureConfig = config;
      this.setRegistryName(this.getFeatureName());
   }

   public boolean needsGround() {
      return this.getConfig().getNeedsGround();
   }

   public IStructureConfig getConfig() {
      return this.structureConfig;
   }

   public List<? extends String> getDimensions() {
      return this.getConfig().getDimensions();
   }

   public void buildConfig(final ForgeConfigSpec.Builder BUILDER) {
      if (this.structureConfig instanceof ConfigBuilder) {
         LOGGER.info("Building Config");
         this.structureConfig = ((ConfigBuilder) this.structureConfig).build(BUILDER);
      }
   }

   @Override
   protected boolean isFeatureChunk(ChunkGenerator generator, BiomeProvider provider, long seed, SharedSeedRandom rand,
                                    int chunkX, int chunkZ, Biome biome, ChunkPos pos, IFeatureConfig config) {
      return this.validateGeneration(generator, provider, seed, rand, chunkX, chunkZ, biome, pos, config);
   }

   @Override
   public String getFeatureName() {
      return new ResourceLocation(ShrinesMod.MODID, this.name).toString();
   }

   public boolean validateGeneration(ChunkGenerator generator, BiomeProvider provider, long seed,
                                     SharedSeedRandom rand, int chunkX, int chunkZ, Biome biome, ChunkPos pos, IFeatureConfig config,
                                     @Nullable Structure<?>... exeptStructure) {
      if (isSurfaceFlat(generator, chunkX, chunkZ)) {

         // Check the entire size of the structure to see if it's all a viable biome:
         for (Biome biome1 : provider.getBiomesWithin(chunkX * 16 + 9, generator.getSeaLevel(), chunkZ * 16 + 9,
               getSize() * 16)) {
            if (!biome1.getGenerationSettings().isValidStart(this)) {
               return false;
            }
         }

         rand.setLargeFeatureSeed(seed, chunkX, chunkZ);
         return rand.nextDouble() < getSpawnChance();
//					&& checkForOtherStructures(generator, provider, seed, rand,chunkX, chunkZ, biome, pos, config, exeptStructure);
      }

      return false;
   }

   protected boolean isSurfaceFlat(@Nonnull ChunkGenerator generator, int chunkX, int chunkZ) {
      // Size of the area to check.
      int offset = this.getSize() * 16;

      int xStart = (chunkX << 4);
      int zStart = (chunkZ << 4);

      int i1 = generator.getBaseHeight(xStart, zStart, Heightmap.Type.WORLD_SURFACE_WG);
      int j1 = generator.getBaseHeight(xStart, zStart + offset, Heightmap.Type.WORLD_SURFACE_WG);
      int k1 = generator.getBaseHeight(xStart + offset, zStart, Heightmap.Type.WORLD_SURFACE_WG);
      int l1 = generator.getBaseHeight(xStart + offset, zStart + offset, Heightmap.Type.WORLD_SURFACE_WG);
      int minHeight = Math.min(Math.min(i1, j1), Math.min(k1, l1));
      int maxHeight = Math.max(Math.max(i1, j1), Math.max(k1, l1));

      return Math.abs(maxHeight - minHeight) <= 4;
   }

   public int getSize() {
      return this.size;
   }

   public double getSpawnChance() {
      return this.getConfig().getSpawnChance();
   }

   protected boolean checkForOtherStructures(ChunkGenerator generator, BiomeProvider provider, long seed,
                                             SharedSeedRandom rand, int chunkX, int chunkZ, Biome biome, ChunkPos pos, IFeatureConfig config,
                                             @Nullable Structure<?>... exeptStructure) {
      for (AbstractStructure<?> s : NewStructureInit.STRUCTURES.values()) {
         if (exeptStructure != null)
            for (Structure<?> es : exeptStructure) {
               if (es.equals(s))
                  continue;
            }
         if (new ChunkPos(chunkX, chunkZ).equals(s.getPotentialFeatureChunk(
               new StructureSeparationSettings(s.getDistance(), s.getSeparation(), s.getSeedModifier()), seed,
               rand, chunkX, chunkZ))
               && s.validateGeneration(generator, provider, seed, rand, chunkX, chunkZ, biome, pos, config,
               Lists.asList(s, exeptStructure).toArray(new Structure<?>[exeptStructure.length]))) {
            return false;
         }
      }
      return true;
   }

   public int getDistance() {
      return (int) (this.getConfig().getDistance() * Config.SETTINGS.DISTANCE_FACTOR.get());
   }

   public int getSeparation() {
      return (int) (this.getConfig().getSeparation() * Config.SETTINGS.SEPERATION_FACTOR.get());
   }

   public int getSeedModifier() {
      return this.getConfig().getSeed();
   }
}