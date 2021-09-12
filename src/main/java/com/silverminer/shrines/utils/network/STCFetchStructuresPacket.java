package com.silverminer.shrines.utils.network;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.silverminer.shrines.client.gui.StructureNovelsScreen;
import com.silverminer.shrines.new_custom_structures.StructuresPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class STCFetchStructuresPacket implements IPacket {
	private final List<StructuresPacket> packets;

	public STCFetchStructuresPacket(List<StructuresPacket> structurePackets) {
		this.packets = structurePackets;
	}

	public static void encode(STCFetchStructuresPacket pkt, PacketBuffer buf) {
		List<StructuresPacket> structurePackets = pkt.packets;
		buf.writeInt(structurePackets.size());
		for (StructuresPacket packet : structurePackets) {
			buf.writeNbt(StructuresPacket.toCompound(packet));
		}
	}

	public static STCFetchStructuresPacket decode(PacketBuffer buf) {
		List<StructuresPacket> structurePackets = Lists.newArrayList();
		int packets = buf.readInt();
		for (int i = 0; i < packets; i++) {
			structurePackets.add(StructuresPacket.fromCompound(buf.readNbt(), null));
		}
		return new STCFetchStructuresPacket(structurePackets);
	}

	public static void handle(STCFetchStructuresPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
					() -> () -> Minecraft.getInstance().setScreen(new StructureNovelsScreen(null, packet.packets)));
		});
		context.get().setPacketHandled(true);
	}
}