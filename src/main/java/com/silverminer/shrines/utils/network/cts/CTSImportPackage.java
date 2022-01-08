/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.cts;

import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.container.StructurePackageContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CTSImportPackage extends CTSSavePackages {
   private final byte[] packageFile;

   public CTSImportPackage(byte[] packageFile, StructurePackageContainer packages) {
      super(packages);
      this.packageFile = packageFile;
   }

   public CTSImportPackage(@NotNull FriendlyByteBuf buf) {
      super(buf);
      this.packageFile = buf.readByteArray();
   }

   public void toBytes(@NotNull FriendlyByteBuf buf) {
      super.toBytes(buf);
      buf.writeByteArray(this.packageFile);
   }

   public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
      super.handle(ctx);
      ctx.get().enqueueWork(() -> {
         // All packages are saved from super class, and we can now import the package
         PackageManagerProvider.SERVER.importPackage(this.packageFile);
      });
      ctx.get().setPacketHandled(true);
   }
}