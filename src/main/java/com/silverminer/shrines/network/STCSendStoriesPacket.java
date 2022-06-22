/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.network;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.silverminer.shrines.client.ClientUtils;
import com.silverminer.shrines.stories.Snippet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public record STCSendStoriesPacket(List<List<Snippet>> unlockedStories) {
   private static final Codec<List<List<Snippet>>> CODEC = Codec.list(Codec.list(Snippet.CODEC));
   @Contract("_ -> new")
   public static @NotNull STCSendStoriesPacket decoder(@NotNull FriendlyByteBuf buffer) {
      return new STCSendStoriesPacket(CODEC.decode(NbtOps.INSTANCE, Objects.requireNonNull(buffer.readNbt()).get("snippets")).result().map(Pair::getFirst).orElseThrow());
   }

   public void encoder(@NotNull FriendlyByteBuf buffer) {
      CompoundTag tag = new CompoundTag();
      tag.put("snippets", CODEC.encodeStart(NbtOps.INSTANCE, this.unlockedStories()).result().orElseThrow());
      buffer.writeNbt(tag);
   }

   public void messageConsumer(@NotNull Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> ClientUtils.openStoriesScreen(this.unlockedStories()));
      ctx.get().setPacketHandled(true);
   }
}
