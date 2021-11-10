package com.silverminer.shrines.utils.network.cts;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.TemplateIdentifier;
import com.silverminer.shrines.utils.TemplatePool;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCEditStructuresPacketPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CTSAddTemplatePoolPacket implements IPacket {
    private final TemplatePool pool;
    private final String packetID;

    public CTSAddTemplatePoolPacket(TemplatePool pool, String packetID) {
        this.pool = pool;
        this.packetID = packetID;
    }

    public static void encode(CTSAddTemplatePoolPacket pkt, PacketBuffer buf) {
        buf.writeNbt(pkt.pool.write());
        buf.writeUtf(pkt.packetID);
    }

    public static CTSAddTemplatePoolPacket decode(PacketBuffer buf) {
        return new CTSAddTemplatePoolPacket(TemplatePool.read(buf.readNbt()), buf.readUtf());
    }

    public static void handle(CTSAddTemplatePoolPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    private static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSAddTemplatePoolPacket packet, ServerPlayerEntity sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.addTemplatePool(packet.pool, packet.packetID);
                    ArrayList<StructuresPacket> packets = Lists.newArrayList();
                    packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
                    ShrinesPacketHandler.sendTo(new STCEditStructuresPacketPacket(packets, packet.packetID, 2),
                            sender);
                }
            };
        }
    }
}