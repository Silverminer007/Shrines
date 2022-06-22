/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.network;

import com.mojang.datafixers.util.Pair;
import com.silverminer.shrines.capabilities.CapabilityManager;
import com.silverminer.shrines.stories.Story;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public record CTSFetchStoriesPacket() {
   @Contract("_ -> new")
   public static @NotNull CTSFetchStoriesPacket decoder(@NotNull FriendlyByteBuf buffer) {
      return new CTSFetchStoriesPacket();
   }

   public void encoder(@NotNull FriendlyByteBuf buffer) {
   }

   public void messageConsumer(@NotNull Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> Optional.ofNullable(ctx.get().getSender()).ifPresent(player -> player.getCapability(CapabilityManager.STORY).ifPresent(iStoryCapability -> {
         Registry<Story> storyRegistry = player.getLevel().registryAccess().ownedRegistryOrThrow(Story.REGISTRY);
         var unlockedStories = iStoryCapability.getUnlockedSnippets().stream().map(pair -> {
            Story story = storyRegistry.get(pair.getFirst());
            if(story == null){
               return null;
            }
            return story.snippets().stream().filter(snippet -> snippet.id() <= pair.getSecond()).toList();
         }).filter(Objects::nonNull).toList();
         NetworkManager.SIMPLE_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new STCSendStoriesPacket(unlockedStories));
      })));
      ctx.get().setPacketHandled(true);
   }
}
