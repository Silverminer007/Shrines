/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.events;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSFetchStructuresPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientEvents {
   protected static final Logger LOGGER = LogManager.getLogger(ClientEvents.class);

   @EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
   public static class ForgeEventBus {
      @SubscribeEvent
      public static void onClientTick(TickEvent.ClientTickEvent event) {
         Minecraft mc = Minecraft.getInstance();
         if (ClientUtils.structuresScreen.isDown()) {
            ShrinesPacketHandler.sendToServer(new CTSFetchStructuresPacket(false));
            mc.setScreen(new WorkingScreen());
         }
      }
   }

   @EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
   public static class ModEventBus {
      @SubscribeEvent
      public static void clientSetupEvent(FMLClientSetupEvent event) {
         ClientUtils.structuresScreen = new KeyBinding("key.customStructuresScreen", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, 75,
               "key.categories.shrines");
         ClientRegistry.registerKeyBinding(ClientUtils.structuresScreen);
      }
   }
}