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
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonEvents {
	protected static final Logger LOGGER = LogManager.getLogger(CommonEvents.class);

	@EventBusSubscriber(modid = Shrines.MODID, bus = Bus.MOD)
	public static class ModEventBus {
		@SuppressWarnings("deprecation")
		@SubscribeEvent
		public static void commonSetupEvent(FMLCommonSetupEvent event) {
			DeferredWorkQueue.runLater(() -> Generator.setupWorldGen());
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
			event.getGeneration().func_242516_a(ModStructureFeatures.BALLON);
			event.getGeneration().func_242516_a(ModStructureFeatures.BEES);
			event.getGeneration().func_242516_a(ModStructureFeatures.HIGH_TEMPEL);
			event.getGeneration().func_242516_a(ModStructureFeatures.NETHER_PYRAMID);
			event.getGeneration().func_242516_a(ModStructureFeatures.NETHER_SHRINE);
			event.getGeneration().func_242516_a(ModStructureFeatures.SMALL_TEMPEL);
			event.getGeneration().func_242516_a(ModStructureFeatures.WATER_SHRINE);
		}
	}
}