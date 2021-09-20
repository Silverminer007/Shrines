package com.silverminer.shrines.utils.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.silverminer.shrines.new_custom_structures.StructuresPacket;
import com.silverminer.shrines.new_custom_structures.StructuresPacket.Mode;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
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
			Utils.STRUCTURE_PACKETS = ImmutableList.copyOf(packet.packets);

			Utils.saveStructures(false);

			MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
			ArrayList<StructuresPacket> packets = Lists.newArrayList();
			packets.addAll(Utils.STRUCTURE_PACKETS);
			ShrinesPacketHandler.sendTo(new STCFetchStructuresPacket(packets, true), server.getPlayerList().getPlayer(packet.player));
		});
		context.get().setPacketHandled(true);
	}
}