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
package com.silverminer.shrines.events;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.commands.ShrinesCommand;
import com.silverminer.shrines.commands.arguments.BiomeCSArgumentType;
import com.silverminer.shrines.commands.arguments.BiomeCategoryCSArgumentType;
import com.silverminer.shrines.commands.arguments.NameCSArgumentType;
import com.silverminer.shrines.commands.arguments.OptionCSArgumentType;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.Generator;
import com.silverminer.shrines.structures.StructurePieceTypes;
import com.silverminer.shrines.structures.StructurePools;
import com.silverminer.shrines.utils.custom_structures.Utils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.saves.BoundSaveData;

import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
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
			StructurePools.load();
		}
	}

	@EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.FORGE)
	public static class ForgeEventBus {

		@SubscribeEvent(priority = EventPriority.HIGH)
		public static void onBiomeLoadHigh(BiomeLoadingEvent event) {
			LOGGER.debug("Loading Biome and registering structures. Biome: {}", event.getName());
			if (!Config.SETTINGS.BLACKLISTED_BIOMES.get().contains(event.getName().toString())) {
				for (AbstractStructure struct : NewStructureInit.STRUCTURES.values()) {
					if (struct.getConfig().getGenerate() && checkBiome(struct.getConfig().getWhitelist(),
							struct.getConfig().getBlacklist(), event.getName(), event.getCategory())) {
						StructureFeature<VillageConfig, ? extends Structure<VillageConfig>>  temp = struct.configured(new VillageConfig(() -> struct.getPools(), 7));
						WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, struct.getRegistryName().toString(), temp);
						event.getGeneration().addStructureStart(temp);
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
			Utils.setSend(true);
			Utils.onChanged(true);
			LOGGER.info(Utils.getStructures(true).stream().map(st -> st.getName()).collect(Collectors.toList()));
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
				Utils.getStructures(true).forEach(csd -> csd.PIECES_ON_FLY.clear());
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