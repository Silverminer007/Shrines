package com.silverminer.shrines.utils.network.cts;

import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CTSPlayerLeaveQueue implements IPacket {
   private final UUID playerID;

   public CTSPlayerLeaveQueue(UUID playerID) {
      this.playerID = playerID;
   }

   public CTSPlayerLeaveQueue(FriendlyByteBuf buf) {
      this.playerID = buf.readUUID();
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeUUID(this.playerID);
   }

   public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         PackageManagerProvider.SERVER.onPlayerLeaveQueue(this.playerID);
      });
      ctx.get().setPacketHandled(true);
   }
}