package com.silverminer.shrines.utils.network.stc;

import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class STCErrorPacket implements IPacket {
    private final String errorTitle;
    private final String error;

    public STCErrorPacket(String errorTitle, String error) {
        this.errorTitle = errorTitle;
        this.error = error;
    }

    public static void encode(STCErrorPacket pkt, FriendlyByteBuf buf) {
        buf.writeUtf(pkt.errorTitle);
        buf.writeUtf(pkt.error);
    }

    public static STCErrorPacket decode(FriendlyByteBuf buf) {
        return new STCErrorPacket(buf.readUtf(), buf.readUtf());
    }

    public static void handle(STCErrorPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.handle(packet)));
        context.get().setPacketHandled(true);
    }

    public static class Handle {
        public static DistExecutor.SafeRunnable handle(STCErrorPacket packet) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    ClientUtils.showErrorToast(packet.errorTitle, packet.error);
                }
            };
        }
    }
}