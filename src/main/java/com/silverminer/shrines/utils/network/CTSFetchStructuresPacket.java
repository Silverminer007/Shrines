package com.silverminer.shrines.utils.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class CTSFetchStructuresPacket implements IPacket {
	private final UUID player;

	public CTSFetchStructuresPacket(PlayerEntity player) {
		this(player.getUUID());
	}

	public CTSFetchStructuresPacket(UUID player) {
		this.player = player;
	}

	public static void encode(CTSFetchStructuresPacket pkt, PacketBuffer buf) {
		buf.writeUUID(pkt.player);
	}

	public static CTSFetchStructuresPacket decode(PacketBuffer buf) {
		return new CTSFetchStructuresPacket(buf.readUUID());
	}

	public static void handle(CTSFetchStructuresPacket packet, Supplier<NetworkEvent.Context> context) {
		ShrinesMod.LOGGER.info("Handling client to server packet");
		context.get().enqueueWork(() -> {
			MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
			ShrinesPacketHandler.sendTo(new STCFetchStructuresPacket(Utils.STRUCTURE_PACKETS), server.getPlayerList().getPlayer(packet.player));
		});
		context.get().setPacketHandled(true);
	}
}