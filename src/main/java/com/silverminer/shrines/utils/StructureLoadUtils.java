/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.utils;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.config.DefaultStructureConfig;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCErrorPacket;
import com.silverminer.shrines.utils.network.stc.STCFetchStructuresPacket;
import com.silverminer.shrines.utils.network.stc.STCOpenStructuresPacketEditPacket;
import com.silverminer.shrines.utils.network.stc.STCUpdateQueueScreenPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

public class StructureLoadUtils {
    public static final int PACKET_VERSION = 2;
    protected static final Logger LOGGER = LogManager.getLogger(StructureLoadUtils.class);
    private static final List<String> deletedStructures = ImmutableList.of("ballon", "small_tempel", "high_tempel");
    /**
     * This list is equals to the Structure Packet which the user can see, so Packets that were deleted aren't here anymore, but new packets are here.
     * This list is only on the server side, but synchronized between its threads
     */
    public static List<StructuresPacket> STRUCTURE_PACKETS;
    /**
     * This list is a copy of the structure packets that were loaded from disk. This list should be used for any world generation access operations, to prevent strange behaviors because of changes in the GUI
     */
    public static ImmutableList<StructuresPacket> FINAL_STRUCTURES_PACKETS;
    /**
     * First player in queue is already allowed to edit the structure packets. All
     * players behind need to wait until the first player is done. This is needed to
     * prevent Overrides of changes one player made while another one is in
     * configuration screen and is going to set the changes later
     */
    public static ArrayList<UUID> PLAYERS_IN_EDIT_QUEUE = Lists.newArrayList();
    private static boolean upgradeFrom1_8_1 = false;

    public static File getImportCacheLocation() {
        return FileUtils.getFile(StructureLoadUtils.getShrinesSavesLocation(), "Cache", "Import");
    }

    public static File getExportCacheLocation() {
        return FileUtils.getFile(StructureLoadUtils.getShrinesSavesLocation(), "Cache", "Export");
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
    public static void loadStructures() {
        try {
            ArrayList<StructuresPacket> structure_packets = Lists.newArrayList();

            File shrines_saves = StructureLoadUtils.getPacketsSaveLocation().getCanonicalFile();

            if (!upgradeFrom1_8_1) {
                upgradeFrom1_8_1 = new File(shrines_saves.getParentFile(), "structures.txt").exists() && !(new File(shrines_saves, "info.txt").exists());
            }

            if (!shrines_saves.exists()) {
                if (!shrines_saves.mkdirs()) {
                    LOGGER.error("Failed to Load shrines structure, because directory creation failed");
                }
            }

            // Get the list of all directories in shrines saves folder as possible structure
            // packets
            Files.find(shrines_saves.toPath(), 1, (path, basicFileAttributes) -> Files.isDirectory(path)).forEach(path -> {
                try {
                    if (Files.isSameFile(path, shrines_saves.toPath())) {
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                StructuresPacket packet = loadStructuresPacket(path);
                if (packet != null) {
                    packet.setSaveName(path.getFileName().toString());
                    structure_packets.add(packet);
                }
            });

            // Check if the included structures are already loaded or need to be defaulted
            boolean has_included_structures = false;
            for (StructuresPacket packet : structure_packets) {
                if (packet.isIncluded()) {
                    has_included_structures = true;
                    break;
                }
            }
            // Check for structure key duplicates and disable affected structures
            checkStructureKeyDuplicates(structure_packets);
            // Save Structure Packets for later use
            STRUCTURE_PACKETS = structure_packets;
            if (!has_included_structures) {
                StructuresPacket includedStructures = upgradeFrom1_8_1 ? loadLegacyIncludedStructures() : StructureLoadUtils.getIncludedStructures();
                StructureLoadUtils.saveStructures(includedStructures);
                if (upgradeFrom1_8_1) {
                    LegacyPacketImportUtils.importLegacyPacketOnServer(shrines_saves.getParentFile().toPath(), "Silverm7ner");
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static StructuresPacket loadLegacyIncludedStructures() {
        Path configPath = FMLPaths.CONFIGDIR.get().resolve("shrines-server.toml");
        CommentedFileConfig configData = CommentedFileConfig.of(configPath);
        configData.load();
        List<StructureData> structures = getLegacyIncludedStructures();
        for (StructureData structureData : structures) {
            String key = new ResourceLocation(structureData.getKey()).getPath();
            try {
                structureData.setTransformLand(configData.get(Arrays.asList("structures", key, "needs_ground")));
                structureData.setGenerate(configData.get(Arrays.asList("structures", key, "generate")));
                structureData.setSpawn_chance(configData.get(Arrays.asList("structures", key, "spawn_chance")));
                structureData.setDistance(configData.get(Arrays.asList("structures", key, "distance")));
                structureData.setSeperation(configData.get(Arrays.asList("structures", key, "seperation")));
                structureData.setSeed_modifier(configData.get(Arrays.asList("structures", key, "seed")));
                structureData.setBiomeBlacklist(configData.get(Arrays.asList("structures", key, "blacklist")));
                structureData.setBiomeCategoryWhitelist(configData.get(Arrays.asList("structures", key, "categories")));
                structureData.setDimension_whitelist(configData.get(Arrays.asList("structures", key, "dimensions")));
                List<String> dimensionWhitelist = structureData.getDimension_whitelist();
                if (dimensionWhitelist.contains("end")) {
                    dimensionWhitelist.remove("end");
                    dimensionWhitelist.add("the_end");
                }
                if (dimensionWhitelist.contains("nether")) {
                    dimensionWhitelist.remove("nether");
                    dimensionWhitelist.add("the_nether");
                }
                structureData.setUse_random_varianting(configData.get(Arrays.asList("structures", key, "use_random_varianting")));
                if (key.equals("ballon")) {
                    structureData.setKey("balloon");
                }
                if (key.equals("small_tempel")) {
                    structureData.setKey("small_temple");
                }
                if (key.equals("high_tempel")) {
                    structureData.setKey("high_temple");
                }
            } catch (NullPointerException e) {
                if (!key.equals("ballon")
                        && !key.equals("small_tempel")
                        && !key.equals("high_tempel")) {
                    LOGGER.error("Failed to import legacy structure config from shrines-server.toml for structure [{}]", structureData.getKey());
                }
            }
        }
        StructureData ballon = new StructureData(DefaultStructureConfig.DELETED_STRUCTURE_CONFIG);
        ballon.setKey("ballon");
        StructureData small_tempel = new StructureData(DefaultStructureConfig.DELETED_STRUCTURE_CONFIG);
        small_tempel.setKey("small_tempel");
        StructureData high_tempel = new StructureData(DefaultStructureConfig.DELETED_STRUCTURE_CONFIG);
        high_tempel.setKey("high_tempel");
        structures.add(ballon);
        structures.add(small_tempel);
        structures.add(high_tempel);
        return new StructuresPacket("Legacy Included Structures (Auto import)", "Included Structures", structures, true, "Silverm7ner");
    }

    /**
     * @return all structures that are available by default in 2.x.x but with legacy (1.8.1) structure keys
     */
    private static List<StructureData> getLegacyIncludedStructures() {
        List<StructureData> structures = new ArrayList<>();
        structures.add(new StructureData(DefaultStructureConfig.ABANDONEDWITCHHOUSE_CONFIG));
        StructureData balloonData = new StructureData(DefaultStructureConfig.BALLON_CONFIG);
        balloonData.setKey(new ResourceLocation(ShrinesMod.MODID, "ballon").toString());
        structures.add(balloonData);
        structures.add(new StructureData(DefaultStructureConfig.BEES_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.ENDTEMPLE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.FLOODEDTEMPLE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.GUARDIANMEETING_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.HARBOUR_CONFIG));
        StructureData highTempleData = new StructureData(DefaultStructureConfig.HIGHTEMPLE_CONFIG);
        highTempleData.setKey(new ResourceLocation(ShrinesMod.MODID, "high_tempel").toString());
        structures.add(highTempleData);
        structures.add(new StructureData(DefaultStructureConfig.INFESTEDPRISON_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.JUNGLETOWER_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.MINERALTEMPLE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.NETHERPYRAMID_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.NETHERSHRINE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.ORIENTALSANCTUARY_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.PLAYERHOUSE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.SHRINEOFSAVANNA_CONFIG));
        StructureData smallTempleData = new StructureData(DefaultStructureConfig.SMALLTEMPLE_CONFIG);
        smallTempleData.setKey(new ResourceLocation(ShrinesMod.MODID, "small_tempel").toString());
        structures.add(smallTempleData);
        structures.add(new StructureData(DefaultStructureConfig.TRADER_HOUSE_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.WATCHTOWER_CONFIG));
        structures.add(new StructureData(DefaultStructureConfig.WATERSHRINE_CONFIG));
        return structures;
    }

    public static void checkStructureKeyDuplicates(List<StructuresPacket> structuresPackets) {
        HashMap<String, StructuresPacket> temp = Maps.newHashMap();
        ArrayList<StructuresPacket> packets_with_issue = Lists.newArrayList();
        // Check for duplicated structure names and warn/stop loading the structure and
        // open GUI after start
        for (StructuresPacket packet : structuresPackets) {
            String packet_name = packet.getSaveName();
            for (StructureData data : packet.getStructures()) {
                String key = data.getKey();
                if (temp.containsKey(key)) {
                    LOGGER.warn(
                            "A conflict was detected when loading structures. There are two or more structures with the same name. Key [{}], Packet 1 [{}], Packet 2 [{}]",
                            key, packet_name, temp.get(key).getSaveName());
                    data.successful = false;
                    packets_with_issue.add(packet);
                } else {
                    data.successful = true;
                }
                temp.put(key, packet);
            }
        }
        for (StructuresPacket packet : structuresPackets) {
            packet.hasIssues = packets_with_issue.contains(packet);
        }
    }

    public static StructuresPacket loadStructuresPacket(Path path) {
        Path structures_file = path.resolve("structures.nbt");
        if (Files.notExists(structures_file)) {
            return null;
        }
        // Read the file here in CompoundNBT tag
        CompoundNBT data_structure = readNBTFile(structures_file.toFile());
        // Create an instance of a packet datastructures and write it to a read/write
        // array
        ArrayList<ResourceLocation> templates = StructureLoadUtils.searchForTemplates(path);
        ArrayList<TemplatePool> pools = StructureLoadUtils.loadTemplatePools(path);
        StructuresPacket packet = StructuresPacket.read(data_structure, path.toFile());
        if (packet == null) {
            return null;
        }
        packet.setTemplates(templates);
        packet.setPools(pools);
        return packet;
    }

    private static ArrayList<ResourceLocation> searchForTemplates(Path path) {
        Path data = path.resolve("data");
        if (!Files.isDirectory(data)) {
            return Lists.newArrayList();
        }
        ArrayList<ResourceLocation> templates = Lists.newArrayList();
        try {
            Files.find(data, Integer.MAX_VALUE, (p, basicFileAttributes) -> p.toString().endsWith(".nbt")).forEach(templatePath -> {
                Path relativeTemplate = data.relativize(templatePath).normalize();
                if (relativeTemplate.getNameCount() > 2) {
                    String namespace = relativeTemplate.getName(0).toString();
                    String locationPath = Paths.get(relativeTemplate.getName(0).toString(), relativeTemplate.getName(1).toString()).relativize(relativeTemplate).toString();
                    ResourceLocation template = new ResourceLocation(namespace, locationPath.replace(".nbt", ""));
                    templates.add(template);
                }
            });
        } catch (IOException e) {
            LOGGER.error("Failed to read Templates", e);
        }
        return templates;
    }

    private static ArrayList<TemplatePool> loadTemplatePools(Path path) {
        Path data = path.resolve("data");
        if (!Files.isDirectory(data)) {
            return Lists.newArrayList();
        }
        ArrayList<TemplatePool> templates = Lists.newArrayList();
        try {
            Files.find(data, 1, ((path1, basicFileAttributes) -> {
                try {
                    return Files.isDirectory(path1) && !Files.isSameFile(data, path1);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            })).forEach(namespacePath -> {
                try {
                    Path poolsPath = namespacePath.resolve("worldgen").resolve("template_pool");
                    if (Files.exists(poolsPath)) {
                        Files.find(poolsPath, Integer.MAX_VALUE, (p, basicFileAttributes) -> Files.isRegularFile(p) && p.toString().endsWith(".json")).forEach(poolPath -> {
                            try {
                                StringBuilder poolString = new StringBuilder();
                                Files.readAllLines(poolPath).forEach(poolString::append);
                                TemplatePool pool = TemplatePool.fromString(poolString.toString());
                                pool.setSaveName(new ResourceLocation(namespacePath.getFileName().toString(), poolsPath.relativize(poolPath).toString().replaceAll(".json", "")));
                                templates.add(pool);
                            } catch (IOException e) {
                                LOGGER.error("Failed to parse Template Pool", e);
                            }
                        });
                    }
                } catch (IOException e) {
                    LOGGER.error("Failed to collect template pools", e);
                }
            });
        } catch (IOException e) {
            LOGGER.error("Failed to read Templates", e);
        }
        return templates;
    }

    public static String getSavePath(String name) {
        String resultName = name.trim();
        if (resultName.isEmpty()) {
            resultName = "Structures Packet";
        }
        try {
            resultName = FileUtil.findAvailableName(getPacketsSaveLocation().toPath(), resultName, "");
        } catch (Exception exception1) {
            resultName = "Structures Packet";

            try {
                resultName = FileUtil.findAvailableName(getPacketsSaveLocation().toPath(), resultName, "");
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
        return new StructuresPacket("Included Structures", null, structures, true, "Silverm7ner");
    }

    public static void warnInvalidStructureFile(File packet) {
        LOGGER.info("Unable to load Structure Packet, because the structure of structures.nbt is invalid. Packet: {}",
                packet);
    }

    public static CompoundNBT readNBTFile(File path) {
        try (InputStream inputstream = new FileInputStream(path)) {
            return CompressedStreamTools.readCompressed(inputstream);
        } catch (IOException exception) {
            return null;
        }
    }

    public static void saveStructures() {
        saveStructures(null);
    }

    public static void addStructuresPacket(StructuresPacket newPacket) {
        saveStructures(newPacket);
    }

    public static void deleteStructuresPacket(String packetToDelete) {
        saveStructures();
        try {
            File shrines_saves = StructureLoadUtils.getPacketsSaveLocation().getCanonicalFile();
            File packet_path = new File(shrines_saves, packetToDelete);
            FileUtils.deleteDirectory(packet_path);
        } catch (Throwable e) {
            LOGGER.warn("Failed to delete Structure Package {}", packetToDelete);
        }
        loadStructures();
    }

    public static void updateStructuresPacket(StructuresPacket packet) {
        ArrayList<StructuresPacket> packets = Lists.newArrayList(StructureLoadUtils.STRUCTURE_PACKETS);
        int i = packets.indexOf(packets.stream().filter(p -> packet.getSaveName().equals(p.getSaveName())).collect(Collectors.toList()).get(0));
        packets.set(i, packet);
        StructureLoadUtils.STRUCTURE_PACKETS = Collections.synchronizedList(packets);
        StructureLoadUtils.saveStructures();
    }

    private static void saveStructures(StructuresPacket newPacket) {
        try {
            File shrines_saves = StructureLoadUtils.getPacketsSaveLocation().getCanonicalFile();
            if (!shrines_saves.exists()) {
                if (!shrines_saves.mkdirs()) {
                    LOGGER.error("Failed to save shrines structure, because directory creation failed");
                    return;
                }
            }

            List<String> lines = new ArrayList<>();
            lines.add("Structure Packets were saved by version: " + ShrinesMod.VERSION);
            Files.write(shrines_saves.toPath().resolve("info.txt"), lines);
            ArrayList<File> usedPath = Lists.newArrayList();
            for (StructuresPacket packet : StructureLoadUtils.STRUCTURE_PACKETS) {
                usedPath.add(savePacket(shrines_saves, packet, usedPath));
            }
            if (newPacket != null) {
                savePacket(shrines_saves, newPacket, usedPath);
            }
            loadStructures();
            StructureLoadUtils.deleteDirectory(StructureLoadUtils.getExportCacheLocation().toPath());
            StructureLoadUtils.deleteDirectory(StructureLoadUtils.getImportCacheLocation().toPath());
        } catch (Throwable t) {
            LOGGER.error("Failed to save shrines structures for unknown reason", t);
        }
    }

    public static void clearImagesCache() {
        try {
            StructureLoadUtils.deleteDirectory(StructureLoadUtils.getImagesCacheLocation().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException e)
                        throws IOException {
                    if (e == null) {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    } else {
                        throw e;
                    }
                }
            });
        }
    }

    public static File getImagesCacheLocation() {
        return FileUtils.getFile(StructureLoadUtils.getShrinesSavesLocation(), "Cache", "Images");
    }

    public static File getShrinesSavesLocation() {
        return FileUtils.getFile(ShrinesMod.getMinecraftDirectory(), "shrines-saves");
    }

    public static File savePacket(File shrines_saves, StructuresPacket packet, ArrayList<File> usedPath) {
        File packet_path = new File(shrines_saves, packet.hasSaveName() ? packet.getSaveName() : packet.getDisplayName());
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

        CompoundNBT compoundnbt = StructuresPacket.saveToDisk(packet);
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
        StructureLoadUtils.saveTemplatePools(packet.getPools(), packet_path.toPath());
        return packet_path;
    }

    public static void addTemplatesToPacket(List<TemplateIdentifier> templates, String packetSaveName) {
        List<StructuresPacket> packets = StructureLoadUtils.STRUCTURE_PACKETS.stream().filter(packet -> packet.getSaveName().equals(packetSaveName)).collect(Collectors.toList());
        if (packets.size() < 1) {
            return;
        }
        StructuresPacket packet = packets.get(0);
        File packet_path = new File(StructureLoadUtils.getPacketsSaveLocation(), packet.getSaveName());
        for (TemplateIdentifier template : templates) {
            ResourceLocation location = template.getLocation();
            File template_path = FileUtils.getFile(packet_path, "data", location.getNamespace(), "structures", location.getPath() + ".nbt");
            if (!template_path.getParentFile().isDirectory() && !template_path.getParentFile().mkdirs()) {
                LOGGER.error("Failed to save new template because directory creation failed");
                return;
            }
            try (OutputStream outputstream = new FileOutputStream(template_path)) {
                CompressedStreamTools.writeCompressed(template.getTemplate(), outputstream);
            } catch (Throwable throwable) {
                LOGGER.error("Failed to save new template {} to {}", location, template_path,
                        throwable);
            }
        }
        StructureLoadUtils.saveStructures();
    }

    public static void deleteTemplates(ResourceLocation template, String packetID) {
        List<StructuresPacket> packets = StructureLoadUtils.STRUCTURE_PACKETS.stream().filter(packet -> packet.getSaveName().equals(packetID)).collect(Collectors.toList());
        if (packets.size() < 1) {
            return;
        }
        StructuresPacket packet = packets.get(0);
        File packet_path = new File(StructureLoadUtils.getPacketsSaveLocation(), packet.getSaveName());
        File template_path = FileUtils.getFile(packet_path, "data", template.getNamespace(), "structures", template.getPath().replace(".nbt", "") + ".nbt");
        if (template_path.delete()) {
            StructureLoadUtils.saveStructures();
        } else {
            LOGGER.error("Failed to delete Template [{}, {}]", template, template_path);
        }
    }

    public static void renameTemplates(ResourceLocation from, ResourceLocation to, String packetID) {
        List<StructuresPacket> packets = StructureLoadUtils.STRUCTURE_PACKETS.stream().filter(packet -> packet.getSaveName().equals(packetID)).collect(Collectors.toList());
        if (packets.size() < 1) {
            return;
        }
        StructuresPacket packet = packets.get(0);
        File packet_path = new File(StructureLoadUtils.getPacketsSaveLocation(), packet.getSaveName());
        File template_path = FileUtils.getFile(packet_path, "data", from.getNamespace(), "structures", from.getPath().replace(".nbt", "") + ".nbt");
        File new_template_path = FileUtils.getFile(packet_path, "data", to.getNamespace(), "structures", to.getPath().replace(".nbt", "") + ".nbt");
        if (template_path.renameTo(new_template_path)) {
            // Update depending template pools
            for (TemplatePool pool : packet.getPools()) {
                for (TemplatePool.Entry poolEntry : pool.getEntries()) {
                    if (poolEntry.getTemplate().equals(from)) {
                        poolEntry.setTemplate(to);
                    }
                }
            }
            StructureLoadUtils.saveStructures();
        } else {
            LOGGER.error("Failed to rename Template [{}, {}], [{}, {}]", from, to, template_path, new_template_path);
        }
    }

    public static void saveTemplatePools(List<TemplatePool> templatePools, Path packetPath) {
        for (TemplatePool templatePool : templatePools) {
            Path poolPath = packetPath.resolve("data").resolve(templatePool.getSaveName().getNamespace()).resolve("worldgen").resolve("template_pool").resolve(templatePool.getSaveName().getPath() + ".json");
            try {
                FileUtils.writeStringToFile(poolPath.toFile(), templatePool.toString(), Charset.defaultCharset());
            } catch (IOException e) {
                LOGGER.error("Failed to save template pool [{}]", templatePool.getSaveName(), e);
            }
        }
    }

    public static void deleteTemplatePool(ResourceLocation saveName, String packetID) {
        Path poolPath = getTemplatePoolPath(packetID, saveName);
        try {
            Files.delete(poolPath);
            StructureLoadUtils.loadStructures();
        } catch (IOException e) {
            LOGGER.error("Failed to delete Template Pool [{}, {}]", saveName, poolPath);
        }
    }

    public static void addTemplatePool(TemplatePool newPool, String packetID) {
        StructureLoadUtils.saveStructures();
        Path newPoolPath = getTemplatePoolPath(packetID, newPool.getName());
        try {
            FileUtils.writeStringToFile(newPoolPath.toFile(), newPool.toString(), Charset.defaultCharset());
        } catch (IOException e) {
            LOGGER.error("Failed to save new template", e);
        }
        StructureLoadUtils.loadStructures();
    }

    public static Path getTemplatePoolPath(String packetSaveName, ResourceLocation poolLocation) {
        return StructureLoadUtils.getPacketsSaveLocation().toPath().resolve(packetSaveName).resolve("data").resolve(poolLocation.getNamespace()).resolve("worldgen").resolve("template_pool").resolve(poolLocation.getPath() + ".json");
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

    private static void sendUpdatesToNonFirst() {
        for (int i = 1; i < StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.size(); i++) {
            UUID player = StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.get(i);
            ShrinesPacketHandler.sendTo(new STCUpdateQueueScreenPacket(i), player);
        }
    }

    public static void playerLeftQueue(UUID leftPlayer) {
        int position = StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.indexOf(leftPlayer);
        StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.remove(position);
        if (position >= 0) {
            if (position == 0 && StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.size() > 0) {
                UUID first = StructureLoadUtils.PLAYERS_IN_EDIT_QUEUE.get(0);
                ShrinesPacketHandler.sendTo(new STCOpenStructuresPacketEditPacket(Lists.newArrayList(StructureLoadUtils.STRUCTURE_PACKETS)), first);
            } else {
                StructureLoadUtils.sendUpdatesToNonFirst();
            }
            return;
        }
        ShrinesPacketHandler.sendTo(new STCFetchStructuresPacket(Lists.newArrayList(StructureLoadUtils.FINAL_STRUCTURES_PACKETS), false), leftPlayer);
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

    public static List<IPackFinder> getPackFinders() {
        List<IPackFinder> packFinders = Lists.newArrayList();
        packFinders.add(new FolderPackFinder(StructureLoadUtils.getPacketsSaveLocation(), IPackNameDecorator.DEFAULT));
        packFinders.add(new FolderPackFinder(StructureLoadUtils.getImagesCacheLocation(), IPackNameDecorator.DEFAULT));
        return packFinders;
    }

    public static File getPacketsSaveLocation() {
        return FileUtils.getFile(StructureLoadUtils.getShrinesSavesLocation(), "Packets");
    }

    public static void importStructuresPacket(String fileName, byte[] archive, ServerPlayerEntity sender) {
        StructureLoadUtils.saveStructures();
        File saveDestination = new File(StructureLoadUtils.getImportCacheLocation(), fileName + ".zip");
        if (saveDestination.exists()) {
            if (!saveDestination.delete()) {
                LOGGER.error("Failed to clear cache before structure packet was imported");
                ShrinesPacketHandler.sendTo(new STCErrorPacket("Failed to import Structures Packet", "Failed to clear cache before structure packet was imported"),
                        sender);
                return;
            }
        }
        try {
            if (!StructureLoadUtils.getImportCacheLocation().exists() && !StructureLoadUtils.getImportCacheLocation().mkdirs()) {
                LOGGER.error("Failed to create Directory to import structures packet");
                ShrinesPacketHandler.sendTo(new STCErrorPacket("Failed to import Structures Packet", "Failed to clear cache before structure packet was imported"),
                        sender);
                return;
            }
            saveDestination = Files.write(saveDestination.toPath(), archive).toFile();
            if (ZIPUtils.extractArchive(saveDestination, StructureLoadUtils.getImportCacheLocation())) {
                Files.find(StructureLoadUtils.getImportCacheLocation().toPath(), 1, ((path, basicFileAttributes) -> Files.isDirectory(path))).forEach(path -> importUpToDatePacket(path, sender));
            } else {
                LOGGER.error("Failed to decompress archive");
                ShrinesPacketHandler.sendTo(new STCErrorPacket("Failed to import Structures Packet", "Failed to decompress archive"),
                        sender);
            }
        } catch (Exception e) {
            ShrinesPacketHandler.sendTo(new STCErrorPacket("Failed to import Structures Packet", e.getMessage()), sender);
        }
        StructureLoadUtils.loadStructures();
        ArrayList<StructuresPacket> packets = Lists.newArrayList();
        packets.addAll(StructureLoadUtils.STRUCTURE_PACKETS);
        ShrinesPacketHandler.sendTo(new STCOpenStructuresPacketEditPacket(packets),
                sender);
    }

    private static void importUpToDatePacket(Path path, ServerPlayerEntity sender) {
        StructuresPacket structuresPacket = StructureLoadUtils.loadStructuresPacket(path);
        if (structuresPacket != null) {
            if (validateStructureKeys(structuresPacket, path.toFile())) {
                ShrinesPacketHandler.sendTo(new STCErrorPacket("Structures were renamed", "Some structures were renamed to prevent structure key duplicates"), sender);
            }
            savePacket(path.toFile().getParentFile(), structuresPacket, Lists.newArrayList());
            File packetDest = new File(StructureLoadUtils.getPacketsSaveLocation(), getSavePath(structuresPacket.getDisplayName()));
            try {
                Files.move(path, packetDest.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean validateStructureKeys(StructuresPacket packet, File savePath) {
        boolean changed = false;
        List<String> oldKeys = Lists.newArrayList();
        for (StructuresPacket p : STRUCTURE_PACKETS) {
            oldKeys.addAll(p.getStructures().stream().map(StructureData::getKey).collect(Collectors.toList()));
        }
        Random r = new Random();
        for (StructureData data : packet.getStructures()) {
            int i = 0;
            String key = data.getKey();
            String newKey = key;
            while (oldKeys.contains(newKey)) {
                newKey = key + i++;
                changed = true;
            }
            renameIcon(savePath, key, newKey);
            data.setKey(newKey);
            data.setSeed_modifier(r.nextInt());
        }
        return changed;
    }

    private static void renameIcon(File packetPath, String oldKey, String newKey) {
        if (!Objects.equals(oldKey, newKey)) {
            ResourceLocation oldKeyLoc = new ResourceLocation(oldKey + ".png");
            File oldKeyIcon = FileUtils.getFile(packetPath, "assets", oldKeyLoc.getNamespace(), "textures", "structures", oldKeyLoc.getPath());
            if (oldKeyIcon.exists() && oldKeyIcon.isFile()) {
                ResourceLocation newKeyLoc = new ResourceLocation(newKey + ".png");
                File newKeyIcon = FileUtils.getFile(packetPath, "assets", newKeyLoc.getNamespace(), "textures", "structures", newKeyLoc.getPath());
                try {
                    Files.move(oldKeyIcon.toPath(), newKeyIcon.toPath());
                } catch (IOException e) {
                    LOGGER.error("Failed to rename Structures Icon");
                }
            }
        }
    }

    @Nonnull
    public static HashMap<ResourceLocation, byte[]> findStructureIcons() {
        HashMap<ResourceLocation, byte[]> icons = Maps.newHashMap();
        File packetsPath = StructureLoadUtils.getPacketsSaveLocation();
        for (StructuresPacket p : StructureLoadUtils.FINAL_STRUCTURES_PACKETS) {
            File path = new File(packetsPath, p.getSaveName());
            for (StructureData d : p.getStructures()) {
                ResourceLocation key = new ResourceLocation(d.getKey() + ".png");
                File iconPath = FileUtils.getFile(path, "assets", key.getNamespace(), "textures", "structures", key.getPath());
                if (iconPath.exists() && iconPath.isFile()) {
                    try {
                        icons.put(key, Files.readAllBytes(iconPath.toPath()));
                    } catch (IOException e) {
                        LOGGER.info("Failed to read structures icon {}", iconPath);
                    }
                }
            }
        }
        return icons;
    }

    public static void cacheStructureIcons(HashMap<ResourceLocation, byte[]> icons) {
        File cache = StructureLoadUtils.getImagesCacheLocation();
        try {
            FileUtils.deleteDirectory(cache);
        } catch (IOException e) {
            LOGGER.error("Failed to delete Images Cache", e);
        }
        for (Map.Entry<ResourceLocation, byte[]> icon : icons.entrySet()) {
            ResourceLocation key = icon.getKey();
            File iconPath = FileUtils.getFile(cache, "Icons", "assets", key.getNamespace(), "textures", "structures", key.getPath());
            if (!iconPath.getParentFile().exists() && !iconPath.getParentFile().mkdirs()) {
                continue;
            }
            try {
                Files.write(iconPath.toPath(), icon.getValue());
            } catch (IOException e) {
                LOGGER.error("Failed to cache structure icon [{}]", key);
            }
        }
    }
}