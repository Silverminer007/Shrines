package com.silverminer.shrines.utils.network.cts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.structures.load.StructuresPacket.Mode;
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

public class CTSUpdateStructuresPacketsPacket implements IPacket {
	private final List<StructuresPacket> packets;
	private final UUID player;

	public CTSUpdateStructuresPacketsPacket(List<StructuresPacket> structurePackets, UUID player) {
		this.packets = structurePackets;
		this.player = player;
	}

	public static void encode(CTSUpdateStructuresPacketsPacket pkt, PacketBuffer buf) {
		List<StructuresPacket> structurePackets = pkt.packets;
		buf.writeInt(structurePackets.size());
		for (StructuresPacket packet : structurePackets) {
			buf.writeNbt(StructuresPacket.toCompound(packet));
		}
		buf.writeUUID(pkt.player);
	}

	public static CTSUpdateStructuresPacketsPacket decode(PacketBuffer buf) {
		List<StructuresPacket> structurePackets = Lists.newArrayList();
		int packets = buf.readInt();
		for (int i = 0; i < packets; i++) {
			structurePackets.add(StructuresPacket.fromCompound(buf.readNbt(), null, Mode.NETWORK));
		}
		return new CTSUpdateStructuresPacketsPacket(structurePackets, buf.readUUID());
	}

	public static void handle(CTSUpdateStructuresPacketsPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			Handle.handle(packet);
		});
		context.get().setPacketHandled(true);
	}

	private static class Handle {
		public static DistExecutor.SafeRunnable handle(CTSUpdateStructuresPacketsPacket packet) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					StructureLoadUtils.STRUCTURE_PACKETS = ImmutableList.copyOf(packet.packets);

					StructureLoadUtils.saveStructures(false);

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