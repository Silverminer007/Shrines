/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.utils.network;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;

import net.minecraft.network.PacketBuffer;

/**
 * Packet to send from server to client
 *
 * @author Silverminer
 */
public class SCustomStructuresPacket implements IPacket {
    protected static final Logger LOGGER = LogManager.getLogger(SCustomStructuresPacket.class);

    public final ArrayList<CustomStructureData> datas;

    public SCustomStructuresPacket(ArrayList<CustomStructureData> datas) {
        this.datas = datas;
    }

    public static void encode(SCustomStructuresPacket pkt, PacketBuffer buf) {
        buf.writeInt(pkt.datas.size());
        for (CustomStructureData csd : pkt.datas) {
            buf.writeNbt(CustomStructureData.write(csd));
        }
    }

    public static SCustomStructuresPacket decode(PacketBuffer buf) {
        ArrayList<CustomStructureData> datas = Lists.newArrayList();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            datas.add(CustomStructureData.read(buf.readNbt()));
        }
        return new SCustomStructuresPacket(datas);
    }
}