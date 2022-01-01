package com.silverminer.shrines.utils.network.stc;

import com.silverminer.shrines.gui.packets.WorkingScreen;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.packages.PackageManagerProvider;
import net.minecraft.client.Minecraft;
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
      ctx.get().enqueueWork(() -> {
         if (Minecraft.getInstance().screen instanceof WorkingScreen savingScreen) {
            Minecraft.getInstance().setScreen(this.success ? savingScreen.getLastScreen() : savingScreen.getScreenOnFail());
            if (success) {
               PackageManagerProvider.CLIENT.stopEditing();
            }
         } else {
            Minecraft.getInstance().setScreen(null);
            PackageManagerProvider.CLIENT.stopEditing();
         }
      });
      ctx.get().setPacketHandled(true);
   }
}