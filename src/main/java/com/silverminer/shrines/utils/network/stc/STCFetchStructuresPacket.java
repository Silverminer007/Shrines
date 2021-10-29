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
			buf.writeNbt(StructuresPacket.saveToNetwork(packet));
		}
		buf.writeBoolean(pkt.op_mode);
	}

	public static STCFetchStructuresPacket decode(PacketBuffer buf) {
		ArrayList<StructuresPacket> structurePackets = Lists.newArrayList();
		int packets = buf.readInt();
		for (int i = 0; i < packets; i++) {
			structurePackets.add(StructuresPacket.read(buf.readNbt(), null));
		}
		return new STCFetchStructuresPacket(structurePackets, buf.readBoolean());
	}

	public static void handle(STCFetchStructuresPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.handle(packet));
		});
		context.get().setPacketHandled(true);
	}

	private static class Handle {
		public static DistExecutor.SafeRunnable handle(STCFetchStructuresPacket packet) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					StructureNovelsScreen screen = new StructureNovelsScreen(null, packet.packets);
					Minecraft.getInstance().setScreen(screen);
					screen.refreshList();
					if (packet.op_mode) {
						screen.addPlayerToQueue();
					}
				}
			};
		}
	}
}