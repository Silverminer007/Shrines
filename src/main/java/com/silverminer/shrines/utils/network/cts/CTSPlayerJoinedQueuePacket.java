package com.silverminer.shrines.utils.network.cts;

import java.util.function.Supplier;

import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CTSPlayerJoinedQueuePacket implements IPacket {

	public CTSPlayerJoinedQueuePacket() {
	}

	public static void encode(CTSPlayerJoinedQueuePacket pkt, FriendlyByteBuf buf) {

	}

	public static CTSPlayerJoinedQueuePacket decode(FriendlyByteBuf buf) {
		return new CTSPlayerJoinedQueuePacket();
	}

	public static void handle(CTSPlayerJoinedQueuePacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
		context.get().setPacketHandled(true);
	}

	private static class Handle {
		public static DistExecutor.SafeRunnable handle(CTSPlayerJoinedQueuePacket packet, ServerPlayer sender) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.remove(sender.getUUID());
					StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.add(sender.getUUID());
					StructureLoadUtils.sendQueueUpdatesToPlayers();
				}
			};
		}
	}
}