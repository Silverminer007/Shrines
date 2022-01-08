/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.events;

import com.mojang.blaze3d.platform.InputConstants;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.io.DirectoryStructureAccessor;
import com.silverminer.shrines.utils.ClientUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
         if (ClientUtils.editPackagesKeyMapping.isDown()) {
            PackageManagerProvider.CLIENT.joinQueue();
         }
         if (ClientUtils.openNovelsKeyMapping.isDown()) {
            PackageManagerProvider.CLIENT.showNovelsOverview();
         }
      }
   }

   @EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
   public static class ModEventBus {
      @SubscribeEvent
      public static void clientSetupEvent(FMLClientSetupEvent event) {
         ClientUtils.openNovelsKeyMapping = new KeyMapping("key.showStructureNovels", KeyConflictContext.IN_GAME, KeyModifier.ALT, InputConstants.getKey(InputConstants.KEY_K, 0), "key.categories.shrines");
         ClientRegistry.registerKeyBinding(ClientUtils.openNovelsKeyMapping);
         ClientUtils.editPackagesKeyMapping = new KeyMapping("key.customStructuresScreen", KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputConstants.getKey(InputConstants.KEY_K, 0), "key.categories.shrines");
         ClientRegistry.registerKeyBinding(ClientUtils.editPackagesKeyMapping);
      }

      @SubscribeEvent
      public static void addPackFinder(AddPackFindersEvent event) {
         // Register the icons Cache here on client only and the packages in CommonEvents, because we need these on both sides
         PackSource source = PackSource.decorating("pack.source.shrines");
         event.addRepositorySource(new FolderRepositorySource(DirectoryStructureAccessor.RECENT.getImagesCachePath().toFile(), source));
      }
   }
}