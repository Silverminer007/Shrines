package com.silverminer.shrines.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class PrideMonthProcessor {
   public static void process(@NotNull WorldGenLevel worldGenLevel, @NotNull BoundingBox chunkBounds,
                              @NotNull BoundingBox structureBounds, Predicate<BlockPos> isInside) {
      Block[] colorsInRainbow = new Block[]{Blocks.PURPLE_WOOL, Blocks.BLUE_WOOL, Blocks.GREEN_WOOL, Blocks.YELLOW_WOOL, Blocks.ORANGE_WOOL, Blocks.RED_WOOL};
      // Iterate over the positions first to prevent double overrides of one BlockPos
      for (int x = chunkBounds.minX(); x <= chunkBounds.maxX(); x++) {
         for (int y = chunkBounds.minY(); y <= chunkBounds.maxY(); y++) {
            for (int z = chunkBounds.minZ(); z <= chunkBounds.maxZ(); z++) {
               BlockPos position = new BlockPos(x, y, z);
               if (worldGenLevel.isEmptyBlock(position) || !structureBounds.isInside(position) || !isInside.test(position)) {
                  continue;
               }
               BlockState blockStateAtPos = worldGenLevel.getBlockState(position);
               if(blockStateAtPos.is(BlockTags.WOOL)){
                  worldGenLevel.setBlock(position, colorsInRainbow[Math.abs(y % 6)].withPropertiesOf(blockStateAtPos), 18);
               }
            }
         }
      }
   }
}