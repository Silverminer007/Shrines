/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.stc;

import java.util.function.Supplier;

import com.silverminer.shrines.gui.novels.StructureNovelScreen;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.utils.network.IPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class STCFetchNovelsAmountPacket implements IPacket {
	private final double amount;
	private final StructureData structure;

	public STCFetchNovelsAmountPacket(StructureData structure, double amount) {
		this.amount = amount;
		this.structure = structure;
	}

	public static void encode(STCFetchNovelsAmountPacket pkt, PacketBuffer buf) {
		buf.writeNbt(pkt.structure.write(new CompoundNBT()));
		buf.writeDouble(pkt.amount);
	}

	public static STCFetchNovelsAmountPacket decode(PacketBuffer buf) {
		return new STCFetchNovelsAmountPacket(new StructureData(buf.readNbt()), buf.readDouble());
	}

	public static void handle(STCFetchNovelsAmountPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.handle(packet));
		});
		context.get().setPacketHandled(true);
	}

	private static class Handle {
		public static DistExecutor.SafeRunnable handle(STCFetchNovelsAmountPacket packet) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					StructureNovelScreen screen = new StructureNovelScreen(packet.structure, packet.amount);
					Minecraft.getInstance().setScreen(screen);
				}
			};
		}
	}
}