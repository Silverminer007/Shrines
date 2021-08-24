package com.silverminer.shrines.structures.processors;

import com.silverminer.shrines.ShrinesMod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;

public class ProcessorTypes {

	public static IStructureProcessorType<RemoveBlocksProcessor> REMOVE_BLOCKS_PROCESSOR = () -> RemoveBlocksProcessor.CODEC;

	public static void register() {
		registerProcessor(REMOVE_BLOCKS_PROCESSOR, "remove_block_processor");
	}

	private static void registerProcessor(IStructureProcessorType<?> type, String name) {
		Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(ShrinesMod.MODID, name), type);
	}
}