package com.silverminer.shrines.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.Shrines;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class ClientEvents {
	protected static final Logger LOGGER = LogManager.getLogger(ClientEvents.class);

	@EventBusSubscriber(modid = Shrines.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
	public static class ModEventBus {
		@SubscribeEvent
		public static void renderWorldLast(RenderWorldLastEvent event) {
			event.getMatrixStack();
		}
	}
}