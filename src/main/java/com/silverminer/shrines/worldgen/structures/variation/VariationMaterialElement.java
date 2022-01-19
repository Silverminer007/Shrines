package com.silverminer.shrines.worldgen.structures.variation;

import com.silverminer.shrines.packages.datacontainer.NestedVariationConfiguration;
import com.silverminer.shrines.packages.datacontainer.SimpleVariationConfiguration;
import com.silverminer.shrines.packages.datacontainer.VariationConfiguration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;
import java.util.function.Function;

public record VariationMaterialElement(Block block, Function<VariationConfiguration, Boolean> property,
                                       BiFunction<BlockState, BlockState, BlockState> blockStateApplier) {
   public static VariationMaterialElement createNestedVariationElement(Block block, Function<NestedVariationConfiguration, Boolean> property, BiFunction<BlockState,
         BlockState, BlockState> blockStateApplier) {
      return new VariationMaterialElement(block, (variationConfiguration -> property.apply(variationConfiguration.getNestedVariationConfiguration())), blockStateApplier);
   }

   public static VariationMaterialElement createSimpleVariationElement(Block block, Function<SimpleVariationConfiguration, Boolean> property) {
      return new VariationMaterialElement(block, (variationConfiguration -> property.apply(variationConfiguration.getSimpleVariationConfiguration())),
            ((blockState, blockState2) -> blockState2));
   }

   public boolean isEnabled(VariationConfiguration variationConfiguration) {
      return this.property.apply(variationConfiguration);
   }

   public boolean isMaterial(Block block) {
      return this.block().equals(block);
   }

   public BlockState applyAllProperties(BlockState oldBlockState, BlockState newBlockState) {
      return this.blockStateApplier().apply(oldBlockState, newBlockState);
   }
}