package com.silverminer.shrines.events;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.ModStructureFeatures;
import com.silverminer.shrines.structures.Generator;
import com.silverminer.shrines.structures.StructurePieceTypes;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
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
			event.enqueueWork(() -> {
				Generator.setupWorldGen();
				StructurePieceTypes.regsiter();
			});
		}
	}

	@EventBusSubscriber(modid = Shrines.MODID, bus = Bus.FORGE)
	public static class ForgeEventBus {

		@SubscribeEvent(priority = EventPriority.HIGH)
		public static void onBiomeLoadHigh(BiomeLoadingEvent event) {
			if (!Config.STRUCTURES.BLACKLISTED_BIOMES.get().contains(event.getName().toString())) {
				if (Config.STRUCTURES.BALLON.GENERATE.get()
						&& checkBiome(Config.STRUCTURES.BALLON.BIOME_CATEGORIES.get(),
								Config.STRUCTURES.BALLON.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.BALLON);
				}
				if (Config.STRUCTURES.BEES.GENERATE.get() && checkBiome(Config.STRUCTURES.BEES.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.BEES.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.BEES);
				}
				if (Config.STRUCTURES.HIGH_TEMPEL.GENERATE.get() && checkBiome(
						Config.STRUCTURES.HIGH_TEMPEL.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.HIGH_TEMPEL.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.HIGH_TEMPEL);
				}
				if (Config.STRUCTURES.MINERAL_TEMPLE.GENERATE.get() && checkBiome(
						Config.STRUCTURES.MINERAL_TEMPLE.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.MINERAL_TEMPLE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.MINERAL_TEMPLE);
				}
				if (Config.STRUCTURES.FLOODED_TEMPLE.GENERATE.get() && checkBiome(
						Config.STRUCTURES.FLOODED_TEMPLE.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.FLOODED_TEMPLE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.FLOODED_TEMPLE);
				}
				if (Config.STRUCTURES.NETHER_PYRAMID.GENERATE.get() && checkBiome(
						Config.STRUCTURES.NETHER_PYRAMID.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.NETHER_PYRAMID.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.NETHER_PYRAMID);
				}
				if (Config.STRUCTURES.NETHER_SHRINE.GENERATE.get() && checkBiome(
						Config.STRUCTURES.NETHER_SHRINE.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.NETHER_SHRINE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.NETHER_SHRINE);
				}
				if (Config.STRUCTURES.PLAYER_HOUSE.GENERATE.get() && checkBiome(
						Config.STRUCTURES.PLAYER_HOUSE.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.PLAYER_HOUSE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.PLAYER_HOUSE);
				}
				if (Config.STRUCTURES.SMALL_TEMPEL.GENERATE.get() && checkBiome(
						Config.STRUCTURES.SMALL_TEMPEL.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.SMALL_TEMPEL.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.SMALL_TEMPEL);
				}
				if (Config.STRUCTURES.WATER_SHRINE.GENERATE.get() && checkBiome(
						Config.STRUCTURES.WATER_SHRINE.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.WATER_SHRINE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.WATER_SHRINE);
				}
				if (Config.STRUCTURES.HARBOUR.GENERATE.get() && checkBiome(
						Config.STRUCTURES.HARBOUR.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.HARBOUR.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.HARBOUR);
				}
				if (Config.STRUCTURES.INFESTED_PRISON.GENERATE.get()
						&& checkBiome(Config.STRUCTURES.INFESTED_PRISON.BIOME_CATEGORIES.get(),
								Config.STRUCTURES.INFESTED_PRISON.BIOME_BLACKLIST.get(), event.getName(),
								event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.INFESTED_PRISON);
				}
				if (Config.STRUCTURES.WITCH_HOUSE.GENERATE.get() && checkBiome(
						Config.STRUCTURES.WITCH_HOUSE.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.WITCH_HOUSE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.WITCH_HOUSE);
				}
				if (Config.STRUCTURES.JUNGLE_TOWER.GENERATE.get() && checkBiome(
						Config.STRUCTURES.JUNGLE_TOWER.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.JUNGLE_TOWER.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.JUNGLE_TOWER);
				}
				if (Config.STRUCTURES.DUMMY.GENERATE.get() && checkBiome(
						Config.STRUCTURES.DUMMY.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.DUMMY.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.DUMMY);
				}
				if (Config.STRUCTURES.GUARDIAN.GENERATE.get() && checkBiome(
						Config.STRUCTURES.GUARDIAN.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.GUARDIAN.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().withStructure(ModStructureFeatures.GUARDIAN);
				}
			}
			LOGGER.info(event.getCategory());
			if (Config.STRUCTURES.END_TEMPLE.GENERATE.get()
					&& checkBiome(Config.STRUCTURES.END_TEMPLE.BIOME_CATEGORIES.get(),
							Config.STRUCTURES.END_TEMPLE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
				event.getGeneration().withStructure(ModStructureFeatures.END_TEMPLE);
			}
		}

		private static boolean checkBiome(List<? extends Object> allowedBiomeCategories,
				List<? extends String> blacklistedBiomes, ResourceLocation name, Biome.Category category) {
			boolean flag = allowedBiomeCategories.contains(category.toString())
					|| allowedBiomeCategories.contains(category);

			if (!blacklistedBiomes.isEmpty() && flag) {
				flag = !blacklistedBiomes.contains(name.toString());
			}

			return flag;
		}
	}
}