/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines;

import com.silverminer.shrines.capabilities.CapabilityManager;
import com.silverminer.shrines.client.ClientUtils;
import com.silverminer.shrines.client.StoryScreen;
import com.silverminer.shrines.commands.LocateInBiomeCommand;
import com.silverminer.shrines.commands.VariationCommand;
import com.silverminer.shrines.config.ShrinesConfig;
import com.silverminer.shrines.network.NetworkManager;
import com.silverminer.shrines.network.STCUnlockSnippetPacket;
import com.silverminer.shrines.registries.StructureRegistry;
import com.silverminer.shrines.stories.Story;
import com.silverminer.shrines.test.Tests;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.datafix.fixes.StructuresBecomeConfiguredFix;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class Events {
   @Mod.EventBusSubscriber(modid = Shrines.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
   public static class ModEventBus {
      @SubscribeEvent
      public static void fmlClientSetupEvent(FMLClientSetupEvent event) {
         ClientRegistry.registerKeyBinding(ClientUtils.KeyMappings.openStoryScreen);
      }

      @SubscribeEvent
      public static void addPackFinders(@NotNull AddPackFindersEvent event) {
         PackSource source = PackSource.decorating("pack.source.shrines");
         event.addRepositorySource(new FolderRepositorySource(FMLPaths.GAMEDIR.get().resolve("datapacks").toFile(), source));
      }

      @SubscribeEvent
      public static void registerGameTests(@NotNull RegisterGameTestsEvent event) {
         event.register(Tests.class);
      }
   }

   @Mod.EventBusSubscriber(modid = Shrines.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
   public static class ForgeEventBus {
      @SubscribeEvent
      public static void registerCommands(@NotNull RegisterCommandsEvent event) {
         VariationCommand.register(event.getDispatcher());
         LocateInBiomeCommand.register(event.getDispatcher());
      }

      @SubscribeEvent
      public static void registerStructureConversions(@NotNull RegisterStructureConversionsEvent event) {
         event.register("shrines:ballon", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:balloon"));
         event.register("shrines:high_tempel", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:high_temple"));
         event.register("shrines:small_tempel", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:small_temple"));
         event.register("shrines:player_house", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:tall_player_house"));
         for (String structure : ShrinesConfig.removedStructures.get()) {
            try {
               event.register(structure, StructuresBecomeConfiguredFix.Conversion.trivial(StructureRegistry.DELETED_STRUCTURE.getId().toString()));
            } catch (NullPointerException | IllegalArgumentException e) {
               throw new RuntimeException("You've miss-configured shrines removed structures: " + e);
            }
         }
      }

      @SubscribeEvent
      public static void onPlayerTickEvent(TickEvent.@NotNull PlayerTickEvent event) {
         if (event.player instanceof ServerPlayer serverPlayer) {
            Level level = serverPlayer.getLevel();
            if (event.side.isClient() || level.isClientSide()) {
               return;
            }
            RegistryAccess registryAccess = level.registryAccess();
            Registry<Story> storyRegistry = registryAccess.ownedRegistryOrThrow(Story.REGISTRY);
            for (var story : storyRegistry) {
               if (story.triggers().stream().anyMatch(trigger -> trigger.matches(level, serverPlayer))) {
                  serverPlayer.getCapability(CapabilityManager.STORY).ifPresent(iStoryCapability -> {
                     String text = iStoryCapability.unlockSnippet(story, level);
                     NetworkManager.SIMPLE_CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new STCUnlockSnippetPacket(storyRegistry.getKey(story), text));
                  });
               }
            }
         }
      }

      @SubscribeEvent
      public static void clientTickEvent(TickEvent.ClientTickEvent event) {
         if (ClientUtils.KeyMappings.openStoryScreen.isDown()) {
            StoryScreen.open();
         }
      }

      @SubscribeEvent
      public static void onPlayerClone(PlayerEvent.@NotNull Clone event) {
         if(event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(CapabilityManager.STORY).ifPresent(originalStoryCap ->
                  event.getPlayer().getCapability(CapabilityManager.STORY).ifPresent(newStoryCap -> newStoryCap.deserializeNBT(originalStoryCap.serializeNBT())));
            event.getOriginal().invalidateCaps();
         }
      }
   }
}