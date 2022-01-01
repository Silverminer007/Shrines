package com.silverminer.shrines.worldgen.processors;

import com.google.common.collect.ImmutableList;
import com.silverminer.shrines.ShrinesMod;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ProcessorTypes {

   public static StructureProcessorType<RemoveBlocksProcessor> REMOVE_BLOCKS_PROCESSOR = () -> RemoveBlocksProcessor.CODEC;
   public static StructureProcessorList DEFAULT_PROCESSORS;

   public static void register() {
      registerProcessor(REMOVE_BLOCKS_PROCESSOR, "remove_block_processor");
      DEFAULT_PROCESSORS = registerProcessorList("default_processors", ImmutableList.of(new RemoveBlocksProcessor()));
   }

   private static void registerProcessor(StructureProcessorType<?> type, String name) {
      Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(ShrinesMod.MODID, name), type);
   }

   private static StructureProcessorList registerProcessorList(String name, ImmutableList<StructureProcessor> processorImmutableList) {
      ResourceLocation resourcelocation = new ResourceLocation(ShrinesMod.MODID, name);
      StructureProcessorList structureprocessorlist = new StructureProcessorList(processorImmutableList);
      return BuiltinRegistries.register(BuiltinRegistries.PROCESSOR_LIST, resourcelocation, structureprocessorlist);
   }
}