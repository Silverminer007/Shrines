package com.silverminer.shrines.utils.network.cts;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCOpenStructuresPacketEditPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CTSEditedStructurePacketPacket implements IPacket {
    private final StructuresPacket packet;

    public CTSEditedStructurePacketPacket(StructuresPacket packet) {
        this.packet = packet;
    }

    public static void encode(CTSEditedStructurePacketPacket pkt, FriendlyByteBuf buf) {
        buf.writeNbt(StructuresPacket.saveToNetwork(pkt.packet));
    }

    public static CTSEditedStructurePacketPacket decode(FriendlyByteBuf buf) {
        return new CTSEditedStructurePacketPacket(StructuresPacket.read(buf.readNbt(), null));
    }

    public static void handle(CTSEditedStructurePacketPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    private static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSEditedStructurePacketPacket packet, ServerPlayer sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.updateStructuresPacket(packet.packet);
                    ArrayList<StructuresPacket> packets = Lists.newArrayList();
                    packets.addAll(StructureLoadUtils.getStructurePackets());
                    ShrinesPacketHandler.sendTo(new STCOpenStructuresPacketEditPacket(packets),
                            sender);
                }
            };
        }
    }
}