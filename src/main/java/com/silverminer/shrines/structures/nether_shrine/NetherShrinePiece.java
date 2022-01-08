/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.structures.nether_shrine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;
import com.silverminer.shrines.utils.StructureUtils;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class NetherShrinePiece {
   private static final ArrayList<ResourceLocation> location = Lists.newArrayList(
         new ResourceLocation("shrines:nether_shrine/nether_shrine_001"),
         new ResourceLocation("shrines:nether_shrine/nether_shrine_002"),
         new ResourceLocation("shrines:nether_shrine/nether_shrine_003"),
         new ResourceLocation("shrines:nether_shrine/nether_shrine_004"),
         new ResourceLocation("shrines:nether_shrine/nether_shrine_005"),
         new ResourceLocation("shrines:nether_shrine/nether_shrine_006"),
         new ResourceLocation("shrines:nether_shrine/nether_shrine_007"),
         new ResourceLocation("shrines:nether_shrine/nether_shrine_008"),
         new ResourceLocation("shrines:nether_shrine/nether_shrine_009"),
         new ResourceLocation("shrines:nether_shrine/nether_shrine_010"),
         new ResourceLocation("shrines:nether_shrine/nether_shrine_011"));
   private static final ResourceLocation sandstone = new ResourceLocation(
         "shrines:nether_shrine/nether_shrine_sandstone");

   public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
                               List<StructurePiece> pieces, Random random, ChunkGenerator chunkGenerator) {
      int size = 32;
      MutableBoundingBox mbb = MutableBoundingBox.createProper(-size, 0, -size, size, 0, size);
      mbb.move(pos);
      int height = StructureUtils.getHeight(chunkGenerator, new BlockPos(mbb.x0, mbb.y0, mbb.z0), mbb,
            random);
      boolean debug = false;
      if (!debug) {
         List<Category> cats = chunkGenerator.getBiomeSource().getBiomesWithin(pos.getX(), height, pos.getZ(), 32)
               .stream().map(biome -> biome.getBiomeCategory()).collect(Collectors.toList());
         if (cats.contains(Category.DESERT) || cats.contains(Category.MESA)
               || (cats.contains(Category.SAVANNA) && random.nextFloat() < 0.05f)) {
            pieces.add(new NetherShrinePiece.Piece(templateManager, sandstone, pos, rotation, 0, height));
         } else {
            pieces.add(new NetherShrinePiece.Piece(templateManager, location.get(random.nextInt(location.size())),
                  pos, rotation, 0, height));
         }
      } else {
         pieces.add(new NetherShrinePiece.Piece(templateManager, sandstone, pos, rotation, 0, height));
      }
   }

   public static class Piece extends ColorStructurePiece {

      public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
                   int componentTypeIn, int height) {
         super(StructurePieceTypes.NETHER_SHRINE, templateManager, location, pos, rotation, componentTypeIn, true,
               height);
      }

      public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
         super(StructurePieceTypes.NETHER_SHRINE, templateManager, cNBT);
      }

      @Override
      protected boolean useRandomVarianting() {
         return NewStructureInit.STRUCTURES.get("nether_shrine").getConfig().getUseRandomVarianting();
      }

      public float getStoneChangeChance() {
         return 0.2F;
      }

      @Override
      protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
                                      MutableBoundingBox sbb) {
         boolean loot = NewStructureInit.STRUCTURES.get("nether_shrine").getConfig().getLootChance() > rand.nextDouble();
         if ("chest1".equals(function) || "chest2".equals(function)) {
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(),
                  loot ? ShrinesLootTables.getRandomNetherLoot(rand) : ShrinesLootTables.EMPTY);
         }
      }

      @Override
      public StructureProcessor getProcessor() {
         return BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR;
      }
   }
}