/*
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

import java.util.function.Supplier;

public class CTSSyncPackagesRequest implements IPacket {
   public CTSSyncPackagesRequest() {
   }

   public CTSSyncPackagesRequest(FriendlyByteBuf buf) {
   }

   public void toBytes(FriendlyByteBuf buf) {
   }

   public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         PackageManagerProvider.SERVER.syncPackagesToClient(ctx.get().getSender().getUUID());
      });
      ctx.get().setPacketHandled(true);
   }
}