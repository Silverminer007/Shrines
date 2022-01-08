/*
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
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class STCSaveExportedPackage implements IPacket {
   private final byte[] packageFile;

   public STCSaveExportedPackage(byte[] packageFile) {
      this.packageFile = packageFile;
   }

   public STCSaveExportedPackage(@NotNull FriendlyByteBuf buf) {
      this.packageFile = buf.readByteArray();
   }

   public void toBytes(@NotNull FriendlyByteBuf buf) {
      buf.writeByteArray(this.packageFile);
   }

   public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> PackageManagerProvider.CLIENT.saveExportedPackage(this.packageFile));
      ctx.get().setPacketHandled(true);
   }
}