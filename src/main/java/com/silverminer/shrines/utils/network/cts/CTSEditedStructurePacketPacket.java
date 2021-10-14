package com.silverminer.shrines.utils.network.cts;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCOpenStructuresPacketEditPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CTSEditedStructurePacketPacket implements IPacket {
    private final StructuresPacket packet;

    public CTSEditedStructurePacketPacket(StructuresPacket packet) {
        this.packet = packet;
    }

    public static void encode(CTSEditedStructurePacketPacket pkt, PacketBuffer buf) {
        buf.writeNbt(StructuresPacket.toCompound(pkt.packet));
    }

    public static CTSEditedStructurePacketPacket decode(PacketBuffer buf) {
        return new CTSEditedStructurePacketPacket(StructuresPacket.fromCompound(buf.readNbt(), null, true));
    }

    public static void handle(CTSEditedStructurePacketPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    private static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSEditedStructurePacketPacket packet, ServerPlayerEntity sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.updateStructuresPacket(false, packet.packet);
                    ArrayList<StructuresPacket> packets = Lists.newArrayList();
                    packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
                    ShrinesPacketHandler.sendTo(new STCOpenStructuresPacketEditPacket(packets),
                            sender);
                }
            };
        }
    }
}