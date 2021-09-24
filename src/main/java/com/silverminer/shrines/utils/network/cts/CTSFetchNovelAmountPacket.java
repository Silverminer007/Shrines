package com.silverminer.shrines.utils.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.novels.NovelsDataRegistry;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCFetchNovelsAmountPacket;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class CTSFetchNovelAmountPacket implements IPacket {
	private final UUID player;
	private final StructureData structure;

	public CTSFetchNovelAmountPacket(PlayerEntity player, StructureData structure) {
		this(player.getUUID(), structure);
	}

	public CTSFetchNovelAmountPacket(UUID player, StructureData structure) {
		this.player = player;
		this.structure = structure;
	}

	public static void encode(CTSFetchNovelAmountPacket pkt, PacketBuffer buf) {
		buf.writeUUID(pkt.player);
		buf.writeNbt(pkt.structure.write(new CompoundNBT()));
	}

	public static CTSFetchNovelAmountPacket decode(PacketBuffer buf) {
		return new CTSFetchNovelAmountPacket(buf.readUUID(), new StructureData(buf.readNbt()));
	}

	public static void handle(CTSFetchNovelAmountPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			Handle.handle(packet);
		});
		context.get().setPacketHandled(true);
	}

	private static class Handle {
		public static DistExecutor.SafeRunnable handle(CTSFetchNovelAmountPacket packet) {
			return new DistExecutor.SafeRunnable() {

				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
					ShrinesPacketHandler.sendTo(
							new STCFetchNovelsAmountPacket(packet.structure,
									NovelsDataRegistry.getNovelAmount(packet.structure.getKey())),
							server.getPlayerList().getPlayer(packet.player));
				}
			};
		}
	}
}