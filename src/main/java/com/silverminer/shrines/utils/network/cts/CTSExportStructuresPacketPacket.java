/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.cts;

import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.ZIPUtils;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCErrorPacket;
import com.silverminer.shrines.utils.network.stc.STCExportStructuresPacketPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Supplier;

public class CTSExportStructuresPacketPacket implements IPacket {
    private final String packetSaveName;
    private final String packetDisplayName;
    private final String exportDestination;

    public CTSExportStructuresPacketPacket(String packetSaveName, String packetDisplayName, String exportDestination) {
        this.packetSaveName = packetSaveName;
        this.packetDisplayName = packetDisplayName;
        this.exportDestination = exportDestination;
    }

    public static void encode(CTSExportStructuresPacketPacket pkt, PacketBuffer buf) {
        buf.writeUtf(pkt.packetSaveName);
        buf.writeUtf(pkt.packetDisplayName);
        buf.writeUtf(pkt.exportDestination);
    }

    public static CTSExportStructuresPacketPacket decode(PacketBuffer buf) {
        return new CTSExportStructuresPacketPacket(buf.readUtf(), buf.readUtf(), buf.readUtf());
    }

    public static void handle(CTSExportStructuresPacketPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    private static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSExportStructuresPacketPacket packet, ServerPlayerEntity sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.saveStructures();
                    File source = new File(StructureLoadUtils.getPacketsSaveLocation(), packet.packetSaveName);
                    File destinationName = StructureLoadUtils.getExportCacheLocation();
                    if (source != null) {
                        File exportedFile = ZIPUtils.compressArchive(source, destinationName, packet.packetDisplayName);
                        if (exportedFile != null) {
                            try {
                                byte[] archive = Files.readAllBytes(exportedFile.toPath());
                                ShrinesPacketHandler.sendTo(new STCExportStructuresPacketPacket(packet.exportDestination, packet.packetDisplayName, archive), sender);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        ShrinesPacketHandler.sendTo(new STCErrorPacket("Failed to export Structures Packet", "Failed to read Structures Packet from disk"),
                                sender);
                    }
                }
            };
        }
    }
}