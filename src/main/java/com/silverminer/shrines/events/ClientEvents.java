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
import com.silverminer.shrines.packages.client.ClientStructurePackageManager;
import com.silverminer.shrines.packages.io.DirectoryStructureAccessor;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSSyncAvailableDimensionsRequest;
import com.silverminer.shrines.utils.network.cts.CTSSyncAvailableMaterialATypesRequest;
import com.silverminer.shrines.utils.network.cts.CTSSyncInitialPackagesRequest;
import com.silverminer.shrines.utils.network.cts.CTSSyncStructureIconsRequest;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
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
            if (ClientUtils.editPackagesKeyMapping.isDown() && Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasPermissions(2)) {
                PackageManagerProvider.CLIENT.joinQueue();
            }
            if (ClientUtils.openNovelsKeyMapping.isDown()) {
                PackageManagerProvider.CLIENT.showNovelsOverview();
            }
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(ClientPlayerNetworkEvent.LoggedInEvent event) {
            if (event.getPlayer() != null) {
                PackageManagerProvider.CLIENT.setCurrentStage(ClientStructurePackageManager.Stage.AVAILABLE);
                PackageManagerProvider.CLIENT.setPlayerID(event.getPlayer().getUUID());
                ShrinesPacketHandler.sendToServer(new CTSSyncStructureIconsRequest());
                ShrinesPacketHandler.sendToServer(new CTSSyncAvailableDimensionsRequest());
                ShrinesPacketHandler.sendToServer(new CTSSyncAvailableMaterialATypesRequest());
                ShrinesPacketHandler.sendToServer(new CTSSyncInitialPackagesRequest());
            } else {
                LOGGER.error("Failed to initialise CLIENT Package Manager, because no player was available");
            }
        }

        @SubscribeEvent
        public static void onPlayerLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
            // Make sure that we're able to contact the server before we clear everything
            // Don't show an error, because Minecraft logs the player out before a world can be entered and the player is logged in again. At the first log out the connections aren't available
            if (event.getConnection() != null) {
                PackageManagerProvider.CLIENT.leaveQueue();
                PackageManagerProvider.CLIENT.setCurrentStage(ClientStructurePackageManager.Stage.EMPTY);
                PackageManagerProvider.CLIENT.setPlayerID(null);
                PackageManagerProvider.CLIENT.setNovels(null);
                PackageManagerProvider.CLIENT.setNovelsRegistryData(null);
                PackageManagerProvider.CLIENT.setAvailableDimensions(null);
                PackageManagerProvider.CLIENT.setAvailableMaterials(null);
                PackageManagerProvider.CLIENT.setAvailableTypes(null);
                PackageManagerProvider.CLIENT.clearCache();
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