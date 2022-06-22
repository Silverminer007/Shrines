/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.network;

import com.silverminer.shrines.client.ClientUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record STCUnlockSnippetPacket(ResourceLocation storyID, String snippetText) {
   @Contract("_ -> new")
   public static @NotNull STCUnlockSnippetPacket decoder(@NotNull FriendlyByteBuf buffer) {
      return new STCUnlockSnippetPacket(buffer.readResourceLocation(), buffer.readUtf());
   }

   public void encoder(@NotNull FriendlyByteBuf buffer) {
      buffer.writeResourceLocation(this.storyID());
      buffer.writeUtf(this.snippetText());
   }

   public void messageConsumer(@NotNull Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> ClientUtils.onUnlockSnippet(this.storyID(), this.snippetText()));
      ctx.get().setPacketHandled(true);
   }
}
