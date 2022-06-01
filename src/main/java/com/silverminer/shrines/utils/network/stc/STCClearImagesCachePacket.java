package com.silverminer.shrines.utils.network.stc;

import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class STCClearImagesCachePacket implements IPacket {

    public STCClearImagesCachePacket() {
    }

    public static void encode(STCClearImagesCachePacket pkt, PacketBuffer buf) {
    }

    public static STCClearImagesCachePacket decode(PacketBuffer buf) {
        return new STCClearImagesCachePacket();
    }

    public static void handle(STCClearImagesCachePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.handle(packet)));
        context.get().setPacketHandled(true);
    }

    public static class Handle {
        public static DistExecutor.SafeRunnable handle(STCClearImagesCachePacket packet) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.clearImagesCache();
                }
            };
        }
    }
}