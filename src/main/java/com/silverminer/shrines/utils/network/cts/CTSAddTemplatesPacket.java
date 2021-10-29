package com.silverminer.shrines.utils.network.cts;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.TemplateIdentifier;
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

public class CTSAddTemplatesPacket implements IPacket {
    private final List<TemplateIdentifier> templates;
    private final String packetID;

    public CTSAddTemplatesPacket(List<TemplateIdentifier> templates, String packetID) {
        this.templates = templates;
        this.packetID = packetID;
    }

    public static void encode(CTSAddTemplatesPacket pkt, PacketBuffer buf) {
        buf.writeInt(pkt.templates.size());
        for (TemplateIdentifier template : pkt.templates) {
            buf.writeNbt(template.write());
        }
        buf.writeUtf(pkt.packetID);
    }

    public static CTSAddTemplatesPacket decode(PacketBuffer buf) {
        int templateCount = buf.readInt();
        ArrayList<TemplateIdentifier> templates = Lists.newArrayList();
        for (int i = 0; i < templateCount; i++) {
            CompoundNBT nbt = buf.readNbt(NBTSizeTracker.UNLIMITED);// Not really save, but template files can be huge and general size limit is to low
            if (nbt == null) {
                continue;
            }
            templates.add(TemplateIdentifier.read(nbt));
        }
        return new CTSAddTemplatesPacket(templates, buf.readUtf());
    }

    public static void handle(CTSAddTemplatesPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    private static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSAddTemplatesPacket packet, ServerPlayerEntity sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.addTemplatesToPacket(packet.templates, packet.packetID);
                    ArrayList<StructuresPacket> packets = Lists.newArrayList();
                    packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
                    ShrinesPacketHandler.sendTo(new STCEditStructuresPacketPacket(packets, packet.packetID, 1),
                            sender);
                }
            };
        }
    }
}