/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.worldgen.structures;

import com.google.common.collect.ImmutableList;
import com.silverminer.shrines.packages.datacontainer.NestedVariationConfiguration;
import com.silverminer.shrines.packages.datacontainer.StructureData;
import com.silverminer.shrines.packages.datacontainer.VariationConfiguration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class NestedVariation implements Variation<NestedVariationConfiguration> {
   protected static final Logger LOGGER = LogManager.getLogger(NestedVariation.class);
   private final Function<NestedVariationConfiguration, Boolean> enabledFunction;
   private final List<Block> identifierBlocks;
   private final List<Block> targetBlocks;
   private final BiFunction<BlockState, BlockState, BlockState> applyProperties;

   public NestedVariation(Function<NestedVariationConfiguration, Boolean> enabledFunction, List<Block> identifierBlocks, List<Block> targetBlocks, BiFunction<BlockState, BlockState, BlockState> applyProperties) {
      this.enabledFunction = enabledFunction;
      this.identifierBlocks = ImmutableList.copyOf(Objects.requireNonNull(identifierBlocks));
      this.targetBlocks = ImmutableList.copyOf(Objects.requireNonNull(targetBlocks));
      this.applyProperties = Objects.requireNonNull(applyProperties);
      if (this.identifierBlocks.size() != this.targetBlocks.size()) {
         throw new IllegalArgumentException("Identifier blocks must have the same size as target blocks");
      }
   }

   @Override
   public boolean isEnabled(NestedVariationConfiguration variationConfiguration) {
      return this.enabledFunction.apply(variationConfiguration);
   }

   @Override
   public NestedVariationConfiguration getConfiguration(VariationConfiguration variationConfiguration) {
      return variationConfiguration.getNestedVariationConfiguration();
   }

   @Override
   public List<Block> getPossibleValues() {
      return this.targetBlocks;
   }

   @Override
   public BlockState applyAllProperties(BlockState oldBlockState, BlockState newBlockState) {
      return this.applyProperties.apply(oldBlockState, newBlockState);
   }

   @Override
   public Block getBlockRemap(Block oldBlock, Map<Block, Block> blockRemapMap) {
      try {
         int index = this.targetBlocks.indexOf(oldBlock);
         if (index < 0) {
            return oldBlock;
         } else {
            Block idBlock = this.identifierBlocks.get(index);
            Block targetBlockID = blockRemapMap.get(idBlock);
            if (targetBlockID == null) {
               return oldBlock;
            }
            int targetIndex = this.identifierBlocks.indexOf(targetBlockID);
            if (targetIndex < 0) {
               return oldBlock;
            } else {
               return this.targetBlocks.get(targetIndex);
            }
         }
      } catch (ArrayIndexOutOfBoundsException e) {
         LOGGER.error("Random variation for shrines structure failed");
         LOGGER.error("Old Block to replace: {}", oldBlock);
         LOGGER.error("ID Blocks [{}], [{}]", this.identifierBlocks.size(), this.identifierBlocks);
         LOGGER.error("Target Blocks [{}], [{}]", this.targetBlocks.size(), this.targetBlocks);
         LOGGER.error("Block Remaps: [{}]", blockRemapMap);
         throw e;
      }
   }

   @Override
   public Map<Block, Block> createRemaps(Random rand) {
      return new HashMap<>();
   }
}
