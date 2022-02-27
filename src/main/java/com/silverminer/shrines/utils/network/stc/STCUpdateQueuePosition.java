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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class STCUpdateQueuePosition implements IPacket {
   private final int newPosition;

   public STCUpdateQueuePosition(int newPosition) {
      this.newPosition = newPosition;
   }

   public STCUpdateQueuePosition(FriendlyByteBuf buf) {
      this.newPosition = buf.readInt();
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeInt(this.newPosition);
   }

   public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> PackageManagerProvider.CLIENT.updateQueuePosition(this.newPosition));
      ctx.get().setPacketHandled(true);
   }
}