package com.silverminer.shrines.utils.network.cts;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCFetchStructuresPacket;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class CTSFetchStructuresPacket implements IPacket {
	private final UUID player;
	private final boolean op_mode;

	public CTSFetchStructuresPacket(PlayerEntity player, boolean op_mode) {
		this(player.getUUID(), op_mode);
	}

	public CTSFetchStructuresPacket(UUID player, boolean op_mode) {
		this.player = player;
		this.op_mode = op_mode;
	}

	public static void encode(CTSFetchStructuresPacket pkt, PacketBuffer buf) {
		buf.writeUUID(pkt.player);
		buf.writeBoolean(pkt.op_mode);
	}

	public static CTSFetchStructuresPacket decode(PacketBuffer buf) {
		return new CTSFetchStructuresPacket(buf.readUUID(), buf.readBoolean());
	}

	public static void handle(CTSFetchStructuresPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(Handle.handle(packet));
		context.get().setPacketHandled(true);
	}

	private static class Handle {
		public static DistExecutor.SafeRunnable handle(CTSFetchStructuresPacket packet) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
					ArrayList<StructuresPacket> packets = Lists.newArrayList();
					packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
					ShrinesPacketHandler.sendTo(new STCFetchStructuresPacket(packets, packet.op_mode),
							server.getPlayerList().getPlayer(packet.player));
				}
			};
		}
	}
}