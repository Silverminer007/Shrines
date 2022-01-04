package com.silverminer.shrines.utils.network.stc;

import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class STCSendSaveResult implements IPacket {
   private final boolean success;

   public STCSendSaveResult(boolean success) {
      this.success = success;
   }

   public STCSendSaveResult(FriendlyByteBuf buf) {
      this.success = buf.readBoolean();
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeBoolean(this.success);
   }

   public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> PackageManagerProvider.CLIENT.handleSaveResult(this.success));
      ctx.get().setPacketHandled(true);
   }
}