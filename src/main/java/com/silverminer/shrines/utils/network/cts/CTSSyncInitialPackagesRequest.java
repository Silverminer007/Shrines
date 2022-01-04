package com.silverminer.shrines.utils.network.cts;

import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CTSSyncInitialPackagesRequest implements IPacket {
   public CTSSyncInitialPackagesRequest() {
   }

   public CTSSyncInitialPackagesRequest(FriendlyByteBuf buf) {
   }

   public void toBytes(FriendlyByteBuf buf) {
   }

   public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> PackageManagerProvider.SERVER.syncInitialPackagesToClient(ctx.get().getSender().getUUID()));
      ctx.get().setPacketHandled(true);
   }
}