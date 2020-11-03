package com.silverminer.shrines.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.init.ModStructureFeatures;
import com.silverminer.shrines.structures.Generator;

import net.minecraft.world.biome.Biome.Category;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonEvents {
	protected static final Logger LOGGER = LogManager.getLogger(CommonEvents.class);

	@EventBusSubscriber(modid = Shrines.MODID, bus = Bus.MOD)
	public static class ModEventBus {
		@SubscribeEvent
		public static void commonSetupEvent(FMLCommonSetupEvent event) {
			event.enqueueWork(() -> Generator.setupWorldGen());
		}
	}

	@EventBusSubscriber(modid = Shrines.MODID, bus = Bus.FORGE)
	public static class ForgeEventBus {

		@SubscribeEvent(priority = EventPriority.HIGH)
		public static void onBiomeLoadHigh(BiomeLoadingEvent event) {
			if (event.getCategory() == Category.NETHER || event.getCategory() == Category.THEEND
					|| event.getCategory() == Category.OCEAN || event.getCategory() == Category.RIVER) {
				return;
			}
			event.getGeneration().withStructure(ModStructureFeatures.BALLON);
			event.getGeneration().withStructure(ModStructureFeatures.BEES);
			event.getGeneration().withStructure(ModStructureFeatures.HIGH_TEMPEL);
			event.getGeneration().withStructure(ModStructureFeatures.NETHER_PYRAMID);
			event.getGeneration().withStructure(ModStructureFeatures.NETHER_SHRINE);
			event.getGeneration().withStructure(ModStructureFeatures.SMALL_TEMPEL);
			event.getGeneration().withStructure(ModStructureFeatures.WATER_SHRINE);
		}
	}
}