package com.silverminer.shrines.events;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.commands.ShrinesCommand;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.ModStructureFeatures;
import com.silverminer.shrines.structures.Generator;
import com.silverminer.shrines.structures.StructurePieceTypes;
import com.silverminer.shrines.structures.custom.CustomStructure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

public class CommonEvents {
	protected static final Logger LOGGER = LogManager.getLogger(CommonEvents.class);

	@EventBusSubscriber(modid = Shrines.MODID, bus = Bus.MOD)
	public static class ModEventBus {
		@SubscribeEvent
		public static void commonSetupEvent(FMLLoadCompleteEvent event) {
			event.enqueueWork(() -> {
				LOGGER.debug("Registering structure pieces and structures to dimensions");
				Generator.setupWorldGen();
				StructurePieceTypes.regsiter();
			});
		}
	}

	@EventBusSubscriber(modid = Shrines.MODID, bus = Bus.FORGE)
	public static class ForgeEventBus {

		@SubscribeEvent(priority = EventPriority.HIGH)
		public static void onBiomeLoadHigh(BiomeLoadingEvent event) {
			LOGGER.debug("Loading Biome and registering structures. Biome: {}", event.getName());
			if (!Config.STRUCTURES.BLACKLISTED_BIOMES.get().contains(event.getName().toString())) {
				if (Config.STRUCTURES.BALLON.GENERATE.get()
						&& checkBiome(Config.STRUCTURES.BALLON.BIOME_CATEGORIES.get(),
								Config.STRUCTURES.BALLON.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.BALLON);
				}
				if (Config.STRUCTURES.BEES.GENERATE.get() && checkBiome(Config.STRUCTURES.BEES.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.BEES.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.BEES);
				}
				if (Config.STRUCTURES.HIGH_TEMPEL.GENERATE.get() && checkBiome(
						Config.STRUCTURES.HIGH_TEMPEL.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.HIGH_TEMPEL.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.HIGH_TEMPEL);
				}
				if (Config.STRUCTURES.MINERAL_TEMPLE.GENERATE.get() && checkBiome(
						Config.STRUCTURES.MINERAL_TEMPLE.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.MINERAL_TEMPLE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.MINERAL_TEMPLE);
				}
				if (Config.STRUCTURES.FLOODED_TEMPLE.GENERATE.get() && checkBiome(
						Config.STRUCTURES.FLOODED_TEMPLE.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.FLOODED_TEMPLE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.FLOODED_TEMPLE);
				}
				if (Config.STRUCTURES.NETHER_PYRAMID.GENERATE.get() && checkBiome(
						Config.STRUCTURES.NETHER_PYRAMID.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.NETHER_PYRAMID.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.NETHER_PYRAMID);
				}
				if (Config.STRUCTURES.NETHER_SHRINE.GENERATE.get() && checkBiome(
						Config.STRUCTURES.NETHER_SHRINE.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.NETHER_SHRINE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.NETHER_SHRINE);
				}
				if (Config.STRUCTURES.PLAYER_HOUSE.GENERATE.get() && checkBiome(
						Config.STRUCTURES.PLAYER_HOUSE.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.PLAYER_HOUSE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.PLAYER_HOUSE);
				}
				if (Config.STRUCTURES.SMALL_TEMPEL.GENERATE.get() && checkBiome(
						Config.STRUCTURES.SMALL_TEMPEL.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.SMALL_TEMPEL.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.SMALL_TEMPEL);
				}
				if (Config.STRUCTURES.WATER_SHRINE.GENERATE.get() && checkBiome(
						Config.STRUCTURES.WATER_SHRINE.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.WATER_SHRINE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.WATER_SHRINE);
				}
				if (Config.STRUCTURES.HARBOUR.GENERATE.get() && checkBiome(
						Config.STRUCTURES.HARBOUR.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.HARBOUR.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.HARBOUR);
				}
				if (Config.STRUCTURES.INFESTED_PRISON.GENERATE.get()
						&& checkBiome(Config.STRUCTURES.INFESTED_PRISON.BIOME_CATEGORIES.get(),
								Config.STRUCTURES.INFESTED_PRISON.BIOME_BLACKLIST.get(), event.getName(),
								event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.INFESTED_PRISON);
				}
				if (Config.STRUCTURES.WITCH_HOUSE.GENERATE.get() && checkBiome(
						Config.STRUCTURES.WITCH_HOUSE.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.WITCH_HOUSE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.WITCH_HOUSE);
				}
				if (Config.STRUCTURES.JUNGLE_TOWER.GENERATE.get() && checkBiome(
						Config.STRUCTURES.JUNGLE_TOWER.BIOME_CATEGORIES.get(),
						Config.STRUCTURES.JUNGLE_TOWER.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.JUNGLE_TOWER);
				}
				if (Config.STRUCTURES.GUARDIAN_MEETING.GENERATE.get()
						&& checkBiome(Config.STRUCTURES.GUARDIAN_MEETING.BIOME_CATEGORIES.get(),
								Config.STRUCTURES.GUARDIAN_MEETING.BIOME_BLACKLIST.get(), event.getName(),
								event.getCategory())) {
					event.getGeneration().addStructureStart(ModStructureFeatures.GUARDIAN_MEETING);
				}
			}
			if (Config.STRUCTURES.END_TEMPLE.GENERATE.get()
					&& checkBiome(Config.STRUCTURES.END_TEMPLE.BIOME_CATEGORIES.get(),
							Config.STRUCTURES.END_TEMPLE.BIOME_BLACKLIST.get(), event.getName(), event.getCategory())) {
				event.getGeneration().addStructureStart(ModStructureFeatures.END_TEMPLE);
			}
			for (StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> s : ModStructureFeatures.USERS_STRUCTURES) {
				if (s.feature instanceof CustomStructure) {
					CustomStructure cS = (CustomStructure) s.feature;
					if (cS.validateSpawn(event.getName(), event.getCategory())) {
						event.getGeneration().addStructureStart(s);
					}
				}
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

		@SubscribeEvent
		public static void onServerStop(FMLServerStoppingEvent event) {
			if (Shrines.USECUSTOMSTRUCTURES) {
				File path = event.getServer().getFile("");
				try {
					path = new File(path, "shrines-saves").getCanonicalFile();
					LOGGER.info("Saving config options on path: {}", path);
					if (!path.exists())
						path.mkdirs();
					File structures = new File(path, "structures.txt");
					if (!structures.exists()) {
						structures.createNewFile();
					}
					for(String key : Shrines.customsToDelete) {
						File st = new File(path, "shrines");
						st = new File(st, key);
						if (!st.isDirectory()) {
							continue;
						}
						st.delete();
					}
					FileWriter fw = new FileWriter(structures);
					for (String key : Shrines.customStructures.keySet()) {
						LOGGER.debug("Writing config options of custom structure with name {}", key);
						fw.write(key + "\n");
						File st = new File(path, "shrines");
						st = new File(st, key);
						if (!st.isDirectory()) {
							st.mkdirs();
						}
						st = new File(st, key + ".txt");
						if (!st.exists()) {
							st.createNewFile();
						}
						FileWriter cfw = new FileWriter(st);
						for (String v : Shrines.customStructures.get(key)) {
							cfw.write(v + "\n");
						}
						cfw.close();
					}
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		@SubscribeEvent
		public static void registerCommands(RegisterCommandsEvent event) {
			LOGGER.debug("Registering shrines commands");
			ShrinesCommand.register(event.getDispatcher());
		}
	}
}