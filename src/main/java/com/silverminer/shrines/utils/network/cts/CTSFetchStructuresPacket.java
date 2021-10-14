package com.silverminer.shrines.utils.network.cts;

import java.util.ArrayList;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCFetchStructuresPacket;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class CTSFetchStructuresPacket implements IPacket {
	private final boolean op_mode;

	public CTSFetchStructuresPacket(boolean op_mode) {
		this.op_mode = op_mode;
	}

	public static void encode(CTSFetchStructuresPacket pkt, PacketBuffer buf) {
		buf.writeBoolean(pkt.op_mode);
	}

	public static CTSFetchStructuresPacket decode(PacketBuffer buf) {
		return new CTSFetchStructuresPacket(buf.readBoolean());
	}

	public static void handle(CTSFetchStructuresPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
		context.get().setPacketHandled(true);
	}

	private static class Handle {
		public static DistExecutor.SafeRunnable handle(CTSFetchStructuresPacket packet, ServerPlayerEntity sender) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					ArrayList<StructuresPacket> packets = Lists.newArrayList();
					packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
					ShrinesPacketHandler.sendTo(new STCFetchStructuresPacket(packets, packet.op_mode),
							sender);
				}
			};
		}
	}
}