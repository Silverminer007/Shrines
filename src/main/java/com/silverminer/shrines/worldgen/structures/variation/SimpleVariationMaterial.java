package com.silverminer.shrines.worldgen.structures.variation;

import com.silverminer.shrines.packages.datacontainer.SimpleVariationConfiguration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public record SimpleVariationMaterial(Block block, Function<SimpleVariationConfiguration, Boolean> property) implements VariationMaterial {
   public BlockState applyProperties(BlockState oldBlockState, BlockState newBlockState) {
      return newBlockState;
   }

   public Block getNewBlock(VariationMaterial variationMaterial, Block block) {
      if (variationMaterial instanceof SimpleVariationMaterial simpleVariationMaterial) {
         return simpleVariationMaterial.block();
      } else {
         return block;
      }
   }

   public VariationMaterialElement getElement(Block block) {
      if (block.equals(this.block())) {
         return VariationMaterialElement.createSimpleVariationElement(this.block(), this.property());
      } else {
         return null;
      }
   }
}