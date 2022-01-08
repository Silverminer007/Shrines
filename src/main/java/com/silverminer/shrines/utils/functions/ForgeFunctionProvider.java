/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.utils.functions;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Silverminer
 */
public class ForgeFunctionProvider implements IFunctionProvider {

   @Override
   public List<String> getBiomes() {
      return ForgeRegistries.BIOMES.getKeys().stream().map(b -> b.toString()).collect(Collectors.toList());
   }

   @Override
   public Block getBlockByID(String ID) {
      return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ID));
   }

}