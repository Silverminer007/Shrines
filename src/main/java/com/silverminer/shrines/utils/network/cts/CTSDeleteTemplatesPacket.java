package com.silverminer.shrines.utils.network.cts;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCEditStructuresPacketPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CTSDeleteTemplatesPacket implements IPacket {
    private final ResourceLocation template;
    private final int packetID;

    public CTSDeleteTemplatesPacket(ResourceLocation template, int packetID) {
        this.template = template;
        this.packetID = packetID;
    }

    public static void encode(CTSDeleteTemplatesPacket pkt, PacketBuffer buf) {
        buf.writeUtf(pkt.template.toString());
        buf.writeInt(pkt.packetID);
    }

    public static CTSDeleteTemplatesPacket decode(PacketBuffer buf) {
        return new CTSDeleteTemplatesPacket(new ResourceLocation(buf.readUtf()), buf.readInt());
    }

    public static void handle(CTSDeleteTemplatesPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    private static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSDeleteTemplatesPacket packet, ServerPlayerEntity sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.deleteTemplates(packet.template, packet.packetID);
                    ArrayList<StructuresPacket> packets = Lists.newArrayList();
                    packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
                    ShrinesPacketHandler.sendTo(new STCEditStructuresPacketPacket(packets, packet.packetID, 1),
                            sender);
                }
            };
        }
    }
}