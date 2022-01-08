/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.stc;

import com.google.common.collect.Lists;
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

public class STCOpenStructuresPacketEditPacket implements IPacket {
	private final ArrayList<StructuresPacket> packets;

	public STCOpenStructuresPacketEditPacket(ArrayList<StructuresPacket> structurePackets) {
		this.packets = structurePackets;
	}

	public static void encode(STCOpenStructuresPacketEditPacket pkt, PacketBuffer buf) {
		ArrayList<StructuresPacket> structurePackets = pkt.packets;
		buf.writeInt(structurePackets.size());
		for (StructuresPacket packet : structurePackets) {
			buf.writeNbt(StructuresPacket.saveToNetwork(packet));
		}
	}

	public static STCOpenStructuresPacketEditPacket decode(PacketBuffer buf) {
		ArrayList<StructuresPacket> structurePackets = Lists.newArrayList();
		int packets = buf.readInt();
		for (int i = 0; i < packets; i++) {
			structurePackets.add(StructuresPacket.read(buf.readNbt(), null));
		}
		return new STCOpenStructuresPacketEditPacket(structurePackets);
	}

	public static void handle(STCOpenStructuresPacketEditPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.handle(packet)));
		context.get().setPacketHandled(true);
	}

	public static class Handle {
		public static DistExecutor.SafeRunnable handle(STCOpenStructuresPacketEditPacket packet) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					Minecraft.getInstance().setScreen(new StructuresPacketsScreen(null, packet.packets));
				}
			};
		}
	}
}