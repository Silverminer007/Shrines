/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.structures.processors;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.Config;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;

public class RemoveBlocksProcessor extends StructureProcessor {
	public static final Codec<RemoveBlocksProcessor> CODEC = Codec.unit(RemoveBlocksProcessor::new);

	@Override
	protected IStructureProcessorType<?> getType() {
		return ProcessorTypes.REMOVE_BLOCKS_PROCESSOR;
	}

	@Override
	public Template.BlockInfo process(IWorldReader world, BlockPos position1, BlockPos position2,
			Template.BlockInfo block1, Template.BlockInfo block2, PlacementSettings settings, @Nullable Template template) {
		if (Config.SETTINGS.BANNED_BLOCKS.get().contains(block2.state.getBlock().getRegistryName().toString())) {
			return new Template.BlockInfo(block2.pos, Blocks.AIR.defaultBlockState(), block2.nbt);
		}
		return block2;
	}

	@Override
	public Template.EntityInfo processEntity(IWorldReader world, BlockPos seedPos, Template.EntityInfo rawEntityInfo,
			Template.EntityInfo entityInfo, PlacementSettings placementSettings, Template template) {
		if (entityInfo.nbt.contains("id") && Config.SETTINGS.BANNED_ENTITIES.get().contains(entityInfo.nbt.getString("id"))) {
			return null;
		}
		return entityInfo;
	}
}