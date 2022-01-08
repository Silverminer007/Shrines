/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.stc;

import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.container.StructurePackageContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class STCSyncInitialPackages extends STCSyncPackages {

   public STCSyncInitialPackages(StructurePackageContainer packages) {
      super(packages);
   }

   public STCSyncInitialPackages(FriendlyByteBuf buf) {
      super(buf);
   }

   @Override
   public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> PackageManagerProvider.CLIENT.setInitialPackages(this.packages));
      ctx.get().setPacketHandled(true);
   }
}