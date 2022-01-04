package com.silverminer.shrines.utils.network.stc;

import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.container.StructurePackageContainer;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class STCSyncPackages implements IPacket {
   protected final StructurePackageContainer packages;

   public STCSyncPackages(StructurePackageContainer packages) {
      this.packages = packages;
   }

   public STCSyncPackages(FriendlyByteBuf buf) {
      this.packages = StructurePackageContainer.load(buf.readNbt());
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeNbt(StructurePackageContainer.save(this.packages));
   }

   public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> PackageManagerProvider.CLIENT.setPackages(this.packages));
      ctx.get().setPacketHandled(true);
   }
}