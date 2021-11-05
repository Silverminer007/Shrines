package com.silverminer.shrines.utils.network.cts;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.ZIPUtils;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCErrorPacket;
import com.silverminer.shrines.utils.network.stc.STCOpenStructuresPacketEditPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.function.Supplier;

public class CTSImportStructuresPacketPacket implements IPacket {
    private final String fileName;
    private final byte[] archive;

    public CTSImportStructuresPacketPacket(String fileName, byte[] archive) {
        this.fileName = fileName;
        this.archive = archive;
    }

    public static void encode(CTSImportStructuresPacketPacket pkt, PacketBuffer buf) {
        buf.writeUtf(pkt.fileName);
        buf.writeByteArray(pkt.archive);
    }

    public static CTSImportStructuresPacketPacket decode(PacketBuffer buf) {
        return new CTSImportStructuresPacketPacket(buf.readUtf(), buf.readByteArray());
    }

    public static void handle(CTSImportStructuresPacketPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    public static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSImportStructuresPacketPacket packet, ServerPlayerEntity sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.importStructuresPacket(packet.fileName, packet.archive, sender);
                }
            };
        }
    }
}