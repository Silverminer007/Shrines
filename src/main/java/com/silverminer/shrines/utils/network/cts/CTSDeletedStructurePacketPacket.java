package com.silverminer.shrines.utils.network.cts;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCOpenStructuresPacketEditPacket;

import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class CTSDeletedStructurePacketPacket implements IPacket {
	private int packet;
	private UUID player;

	public CTSDeletedStructurePacketPacket(int packet, UUID player) {
		this.packet = packet;
		this.player = player;
	}

	public static void encode(CTSDeletedStructurePacketPacket pkt, PacketBuffer buf) {
		buf.writeInt(pkt.packet);
		buf.writeUUID(pkt.player);
	}

	public static CTSDeletedStructurePacketPacket decode(PacketBuffer buf) {
		return new CTSDeletedStructurePacketPacket(buf.readInt(), buf.readUUID());
	}

	public static void handle(CTSDeletedStructurePacketPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			Handle.handle(packet);
		});
		context.get().setPacketHandled(true);
	}

	private static class Handle {
		public static DistExecutor.SafeRunnable handle(CTSDeletedStructurePacketPacket packet) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					StructureLoadUtils.saveStructures(false, packet.packet);
					MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
					ArrayList<StructuresPacket> packets = Lists.newArrayList();
					packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
					ShrinesPacketHandler.sendTo(new STCOpenStructuresPacketEditPacket(packets),
							server.getPlayerList().getPlayer(packet.player));
				}
			};
		}
	}
}