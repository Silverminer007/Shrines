/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.stc;

import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class STCSyncAvailableDimensions implements IPacket {
   private final List<String> availableDimensions;

   public STCSyncAvailableDimensions(List<String> availableDimensions) {
      this.availableDimensions = availableDimensions;
   }

   public STCSyncAvailableDimensions(FriendlyByteBuf buf) {
      CompoundTag tag = buf.readNbt();
      if (tag == null) {
         this.availableDimensions = new ArrayList<>();
      } else {
         ListTag dimensions = tag.getList("dimensions", 8);// Type 8 is StringTag
         this.availableDimensions = dimensions.stream().filter(dimension -> dimension instanceof StringTag).map(Tag::getAsString).collect(Collectors.toList());
      }
   }

   public void toBytes(FriendlyByteBuf buf) {
      CompoundTag tag = new CompoundTag();
      ListTag dimensions = new ListTag();
      dimensions.addAll(this.availableDimensions.stream().map(StringTag::valueOf).toList());
      tag.put("dimensions", dimensions);
      buf.writeNbt(tag);
   }

   public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         PackageManagerProvider.CLIENT.setAvailableDimensions(this.availableDimensions);
      });
      ctx.get().setPacketHandled(true);
   }
}