/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.stc;

import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Supplier;

public class STCExportStructuresPacketPacket implements IPacket {
    private final String exportDestination;
    private final String fileName;
    private final byte[] archive;

    public STCExportStructuresPacketPacket(String exportDestination, String fileName, byte[] archive) {
        this.exportDestination = exportDestination;
        this.fileName = fileName;
        this.archive = archive;
    }

    public static void encode(STCExportStructuresPacketPacket pkt, PacketBuffer buf) {
        buf.writeUtf(pkt.exportDestination);
        buf.writeUtf(pkt.fileName);
        buf.writeByteArray(pkt.archive);
    }

    public static STCExportStructuresPacketPacket decode(PacketBuffer buf) {
        return new STCExportStructuresPacketPacket(buf.readUtf(), buf.readUtf(), buf.readByteArray());
    }

    public static void handle(STCExportStructuresPacketPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.handle(packet)));
        context.get().setPacketHandled(true);
    }

    public static class Handle {
        public static DistExecutor.SafeRunnable handle(STCExportStructuresPacketPacket packet) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    File saveDestination = new File(packet.exportDestination, packet.fileName + ".zip");
                    int i = 1;
                    while (saveDestination.exists()) {
                        saveDestination = new File(packet.exportDestination, packet.fileName + " (" + i + ")" + ".zip");
                        i++;
                    }
                    try {
                        Files.write(saveDestination.toPath(), packet.archive);
                        Util.getPlatform().openFile(new File(packet.exportDestination));
                    } catch (IOException e) {
                        ClientUtils.showErrorToast("Failed to export Structures Packet", e.getMessage());
                    }
                }
            };
        }
    }
}