package com.silverminer.shrines.structures.load;

import com.google.common.collect.Lists;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.TemplatePool;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class StructuresPacket implements Comparable<StructuresPacket> {
    protected static final Logger LOGGER = LogManager.getLogger(StructuresPacket.class);
    protected final String author;
    // Runtime only
    @Nullable
    public List<String> possibleDimensions;
    // Runtime only
    public boolean hasIssues = false;
    protected String displayName;
    @Nullable
    protected String saveName;
    protected boolean isIncluded;
    protected List<StructureData> structures;
    // Runtime only
    protected List<ResourceLocation> templates;
    // Runtime only
    protected List<TemplatePool> pools;


    public StructuresPacket(String displayName, String saveName, ListTag structures, boolean isIncluded, String author) {
        this(displayName, saveName, structures.stream().map((inbt) -> {
            if (inbt.getId() == 10)
                return new StructureData((CompoundTag) inbt);
            else
                return null;
        }).filter(Objects::nonNull).collect(Collectors.toList()), isIncluded, author);
    }

    public StructuresPacket(String displayName, String saveName, List<StructureData> structures, boolean isIncluded, String author) {
        this.displayName = displayName;
        this.saveName = saveName == null || saveName.isEmpty() ? null : saveName;
        this.structures = structures;
        this.isIncluded = isIncluded;
        this.author = author;
        this.pools = Lists.newArrayList();
    }

    public static StructuresPacket read(CompoundTag nbt, @Nullable File path) {
        if (nbt == null) {
            if (path != null)
                LOGGER.info("Failed to load structures packet: Unable to load structures.nbt file. Packet: {}", path);
            return null;
        }
        // Get the name of the packet and basic properties of the packet here
        if (!nbt.contains("Packet Version")) {
            if (path != null)
                StructureLoadUtils.warnInvalidStructureFile(path);
            return null;
        }
        int packet_version = nbt.getInt("Packet Version");
        if (packet_version != StructureLoadUtils.PACKET_VERSION) {
            if (packet_version < StructureLoadUtils.PACKET_VERSION) {
                LOGGER.info("Unable to load Structure Packet. This packet was made for an older version of this Mod");
            }
            if (packet_version > StructureLoadUtils.PACKET_VERSION) {
                LOGGER.info("Unable to load Structure Packet. This packet was made for an newer version of this Mod");
            }
            return null;
        }
        String packet_name = nbt.getString("Packet Name");
        String save_name = nbt.getString("Save Name");
        if (save_name.isEmpty()) save_name = null;
        ListTag structures = nbt.getList("Structures", 10);
        boolean is_included = nbt.getBoolean("Is Included");
        String author = nbt.getString("Author");

        StructuresPacket packet = new StructuresPacket(packet_name, save_name, structures, is_included, author);

        // Network Only
        packet.possibleDimensions = Arrays.asList(nbt.getString("Possible Dimensions").split(";"));
        packet.hasIssues = nbt.getBoolean("HasIssues");
        String rawTemplates = nbt.getString("Templates");
        if (!rawTemplates.replace(";", "").isEmpty()) {
            packet.templates = Arrays.stream(rawTemplates.split(";")).map(ResourceLocation::new).collect(Collectors.toList());
        } else {
            packet.templates = Lists.newArrayList();
        }
        if (nbt.contains("Pools")) {
            List<TemplatePool> pools = Lists.newArrayList();
            CompoundTag poolsTag = nbt.getCompound("Pools");
            int i = 0;
            while (poolsTag.contains(String.valueOf(i))) {
                CompoundTag pool = poolsTag.getCompound(String.valueOf(i));
                pools.add(TemplatePool.read(pool));
                i++;
            }
            packet.pools = pools;
        }
        // Network Only End

        return packet;
    }

    public static CompoundTag saveToDisk(StructuresPacket packet) {
        CompoundTag compoundnbt = new CompoundTag();
        compoundnbt.putInt("Packet Version", StructureLoadUtils.PACKET_VERSION);
        compoundnbt.putString("Packet Name", packet.getDisplayName());
        if (packet.hasSaveName()) {
            compoundnbt.putString("Save Name", packet.getSaveName());
        }
        ListTag structures = new ListTag();
        structures.addAll(packet.getStructures().stream().map(structure -> structure.write(new CompoundTag()))
                .collect(Collectors.toList()));
        compoundnbt.put("Structures", structures);
        compoundnbt.putBoolean("Is Included", packet.isIncluded());
        compoundnbt.putString("Author", packet.getAuthor());
        return compoundnbt;
    }

    public static CompoundTag saveToNetwork(StructuresPacket packet) {
        CompoundTag compoundNBT = StructuresPacket.saveToDisk(packet);
        StringBuilder templates = new StringBuilder();
        if (packet.templates != null) {
            for (ResourceLocation s : packet.templates) {
                templates.append(s).append(";");
            }
        }
        compoundNBT.putString("Templates", templates.toString());
        StringBuilder dims = new StringBuilder();
        for (String s : StructureLoadUtils.getPossibleDimensions()) {
            dims.append(s).append(";");
        }
        compoundNBT.putString("Possible Dimensions", dims.toString());
        compoundNBT.putBoolean("HasIssues", packet.hasIssues);
        CompoundTag pools = new CompoundTag();
        int i = 0;
        for (TemplatePool templatePool : packet.pools) {
            pools.put(String.valueOf(i++), templatePool.write());
        }
        compoundNBT.put("Pools", pools);
        return compoundNBT;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSaveName() throws RuntimeException {
        if (this.hasSaveName())
            return this.saveName;
        else
            throw new RuntimeException("Structure Packets Save name was empty");
    }

    public void setSaveName(@Nullable String saveName) {
        this.saveName = saveName;
    }

    public boolean hasSaveName() {
        return this.saveName != null;
    }

    public List<StructureData> getStructures() {
        return structures;
    }

    public void setStructures(ArrayList<StructureData> structures) {
        this.structures = structures;
    }

    public List<ResourceLocation> getTemplates() {
        return this.templates;
    }

    public void setTemplates(ArrayList<ResourceLocation> templates) {
        this.templates = templates;
    }

    public boolean isIncluded() {
        return isIncluded;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public int compareTo(StructuresPacket o) {
        int c = this.getDisplayName().compareTo(o.getDisplayName());
        return c != 0 ? c : this.getSaveName().compareTo(o.getSaveName());
    }

    public List<TemplatePool> getPools() {
        return pools;
    }

    public void setPools(List<TemplatePool> pools) {
        this.pools = pools;
    }
}