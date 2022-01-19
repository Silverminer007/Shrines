package com.silverminer.shrines.worldgen.structures.variation;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface VariationMaterial {
   BlockState applyProperties(BlockState oldBlockState, BlockState newBlockState);

   Block getNewBlock(VariationMaterial variationMaterial, Block block);

   VariationMaterialElement getElement(Block block);
}