package com.silverminer.shrines.utils.network;

import java.util.function.Supplier;

import com.silverminer.shrines.client.gui.novels.StructureNovelScreen;
import com.silverminer.shrines.new_custom_structures.StructureData;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class STCFetchNovelsAmountPacket implements IPacket {
	private final double amount;
	private final StructureData structure;

	public STCFetchNovelsAmountPacket(StructureData structure, double amount) {
		this.amount = amount;
		this.structure = structure;
	}

	public static void encode(STCFetchNovelsAmountPacket pkt, PacketBuffer buf) {
		buf.writeNbt(pkt.structure.write(new CompoundNBT()));
		buf.writeDouble(pkt.amount);
	}

	public static STCFetchNovelsAmountPacket decode(PacketBuffer buf) {
		return new STCFetchNovelsAmountPacket(new StructureData(buf.readNbt()), buf.readDouble());
	}

	public static void handle(STCFetchNovelsAmountPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				StructureNovelScreen screen = new StructureNovelScreen(packet.structure, packet.amount);
				Minecraft.getInstance().setScreen(screen);
			});
		});
		context.get().setPacketHandled(true);
	}
}