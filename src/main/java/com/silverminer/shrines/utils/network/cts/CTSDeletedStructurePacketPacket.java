/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.cts;

import java.util.ArrayList;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCOpenStructuresPacketEditPacket;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class CTSDeletedStructurePacketPacket implements IPacket {
	private final String packet;

	public CTSDeletedStructurePacketPacket(String packet) {
		this.packet = packet;
	}

	public static void encode(CTSDeletedStructurePacketPacket pkt, PacketBuffer buf) {
		buf.writeUtf(pkt.packet);
	}

	public static CTSDeletedStructurePacketPacket decode(PacketBuffer buf) {
		return new CTSDeletedStructurePacketPacket(buf.readUtf());
	}

	public static void handle(CTSDeletedStructurePacketPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
		context.get().setPacketHandled(true);
	}

	private static class Handle {
		public static DistExecutor.SafeRunnable handle(CTSDeletedStructurePacketPacket packet, ServerPlayerEntity sender) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					StructureLoadUtils.deleteStructuresPacket(packet.packet);
					ArrayList<StructuresPacket> packets = Lists.newArrayList();
					packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
					ShrinesPacketHandler.sendTo(new STCOpenStructuresPacketEditPacket(packets),
							sender);
				}
			};
		}
	}
}