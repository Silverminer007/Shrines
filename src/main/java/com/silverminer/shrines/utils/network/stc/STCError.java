package com.silverminer.shrines.utils.network.stc;

import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.utils.CalculationError;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class STCError implements IPacket {
   private final CalculationError error;

   public STCError(CalculationError error) {
      this.error = error;
   }

   public STCError(FriendlyByteBuf buf) {
      CompoundTag tag = buf.readNbt();
      if (tag != null) {
         this.error = new CalculationError(tag);
      } else {
         this.error = new CalculationError("Unable to read error", "Failed to parse/read error from server. There was an other one but this one happened afterwards");
      }
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeNbt(this.error.save());
   }

   public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         PackageManagerProvider.CLIENT.onError(this.error);
      });
      ctx.get().setPacketHandled(true);
   }
}