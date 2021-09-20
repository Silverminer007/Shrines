package com.silverminer.shrines.utils.network;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.silverminer.shrines.new_custom_structures.StructuresPacket;
import com.silverminer.shrines.new_custom_structures.StructuresPacket.Mode;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class CTSRenamedStructurePacketPacket implements IPacket {
	private StructuresPacket packet;
	private UUID player;
	private int packetID;
	public CTSRenamedStructurePacketPacket(StructuresPacket packet, UUID player, int IDtoDelete) {
		this.packet = packet;
		this.player = player;
		this.packetID = IDtoDelete;
	}

	public static void encode(CTSRenamedStructurePacketPacket pkt, PacketBuffer buf) {
		buf.writeNbt(StructuresPacket.toCompound(pkt.packet));
		buf.writeUUID(pkt.player);
		buf.writeInt(pkt.packetID);
	}

	public static CTSRenamedStructurePacketPacket decode(PacketBuffer buf) {
		return new CTSRenamedStructurePacketPacket(StructuresPacket.fromCompound(buf.readNbt(), null, Mode.NETWORK), buf.readUUID(), buf.readInt());
	}

	public static void handle(CTSRenamedStructurePacketPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			Utils.saveStructures(false, packet.packetID, packet.packet);
			MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
			ArrayList<StructuresPacket> packets = Lists.newArrayList();
			packets.addAll(Utils.STRUCTURE_PACKETS);
			ShrinesPacketHandler.sendTo(new STCFetchStructuresPacket(packets, true), server.getPlayerList().getPlayer(packet.player));
		});
		context.get().setPacketHandled(true);
	}
}