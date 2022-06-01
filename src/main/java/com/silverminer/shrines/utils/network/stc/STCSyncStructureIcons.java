package com.silverminer.shrines.utils.network.stc;

import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.container.StructureIconContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class STCSyncStructureIcons implements IPacket {
   private final StructureIconContainer structureIconContainer;

   public STCSyncStructureIcons(StructureIconContainer packages) {
      this.structureIconContainer = packages;
   }

   public STCSyncStructureIcons(FriendlyByteBuf buf) {
      this.structureIconContainer = StructureIconContainer.load(buf.readNbt());
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeNbt(StructureIconContainer.save(this.structureIconContainer));
   }

   public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> PackageManagerProvider.CLIENT.cacheStructureIcons(this.structureIconContainer));
      ctx.get().setPacketHandled(true);
   }
}