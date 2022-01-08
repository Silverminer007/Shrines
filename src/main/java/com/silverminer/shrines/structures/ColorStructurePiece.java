/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

public abstract class ColorStructurePiece extends AbstractStructurePiece {
   protected static final Logger LOG = LogManager.getLogger(ColorStructurePiece.class);
   protected static final ArrayList<Block> WOOLS = Lists.newArrayList(Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL,
         Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.YELLOW_WOOL, Blocks.LIME_WOOL, Blocks.PINK_WOOL,
         Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL, Blocks.BLACK_WOOL, Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL,
         Blocks.BLUE_WOOL, Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL);

   protected static final ArrayList<Block> TERRACOTTAS = Lists.newArrayList(Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA,
         Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA,
         Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA,
         Blocks.BLACK_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA,
         Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA);

   protected static final ArrayList<Block> GLAZED_TERRACOTTAS = Lists.newArrayList(Blocks.WHITE_GLAZED_TERRACOTTA,
         Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA,
         Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA,
         Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA,
         Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA,
         Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA);

   protected static final ArrayList<Block> CONCRETE = Lists.newArrayList(Blocks.WHITE_CONCRETE, Blocks.ORANGE_CONCRETE,
         Blocks.MAGENTA_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE, Blocks.YELLOW_CONCRETE, Blocks.LIME_CONCRETE,
         Blocks.PINK_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE, Blocks.BLACK_CONCRETE,
         Blocks.CYAN_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.BLUE_CONCRETE, Blocks.BROWN_CONCRETE,
         Blocks.GREEN_CONCRETE, Blocks.RED_CONCRETE);

   protected static final ArrayList<Block> CONCRETE_POWDERS = Lists.newArrayList(Blocks.WHITE_CONCRETE_POWDER,
         Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER,
         Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER,
         Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER,
         Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER,
         Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER);
   protected static final ArrayList<Block> PLANKS = Lists.newArrayList(Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS,
         Blocks.DARK_OAK_PLANKS, Blocks.BIRCH_PLANKS, Blocks.ACACIA_PLANKS, Blocks.JUNGLE_PLANKS);
   protected static final ArrayList<Block> ORES = Lists.newArrayList(Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE,
         Blocks.DIAMOND_ORE, Blocks.REDSTONE_ORE, Blocks.EMERALD_ORE);

   protected static final ArrayList<Block> STONES = Lists.newArrayList(Blocks.COBBLESTONE, Blocks.STONE_BRICKS,
         Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.MOSSY_COBBLESTONE, Blocks.MOSSY_STONE_BRICKS,
         Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_STONE_BRICKS);

   protected static final ArrayList<Block> BEES = Lists.newArrayList(Blocks.BEEHIVE, Blocks.BEE_NEST);
   protected final HashMap<Block, Block> COLORS = new HashMap<Block, Block>();
   protected final ArrayList<BlockPos> CHANGED_POS = new ArrayList<BlockPos>();
   protected boolean defaultValue = true;

   public ColorStructurePiece(IStructurePieceType pieceType, TemplateManager templateManager,
                              ResourceLocation location, BlockPos pos, Rotation rotation, int componentTypeIn, boolean defaultValue, int height) {
      super(pieceType, templateManager, location, pos, rotation, componentTypeIn, height);
      this.defaultValue = defaultValue;
      this.addOresOfMoreOre();
   }

   protected void addOresOfMoreOre() {
      Block ruby = ShrinesMod.getInstance().getFunctionProvider().getBlockByID("moreore:rubin_ore");
      if (ruby != null && ruby != Blocks.AIR && !ORES.contains(ruby))
         ORES.add(ruby);
      Block saphire = ShrinesMod.getInstance().getFunctionProvider().getBlockByID("moreore:saphir_ore");
      if (saphire != null && ruby != Blocks.AIR && !ORES.contains(saphire))
         ORES.add(saphire);
      Block silver = ShrinesMod.getInstance().getFunctionProvider().getBlockByID("moreore:silver_ore");
      if (silver != null && ruby != Blocks.AIR && !ORES.contains(silver))
         ORES.add(silver);
      Block alexandrit = ShrinesMod.getInstance().getFunctionProvider().getBlockByID("moreore:alexandrit_ore");
      if (alexandrit != null && ruby != Blocks.AIR && !ORES.contains(alexandrit))
         ORES.add(alexandrit);
   }

   public ColorStructurePiece(IStructurePieceType pieceType, TemplateManager templateManager, CompoundNBT cNBT) {
      super(pieceType, templateManager, cNBT);
      if (cNBT.contains("DefaultValue"))
         this.defaultValue = cNBT.getBoolean("DefaultValue");
      this.addOresOfMoreOre();
   }

   /**
    * (abstract) Helper method to read subclass data from NBT
    */
   protected void addAdditionalSaveData(CompoundNBT tagCompound) {
      super.addAdditionalSaveData(tagCompound);
      tagCompound.putBoolean("DefaultValue", this.defaultValue);
   }

   public boolean postProcess(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGen,
                              Random rand, MutableBoundingBox mbb, ChunkPos chunkPos, BlockPos blockPos) {
      boolean flag = super.postProcess(world, structureManager, chunkGen, rand, mbb, chunkPos, blockPos);
      Biome biome = chunkGen.getBiomeSource().getNoiseBiome(chunkPos.x, 0, chunkPos.z);
      if (this.useRandomVarianting()) {
         if (this.overwriteWool()) {
            for (Block block : WOOLS) {
               if (COLORS.get(block) == null)
                  COLORS.put(block, WOOLS.get(rand.nextInt(WOOLS.size())));
               BlockState newBlock = COLORS.get(block).defaultBlockState();
               for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                     this.placeSettings, block)) {
                  if (block == Blocks.BLACK_WOOL)
                     newBlock = WOOLS.get(rand.nextInt(WOOLS.size())).defaultBlockState();
                  this.changeBlock(template$blockinfo.pos, newBlock, world, rand);
               }
            }
         }
         if (this.overwriteWood()) {
            for (Block block : PLANKS) {
               if (COLORS.get(block) == null) {
                  COLORS.put(block, this.getPlankByBiome(biome, block, rand));
               }
               Block newBlock = COLORS.get(block);
               if (this.overwritePlanks()) {
                  for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                        this.placeSettings, block)) {
                     this.changeBlock(template$blockinfo.pos, newBlock.defaultBlockState(), world, rand);
                  }
               }
               if (this.overwriteSlabs()) {
                  for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                        this.placeSettings, this.getSlab(block))) {
                     this.changeBlock(template$blockinfo.pos, this.getSlab(newBlock).defaultBlockState()
                                 .setValue(SlabBlock.TYPE, template$blockinfo.state.getValue(SlabBlock.TYPE)), world,
                           rand);
                  }
               }
               if (this.overwriteFences()) {
                  for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                        this.placeSettings, this.getFence(block))) {
                     this.changeBlock(template$blockinfo.pos, this.getFence(newBlock).defaultBlockState()
                                 .setValue(FenceBlock.NORTH, template$blockinfo.state.getValue(FenceBlock.NORTH))
                                 .setValue(FenceBlock.EAST, template$blockinfo.state.getValue(FenceBlock.EAST))
                                 .setValue(FenceBlock.SOUTH, template$blockinfo.state.getValue(FenceBlock.SOUTH))
                                 .setValue(FenceBlock.WEST, template$blockinfo.state.getValue(FenceBlock.WEST))
                                 .setValue(FenceBlock.WATERLOGGED,
                                       template$blockinfo.state.getValue(FenceBlock.WATERLOGGED)),
                           world, rand);
                  }
               }
               if (this.overwriteLogs()) {
                  for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                        this.placeSettings, this.getLog(block, false))) {
                     this.changeBlock(template$blockinfo.pos,
                           this.getLog(newBlock, false).defaultBlockState().setValue(RotatedPillarBlock.AXIS,
                                 template$blockinfo.state.getValue(RotatedPillarBlock.AXIS)),
                           world, rand);
                  }
               }
               if (this.overwriteStrippedLogs()) {
                  for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                        this.placeSettings, this.getLog(block, true))) {
                     this.changeBlock(template$blockinfo.pos,
                           this.getLog(newBlock, true).defaultBlockState().setValue(RotatedPillarBlock.AXIS,
                                 template$blockinfo.state.getValue(RotatedPillarBlock.AXIS)),
                           world, rand);
                  }
               }
               if (this.overwriteTrapdoors()) {
                  for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                        this.placeSettings, this.getTrapdoor(block))) {
                     this.changeBlock(template$blockinfo.pos, this.getTrapdoor(newBlock).defaultBlockState()
                                 .setValue(TrapDoorBlock.HALF, template$blockinfo.state.getValue(TrapDoorBlock.HALF))
                                 .setValue(TrapDoorBlock.OPEN, template$blockinfo.state.getValue(TrapDoorBlock.OPEN))
                                 .setValue(TrapDoorBlock.POWERED,
                                       template$blockinfo.state.getValue(TrapDoorBlock.POWERED))
                                 .setValue(TrapDoorBlock.WATERLOGGED,
                                       template$blockinfo.state.getValue(TrapDoorBlock.WATERLOGGED))
                                 .setValue(TrapDoorBlock.FACING,
                                       template$blockinfo.state.getValue(TrapDoorBlock.FACING)),
                           world, rand);
                  }
               }
               if (this.overwriteDoors()) {
                  for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                        this.placeSettings, this.getDoor(block))) {
                     this.changeBlock(template$blockinfo.pos, this.getDoor(newBlock).defaultBlockState()
                                 .setValue(DoorBlock.OPEN, template$blockinfo.state.getValue(DoorBlock.OPEN))
                                 .setValue(DoorBlock.HINGE, template$blockinfo.state.getValue(DoorBlock.HINGE))
                                 .setValue(DoorBlock.FACING, template$blockinfo.state.getValue(DoorBlock.FACING))
                                 .setValue(DoorBlock.HALF, template$blockinfo.state.getValue(DoorBlock.HALF))
                                 .setValue(DoorBlock.POWERED, template$blockinfo.state.getValue(DoorBlock.POWERED)),
                           world, rand);
                  }
               }
               if (this.overwriteStairs()) {
                  for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                        this.placeSettings, this.getStairs(block))) {
                     this.changeBlock(template$blockinfo.pos, this.getStairs(newBlock).defaultBlockState()
                                 .setValue(StairsBlock.WATERLOGGED,
                                       template$blockinfo.state.getValue(StairsBlock.WATERLOGGED))
                                 .setValue(StairsBlock.FACING, template$blockinfo.state.getValue(StairsBlock.FACING))
                                 .setValue(StairsBlock.HALF, template$blockinfo.state.getValue(StairsBlock.HALF))
                                 .setValue(StairsBlock.SHAPE, template$blockinfo.state.getValue(StairsBlock.SHAPE)),
                           world, rand);
                  }
               }
               if (this.overwriteSigns()) {
                  for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                        this.placeSettings, this.getSign(block, true))) {
                     this.changeBlock(template$blockinfo.pos,
                           this.getSign(newBlock, true).defaultBlockState()
                                 .setValue(AbstractSignBlock.WATERLOGGED,
                                       template$blockinfo.state.getValue(AbstractSignBlock.WATERLOGGED))
                                 .setValue(WallSignBlock.FACING,
                                       template$blockinfo.state.getValue(WallSignBlock.FACING)),
                           world, rand);
                  }
                  for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                        this.placeSettings, this.getSign(block, false))) {
                     this.changeBlock(template$blockinfo.pos,
                           this.getSign(newBlock, true).defaultBlockState()
                                 .setValue(AbstractSignBlock.WATERLOGGED,
                                       template$blockinfo.state.getValue(AbstractSignBlock.WATERLOGGED))
                                 .setValue(StandingSignBlock.ROTATION,
                                       template$blockinfo.state.getValue(StandingSignBlock.ROTATION)),
                           world, rand);
                  }
               }
               if (this.overwriteButtons()) {
                  for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                        this.placeSettings, this.getButton(block))) {
                     this.changeBlock(template$blockinfo.pos,
                           this.getButton(newBlock).defaultBlockState()
                                 .setValue(AbstractButtonBlock.POWERED,
                                       template$blockinfo.state.getValue(AbstractButtonBlock.POWERED))
                                 .setValue(AbstractButtonBlock.FACE,
                                       template$blockinfo.state.getValue(AbstractButtonBlock.FACE))
                                 .setValue(AbstractButtonBlock.FACING,
                                       template$blockinfo.state.getValue(AbstractButtonBlock.FACING)),
                           world, rand);
                  }
               }
            }
         }
         if (this.overwriteTerracotta()) {
            for (Block block : TERRACOTTAS) {
               if (COLORS.get(block) == null)
                  COLORS.put(block, TERRACOTTAS.get(rand.nextInt(TERRACOTTAS.size())));
               BlockState newBlock = COLORS.get(block).defaultBlockState();
               for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                     this.placeSettings, block)) {
                  if (block == Blocks.BLACK_TERRACOTTA)
                     newBlock = TERRACOTTAS.get(rand.nextInt(TERRACOTTAS.size())).defaultBlockState();
                  this.changeBlock(template$blockinfo.pos, newBlock, world, rand);
               }
            }
         }
         if (this.overwriteGlazedTerracotta()) {
            for (Block block : GLAZED_TERRACOTTAS) {
               if (COLORS.get(block) == null)
                  COLORS.put(block, GLAZED_TERRACOTTAS.get(rand.nextInt(GLAZED_TERRACOTTAS.size())));
               BlockState newBlock = COLORS.get(block).defaultBlockState();
               for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                     this.placeSettings, block)) {
                  if (block == Blocks.BLACK_GLAZED_TERRACOTTA)
                     newBlock = GLAZED_TERRACOTTAS.get(rand.nextInt(GLAZED_TERRACOTTAS.size()))
                           .defaultBlockState();
                  this.changeBlock(template$blockinfo.pos, newBlock, world, rand);
               }
            }
         }
         if (this.overwriteConcrete()) {
            for (Block block : CONCRETE) {
               if (COLORS.get(block) == null)
                  COLORS.put(block, CONCRETE.get(rand.nextInt(CONCRETE.size())));
               BlockState newBlock = COLORS.get(block).defaultBlockState();
               for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                     this.placeSettings, block)) {
                  if (block == Blocks.BLACK_CONCRETE)
                     newBlock = CONCRETE.get(rand.nextInt(CONCRETE.size())).defaultBlockState();
                  this.changeBlock(template$blockinfo.pos, newBlock, world, rand);
               }
            }
         }
         if (this.overwriteConcretePowder()) {
            for (Block block : CONCRETE_POWDERS) {
               if (COLORS.get(block) == null)
                  COLORS.put(block, CONCRETE_POWDERS.get(rand.nextInt(CONCRETE_POWDERS.size())));
               BlockState newBlock = COLORS.get(block).defaultBlockState();
               for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                     this.placeSettings, block)) {
                  if (block == Blocks.BLACK_CONCRETE_POWDER)
                     newBlock = CONCRETE_POWDERS.get(rand.nextInt(CONCRETE_POWDERS.size())).defaultBlockState();
                  this.changeBlock(template$blockinfo.pos, newBlock, world, rand);
               }
            }
         }
         if (this.overwriteStone()) {
            for (Block block : STONES) {
               if (COLORS.get(block) == null)
                  COLORS.put(block, STONES.get(rand.nextInt(STONES.size())));
               Block newBlock = COLORS.get(block);
               Block newBlock2 = newBlock;
               for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                     this.placeSettings, block)) {
                  newBlock2 = newBlock;
                  if (rand.nextFloat() < this.getStoneChangeChance())
                     newBlock2 = STONES.get(rand.nextInt(STONES.size()));
                  this.changeBlock(template$blockinfo.pos, newBlock2.defaultBlockState(), world, rand);
               }
               if (this.overwriteSlabs()) {
                  for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                        this.placeSettings, this.getSlab(block))) {
                     newBlock2 = newBlock;
                     if (rand.nextFloat() < this.getStoneChangeChance())
                        newBlock2 = STONES.get(rand.nextInt(STONES.size()));
                     this.changeBlock(template$blockinfo.pos, this.getSlab(newBlock2).defaultBlockState()
                                 .setValue(SlabBlock.TYPE, template$blockinfo.state.getValue(SlabBlock.TYPE)), world,
                           rand);
                  }
               }
               if (this.overwriteStairs()) {
                  for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                        this.placeSettings, this.getStairs(block))) {
                     newBlock2 = newBlock;
                     if (rand.nextFloat() < this.getStoneChangeChance())
                        newBlock2 = STONES.get(rand.nextInt(STONES.size()));
                     this.changeBlock(template$blockinfo.pos, this.getStairs(newBlock2).defaultBlockState()
                                 .setValue(StairsBlock.WATERLOGGED,
                                       template$blockinfo.state.getValue(StairsBlock.WATERLOGGED))
                                 .setValue(StairsBlock.FACING, template$blockinfo.state.getValue(StairsBlock.FACING))
                                 .setValue(StairsBlock.HALF, template$blockinfo.state.getValue(StairsBlock.HALF))
                                 .setValue(StairsBlock.SHAPE, template$blockinfo.state.getValue(StairsBlock.SHAPE)),
                           world, rand);
                  }
               }
            }
         }
      }
      if (this.overwriteBeehives()) {
         for (Block block : BEES) {
            if (COLORS.get(block) == null)
               COLORS.put(block, BEES.get(rand.nextInt(BEES.size())));
            BlockState newBlock = COLORS.get(block).defaultBlockState();
            for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                  this.placeSettings, block)) {
               this.changeBlock(template$blockinfo.pos,
                     newBlock.setValue(BeehiveBlock.FACING,
                                 template$blockinfo.state.getValue(BeehiveBlock.FACING))
                           .setValue(BeehiveBlock.HONEY_LEVEL,
                                 template$blockinfo.state.getValue(BeehiveBlock.HONEY_LEVEL)),
                     world, rand);
            }
         }
      }
      if (this.overwriteOres()) {
         for (Block block : ORES) {
            if (COLORS.get(block) == null)
               COLORS.put(block, ORES.get(rand.nextInt(ORES.size())));
            BlockState newBlock = COLORS.get(block).defaultBlockState();
            for (Template.BlockInfo template$blockinfo : this.template.filterBlocks(this.templatePosition,
                  this.placeSettings, block)) {
               this.changeBlock(template$blockinfo.pos, newBlock, world, rand);
            }
         }
      }
      return flag;
   }

   protected abstract boolean useRandomVarianting();

   public boolean overwriteWool() {
      return this.defaultValue;
   }

   protected boolean changeBlock(BlockPos pos, BlockState state, ISeedReader world, Random rand) {
      if (!CHANGED_POS.contains(pos) && this.validateBlock(pos, state, world, rand)) {
         CHANGED_POS.add(pos);
         world.setBlock(pos, state, 3);
         return true;
      }
      return false;
   }

   public boolean overwriteWood() {
      return this.defaultValue;
   }

   protected Block getPlankByBiome(Biome biome, Block block, Random rand) {
      if (block != this.getDefaultPlank()) {
         return PLANKS.get(rand.nextInt(PLANKS.size() - 1));
      } else {
         switch (biome.getBiomeCategory()) {
            case TAIGA:
               return Blocks.SPRUCE_PLANKS;
            case SAVANNA:
               return Blocks.ACACIA_PLANKS;
            case JUNGLE:
               return Blocks.JUNGLE_PLANKS;
            case SWAMP:
               return Blocks.OAK_PLANKS;
            case FOREST:
               if (biome.getRegistryName() == Biomes.BIRCH_FOREST.getRegistryName()
                     || biome.getRegistryName() == Biomes.BIRCH_FOREST_HILLS.getRegistryName()
                     || biome.getRegistryName() == Biomes.TALL_BIRCH_FOREST.getRegistryName()
                     || biome.getRegistryName() == Biomes.TALL_BIRCH_HILLS.getRegistryName()) {
                  return Blocks.BIRCH_PLANKS;
               } else if (biome.getRegistryName() == Biomes.DARK_FOREST.getRegistryName()
                     || biome.getRegistryName() == Biomes.DARK_FOREST_HILLS.getRegistryName()) {
                  return Blocks.ACACIA_PLANKS;
               } else {
                  return Blocks.OAK_PLANKS;
               }
            default:
               return PLANKS.get(rand.nextInt(PLANKS.size() - 1));
         }
      }
   }

   public boolean overwritePlanks() {
      return this.defaultValue;
   }

   public boolean overwriteSlabs() {
      return this.defaultValue;
   }

   protected Block getSlab(Block plank) {
      if (plank == Blocks.OAK_PLANKS) {
         return Blocks.OAK_SLAB;
      } else if (plank == Blocks.DARK_OAK_PLANKS) {
         return Blocks.DARK_OAK_SLAB;
      } else if (plank == Blocks.BIRCH_PLANKS) {
         return Blocks.BIRCH_SLAB;
      } else if (plank == Blocks.JUNGLE_PLANKS) {
         return Blocks.JUNGLE_SLAB;
      } else if (plank == Blocks.ACACIA_PLANKS) {
         return Blocks.ACACIA_SLAB;
      } else if (plank == Blocks.COBBLESTONE) {
         return Blocks.COBBLESTONE_SLAB;
      } else if (plank == Blocks.STONE_BRICKS) {
         return Blocks.STONE_BRICK_SLAB;
      } else if (plank == Blocks.POLISHED_BLACKSTONE_BRICKS) {
         return Blocks.POLISHED_BLACKSTONE_BRICK_SLAB;
      } else if (plank == Blocks.MOSSY_COBBLESTONE) {
         return Blocks.MOSSY_COBBLESTONE_SLAB;
      } else if (plank == Blocks.MOSSY_STONE_BRICKS) {
         return Blocks.MOSSY_STONE_BRICK_SLAB;
      } else if (plank == Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS) {
         return Blocks.POLISHED_BLACKSTONE_BRICK_SLAB;
      } else if (plank == Blocks.CRACKED_STONE_BRICKS) {
         return Blocks.STONE_BRICK_SLAB;
      } else {
         return Blocks.SPRUCE_SLAB;
      }
   }

   public boolean overwriteFences() {
      return this.defaultValue;
   }

   protected Block getFence(Block plank) {
      if (plank == Blocks.OAK_PLANKS) {
         return Blocks.OAK_FENCE;
      } else if (plank == Blocks.DARK_OAK_PLANKS) {
         return Blocks.DARK_OAK_FENCE;
      } else if (plank == Blocks.BIRCH_PLANKS) {
         return Blocks.BIRCH_FENCE;
      } else if (plank == Blocks.JUNGLE_PLANKS) {
         return Blocks.JUNGLE_FENCE;
      } else if (plank == Blocks.ACACIA_PLANKS) {
         return Blocks.ACACIA_FENCE;
      } else {
         return Blocks.SPRUCE_FENCE;
      }
   }

   public boolean overwriteLogs() {
      return this.defaultValue;
   }

   protected Block getLog(Block plank, boolean stripped) {
      if (plank == Blocks.OAK_PLANKS) {
         if (stripped)
            return Blocks.STRIPPED_OAK_LOG;
         else
            return Blocks.OAK_LOG;
      } else if (plank == Blocks.DARK_OAK_PLANKS) {
         if (stripped)
            return Blocks.STRIPPED_DARK_OAK_LOG;
         else
            return Blocks.DARK_OAK_LOG;
      } else if (plank == Blocks.BIRCH_PLANKS) {
         if (stripped)
            return Blocks.STRIPPED_BIRCH_LOG;
         else
            return Blocks.BIRCH_LOG;
      } else if (plank == Blocks.JUNGLE_PLANKS) {
         if (stripped)
            return Blocks.STRIPPED_JUNGLE_LOG;
         else
            return Blocks.JUNGLE_LOG;
      } else if (plank == Blocks.ACACIA_PLANKS) {
         if (stripped)
            return Blocks.STRIPPED_ACACIA_LOG;
         else
            return Blocks.ACACIA_LOG;
      } else {
         if (stripped)
            return Blocks.STRIPPED_SPRUCE_LOG;
         else
            return Blocks.SPRUCE_LOG;
      }
   }

   public boolean overwriteStrippedLogs() {
      return this.defaultValue;
   }

   public boolean overwriteTrapdoors() {
      return this.defaultValue;
   }

   protected Block getTrapdoor(Block plank) {
      if (plank == Blocks.OAK_PLANKS) {
         return Blocks.OAK_TRAPDOOR;
      } else if (plank == Blocks.DARK_OAK_PLANKS) {
         return Blocks.DARK_OAK_TRAPDOOR;
      } else if (plank == Blocks.BIRCH_PLANKS) {
         return Blocks.BIRCH_TRAPDOOR;
      } else if (plank == Blocks.JUNGLE_PLANKS) {
         return Blocks.JUNGLE_TRAPDOOR;
      } else if (plank == Blocks.ACACIA_PLANKS) {
         return Blocks.ACACIA_TRAPDOOR;
      } else {
         return Blocks.SPRUCE_TRAPDOOR;
      }
   }

   public boolean overwriteDoors() {
      return this.defaultValue;
   }

   protected Block getDoor(Block plank) {
      if (plank == Blocks.OAK_PLANKS) {
         return Blocks.OAK_DOOR;
      } else if (plank == Blocks.DARK_OAK_PLANKS) {
         return Blocks.DARK_OAK_DOOR;
      } else if (plank == Blocks.BIRCH_PLANKS) {
         return Blocks.BIRCH_DOOR;
      } else if (plank == Blocks.JUNGLE_PLANKS) {
         return Blocks.JUNGLE_DOOR;
      } else if (plank == Blocks.ACACIA_PLANKS) {
         return Blocks.ACACIA_DOOR;
      } else {
         return Blocks.SPRUCE_DOOR;
      }
   }

   public boolean overwriteStairs() {
      return this.defaultValue;
   }

   protected Block getStairs(Block plank) {
      if (plank == Blocks.OAK_PLANKS) {
         return Blocks.OAK_STAIRS;
      } else if (plank == Blocks.DARK_OAK_PLANKS) {
         return Blocks.DARK_OAK_STAIRS;
      } else if (plank == Blocks.BIRCH_PLANKS) {
         return Blocks.BIRCH_STAIRS;
      } else if (plank == Blocks.JUNGLE_PLANKS) {
         return Blocks.JUNGLE_STAIRS;
      } else if (plank == Blocks.ACACIA_PLANKS) {
         return Blocks.ACACIA_STAIRS;
      } else if (plank == Blocks.COBBLESTONE) {
         return Blocks.COBBLESTONE_STAIRS;
      } else if (plank == Blocks.STONE_BRICKS) {
         return Blocks.STONE_BRICK_STAIRS;
      } else if (plank == Blocks.POLISHED_BLACKSTONE_BRICKS) {
         return Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS;
      } else if (plank == Blocks.MOSSY_COBBLESTONE) {
         return Blocks.MOSSY_COBBLESTONE_STAIRS;
      } else if (plank == Blocks.MOSSY_STONE_BRICKS) {
         return Blocks.MOSSY_STONE_BRICK_STAIRS;
      } else if (plank == Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS) {
         return Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS;
      } else if (plank == Blocks.CRACKED_STONE_BRICKS) {
         return Blocks.STONE_BRICK_STAIRS;
      } else {
         return Blocks.SPRUCE_STAIRS;
      }
   }

   public boolean overwriteSigns() {
      return this.defaultValue;
   }

   protected Block getSign(Block plank, boolean wall) {
      if (plank == Blocks.OAK_PLANKS) {
         if (wall)
            return Blocks.OAK_WALL_SIGN;
         else
            return Blocks.OAK_SIGN;
      } else if (plank == Blocks.DARK_OAK_PLANKS) {
         if (wall)
            return Blocks.DARK_OAK_WALL_SIGN;
         else
            return Blocks.DARK_OAK_SIGN;
      } else if (plank == Blocks.BIRCH_PLANKS) {
         if (wall)
            return Blocks.BIRCH_WALL_SIGN;
         else
            return Blocks.BIRCH_SIGN;
      } else if (plank == Blocks.JUNGLE_PLANKS) {
         if (wall)
            return Blocks.JUNGLE_WALL_SIGN;
         else
            return Blocks.JUNGLE_SIGN;
      } else if (plank == Blocks.ACACIA_PLANKS) {
         if (wall)
            return Blocks.ACACIA_WALL_SIGN;
         else
            return Blocks.ACACIA_SIGN;
      } else {
         if (wall)
            return Blocks.SPRUCE_WALL_SIGN;
         else
            return Blocks.SPRUCE_SIGN;
      }
   }

   public boolean overwriteButtons() {
      return this.defaultValue;
   }

   protected Block getButton(Block plank) {
      if (plank == Blocks.OAK_PLANKS) {
         return Blocks.OAK_BUTTON;
      } else if (plank == Blocks.DARK_OAK_PLANKS) {
         return Blocks.DARK_OAK_BUTTON;
      } else if (plank == Blocks.BIRCH_PLANKS) {
         return Blocks.BIRCH_BUTTON;
      } else if (plank == Blocks.JUNGLE_PLANKS) {
         return Blocks.JUNGLE_BUTTON;
      } else if (plank == Blocks.ACACIA_PLANKS) {
         return Blocks.ACACIA_BUTTON;
      } else {
         return Blocks.SPRUCE_BUTTON;
      }
   }

   public boolean overwriteTerracotta() {
      return this.defaultValue;
   }

   public boolean overwriteGlazedTerracotta() {
      return this.defaultValue;
   }

   public boolean overwriteConcrete() {
      return this.defaultValue;
   }

   public boolean overwriteConcretePowder() {
      return this.defaultValue;
   }

   public boolean overwriteStone() {
      return this.defaultValue;
   }

   public float getStoneChangeChance() {
      return 0.05F;
   }

   public boolean overwriteBeehives() {
      return this.defaultValue;
   }

   public boolean overwriteOres() {
      return false;
   }

   public boolean validateBlock(BlockPos pos, BlockState newState, ISeedReader world, Random rand) {
      return true;
   }

   public Block getDefaultPlank() {
      return Blocks.OAK_PLANKS;
   }

   @Override
   protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
                                   MutableBoundingBox sbb) {
      Block block = ShrinesMod.getInstance().getFunctionProvider().getBlockByID(function);
      if (block != Blocks.AIR) {
         worldIn.setBlock(pos, block.defaultBlockState(), 2);
         CHANGED_POS.add(pos);
      }
   }
}