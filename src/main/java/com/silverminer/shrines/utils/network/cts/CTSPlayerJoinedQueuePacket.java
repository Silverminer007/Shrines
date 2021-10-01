package com.silverminer.shrines.utils.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class CTSPlayerJoinedQueuePacket implements IPacket {
	protected static final Logger LOGGER = LogManager.getLogger(CTSPlayerJoinedQueuePacket.class);
	private final UUID player;

	public CTSPlayerJoinedQueuePacket(UUID player) {
		this.player = player;
	}

	public static void encode(CTSPlayerJoinedQueuePacket pkt, PacketBuffer buf) {
		buf.writeUUID(pkt.player);
	}

	public static CTSPlayerJoinedQueuePacket decode(PacketBuffer buf) {
		return new CTSPlayerJoinedQueuePacket(buf.readUUID());
	}

	public static void handle(CTSPlayerJoinedQueuePacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(Handle.handle(packet));
		context.get().setPacketHandled(true);
	}

	private static class Handle {
		public static DistExecutor.SafeRunnable handle(CTSPlayerJoinedQueuePacket packet) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					if (StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.contains(packet.player)) {
						StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.remove(packet.player);
					}
					StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.add(packet.player);
					StructureLoadUtils.sendQueueUpdatesToPlayers();
				}
			};
		}
	}
}