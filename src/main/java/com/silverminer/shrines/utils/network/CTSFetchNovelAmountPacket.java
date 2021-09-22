package com.silverminer.shrines.utils.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.novels.NovelsDataRegistry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
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
			MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
			ShrinesPacketHandler.sendTo(
					new STCFetchNovelsAmountPacket(packet.structure, NovelsDataRegistry.getNovelAmount(packet.structure.getKey())),
					server.getPlayerList().getPlayer(packet.player));
		});
		context.get().setPacketHandled(true);
	}
}