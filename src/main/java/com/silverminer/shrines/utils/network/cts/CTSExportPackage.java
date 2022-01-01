package com.silverminer.shrines.utils.network.cts;

import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.container.StructurePackageContainer;
import com.silverminer.shrines.utils.CalculationError;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Supplier;

public class CTSExportPackage extends CTSSavePackages {
   private final UUID packageID;

   public CTSExportPackage(UUID packageID, StructurePackageContainer structurePackageContainer) {
      super(structurePackageContainer);
      this.packageID = packageID;
   }

   public CTSExportPackage(@NotNull FriendlyByteBuf buf) {
      super(buf);
      this.packageID = buf.readUUID();
   }

   public void toBytes(@NotNull FriendlyByteBuf buf) {
      super.toBytes(buf);
      buf.writeUUID(this.packageID);
   }

   public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
      super.handle(ctx);
      ctx.get().enqueueWork(() -> PackageManagerProvider.SERVER.exportPackage(this.packageID));
      ctx.get().setPacketHandled(true);
   }
}