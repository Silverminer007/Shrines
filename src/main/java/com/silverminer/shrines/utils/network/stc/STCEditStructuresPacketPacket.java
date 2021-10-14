package com.silverminer.shrines.utils.network.stc;

import com.google.common.collect.Lists;
import com.silverminer.shrines.gui.novels.StructureNovelsScreen;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class STCEditStructuresPacketPacket implements IPacket {
    private final ArrayList<StructuresPacket> packets;
    private final int packetID;
    /**
     * This is a flag to choose the correct edit tab\n
     * 0 -> Structures \n
     * 1 -> Templates\n
     * 2 -> Pools\n
     */
    private final int editLocation;

    public STCEditStructuresPacketPacket(ArrayList<StructuresPacket> structurePackets, int packetID, int editLocation) {
        this.packets = structurePackets;
        this.packetID = packetID;
        this.editLocation = editLocation;
    }

    public static void encode(STCEditStructuresPacketPacket pkt, PacketBuffer buf) {
        ArrayList<StructuresPacket> structurePackets = pkt.packets;
        buf.writeInt(structurePackets.size());
        for (StructuresPacket packet : structurePackets) {
            buf.writeNbt(StructuresPacket.toCompound(packet));
        }
        buf.writeInt(pkt.packetID);
        buf.writeInt(pkt.editLocation);
    }

    public static STCEditStructuresPacketPacket decode(PacketBuffer buf) {
        ArrayList<StructuresPacket> structurePackets = Lists.newArrayList();
        int packets = buf.readInt();
        for (int i = 0; i < packets; i++) {
            structurePackets.add(StructuresPacket.fromCompound(buf.readNbt(), null, true));
        }
        return new STCEditStructuresPacketPacket(structurePackets, buf.readInt(), buf.readInt());
    }

    public static void handle(STCEditStructuresPacketPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.handle(packet)));
        context.get().setPacketHandled(true);
    }

    public static class Handle {
        public static DistExecutor.SafeRunnable handle(STCEditStructuresPacketPacket packet) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureNovelsScreen screen = new StructureNovelsScreen(null, packet.packets);
                    Minecraft.getInstance().setScreen(screen);
                    screen.refreshList();
                    screen.openPacketEdit(packet.packetID, packet.editLocation);
                }
            };
        }
    }
}