package com.silverminer.shrines.utils.network.stc;

import java.util.ArrayList;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.silverminer.shrines.gui.novels.StructureNovelsScreen;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.network.IPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class STCOpenStructuresPacketEditPacket implements IPacket {
	private final ArrayList<StructuresPacket> packets;

	public STCOpenStructuresPacketEditPacket(ArrayList<StructuresPacket> structurePackets) {
		this.packets = structurePackets;
	}

	public static void encode(STCOpenStructuresPacketEditPacket pkt, PacketBuffer buf) {
		ArrayList<StructuresPacket> structurePackets = pkt.packets;
		buf.writeInt(structurePackets.size());
		for (StructuresPacket packet : structurePackets) {
			buf.writeNbt(StructuresPacket.toCompound(packet));
		}
	}

	public static STCOpenStructuresPacketEditPacket decode(PacketBuffer buf) {
		ArrayList<StructuresPacket> structurePackets = Lists.newArrayList();
		int packets = buf.readInt();
		for (int i = 0; i < packets; i++) {
			structurePackets.add(StructuresPacket.fromCompound(buf.readNbt(), null, true));
		}
		return new STCOpenStructuresPacketEditPacket(structurePackets);
	}

	public static void handle(STCOpenStructuresPacketEditPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.handle(packet));
		});
		context.get().setPacketHandled(true);
	}

	public static class Handle {
		public static DistExecutor.SafeRunnable handle(STCOpenStructuresPacketEditPacket packet) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					StructureNovelsScreen screen = new StructureNovelsScreen(null, packet.packets);
					Minecraft.getInstance().setScreen(screen);
					screen.refreshList();
					screen.openOpMode();
				}
			};
		}
	}
}