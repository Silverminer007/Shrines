package com.silverminer.shrines.utils.network.cts;

import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.novels.NovelsDataRegistry;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCFetchNovelsAmountPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CTSFetchNovelAmountPacket implements IPacket {
    private final StructureData structure;

    public CTSFetchNovelAmountPacket(StructureData structure) {
        this.structure = structure;
    }

    public static void encode(CTSFetchNovelAmountPacket pkt, PacketBuffer buf) {
        buf.writeNbt(pkt.structure.write(new CompoundNBT()));
    }

    public static CTSFetchNovelAmountPacket decode(PacketBuffer buf) {
		CompoundNBT nbt = buf.readNbt();
		if(nbt == null){
			throw new RuntimeException("Failed to Fetch Structure Novel Amount. Provided Structure Data was null");
		}
        return new CTSFetchNovelAmountPacket(new StructureData(nbt));
    }

    public static void handle(CTSFetchNovelAmountPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    private static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSFetchNovelAmountPacket packet, ServerPlayerEntity sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    double amount = NovelsDataRegistry.getNovelAmount(packet.structure.getKey());
                    ShrinesPacketHandler.sendTo(
                            new STCFetchNovelsAmountPacket(packet.structure,
                                    amount),
                            sender);
                }
            };
        }
    }
}