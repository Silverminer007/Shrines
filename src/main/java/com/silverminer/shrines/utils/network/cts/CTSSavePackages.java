/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.cts;

import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.container.StructurePackageContainer;
import com.silverminer.shrines.utils.CalculationError;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Supplier;

public class CTSSavePackages implements IPacket {
   private final StructurePackageContainer packages;

   public CTSSavePackages(@NotNull FriendlyByteBuf buf) {
      this.packages = StructurePackageContainer.load(buf.readNbt());
   }

   public CTSSavePackages(StructurePackageContainer packages) {
      this.packages = packages;
   }

   public void toBytes(@NotNull FriendlyByteBuf buf) {
      buf.writeNbt(StructurePackageContainer.save(this.packages));
   }

   public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         ServerPlayer sender = ctx.get().getSender();
         if (sender != null) {
            UUID senderID = sender.getUUID();
            PackageManagerProvider.SERVER.clientSavedPackages(this.packages, senderID);
         } else {
            PackageManagerProvider.SERVER.onError(new CalculationError("Failed to save packages", "Unable to find the clients ID, that triggered save"));
         }
      });
      ctx.get().setPacketHandled(true);
   }
}