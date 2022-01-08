/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.worldgen.structures;

import com.google.common.collect.ImmutableList;
import com.silverminer.shrines.packages.datacontainer.NestedVariationConfiguration;
import com.silverminer.shrines.packages.datacontainer.SimpleVariationConfiguration;
import net.minecraft.world.level.block.*;

import java.util.List;

public class VariationsPool {
   private static final List<Block> WOOLS = ImmutableList.of(Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL,
         Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.YELLOW_WOOL, Blocks.LIME_WOOL, Blocks.PINK_WOOL,
         Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL, Blocks.BLACK_WOOL, Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL,
         Blocks.BLUE_WOOL, Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL);
   public static final Variation<SimpleVariationConfiguration> WOOL_VARIATION = new SimpleVariation(WOOLS, SimpleVariationConfiguration::isWoolEnabled);

   private static final List<Block> TERRACOTTA = ImmutableList.of(Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA,
         Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA,
         Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA,
         Blocks.BLACK_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA,
         Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA);
   public static final Variation<SimpleVariationConfiguration> TERRACOTTA_VARIATION = new SimpleVariation(TERRACOTTA, SimpleVariationConfiguration::isTerracottaEnabled);

   private static final List<Block> GLAZED_TERRACOTTA = ImmutableList.of(Blocks.WHITE_GLAZED_TERRACOTTA,
         Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA,
         Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA,
         Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA,
         Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA,
         Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA);
   public static final Variation<SimpleVariationConfiguration> GLAZED_TERRACOTTA_VARIATION = new SimpleVariation(GLAZED_TERRACOTTA, SimpleVariationConfiguration::isGlazedTerracottaEnabled);

   private static final List<Block> CONCRETE = ImmutableList.of(Blocks.WHITE_CONCRETE, Blocks.ORANGE_CONCRETE,
         Blocks.MAGENTA_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE, Blocks.YELLOW_CONCRETE, Blocks.LIME_CONCRETE,
         Blocks.PINK_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE, Blocks.BLACK_CONCRETE,
         Blocks.CYAN_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.BLUE_CONCRETE, Blocks.BROWN_CONCRETE,
         Blocks.GREEN_CONCRETE, Blocks.RED_CONCRETE);
   public static final Variation<SimpleVariationConfiguration> CONCRETE_VARIATION = new SimpleVariation(CONCRETE, SimpleVariationConfiguration::isConcreteEnabled);

   private static final List<Block> CONCRETE_POWDERS = ImmutableList.of(Blocks.WHITE_CONCRETE_POWDER,
         Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER,
         Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER,
         Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER,
         Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER,
         Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER);
   public static final Variation<SimpleVariationConfiguration> CONCRETE_POWDERS_VARIATION = new SimpleVariation(CONCRETE_POWDERS, SimpleVariationConfiguration::isConcretePowderEnabled);

   private static final List<Block> PLANKS = ImmutableList.of(Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS,
         Blocks.DARK_OAK_PLANKS, Blocks.BIRCH_PLANKS, Blocks.ACACIA_PLANKS, Blocks.JUNGLE_PLANKS);
   public static final Variation<SimpleVariationConfiguration> PLANKS_VARIATION = new SimpleVariation(PLANKS, SimpleVariationConfiguration::arePlanksEnabled);

   private static final List<Block> ORES = ImmutableList.of(Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE,
         Blocks.DIAMOND_ORE, Blocks.REDSTONE_ORE, Blocks.EMERALD_ORE);
   public static final Variation<SimpleVariationConfiguration> ORES_VARIATION = new SimpleVariation(ORES, SimpleVariationConfiguration::areOresEnabled);

   private static final List<Block> STONES = ImmutableList.of(Blocks.COBBLESTONE, Blocks.STONE_BRICKS,
         Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.MOSSY_COBBLESTONE, Blocks.MOSSY_STONE_BRICKS);
   public static final Variation<SimpleVariationConfiguration> STONES_VARIATION = new SimpleVariation(STONES, SimpleVariationConfiguration::areStonesEnabled);

   private static final List<Block> BEES = ImmutableList.of(Blocks.BEEHIVE, Blocks.BEE_NEST);
   public static final Variation<SimpleVariationConfiguration> BEES_VARIATION = new SimpleVariation(BEES, SimpleVariationConfiguration::areBeesEnabled);

   private static final List<Block> WOODEN_SLABS = ImmutableList.of(Blocks.OAK_SLAB, Blocks.SPRUCE_SLAB, Blocks.DARK_OAK_SLAB, Blocks.BIRCH_SLAB, Blocks.ACACIA_SLAB, Blocks.JUNGLE_SLAB);
   public static final Variation<NestedVariationConfiguration> WOODEN_SLABS_VARIATION = new NestedVariation(NestedVariationConfiguration::isAreSlabsEnabled, PLANKS, WOODEN_SLABS,
         (oldBlockState, newBlockState) -> newBlockState.setValue(SlabBlock.TYPE, oldBlockState.getValue(SlabBlock.TYPE)));

   private static final List<Block> STONE_SLABS = ImmutableList.of(Blocks.COBBLESTONE_SLAB, Blocks.STONE_BRICK_SLAB,
         Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB);
   public static final Variation<NestedVariationConfiguration> STONE_SLABS_VARIATION = new NestedVariation(NestedVariationConfiguration::isAreSlabsEnabled, STONES, STONE_SLABS,
         (oldBlockState, newBlockState) -> newBlockState.setValue(SlabBlock.TYPE, oldBlockState.getValue(SlabBlock.TYPE)));

   private static final List<Block> WOODEN_BUTTONS = ImmutableList.of(Blocks.OAK_BUTTON, Blocks.SPRUCE_BUTTON,
         Blocks.DARK_OAK_BUTTON, Blocks.BIRCH_BUTTON, Blocks.ACACIA_BUTTON, Blocks.JUNGLE_BUTTON);
   public static final Variation<NestedVariationConfiguration> WOODEN_BUTTONS_VARIATION = new NestedVariation(NestedVariationConfiguration::isButtonEnabled, PLANKS, WOODEN_BUTTONS,
         (oldBlockState, newBlockState) -> newBlockState
               .setValue(ButtonBlock.POWERED, oldBlockState.getValue(ButtonBlock.POWERED))
               .setValue(ButtonBlock.FACE, oldBlockState.getValue(ButtonBlock.FACE))
               .setValue(ButtonBlock.FACING, oldBlockState.getValue(ButtonBlock.FACING))
   );

   private static final List<Block> WOODEN_STAIRS = ImmutableList.of(Blocks.OAK_STAIRS, Blocks.SPRUCE_STAIRS,
         Blocks.DARK_OAK_STAIRS, Blocks.BIRCH_STAIRS, Blocks.ACACIA_STAIRS, Blocks.JUNGLE_STAIRS);
   public static final Variation<NestedVariationConfiguration> WOODEN_STAIRS_VARIATION = new NestedVariation(NestedVariationConfiguration::isStairEnabled, PLANKS, WOODEN_STAIRS,
         (oldBlockState, newBlockState) -> newBlockState
               .setValue(StairBlock.WATERLOGGED, oldBlockState.getValue(StairBlock.WATERLOGGED))
               .setValue(StairBlock.FACING, oldBlockState.getValue(StairBlock.FACING))
               .setValue(StairBlock.HALF, oldBlockState.getValue(StairBlock.HALF))
               .setValue(StairBlock.SHAPE, oldBlockState.getValue(StairBlock.SHAPE))
   );

   private static final List<Block> STONE_STAIRS = ImmutableList.of(Blocks.COBBLESTONE_STAIRS, Blocks.STONE_BRICK_STAIRS,
         Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS, Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.MOSSY_STONE_BRICK_STAIRS);
   public static final Variation<NestedVariationConfiguration> STONE_STAIRS_VARIATION = new NestedVariation(NestedVariationConfiguration::isStairEnabled, STONES, STONE_STAIRS,
         (oldBlockState, newBlockState) -> newBlockState
               .setValue(StairBlock.WATERLOGGED, oldBlockState.getValue(StairBlock.WATERLOGGED))
               .setValue(StairBlock.FACING, oldBlockState.getValue(StairBlock.FACING))
               .setValue(StairBlock.HALF, oldBlockState.getValue(StairBlock.HALF))
               .setValue(StairBlock.SHAPE, oldBlockState.getValue(StairBlock.SHAPE))
   );

   private static final List<Block> WOODEN_FENCES = ImmutableList.of(Blocks.OAK_FENCE, Blocks.SPRUCE_FENCE,
         Blocks.DARK_OAK_FENCE, Blocks.BIRCH_FENCE, Blocks.ACACIA_FENCE, Blocks.JUNGLE_FENCE);
   public static final Variation<NestedVariationConfiguration> WOODEN_FENCES_VARIATION = new NestedVariation(NestedVariationConfiguration::isFenceEnabled, PLANKS, WOODEN_FENCES,
         (oldBlockState, newBlockState) -> newBlockState
               .setValue(FenceBlock.NORTH, oldBlockState.getValue(FenceBlock.NORTH))
               .setValue(FenceBlock.EAST, oldBlockState.getValue(FenceBlock.EAST))
               .setValue(FenceBlock.SOUTH, oldBlockState.getValue(FenceBlock.SOUTH))
               .setValue(FenceBlock.WEST, oldBlockState.getValue(FenceBlock.WEST))
               .setValue(FenceBlock.WATERLOGGED, oldBlockState.getValue(FenceBlock.WATERLOGGED))
   );

   private static final List<Block> NORMAL_LOGS = ImmutableList.of(Blocks.OAK_LOG, Blocks.SPRUCE_LOG,
         Blocks.DARK_OAK_LOG, Blocks.BIRCH_LOG, Blocks.ACACIA_LOG, Blocks.JUNGLE_LOG);
   public static final Variation<NestedVariationConfiguration> NORMAL_LOGS_VARIATION = new NestedVariation(NestedVariationConfiguration::isAreNormalLogsEnabled, PLANKS, NORMAL_LOGS,
         (oldBlockState, newBlockState) -> newBlockState
               .setValue(RotatedPillarBlock.AXIS, oldBlockState.getValue(RotatedPillarBlock.AXIS))
   );

   private static final List<Block> STRIPPED_LOGS = ImmutableList.of(Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_SPRUCE_LOG,
         Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_BIRCH_LOG, Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_JUNGLE_LOG);
   public static final Variation<NestedVariationConfiguration> STRIPPED_LOGS_VARIATION = new NestedVariation(NestedVariationConfiguration::isAreStrippedLogsEnabled, PLANKS, STRIPPED_LOGS,
         (oldBlockState, newBlockState) -> newBlockState
               .setValue(RotatedPillarBlock.AXIS, oldBlockState.getValue(RotatedPillarBlock.AXIS))
   );

   private static final List<Block> TRAPDOORS = ImmutableList.of(Blocks.OAK_TRAPDOOR, Blocks.SPRUCE_TRAPDOOR,
         Blocks.DARK_OAK_TRAPDOOR, Blocks.BIRCH_TRAPDOOR, Blocks.ACACIA_TRAPDOOR, Blocks.JUNGLE_TRAPDOOR);
   public static final Variation<NestedVariationConfiguration> TRAPDOORS_VARIATION = new NestedVariation(NestedVariationConfiguration::isAreTrapdoorsEnabled, PLANKS, TRAPDOORS,
         (oldBlockState, newBlockState) -> newBlockState
               .setValue(TrapDoorBlock.HALF, oldBlockState.getValue(TrapDoorBlock.HALF))
               .setValue(TrapDoorBlock.OPEN, oldBlockState.getValue(TrapDoorBlock.OPEN))
               .setValue(TrapDoorBlock.POWERED, oldBlockState.getValue(TrapDoorBlock.POWERED))
               .setValue(TrapDoorBlock.WATERLOGGED, oldBlockState.getValue(TrapDoorBlock.WATERLOGGED))
               .setValue(TrapDoorBlock.FACING, oldBlockState.getValue(TrapDoorBlock.FACING))
   );

   private static final List<Block> DOORS = ImmutableList.of(Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR,
         Blocks.DARK_OAK_DOOR, Blocks.BIRCH_DOOR, Blocks.ACACIA_DOOR, Blocks.JUNGLE_DOOR);
   public static final Variation<NestedVariationConfiguration> DOORS_VARIATION = new NestedVariation(NestedVariationConfiguration::isAreDoorsEnabled, PLANKS, DOORS,
         (oldBlockState, newBlockState) -> newBlockState
               .setValue(DoorBlock.OPEN, oldBlockState.getValue(DoorBlock.OPEN))
               .setValue(DoorBlock.HINGE, oldBlockState.getValue(DoorBlock.HINGE))
               .setValue(DoorBlock.FACING, oldBlockState.getValue(DoorBlock.FACING))
               .setValue(DoorBlock.HALF, oldBlockState.getValue(DoorBlock.HALF))
               .setValue(DoorBlock.POWERED, oldBlockState.getValue(DoorBlock.POWERED))
   );

   private static final List<Block> STANDING_SIGNS = ImmutableList.of(Blocks.OAK_SIGN, Blocks.SPRUCE_SIGN,
         Blocks.DARK_OAK_SIGN, Blocks.BIRCH_SIGN, Blocks.ACACIA_SIGN, Blocks.JUNGLE_SIGN);
   public static final Variation<NestedVariationConfiguration> STANDING_SIGNS_VARIATION = new NestedVariation(NestedVariationConfiguration::isStandingSignEnabled, PLANKS, STANDING_SIGNS,
         (oldBlockState, newBlockState) -> newBlockState
               .setValue(SignBlock.WATERLOGGED, oldBlockState.getValue(SignBlock.WATERLOGGED))
               .setValue(StandingSignBlock.ROTATION, oldBlockState.getValue(StandingSignBlock.ROTATION))
   );

   private static final List<Block> WALL_SIGNS = ImmutableList.of(Blocks.OAK_WALL_SIGN, Blocks.SPRUCE_WALL_SIGN,
         Blocks.DARK_OAK_WALL_SIGN, Blocks.BIRCH_WALL_SIGN, Blocks.ACACIA_WALL_SIGN, Blocks.JUNGLE_WALL_SIGN);
   public static final Variation<NestedVariationConfiguration> WALL_SIGNS_VARIATION = new NestedVariation(NestedVariationConfiguration::isWallSignEnabled, PLANKS, WALL_SIGNS,
         (oldBlockState, newBlockState) -> newBlockState
               .setValue(SignBlock.WATERLOGGED, oldBlockState.getValue(SignBlock.WATERLOGGED))
               .setValue(WallSignBlock.FACING, oldBlockState.getValue(WallSignBlock.FACING))
   );
}