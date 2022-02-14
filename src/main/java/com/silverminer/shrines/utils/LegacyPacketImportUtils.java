/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.structures.load.legacy.LegacyStructureData;
import com.silverminer.shrines.structures.load.legacy.PieceData;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSImportLegacyStructuresPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class LegacyPacketImportUtils {
    protected static final Logger LOGGER = LogManager.getLogger(LegacyPacketImportUtils.class);

    /**
     * @deprecated use {@link #importLegacyPacketOnClient(Path, String)} or {@link #importLegacyPacketOnServer(Path, String)} instead
     */
    @Deprecated
    public static void importLegacyPacket(Path path, PlayerEntity sender) {
        importLegacyPacketOnClient(path, sender.getDisplayName().getString());
    }

    public static void importLegacyPacketOnClient(Path path, String sender) {
        Pair<StructuresPacket, List<TemplateIdentifier>> importResult = importLegacyPacket(path, sender, ClientUtils::showErrorToast);
        if (importResult != null) {
            ShrinesPacketHandler.sendToServer(new CTSImportLegacyStructuresPacket(importResult.getFirst(), importResult.getSecond()));
        }
    }

    public static void importLegacyPacketOnServer(Path path, String sender) {
        Pair<StructuresPacket, List<TemplateIdentifier>> importResult = importLegacyPacket(path, sender, (header, text) -> LOGGER.error(header + ". " + text));
        if (importResult != null) {
            StructureLoadUtils.addStructuresPacket(importResult.getFirst());
            String packetID = importResult.getFirst().getSaveName();
            StructureLoadUtils.addTemplatesToPacket(importResult.getSecond(), packetID);
        }
    }

    /**
     * @param path    the parent directory of structures.txt
     * @param sender  the sender that becomes the author of the package
     * @param onError an error callback
     * @return the package with structures and pools and a list of new templates inclusive compoundnbt or null on error
     */
    public static Pair<StructuresPacket, List<TemplateIdentifier>> importLegacyPacket(Path path, String sender, BiConsumer<String, String> onError) {
        ArrayList<LegacyStructureData> structureData = Lists.newArrayList();
        try {
            File f = path.toFile();
            if (!f.exists()) {
                onError.accept("Failed to import legacy structures", "Directory doesn't exists");
                return null;
            }

            File structures = new File(f, "structures.txt");
            if (!structures.exists()) {
                onError.accept("Failed to import legacy structures", "Structures file doesn't exists");
                return null;
            }
            List<String> names = Files.readAllLines(structures.toPath());
            Random rand = new Random();
            for (String n : names) {
                if (n.startsWith("#"))
                    continue;
                File st = new File(f, "shrines");
                st = new File(st, n);
                LegacyStructureData csd = new LegacyStructureData(n, rand);
                if (!st.isDirectory()) {
                    structureData.add(csd);
                    continue;
                }
                st = new File(st, n + ".txt");
                if (!st.exists()) {
                    structureData.add(csd);
                    continue;
                }
                StringBuilder data = new StringBuilder();
                for (String s : Files.readAllLines(st.toPath())) {
                    data.append(s).append("\n");
                }
                csd.fromString(data.toString());
                structureData.add(csd);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        List<StructureData> structures = structureData.stream().map(LegacyStructureData::toUpToDateData).collect(Collectors.toList());
        boolean isIncluded = structureData.stream().anyMatch(LegacyStructureData::isBuiltIn);
        StructuresPacket packet = new StructuresPacket(path.getName(path.getNameCount() - 1).toString(), null, structures, isIncluded, sender);
        List<TemplateIdentifier> newTemplates = Lists.newArrayList();
        List<String> invalidTemplates = Lists.newArrayList();
        List<ResourceLocation> usedLocations = Lists.newArrayList();
        for (LegacyStructureData data : structureData) {
            if (data.pieces.getValue().size() == 1) {
                PieceData piece = data.pieces.getValue().get(0);
                ResourceLocation location = new ResourceLocation(ShrinesMod.MODID, data.getDataName() + "/" + piece.path);
                int i = 0;
                while (usedLocations.contains(location)) {
                    location = new ResourceLocation(ShrinesMod.MODID, data.getDataName() + "/" + piece.path + "_" + i);
                }
                try {
                    TemplateIdentifier template = new TemplateIdentifier(path.resolve("shrines").resolve(data.getName()).resolve(piece.path + ".nbt").toFile(), location);
                    newTemplates.add(template);
                    packet.getPools().add(new TemplatePool(new ResourceLocation(ShrinesMod.MODID, data.getDataName()), Lists.newArrayList(new TemplatePool.Entry(location, 1, false))));
                    usedLocations.add(location);
                } catch (IllegalArgumentException ignored) {
                }
            } else {
                invalidTemplates.add(data.name);
            }
        }
        if (invalidTemplates.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : invalidTemplates) {
                sb.append("\n").append(s);
            }
            onError.accept("Failed to convert structure templates", "These structures are affected:" + sb);
        }
        return Pair.of(packet, newTemplates);
    }
}
