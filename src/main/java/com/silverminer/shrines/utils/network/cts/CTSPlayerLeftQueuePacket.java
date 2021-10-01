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

public class CTSPlayerLeftQueuePacket implements IPacket {
	protected static final Logger LOGGER = LogManager.getLogger(CTSPlayerLeftQueuePacket.class);
	private final UUID player;

	public CTSPlayerLeftQueuePacket(UUID player) {
		this.player = player;
	}

	public static void encode(CTSPlayerLeftQueuePacket pkt, PacketBuffer buf) {
		buf.writeUUID(pkt.player);
	}

	public static CTSPlayerLeftQueuePacket decode(PacketBuffer buf) {
		return new CTSPlayerLeftQueuePacket(buf.readUUID());
	}

	public static void handle(CTSPlayerLeftQueuePacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(Handle.handle(packet));
		context.get().setPacketHandled(true);
	}

	private static class Handle {
		public static DistExecutor.SafeRunnable handle(CTSPlayerLeftQueuePacket packet) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					StructureLoadUtils.playerLeftQueue(packet.player);
				}
			};
		}
	}
}