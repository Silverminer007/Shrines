/*
 * Silverminer (and Team)
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * <p>
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.config.DefaultStructureConfig;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCFetchStructuresPacket;
import com.silverminer.shrines.utils.network.stc.STCOpenStructuresPacketEditPacket;
import com.silverminer.shrines.utils.network.stc.STCUpdateQueueScreenPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.resources.FolderPackFinder;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FileUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class StructureLoadUtils {
    public static final int PACKET_VERSION = 1;
    protected static final Logger LOGGER = LogManager.getLogger(StructureLoadUtils.class);
    public static ImmutableList<StructuresPacket> STRUCTURE_PACKETS;
    /**
     * First player in queue is already allowed to edit the structure packets. All
     * players behind need to wait until the first player is done. This is needed to
     * prevent Overrides of changes one player made while another one is in
     * configuration screen and is going to set the changes later
     */
    public static ArrayList<UUID> PLAYERS_IN_EDIT_QUEUE = Lists.newArrayList();

    public static File getSaveLocation() {
        return FileUtils.getFile(ShrinesMod.getMinecraftDirectory(), "shrines-saves");
    }

    /**
     * Read here the structure of the structure packets from disk. Structure is as
     * follows:
     * <p>
     * .minecraft/shrines-saves/StructurePackets |-> <Name of Packet> |-> | ->
     * Structures.nbt // This file contains any necessary information about the
     * Packet. This file allows the mod to detect the LOGGER.error("Couldn't load
     * structure from {}", path, ioexception);directory as packet. |-> | -> | ->
     * List of all structures // Based on this list, the structure are initialized
     * |-> | -> | -> | -> <List of all options of this structure with values and
     * comments> |-> | -> <Folder for each structure in the structures list above>
     * |-> | -> | -> <Subfolder for further data like loot tables or stuff>
     * <p>
     * .minecraft/shrines-saves/StructureDataPackets // First a placeholder
     * for more data about the structures that should be loaded as datapack, so they
     * could be reloaded during runtime. Here is the place for .nbt files and
     * template pools and loot tables
     */
    public static void loadStructures(boolean initialLoad) {
        try {
            ArrayList<StructuresPacket> structure_packets = Lists.newArrayList();

            File shrines_saves = new File(ShrinesMod.getMinecraftDirectory(), "shrines-saves").getCanonicalFile();
            if (!shrines_saves.exists()) {
                if (!shrines_saves.mkdirs()) {
                    LOGGER.error("Failed to Load shrines structure, because directory creation failed");
                }
            }

            // Get the list of all directories in shrines saves folder as possible structure
            // packets
            File[] af = shrines_saves.listFiles();
            if (af != null) {
                List<File> files_in_shrines_saves = Arrays.stream(af)
                        .filter(File::isDirectory).collect(Collectors.toList());
                StructuresPacket.resetIDCaller();
                for (File p_packet : files_in_shrines_saves) {
                    File structures_file = new File(p_packet, "structures.nbt");
                    if (!structures_file.exists()) {
                        continue;
                    }
                    // Read the file here in CompoundNBT tag
                    CompoundNBT data_structure = readNBTFile(structures_file);
                    // Create an instance of a packet datastructures and write it to a read/write
                    // array
                    ArrayList<ResourceLocation> templates = StructureLoadUtils.searchForTemplates(p_packet);
                    StructuresPacket packet = StructuresPacket.fromCompound(data_structure, p_packet, false);
                    if (packet == null) {
                        continue;
                    }
                    packet.setTemplates(templates);
                    structure_packets.add(packet);
                }
            }

            // Check if the included structures are already loaded or need to be defaulted
            boolean has_included_structures = false;
            for (StructuresPacket packet : structure_packets) {
                if (packet.isIncluded()) {
                    has_included_structures = true;
                    break;
                }
            }
            if (!has_included_structures && initialLoad) {
                structure_packets.add(StructureLoadUtils.getIncludedStructures());
            }
            // Save Structure Packets for later use in immutable list
            STRUCTURE_PACKETS = ImmutableList.copyOf(structure_packets);
            StructureLoadUtils.saveStructures(true);

            HashMap<String, StructuresPacket> temp = Maps.newHashMap();
            ArrayList<StructuresPacket> packets_with_issue = Lists.newArrayList();
            // Check for duplicated structure names and warn/stop loading the structure and
            // open GUI after start
            for (StructuresPacket packet : StructureLoadUtils.STRUCTURE_PACKETS) {
                String packet_name = packet.getDisplayName();
                for (StructureData data : packet.getStructures()) {
                    String key = data.getKey();
                    if (temp.containsKey(key)) {
                        LOGGER.error(
                                "A conflict was detected when loading structures. There are two or more structures with the same name. Key [{}], Packet 1 [{}], Packet 2 [{}]",
                                key, packet_name, temp.get(key).getDisplayName());
                        data.successful = false;
                        packets_with_issue.add(packet);
                    } else {
                        data.successful = true;
                    }
                    temp.put(key, packet);
                }
            }
            for (StructuresPacket packet : StructureLoadUtils.STRUCTURE_PACKETS) {
                packet.hasIssues = packets_with_issue.contains(packet);
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<ResourceLocation> searchForTemplates(File p_packet) {
        p_packet = new File(p_packet, "data");
        if (!p_packet.isDirectory()) {
            return Lists.newArrayList();
        }
        ArrayList<ResourceLocation> templates = Lists.newArrayList();
        File[] namespaces = p_packet.listFiles();
        if (namespaces == null) {
            return templates;
        }
        for (File namespace : namespaces) {
            File templates_path = new File(namespace, "structures");
            ArrayList<File> possibleTemplates = StructureLoadUtils.scanFilesInSubDirs(templates_path, Lists.newArrayList());
            for (File possibleTemplate : possibleTemplates) {
                if (possibleTemplate.getName().endsWith(".nbt")) {
                    String path = templates_path.toPath().relativize(possibleTemplate.toPath()).toString();
                    ResourceLocation template = new ResourceLocation(namespace.getName(), path.replace(".nbt", ""));
                    templates.add(template);
                }
            }
        }
        return templates;
    }

    private static ArrayList<File> scanFilesInSubDirs(File base, ArrayList<File> files) {
        File[] possibleFiles = base.listFiles();
        if (possibleFiles == null) {
            return files;
        }
        for (File file : possibleFiles) {
            if (file.isDirectory()) {
                files = StructureLoadUtils.scanFilesInSubDirs(file, files);
            } else {
                files.add(file);
            }
        }
        return files;
    }

    protected static String getSavePath(String name) {
        String resultName = name.trim();
        if (resultName.isEmpty()) {
            resultName = "Structures Packet";
        }
        try {
            resultName = FileUtil.findAvailableName(getSaveLocation().toPath(), resultName, "");
        } catch (Exception exception1) {
            resultName = "Structures Packet";

            try {
                resultName = FileUtil.findAvailableName(getSaveLocation().toPath(), resultName, "");
            } catch (Exception exception) {
                throw new RuntimeException("Failed to find a suitable Structure packet Name for save", exception);
            }
        }
        return resultName;
    }

    private static StructuresPacket getIncludedStructures() {
        ArrayList<StructureData> structures = Lists.newArrayList();
        structures.add(new StructureData(DefaultStructureConfig.ABANDONEDWITCHHOUSE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.BALLON_CONFIG));// false
        structures.add(new StructureData(DefaultStructureConfig.BEES_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.ENDTEMPLE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.FLOODEDTEMPLE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.GUARDIANMEETING_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.HARBOUR_CONFIG));// false
        structures.add(new StructureData(DefaultStructureConfig.HIGHTEMPLE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.INFESTEDPRISON_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.JUNGLETOWER_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.MINERALTEMPLE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.NETHERPYRAMID_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.NETHERSHRINE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.ORIENTALSANCTUARY_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.PLAYERHOUSE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.SHRINEOFSAVANNA_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.SMALLTEMPLE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.TRADER_HOUSE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.WATCHTOWER_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.WATERSHRINE_CONFIG));
        return new StructuresPacket("Included Structures", null, structures, Lists.newArrayList(), true, "Silverm7ner");
    }

    public static void warnInvalidStructureFile(File packet) {
        LOGGER.info("Unable to load Structure Packet, because the structure of structures.nbt is wrong. Packet: {}",
                packet);
    }

    public static CompoundNBT readNBTFile(File path) {
        try (InputStream inputstream = new FileInputStream(path)) {
            return CompressedStreamTools.readCompressed(inputstream);
        } catch (IOException exception) {
            return null;
        }
    }

    public static void saveStructures(boolean noReload) {// TODO Test Templates and structure packet save
        saveStructures(noReload, -1, null);
    }

    public static void addStructuresPacket(boolean noReload, StructuresPacket newPacket) {
        saveStructures(noReload, -1, newPacket);
    }

    public static void deleteStructuresPacket(boolean noReload, int IDtoDelete) {
        saveStructures(noReload, IDtoDelete, null);
    }

    public static void updateStructuresPacket(boolean noReload, StructuresPacket packet) {
        ArrayList<StructuresPacket> packets = Lists.newArrayList(StructureLoadUtils.STRUCTURE_PACKETS);
        int i = packets.indexOf(packets.stream().filter(p -> packet.getTempID() == p.getTempID()).collect(Collectors.toList()).get(0));
        packets.set(i, packet);
        StructureLoadUtils.STRUCTURE_PACKETS = ImmutableList.copyOf(packets);
        StructureLoadUtils.saveStructures(noReload);
    }

    private static void saveStructures(boolean noReload, int IDtoDelete, StructuresPacket newPacket) {
        try {
            File shrines_saves = new File(ShrinesMod.getMinecraftDirectory(), "shrines-saves").getCanonicalFile();
            if (!shrines_saves.exists()) {
                if (!shrines_saves.mkdirs()) {
                    LOGGER.error("Failed to save shrines structure, because directory creation failed");
                    return;
                }
            }

            ArrayList<File> usedPath = Lists.newArrayList();
            for (StructuresPacket packet : StructureLoadUtils.STRUCTURE_PACKETS) {
                if (IDtoDelete != packet.getTempID()) {
                    usedPath.add(savePacket(shrines_saves, packet, usedPath));
                } else {
                    try {
                        File packet_path = new File(shrines_saves, packet.getSaveName());
                        FileUtils.deleteDirectory(packet_path);
                    } catch (Throwable e) {
                        LOGGER.warn("Failed to delete Structure Package {}", packet.getDisplayName());
                    }
                }
            }
            if (newPacket != null) {
                savePacket(shrines_saves, newPacket, usedPath);
            }
            if (!noReload) {
                loadStructures(false);
            }
        } catch (Throwable t) {
            LOGGER.error("Failed to save shrines structures for unknown reason", t);
        }
    }

    public static File savePacket(File shrines_saves, StructuresPacket packet, ArrayList<File> usedPath) {
        File packet_path = new File(shrines_saves, packet.getSaveName());
        if (usedPath.contains(packet_path)) {
            packet_path = new File(shrines_saves, getSavePath(packet_path.getName()));
        }
        packet.setSaveName(packet_path.getName());
        if (!packet_path.exists()) {
            if (!packet_path.mkdirs()) {
                LOGGER.error("Failed to Create Structure Packet directory {}", packet_path);
                return null;
            }
        }
        File structures_file = new File(packet_path, "structures.nbt");
        File packInfo = new File(packet_path, "pack.mcmeta");

        CompoundNBT compoundnbt = StructuresPacket.toCompound(packet);
        try (OutputStream outputstream = new FileOutputStream(structures_file)) {
            CompressedStreamTools.writeCompressed(compoundnbt, outputstream);
        } catch (Throwable throwable) {
            LOGGER.error("Failed to save structures packet [{}] on path {}, {}", packet.getDisplayName(), structures_file,
                    throwable);
        }

        if (!packInfo.exists()) {
            String info = "{\n" +
                    "\t\"pack\": {\n" +
                    "\t\t\"pack_format\":6,\n" +
                    "\t\t\"description\": \"Datapack to provide information about shrines structures\"\n" +
                    "\t}\n" +
                    "}";
            try {
                FileUtils.writeStringToFile(packInfo, info, Charset.defaultCharset());
            } catch (IOException e) {
                LOGGER.error("Failed to save structures packet [{}] on path {}, {}, while datapack phase", packet.getDisplayName(), structures_file,
                        e);
            }
        }
        return packet_path;
    }

    public static void addTemplatesToPacket(List<TemplateIdentifier> templates, int packetID) {
        List<StructuresPacket> packets = StructureLoadUtils.STRUCTURE_PACKETS.stream().filter(packet -> packet.getTempID() == packetID).collect(Collectors.toList());
        if (packets.size() < 1) {
            return;
        }
        StructuresPacket packet = packets.get(0);
        File packet_path = new File(StructureLoadUtils.getSaveLocation(), packet.getSaveName());
        for (TemplateIdentifier template : templates) {
            ResourceLocation location = template.getLocation();
            File template_path = FileUtils.getFile(packet_path, "data", location.getNamespace(), "structures", location.getPath() + ".nbt");
            try (OutputStream outputstream = new FileOutputStream(template_path)) {
                CompressedStreamTools.writeCompressed(template.getTemplate(), outputstream);
            } catch (Throwable throwable) {
                LOGGER.error("Failed to save new template {} to {}", location, template_path,
                        throwable);
            }
        }
        StructureLoadUtils.saveStructures(false);
    }

    public static void deleteTemplates(ResourceLocation template, int packetID) {
        List<StructuresPacket> packets = StructureLoadUtils.STRUCTURE_PACKETS.stream().filter(packet -> packet.getTempID() == packetID).collect(Collectors.toList());
        if (packets.size() < 1) {
            return;
        }
        StructuresPacket packet = packets.get(0);
        File packet_path = new File(StructureLoadUtils.getSaveLocation(), packet.getSaveName());
        File template_path = FileUtils.getFile(packet_path, "data", template.getNamespace(), "structures", template.getPath().replace(".nbt", "") + ".nbt");
        if (template_path.delete()) {
            StructureLoadUtils.saveStructures(false);
        } else {
            LOGGER.error("Failed to delete Template [{}, {}]", template, template_path);
        }
    }

    public static void renameTemplates(ResourceLocation from, ResourceLocation to, int packetID) {
        List<StructuresPacket> packets = StructureLoadUtils.STRUCTURE_PACKETS.stream().filter(packet -> packet.getTempID() == packetID).collect(Collectors.toList());
        if (packets.size() < 1) {
            return;
        }
        StructuresPacket packet = packets.get(0);
        File packet_path = new File(StructureLoadUtils.getSaveLocation(), packet.getSaveName());
        File template_path = FileUtils.getFile(packet_path, "data", from.getNamespace(), "structures", from.getPath().replace(".nbt", "") + ".nbt");
        File new_template_path = FileUtils.getFile(packet_path, "data", to.getNamespace(), "structures", to.getPath().replace(".nbt", "") + ".nbt");
        if (template_path.renameTo(new_template_path)) {
            StructureLoadUtils.saveStructures(false);
        } else {
            LOGGER.error("Failed to rename Template [{}, {}], [{}, {}]", from, to, template_path, new_template_path);
        }
    }

    public static void sendQueueUpdatesToPlayers() {
        MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        ArrayList<StructuresPacket> packets = Lists.newArrayList();
        packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
        if (StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.size() == 1) {
            UUID first = StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.get(0);
            ShrinesPacketHandler.sendTo(new STCOpenStructuresPacketEditPacket(packets),
                    server.getPlayerList().getPlayer(first));
        } else {
            StructureLoadUtils.sendUpdatesToNonFirst();
        }
    }

    public static void playerLeftQueue(UUID leftPlayer) {
        ArrayList<StructuresPacket> packets = Lists.newArrayList();
        packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
        int position = StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.indexOf(leftPlayer);
        StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.remove(position);
        if (position >= 0) {
            if (position == 0 && StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.size() > 0) {
                UUID first = StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.get(0);
                ShrinesPacketHandler.sendTo(new STCOpenStructuresPacketEditPacket(packets), first);
            } else {
                StructureLoadUtils.sendUpdatesToNonFirst();
            }
            return;
        }
        ShrinesPacketHandler.sendTo(new STCFetchStructuresPacket(packets, false), leftPlayer);
    }

    private static void sendUpdatesToNonFirst() {
        for (int i = 1; i < StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.size(); i++) {
            UUID player = StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.get(i);
            ShrinesPacketHandler.sendTo(new STCUpdateQueueScreenPacket(i), player);
        }
    }

    public static List<String> getPossibleDimensions() {
        try {
            MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
            ArrayList<String> dims = Lists.newArrayList();
            for (ServerWorld w : server.getAllLevels()) {
                dims.add(w.dimension().location().toString());
            }
            return dims;
        } catch (Throwable t) {
            return Lists.newArrayList();
        }
    }

    public static IPackFinder getPackFinder() {
        return new FolderPackFinder(StructureLoadUtils.getSaveLocation(), IPackNameDecorator.DEFAULT);
    }
}