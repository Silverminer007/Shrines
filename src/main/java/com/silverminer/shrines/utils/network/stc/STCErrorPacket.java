/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.stc;

import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class STCErrorPacket implements IPacket {
    private final String errorTitle;
    private final String error;

    public STCErrorPacket(String errorTitle, String error) {
        this.errorTitle = errorTitle;
        this.error = error;
    }

    public static void encode(STCErrorPacket pkt, PacketBuffer buf) {
        buf.writeUtf(pkt.errorTitle);
        buf.writeUtf(pkt.error);
    }

    public static STCErrorPacket decode(PacketBuffer buf) {
        return new STCErrorPacket(buf.readUtf(), buf.readUtf());
    }

    public static void handle(STCErrorPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.handle(packet)));
        context.get().setPacketHandled(true);
    }

    public static class Handle {
        public static DistExecutor.SafeRunnable handle(STCErrorPacket packet) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    ClientUtils.showErrorToast(packet.errorTitle, packet.error);
                }
            };
        }
    }
}