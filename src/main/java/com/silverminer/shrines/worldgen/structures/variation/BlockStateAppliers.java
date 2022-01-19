package com.silverminer.shrines.worldgen.structures.variation;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BlockStateAppliers {
   public static @NotNull BlockState applySlabProperties(@NotNull BlockState oldBlockState, @NotNull BlockState newBlockState) {
      return newBlockState.setValue(SlabBlock.TYPE, oldBlockState.getValue(SlabBlock.TYPE));
   }

   public static @NotNull BlockState applyButtonProperties(@NotNull BlockState oldBlockState, @NotNull BlockState newBlockState) {
      return newBlockState
            .setValue(ButtonBlock.POWERED, oldBlockState.getValue(ButtonBlock.POWERED))
            .setValue(ButtonBlock.FACE, oldBlockState.getValue(ButtonBlock.FACE))
            .setValue(ButtonBlock.FACING, oldBlockState.getValue(ButtonBlock.FACING));
   }

   public static @NotNull BlockState applyStairProperties(@NotNull BlockState oldBlockState, @NotNull BlockState newBlockState) {
      return newBlockState
            .setValue(StairBlock.WATERLOGGED, oldBlockState.getValue(StairBlock.WATERLOGGED))
            .setValue(StairBlock.FACING, oldBlockState.getValue(StairBlock.FACING))
            .setValue(StairBlock.HALF, oldBlockState.getValue(StairBlock.HALF))
            .setValue(StairBlock.SHAPE, oldBlockState.getValue(StairBlock.SHAPE));
   }

   public static @NotNull BlockState applyFenceProperties(@NotNull BlockState oldBlockState, @NotNull BlockState newBlockState) {
      return newBlockState
            .setValue(FenceBlock.NORTH, oldBlockState.getValue(FenceBlock.NORTH))
            .setValue(FenceBlock.EAST, oldBlockState.getValue(FenceBlock.EAST))
            .setValue(FenceBlock.SOUTH, oldBlockState.getValue(FenceBlock.SOUTH))
            .setValue(FenceBlock.WEST, oldBlockState.getValue(FenceBlock.WEST))
            .setValue(FenceBlock.WATERLOGGED, oldBlockState.getValue(FenceBlock.WATERLOGGED));
   }

   public static @NotNull BlockState applyFenceGateProperties(@NotNull BlockState oldBlockState, @NotNull BlockState newBlockState) {
      return newBlockState.setValue(FenceGateBlock.OPEN, oldBlockState.getValue(FenceGateBlock.OPEN))
            .setValue(FenceGateBlock.POWERED, oldBlockState.getValue(FenceGateBlock.POWERED))
            .setValue(FenceGateBlock.IN_WALL, oldBlockState.getValue(FenceGateBlock.IN_WALL))
            .setValue(FenceGateBlock.FACING, oldBlockState.getValue(FenceGateBlock.FACING));
   }

   public static @NotNull BlockState applyLogProperties(@NotNull BlockState oldBlockState, @NotNull BlockState newBlockState) {
      return newBlockState
            .setValue(RotatedPillarBlock.AXIS, oldBlockState.getValue(RotatedPillarBlock.AXIS));
   }

   public static @NotNull BlockState applyTrapdoorProperties(@NotNull BlockState oldBlockState, @NotNull BlockState newBlockState) {
      return newBlockState
            .setValue(TrapDoorBlock.HALF, oldBlockState.getValue(TrapDoorBlock.HALF))
            .setValue(TrapDoorBlock.OPEN, oldBlockState.getValue(TrapDoorBlock.OPEN))
            .setValue(TrapDoorBlock.POWERED, oldBlockState.getValue(TrapDoorBlock.POWERED))
            .setValue(TrapDoorBlock.WATERLOGGED, oldBlockState.getValue(TrapDoorBlock.WATERLOGGED))
            .setValue(TrapDoorBlock.FACING, oldBlockState.getValue(TrapDoorBlock.FACING));
   }

   public static @NotNull BlockState applyDoorProperties(@NotNull BlockState oldBlockState, @NotNull BlockState newBlockState) {
      return newBlockState
            .setValue(DoorBlock.OPEN, oldBlockState.getValue(DoorBlock.OPEN))
            .setValue(DoorBlock.HINGE, oldBlockState.getValue(DoorBlock.HINGE))
            .setValue(DoorBlock.FACING, oldBlockState.getValue(DoorBlock.FACING))
            .setValue(DoorBlock.HALF, oldBlockState.getValue(DoorBlock.HALF))
            .setValue(DoorBlock.POWERED, oldBlockState.getValue(DoorBlock.POWERED));
   }

   public static @NotNull BlockState applyStandingSignProperties(@NotNull BlockState oldBlockState, @NotNull BlockState newBlockState) {
      return newBlockState
            .setValue(SignBlock.WATERLOGGED, oldBlockState.getValue(SignBlock.WATERLOGGED))
            .setValue(StandingSignBlock.ROTATION, oldBlockState.getValue(StandingSignBlock.ROTATION));
   }

   public static @NotNull BlockState applyWallSignProperties(@NotNull BlockState oldBlockState, @NotNull BlockState newBlockState) {
      return newBlockState
            .setValue(SignBlock.WATERLOGGED, oldBlockState.getValue(SignBlock.WATERLOGGED))
            .setValue(WallSignBlock.FACING, oldBlockState.getValue(WallSignBlock.FACING));
   }

   public static @NotNull BlockState applyWallProperties(@NotNull BlockState oldBlockState, @NotNull BlockState newBlockState) {
      return newBlockState.setValue(WallBlock.UP, oldBlockState.getValue(WallBlock.UP))
            .setValue(WallBlock.EAST_WALL, oldBlockState.getValue(WallBlock.EAST_WALL))
            .setValue(WallBlock.NORTH_WALL, oldBlockState.getValue(WallBlock.NORTH_WALL))
            .setValue(WallBlock.SOUTH_WALL, oldBlockState.getValue(WallBlock.SOUTH_WALL))
            .setValue(WallBlock.WEST_WALL, oldBlockState.getValue(WallBlock.WEST_WALL))
            .setValue(WallBlock.WATERLOGGED, oldBlockState.getValue(WallBlock.WATERLOGGED));
   }
}