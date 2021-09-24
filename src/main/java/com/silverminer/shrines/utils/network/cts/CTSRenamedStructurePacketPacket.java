package com.silverminer.shrines.utils.network.cts;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

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
			Handle.handle(packet);
		});
		context.get().setPacketHandled(true);
	}

	private static class Handle {
		public static DistExecutor.SafeRunnable handle(CTSRenamedStructurePacketPacket packet) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					StructureLoadUtils.saveStructures(false, packet.packetID, packet.packet);
					MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
					ArrayList<StructuresPacket> packets = Lists.newArrayList();
					packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
					ShrinesPacketHandler.sendTo(new STCOpenStructuresPacketEditPacket(packets), server.getPlayerList().getPlayer(packet.player));
				}
			};
		}
	}
}