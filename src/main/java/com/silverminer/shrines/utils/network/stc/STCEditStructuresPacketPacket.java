/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.stc;

import com.google.common.collect.Lists;
import com.silverminer.shrines.gui.novels.StructureNovelsScreen;
import com.silverminer.shrines.gui.packets.StructuresPacketsScreen;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class STCEditStructuresPacketPacket implements IPacket {
    private final ArrayList<StructuresPacket> packets;
    private final String packetID;
    /**
     * This is a flag to choose the correct edit tab\n
     * 0 -> Structures \n
     * 1 -> Templates\n
     * 2 -> Pools\n
     */
    private final int editLocation;

    public STCEditStructuresPacketPacket(ArrayList<StructuresPacket> structurePackets, String packetID, int editLocation) {
        this.packets = structurePackets;
        this.packetID = packetID;
        this.editLocation = editLocation;
    }

    public static void encode(STCEditStructuresPacketPacket pkt, PacketBuffer buf) {
        ArrayList<StructuresPacket> structurePackets = pkt.packets;
        buf.writeInt(structurePackets.size());
        for (StructuresPacket packet : structurePackets) {
            buf.writeNbt(StructuresPacket.saveToNetwork(packet));
        }
        buf.writeUtf(pkt.packetID);
        buf.writeInt(pkt.editLocation);
    }

    public static STCEditStructuresPacketPacket decode(PacketBuffer buf) {
        ArrayList<StructuresPacket> structurePackets = Lists.newArrayList();
        int packets = buf.readInt();
        for (int i = 0; i < packets; i++) {
            structurePackets.add(StructuresPacket.read(buf.readNbt(), null));
        }
        return new STCEditStructuresPacketPacket(structurePackets, buf.readUtf(), buf.readInt());
    }

    public static void handle(STCEditStructuresPacketPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.handle(packet)));
        context.get().setPacketHandled(true);
    }

    public static class Handle {
        public static DistExecutor.SafeRunnable handle(STCEditStructuresPacketPacket packet) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructuresPacketsScreen screen = new StructuresPacketsScreen(null, packet.packets);
                    Minecraft.getInstance().setScreen(screen);
                    screen.openPacketAt(packet.packetID, packet.editLocation);
                }
            };
        }
    }
}