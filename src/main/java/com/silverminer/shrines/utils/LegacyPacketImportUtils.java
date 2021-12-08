package com.silverminer.shrines.utils;

import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.structures.load.legacy.LegacyStructureData;
import com.silverminer.shrines.structures.load.legacy.PieceData;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSImportLegacyStructuresPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LegacyPacketImportUtils {

    public static void importLegacyPacket(Path path, Player sender) {
        ArrayList<LegacyStructureData> structureData = Lists.newArrayList();
        try {
            File f = path.toFile();
            if (!f.exists()) {
                ClientUtils.showErrorToast("Failed to import legacy structures", "Directory doesn't exists");
                return;
            }

            File structures = new File(f, "structures.txt");
            if (!structures.exists()) {
                ClientUtils.showErrorToast("Failed to import legacy structures", "Structures file doesn't exists");
                return;
            }
            List<String> names = Files.readAllLines(structures.toPath());
            Random rand = new Random();
            for (String n : names) {
                if (n.startsWith("#"))
                    continue;
                File st = new File(f, "shrines");
                st = new File(st, n);
                if (!st.isDirectory()) {
                    ClientUtils.showErrorToast("Failed to import legacy structure", "Directory of " + n + " doesn't exists");
                    continue;
                }
                st = new File(st, n + ".txt");
                LegacyStructureData csd = new LegacyStructureData(n, rand);
                if (!st.exists()) {
                    ClientUtils.showErrorToast("Failed to import legacy structure", "Config file of " + n + " doesn't exists");
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
        StructuresPacket packet = new StructuresPacket(path.getName(path.getNameCount() - 1).toString(), null, structures, isIncluded, sender.getDisplayName().getString());
        List<TemplateIdentifier> newTemplates = Lists.newArrayList();
        List<String> invalidTemplates = Lists.newArrayList();
        List<ResourceLocation> usedLocations = Lists.newArrayList();
        for (LegacyStructureData data : structureData) {
            if (data.pieces.getValue().size() == 1) {
                PieceData piece = data.pieces.getValue().get(0);
                ResourceLocation location = new ResourceLocation(ShrinesMod.MODID, data.getDataName() + "/" + piece.path);
                int i = 0;
                while(usedLocations.contains(location)){
                    location = new ResourceLocation(ShrinesMod.MODID, data.getDataName() + "/" + piece.path + "_" + i);
                }
                TemplateIdentifier template = new TemplateIdentifier(path.resolve("shrines").resolve(data.getName()).resolve(piece.path + ".nbt").toFile(), location);
                newTemplates.add(template);
                packet.getPools().add(new TemplatePool(new ResourceLocation(ShrinesMod.MODID, data.getDataName()), Lists.newArrayList(new TemplatePool.Entry(location, 1, false))));
                usedLocations.add(location);
            } else {
                invalidTemplates.add(data.name);
            }
        }
        if (invalidTemplates.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : invalidTemplates) {
                sb.append("\n").append(s);
            }
            ClientUtils.showErrorToast("Failed to convert structure templates", "These structures are affected:" + sb);
        }
        ShrinesPacketHandler.sendToServer(new CTSImportLegacyStructuresPacket(packet, newTemplates));
    }
}
