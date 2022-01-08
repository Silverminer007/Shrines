/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.structures.guardian_meeting;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.ConfigBuilder;
import com.silverminer.shrines.config.ConfigBuilder.Type;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.AbstractStructureStart;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class GuardianMeetingStructure extends AbstractStructure<NoFeatureConfig> {
   protected static final ConfigBuilder GUARDIANMEETING_CONFIG = new ConfigBuilder("Guardian Meeting", 1498473232,
         Type.LOOTABLE).setLootChance(1.0D).setDistance(70).setSeparation(17).setUseRandomVarianting(false);

   public GuardianMeetingStructure(Codec<NoFeatureConfig> codec) {
      super(codec, 3, "guardian_meeting", GUARDIANMEETING_CONFIG);
   }

   @Override
   public GenerationStage.Decoration step() {
      return GenerationStage.Decoration.SURFACE_STRUCTURES;
   }

   @Override
   public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
      return GuardianMeetingStructure.Start::new;
   }

   public static class Start extends AbstractStructureStart<NoFeatureConfig> {

      public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingbox,
                   int p_i225806_5_, long seed) {
         super(structure, chunkX, chunkZ, boundingbox, p_i225806_5_, seed);
      }

      @Override
      public void generatePieces(DynamicRegistries registries, ChunkGenerator chunkGenerator,
                                 TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
         int i = chunkX * 16;
         int j = chunkZ * 16;
         BlockPos blockpos = new BlockPos(i, -1, j);
         Rotation rotation = Rotation.getRandom(this.random);
         GuardianMeetingPiece.generate(templateManager, blockpos, rotation, this.pieces, this.random,
               chunkGenerator);
         this.calculateBoundingBox();
      }
   }
}