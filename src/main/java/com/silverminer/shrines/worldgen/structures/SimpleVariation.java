/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.worldgen.structures;

import com.silverminer.shrines.packages.datacontainer.SimpleVariationConfiguration;
import com.silverminer.shrines.packages.datacontainer.StructureData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SimpleVariation implements Variation<SimpleVariationConfiguration> {
   private final Function<SimpleVariationConfiguration, Boolean> enabledFunction;
   private final List<Block> possibleValues;
   private final BiFunction<BlockState, BlockState, BlockState> applyProperties;

   public SimpleVariation(List<Block> possibleValues, Function<SimpleVariationConfiguration, Boolean> enabledFunction) {
      this(enabledFunction, possibleValues, ((oldBlockState, newBlockState) -> newBlockState));
   }

   public SimpleVariation(Function<SimpleVariationConfiguration, Boolean> enabledFunction, List<Block> possibleValues, BiFunction<BlockState, BlockState, BlockState> applyProperties) {
      this.enabledFunction = enabledFunction;
      this.possibleValues = possibleValues;
      this.applyProperties = applyProperties;
   }

   @Override
   public boolean isEnabled(SimpleVariationConfiguration structureConfiguration) {
      return this.enabledFunction.apply(structureConfiguration);
   }

   @Override
   public SimpleVariationConfiguration getConfiguration(@NotNull StructureData structureConfiguration) {
      return structureConfiguration.getVariationConfiguration().getSimpleVariationConfiguration();
   }

   @Override
   public List<Block> getPossibleValues() {
      return this.possibleValues;
   }

   @Override
   public BlockState applyAllProperties(BlockState oldBlockState, BlockState newBlockState) {
      return this.applyProperties.apply(oldBlockState, newBlockState);
   }

   @Override
   public Block getBlockRemap(Block oldBlock, Map<Block, Block> blockRemapMap) {
      return blockRemapMap.get(oldBlock);
   }

   @Override
   public Map<Block, Block> createRemaps(Random rand) {
      Map<Block, Block> remaps = new HashMap<>();
      List<Block> possibleBlocks = new ArrayList<>(this.getPossibleValues());
      for (Block block : this.getPossibleValues()) {
         Block remapTarget = possibleBlocks.get(rand.nextInt(possibleBlocks.size()));
         remaps.put(block, remapTarget);
         possibleBlocks.remove(remapTarget);
      }
      return remaps;
   }
}