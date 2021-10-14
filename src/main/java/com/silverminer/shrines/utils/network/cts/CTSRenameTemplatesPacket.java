package com.silverminer.shrines.utils.network.cts;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCEditStructuresPacketPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

public class CTSRenameTemplatesPacket implements IPacket {
    private final ResourceLocation oldName;
    private final ResourceLocation newName;
    private final int packetID;

    public CTSRenameTemplatesPacket(ResourceLocation oldName, ResourceLocation newName, int packetID) {
        this.oldName = oldName;
        this.newName = newName;
        this.packetID = packetID;
    }

    public static void encode(CTSRenameTemplatesPacket pkt, PacketBuffer buf) {
        buf.writeUtf(pkt.oldName.toString());
        buf.writeUtf(pkt.newName.toString());
        buf.writeInt(pkt.packetID);
    }

    public static CTSRenameTemplatesPacket decode(PacketBuffer buf) {
        return new CTSRenameTemplatesPacket(new ResourceLocation(buf.readUtf()), new ResourceLocation(buf.readUtf()), buf.readInt());
    }

    public static void handle(CTSRenameTemplatesPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    private static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSRenameTemplatesPacket packet, ServerPlayerEntity sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.renameTemplates(packet.oldName, packet.newName, packet.packetID);
                    MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
                    ArrayList<StructuresPacket> packets = Lists.newArrayList();
                    packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
                    ShrinesPacketHandler.sendTo(new STCEditStructuresPacketPacket(packets, packet.packetID, 1),
                            sender);
                }
            };
        }
    }
}