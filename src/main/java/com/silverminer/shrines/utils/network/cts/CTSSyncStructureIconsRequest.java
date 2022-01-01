package com.silverminer.shrines.utils.network.cts;

import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.packages.PackageManagerProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CTSSyncStructureIconsRequest implements IPacket {
   public CTSSyncStructureIconsRequest() {
   }

   public CTSSyncStructureIconsRequest(FriendlyByteBuf buf) {
   }

   public void toBytes(FriendlyByteBuf buf) {
   }

   public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         PackageManagerProvider.SERVER.syncStructureIconsToClient(ctx.get().getSender().getUUID());
      });
      ctx.get().setPacketHandled(true);
   }
}