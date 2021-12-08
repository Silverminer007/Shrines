package com.silverminer.shrines.utils.network.cts;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCEditStructuresPacketPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CTSDeleteTemplatesPacket implements IPacket {
    private final ResourceLocation template;
    private final String packetID;

    public CTSDeleteTemplatesPacket(ResourceLocation template, String packetID) {
        this.template = template;
        this.packetID = packetID;
    }

    public static void encode(CTSDeleteTemplatesPacket pkt, FriendlyByteBuf buf) {
        buf.writeUtf(pkt.template.toString());
        buf.writeUtf(pkt.packetID);
    }

    public static CTSDeleteTemplatesPacket decode(FriendlyByteBuf buf) {
        return new CTSDeleteTemplatesPacket(new ResourceLocation(buf.readUtf()), buf.readUtf());
    }

    public static void handle(CTSDeleteTemplatesPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    private static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSDeleteTemplatesPacket packet, ServerPlayer sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.deleteTemplates(packet.template, packet.packetID);
                    ArrayList<StructuresPacket> packets = Lists.newArrayList();
                    packets.addAll(StructureLoadUtils.getStructurePackets());
                    ShrinesPacketHandler.sendTo(new STCEditStructuresPacketPacket(packets, packet.packetID, 1),
                            sender);
                }
            };
        }
    }
}