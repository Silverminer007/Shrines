package com.silverminer.shrines.utils.network.stc;

import com.silverminer.shrines.packages.container.NovelDataContainer;
import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class STCSyncNovels implements IPacket {
   private final NovelDataContainer novelDataContainer;

   public STCSyncNovels(NovelDataContainer packages) {
      this.novelDataContainer = packages;
   }

   public STCSyncNovels(FriendlyByteBuf buf) {
      this.novelDataContainer = NovelDataContainer.load(buf.readNbt());
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeNbt(NovelDataContainer.save(this.novelDataContainer));
   }

   public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         PackageManagerProvider.CLIENT.setNovels(this.novelDataContainer);
         PackageManagerProvider.CLIENT.openNovelsOverviewScreen();
      });
      ctx.get().setPacketHandled(true);
   }
}