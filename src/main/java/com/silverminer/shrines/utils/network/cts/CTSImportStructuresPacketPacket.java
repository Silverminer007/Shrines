package com.silverminer.shrines.utils.network.cts;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.ZIPUtils;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCOpenStructuresPacketEditPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.Supplier;

public class CTSImportStructuresPacketPacket implements IPacket {
    private final String fileName;
    private final byte[] archive;

    public CTSImportStructuresPacketPacket(String fileName, byte[] archive) {
        this.fileName = fileName;
        this.archive = archive;
    }

    public static void encode(CTSImportStructuresPacketPacket pkt, PacketBuffer buf) {
        buf.writeUtf(pkt.fileName);
        buf.writeByteArray(pkt.archive);
    }

    public static CTSImportStructuresPacketPacket decode(PacketBuffer buf) {
        return new CTSImportStructuresPacketPacket(buf.readUtf(), buf.readByteArray());
    }

    public static void handle(CTSImportStructuresPacketPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(Handle.handle(packet, context.get().getSender()));
        context.get().setPacketHandled(true);
    }

    public static class Handle {
        public static DistExecutor.SafeRunnable handle(CTSImportStructuresPacketPacket packet, ServerPlayerEntity sender) {
            return new DistExecutor.SafeRunnable() {

                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    StructureLoadUtils.saveStructures();
                    File saveDestination = new File(StructureLoadUtils.getImportCacheLocation(), packet.fileName + ".zip");
                    if(saveDestination.exists()){
                        if(!saveDestination.delete()){
                            LOGGER.error("Failed to clear cache before structure packet was imported");
                            return;
                        }
                    }
                    try {
                        if (!StructureLoadUtils.getImportCacheLocation().exists() && !StructureLoadUtils.getImportCacheLocation().mkdirs()) {
                            LOGGER.error("Failed to create Directory to import structures packet");
                            return;
                        }
                        saveDestination = Files.write(saveDestination.toPath(), packet.archive).toFile();
                        if (ZIPUtils.extractArchive(saveDestination, StructureLoadUtils.getImportCacheLocation())) {
                            Files.find(StructureLoadUtils.getImportCacheLocation().toPath(), 1, ((path, basicFileAttributes) -> Files.isDirectory(path))).forEach(path -> {
                                StructuresPacket structuresPacket = StructureLoadUtils.loadStructuresPacket(path);
                                if(structuresPacket != null) {
                                    LOGGER.info("Structures Packet Import. DisplayName: {}, SaveName {}", structuresPacket.getDisplayName(), structuresPacket.getSaveName());
                                    // TODO Validate Packet
                                    File packetDest = new File(StructureLoadUtils.getPacketsSaveLocation(), StructureLoadUtils.getSavePath(structuresPacket.getDisplayName()));
                                    try {
                                        LOGGER.info("Imported bew structures packet and saved it to {}, read zip from {}", packetDest, path);
                                        Files.move(path, packetDest.toPath());
                                        return;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            LOGGER.error("Failed to decompress archive");
                            // TODO Show error message in the GUI
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // TODO Show error message in the GUI
                    }
                    // TODO Fix Structure Packet Rename
                    StructureLoadUtils.loadStructures();
                    ArrayList<StructuresPacket> packets = Lists.newArrayList();
                    packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
                    ShrinesPacketHandler.sendTo(new STCOpenStructuresPacketEditPacket(packets),
                            sender);
                }
            };
        }
    }
}