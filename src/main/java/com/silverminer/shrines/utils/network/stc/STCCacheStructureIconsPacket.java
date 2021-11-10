package com.silverminer.shrines.utils.network.stc;

import com.google.common.collect.Maps;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class STCCacheStructureIconsPacket implements IPacket {
    private final HashMap<ResourceLocation, byte[]> icons;

    public STCCacheStructureIconsPacket(HashMap<ResourceLocation, byte[]> icons) {
        this.icons = icons;
    }

    public static void encode(STCCacheStructureIconsPacket pkt, PacketBuffer buf) {
        buf.writeInt(pkt.icons.size());
        for(Map.Entry<ResourceLocation, byte[]> icon : pkt.icons.entrySet()){
            buf.writeUtf(icon.getKey().toString());
            buf.writeByteArray(icon.getValue());
        }
    }

    public static STCCacheStructureIconsPacket decode(PacketBuffer buf) {
        HashMap<ResourceLocation, byte[]> icons = Maps.newHashMap();
        int count = buf.readInt();
        for(int i = 0; i < count; i++){
            ResourceLocation loc = new ResourceLocation(buf.readUtf());
            byte[] file = buf.readByteArray();
            icons.put(loc, file);
        }
        return new STCCacheStructureIconsPacket(icons);
    }

    public static void handle(STCCacheStructureIconsPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handle.handle(packet)));
        context.get().setPacketHandled(true);
    }

    public static class Handle {
        public static DistExecutor.SafeRunnable handle(STCCacheStructureIconsPacket packet) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.cacheStructureIcons(packet.icons);
                }
            };
        }
    }
}