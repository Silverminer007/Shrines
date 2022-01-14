/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.worldgen.structures;

import com.silverminer.shrines.packages.datacontainer.StructureData;
import com.silverminer.shrines.packages.datacontainer.VariationConfiguration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.Random;

public interface Variation<C> {
   default boolean isEnabled(VariationConfiguration structureConfiguration) {
      return this.isEnabled(this.getConfiguration(structureConfiguration));
   }

   boolean isEnabled(C variationConfiguration);

   C getConfiguration(VariationConfiguration structureConfiguration);

   List<Block> getPossibleValues();

   BlockState applyAllProperties(BlockState oldBlockState, BlockState newBlockState);

   Block getBlockRemap(Block oldBlock, Map<Block, Block> blockRemapMap);

   Map<Block, Block> createRemaps(Random rand);
}