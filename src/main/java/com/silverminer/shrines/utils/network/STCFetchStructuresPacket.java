package com.silverminer.shrines.utils.network;

import java.util.ArrayList;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.silverminer.shrines.client.gui.novels.StructureNovelsScreen;
import com.silverminer.shrines.new_custom_structures.StructuresPacket;
import com.silverminer.shrines.new_custom_structures.StructuresPacket.Mode;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class STCFetchStructuresPacket implements IPacket {
	private final ArrayList<StructuresPacket> packets;
	private final boolean op_mode;

	public STCFetchStructuresPacket(ArrayList<StructuresPacket> structurePackets, boolean op_mode) {
		this.packets = structurePackets;
		this.op_mode = op_mode;
	}

	public static void encode(STCFetchStructuresPacket pkt, PacketBuffer buf) {
		ArrayList<StructuresPacket> structurePackets = pkt.packets;
		buf.writeInt(structurePackets.size());
		for (StructuresPacket packet : structurePackets) {
			buf.writeNbt(StructuresPacket.toCompound(packet));
		}
		buf.writeBoolean(pkt.op_mode);
	}

	public static STCFetchStructuresPacket decode(PacketBuffer buf) {
		ArrayList<StructuresPacket> structurePackets = Lists.newArrayList();
		int packets = buf.readInt();
		for (int i = 0; i < packets; i++) {
			structurePackets.add(StructuresPacket.fromCompound(buf.readNbt(), null, Mode.NETWORK));
		}
		return new STCFetchStructuresPacket(structurePackets, buf.readBoolean());
	}

	public static void handle(STCFetchStructuresPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				StructureNovelsScreen screen = new StructureNovelsScreen(null, packet.packets);
				Minecraft.getInstance().setScreen(screen);
				screen.refreshList();
				if (packet.op_mode) {
					screen.openOpMode();
				}
			});
		});
		context.get().setPacketHandled(true);
	}
}