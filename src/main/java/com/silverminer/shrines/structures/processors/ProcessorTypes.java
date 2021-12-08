package com.silverminer.shrines.structures.processors;

import com.silverminer.shrines.ShrinesMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ProcessorTypes {

	public static StructureProcessorType<RemoveBlocksProcessor> REMOVE_BLOCKS_PROCESSOR = () -> RemoveBlocksProcessor.CODEC;

	public static void register() {
		registerProcessor(REMOVE_BLOCKS_PROCESSOR, "remove_block_processor");
	}

	private static void registerProcessor(StructureProcessorType<?> type, String name) {
		Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(ShrinesMod.MODID, name), type);
	}
}