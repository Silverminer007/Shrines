/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.cts;

import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CTSPlayerJoinQueue implements IPacket {
   private final UUID playerID;

   public CTSPlayerJoinQueue(UUID playerID) {
      this.playerID = playerID;
   }

   public CTSPlayerJoinQueue(FriendlyByteBuf buf) {
      this.playerID = buf.readUUID();
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeUUID(this.playerID);
   }

   public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         PackageManagerProvider.SERVER.onPlayerJoinQueue(this.playerID);
      });
      ctx.get().setPacketHandled(true);
   }
}