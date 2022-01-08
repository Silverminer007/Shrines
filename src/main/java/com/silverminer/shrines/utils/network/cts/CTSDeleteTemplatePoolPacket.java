/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

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

public class CTSDeleteTemplatePoolPacket implements IPacket {
    private final ResourceLocation pool;
    private final String packetID;

    public CTSDeleteTemplatePoolPacket(ResourceLocation template, String packetID) {
        this.pool = template;
        this.packetID = packetID;
    }

    public static void encode(CTSDeleteTemplatePoolPacket pkt, PacketBuffer buf) {
        buf.writeUtf(pkt.pool.toString());
        buf.writeUtf(pkt.packetID);
    }

    public static CTSDeleteTemplatePoolPacket decode(PacketBuffer buf) {
        return new CTSDeleteTemplatePoolPacket(new ResourceLocation(buf.readUtf()), buf.readUtf());
    }

    public static void handle(CTSDeleteTemplatePoolPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    private static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSDeleteTemplatePoolPacket packet, ServerPlayerEntity sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.deleteTemplatePool(packet.pool, packet.packetID);
                    ArrayList<StructuresPacket> packets = Lists.newArrayList();
                    packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
                    ShrinesPacketHandler.sendTo(new STCEditStructuresPacketPacket(packets, packet.packetID, 2),
                            sender);
                }
            };
        }
    }
}