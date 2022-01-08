/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.cts;

import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class CTSPlayerLeftQueuePacket implements IPacket {
    protected static final Logger LOGGER = LogManager.getLogger(CTSPlayerLeftQueuePacket.class);

    public CTSPlayerLeftQueuePacket() {
    }

    public static void encode(CTSPlayerLeftQueuePacket pkt, PacketBuffer buf) {

    }

    public static CTSPlayerLeftQueuePacket decode(PacketBuffer buf) {
        return new CTSPlayerLeftQueuePacket();
    }

    public static void handle(CTSPlayerLeftQueuePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    private static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSPlayerLeftQueuePacket packet, ServerPlayerEntity sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.playerLeftQueue(sender.getUUID());
                }
            };
        }
    }
}