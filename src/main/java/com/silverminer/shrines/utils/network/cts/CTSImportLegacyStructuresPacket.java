package com.silverminer.shrines.utils.network.cts;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.TemplateIdentifier;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCOpenStructuresPacketEditPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CTSImportLegacyStructuresPacket implements IPacket {
    private final List<TemplateIdentifier> templates;
    private final StructuresPacket packet;

    public CTSImportLegacyStructuresPacket(StructuresPacket packet, List<TemplateIdentifier> templates) {
        this.templates = templates;
        this.packet = packet;
    }

    public static void encode(CTSImportLegacyStructuresPacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.templates.size());
        for (TemplateIdentifier template : pkt.templates) {
            buf.writeNbt(template.write());
        }
        buf.writeNbt(StructuresPacket.saveToNetwork(pkt.packet));
    }

    public static CTSImportLegacyStructuresPacket decode(FriendlyByteBuf buf) {
        int templateCount = buf.readInt();
        ArrayList<TemplateIdentifier> templates = Lists.newArrayList();
        for (int i = 0; i < templateCount; i++) {
            CompoundTag nbt = buf.readNbt(NbtAccounter.UNLIMITED);// Not really save, but template files can be huge and general size limit is to low
            if (nbt == null) {
                continue;
            }
            templates.add(TemplateIdentifier.read(nbt));
        }
        return new CTSImportLegacyStructuresPacket(StructuresPacket.read(buf.readNbt(), null), templates);
    }

    public static void handle(CTSImportLegacyStructuresPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    private static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSImportLegacyStructuresPacket packet, ServerPlayer sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.addStructuresPacket(packet.packet);
                    String packetID = packet.packet.getSaveName();
                    StructureLoadUtils.addTemplatesToPacket(packet.templates, packetID);
                    ArrayList<StructuresPacket> packets = Lists.newArrayList();
                    packets.addAll(StructureLoadUtils.getStructurePackets());
                    ShrinesPacketHandler.sendTo(new STCOpenStructuresPacketEditPacket(packets),
                            sender);
                }
            };
        }
    }
}