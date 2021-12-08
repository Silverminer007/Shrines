package com.silverminer.shrines.utils.network.cts;

import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class CTSPlayerLeftQueuePacket implements IPacket {
    protected static final Logger LOGGER = LogManager.getLogger(CTSPlayerLeftQueuePacket.class);

    public CTSPlayerLeftQueuePacket() {
    }

    public static void encode(CTSPlayerLeftQueuePacket pkt, FriendlyByteBuf buf) {

    }

    public static CTSPlayerLeftQueuePacket decode(FriendlyByteBuf buf) {
        return new CTSPlayerLeftQueuePacket();
    }

    public static void handle(CTSPlayerLeftQueuePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    private static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSPlayerLeftQueuePacket packet, ServerPlayer sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.playerLeftQueue(sender.getUUID());
                }
            };
        }
    }
}