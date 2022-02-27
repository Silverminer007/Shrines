/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.worldgen.processors;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;

public class RemoveBlocksProcessor extends StructureProcessor {
	public static final Codec<RemoveBlocksProcessor> CODEC = Codec.unit(RemoveBlocksProcessor::new);

	@Override
	protected StructureProcessorType<?> getType() {
		return ProcessorTypes.REMOVE_BLOCKS_PROCESSOR;
	}

	@Override
	public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos position1, BlockPos position2,
														StructureTemplate.StructureBlockInfo block1, StructureTemplate.StructureBlockInfo block2, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
		if (Config.SETTINGS.BANNED_BLOCKS.get().contains(block2.state.getBlock().getRegistryName().toString())) {
			return new StructureTemplate.StructureBlockInfo(block2.pos, Blocks.AIR.defaultBlockState(), block2.nbt);
		}
		return block2;
	}

	@Override
	public StructureTemplate.StructureEntityInfo processEntity(LevelReader world, BlockPos seedPos, StructureTemplate.StructureEntityInfo rawEntityInfo,
													  StructureTemplate.StructureEntityInfo entityInfo, StructurePlaceSettings placementSettings, StructureTemplate template) {
		if (entityInfo.nbt.contains("id") && Config.SETTINGS.BANNED_ENTITIES.get().contains(entityInfo.nbt.getString("id"))) {
			return null;
		}
		return entityInfo;
	}
}