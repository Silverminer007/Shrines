package com.silverminer.shrines.init;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.structures.processors.RemoveBlocksProcessor;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.StructureProcessor;

public class RegisterStructureProcessors {
	public static final IStructureProcessorType<RemoveBlocksProcessor> REMOVE_BLOCKS;

	static <P extends StructureProcessor> IStructureProcessorType<P> register(String name, Codec<P> codec) {
		return Registry.register(Registry.STRUCTURE_PROCESSOR, ShrinesMod.MODID + ":" + name, () -> {
			return codec;
		});
	}

	public static void load() {
	};

	static {
		REMOVE_BLOCKS = register("remove_blocks", RemoveBlocksProcessor.CODEC);
	}
}