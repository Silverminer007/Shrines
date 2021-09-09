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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.init.StructureRegistryHolder;
import com.silverminer.shrines.structures.processors.ProcessorTypes;
import com.silverminer.shrines.utils.StructureUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonEvents {
	protected static final Logger LOGGER = LogManager.getLogger(CommonEvents.class);

	@EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.MOD)
	public static class ModEventBus {

		@SubscribeEvent
		public static void commonSetupEvent(FMLCommonSetupEvent event) {
			event.enqueueWork(() -> {
				ShrinesPacketHandler.register();
				ProcessorTypes.register();
				StructureUtils.setupWorldGen();
			});
		}
	}

	@EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.FORGE)
	public static class ForgeEventBus {

		@SubscribeEvent(priority = EventPriority.HIGH)
		public static void onBiomeLoadHigh(BiomeLoadingEvent event) {
			if (!Config.SETTINGS.BLACKLISTED_BIOMES.get().contains(event.getName().toString())) {
				for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
					if (holder.getStructure().getConfig().getGenerate() && StructureUtils.checkBiome(
							holder.getStructure().getConfig().getBiome_blacklist(), event.getName())) {
						event.getGeneration().addStructureStart(holder.getConfiguredStructure());
					}
				}
			}
		}

		@SubscribeEvent
		public static void onWorldLoad(WorldEvent.Load event) {
			if (event.getWorld() instanceof ServerWorld) {
				LOGGER.info("Loading world with dimension: {}",
						((ServerWorld) event.getWorld()).dimension().location().toString());
				LOGGER.info("Configured Dimensions of {}: {}", NewStructureInit.STRUCTURES.get(0).getStructure().getConfig().getName(),
						NewStructureInit.STRUCTURES.get(0).getStructure().getConfig().getDimension_whitelist());
				StructureUtils.addDimensionalSpacing((ServerWorld) event.getWorld());
			}
		}
	}
}