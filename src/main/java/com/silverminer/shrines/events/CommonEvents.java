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

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.init.StructureRegistryHolder;
import com.silverminer.shrines.utils.StructureUtils;
import com.silverminer.shrines.utils.custom_structures.Utils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.saves.BoundSaveData;

import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

public class CommonEvents {
	protected static final Logger LOGGER = LogManager.getLogger(CommonEvents.class);

	@EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.MOD)
	public static class ModEventBus {

		@SubscribeEvent
		public static void commonSetupEvent(FMLCommonSetupEvent event) {
			event.enqueueWork(() -> {
				ShrinesPacketHandler.register();
				StructureUtils.setupWorldGen();
			});
		}
	}

	@EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.FORGE)
	public static class ForgeEventBus {

		@SubscribeEvent(priority = EventPriority.HIGH)
		public static void onBiomeLoadHigh(BiomeLoadingEvent event) {
			if (Config.SETTINGS.ADVANCED_LOGGING.get())
				LOGGER.info("Loading Biome and registering structures. Biome: {}", event.getName());
			if (!Config.SETTINGS.BLACKLISTED_BIOMES.get().contains(event.getName().toString())) {
				for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
					if (holder.getStructure().getConfig().getGenerate() && StructureUtils.checkBiome(
							holder.getStructure().getConfig().getWhitelist(),
							holder.getStructure().getConfig().getBlacklist(), event.getName(), event.getCategory())) {
						event.getGeneration().addStructureStart(holder.getConfiguredStructure());
					}
				}
			}
		}

		@SubscribeEvent
		public static void onPlayerJoin(PlayerLoggedInEvent event) {
			Utils.setSend(true);
			Utils.onChanged(true);
			if (Config.SETTINGS.ADVANCED_LOGGING.get())
				LOGGER.info(Utils.getStructures(true).stream().map(st -> st.getName()).collect(Collectors.toList()));
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
			if (event.getWorld() instanceof ServerWorld) {
				StructureUtils.addDimensionalSpacing((ServerWorld) event.getWorld());
			}
			if (Config.SETTINGS.ADVANCED_LOGGING.get())
				LOGGER.info("Loading bound data from file");
			IWorld iworld = event.getWorld();

			if (iworld instanceof ServerWorld) {
				ServerWorld world = (ServerWorld) iworld;
				if (!world.isClientSide() && world.dimension() == World.OVERWORLD) {
					Utils.boundDataSave = BoundSaveData.get(world);
				}
			}
		}

		@SubscribeEvent
		public static void onServerStarted(FMLServerStartedEvent event) {
			// Apply custom structure data packs here? -> Take a look at ReloadListener and
			// stuff
		}
	}
}