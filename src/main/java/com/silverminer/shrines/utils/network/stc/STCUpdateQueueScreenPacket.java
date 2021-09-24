package com.silverminer.shrines.utils.network.stc;

import java.util.function.Supplier;

import com.silverminer.shrines.gui.packets.WaitInQueueScreen;
import com.silverminer.shrines.utils.network.IPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class STCUpdateQueueScreenPacket implements IPacket {
	private final int position;

	public STCUpdateQueueScreenPacket(int position) {
		this.position = position;
	}

	public static void encode(STCUpdateQueueScreenPacket pkt, PacketBuffer buf) {
		buf.writeInt(pkt.position);
	}

	public static STCUpdateQueueScreenPacket decode(PacketBuffer buf) {
		return new STCUpdateQueueScreenPacket(buf.readInt());
	}

	public static void handle(STCUpdateQueueScreenPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.handle(packet));
		});
		context.get().setPacketHandled(true);
	}

	public static class Handle {
		public static DistExecutor.SafeRunnable handle(STCUpdateQueueScreenPacket packet) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					WaitInQueueScreen screen = new WaitInQueueScreen(packet.position);
					Minecraft.getInstance().setScreen(screen);
				}
			};
		}
	}
}