/**
 * Silverminer (and Team)
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * 
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.forge.events;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.core.commands.ShrinesCommand;
import com.silverminer.shrines.core.commands.arguments.BiomeCSArgumentType;
import com.silverminer.shrines.core.commands.arguments.BiomeCategoryCSArgumentType;
import com.silverminer.shrines.core.commands.arguments.NameCSArgumentType;
import com.silverminer.shrines.core.commands.arguments.OptionCSArgumentType;
import com.silverminer.shrines.core.structures.Generator;
import com.silverminer.shrines.core.structures.StructurePieceTypes;
import com.silverminer.shrines.core.structures.custom.CustomStructure;
import com.silverminer.shrines.core.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.core.utils.custom_structures.Utils;
import com.silverminer.shrines.core.utils.saves.BoundSaveData;
import com.silverminer.shrines.forge.config.Config;
import com.silverminer.shrines.forge.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.init.ModStructureFeatures;

import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public class CommonEvents {
	protected static final Logger LOGGER = LogManager.getLogger(CommonEvents.class);

	@EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.MOD)
	public static class ModEventBus {
		@SubscribeEvent
		public static void loadCompleteEvent(FMLLoadCompleteEvent event) {
			event.enqueueWork(() -> {
				LOGGER.debug("Registering structure pieces and structures to dimensions");
				Generator.setupWorldGen();
				StructurePieceTypes.regsiter();
				ArgumentTypes.register("biome_category", BiomeCategoryCSArgumentType.class,
						new BiomeCategoryCSArgumentType.Serializer());
				ArgumentTypes.register("biome", BiomeCSArgumentType.class, new BiomeCSArgumentType.Serializer());
				ArgumentTypes.register("name", NameCSArgumentType.class, new NameCSArgumentType.Serializer());
				ArgumentTypes.register("option", OptionCSArgumentType.class, new OptionCSArgumentType.Serializer());
			});
		}

		@SubscribeEvent
		public static void commonSetupEvent(FMLCommonSetupEvent event) {
			ShrinesPacketHandler.register();
		}
	}

	@EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.FORGE)
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
		public static void onPlayerJoin(PlayerLoggedInEvent event) {
			CustomStructureData.sendToClient(event.getPlayer());
		}

		@SubscribeEvent
		public static void registerCommands(RegisterCommandsEvent event) {
			LOGGER.debug("Registering shrines commands");
			ShrinesCommand.register(event.getDispatcher());
		}

		@SubscribeEvent
		public static void onWorldSaved(WorldEvent.Save event) {
			if (Utils.properties.autosave)
				Utils.saveStructures();
		}

		@SubscribeEvent
		public static void onWorldStopped(WorldEvent.Unload event) {
			IWorld iworld = event.getWorld();

			if (!(iworld instanceof World))
				return;
			if (!((World) iworld).isClientSide() && ((World) iworld).dimension() == World.OVERWORLD) {
				Utils.customsStructs.forEach(csd -> csd.PIECES_ON_FLY.clear());
			}
		}

		@SubscribeEvent
		public static void onWorldLoad(WorldEvent.Load event) {
			LOGGER.info("Loading bound data from file");
			IWorld iworld = event.getWorld();

			if (!(iworld instanceof World))
				return;
			World world = (World) iworld;
			if (!world.isClientSide() && world.dimension() == World.OVERWORLD && world instanceof ServerWorld) {
				Utils.boundDataSave = BoundSaveData.get((ServerWorld) world);
			}
		}
	}
}