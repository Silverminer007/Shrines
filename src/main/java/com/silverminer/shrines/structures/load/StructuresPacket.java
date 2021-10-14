package com.silverminer.shrines.structures.load;

import com.silverminer.shrines.utils.StructureLoadUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.structure.Structure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StructuresPacket implements Comparable<StructuresPacket> {
    protected static final Logger LOGGER = LogManager.getLogger(StructuresPacket.class);
    private static int IDcaller = 0;
    protected final String author;
    protected final int tempID;
    @Nullable
    public List<String> possibleDimensions;
    public boolean hasIssues = false;
    protected String displayName;
    @Nullable
    protected String saveName;
    protected boolean isIncluded;
    protected List<StructureData> structures;
    protected List<ResourceLocation> templates;

    public StructuresPacket(String displayName, String saveName, ListNBT structures, List<ResourceLocation> templates, boolean isIncluded, String author, int ID) {
        this(displayName, saveName, structures.stream().map((inbt) -> {
            if (inbt.getId() == 10)
                return new StructureData((CompoundNBT) inbt);
            else
                return null;
        }).filter(Objects::nonNull).collect(Collectors.toList()), templates, isIncluded, author, ID);
    }

    public StructuresPacket(String displayName, String saveName, List<StructureData> structures, List<ResourceLocation> templates, boolean isIncluded, String author) {
        this(displayName, saveName, structures, templates, isIncluded, author, IDcaller++);
    }

    public StructuresPacket(String displayName, String saveName, List<StructureData> structures, List<ResourceLocation> templates, boolean isIncluded, String author, int ID) {
        this.displayName = displayName;
        this.saveName = saveName == null || saveName.isEmpty() ? null : saveName;
        this.structures = structures;
        this.templates = templates;
        this.isIncluded = isIncluded;
        this.author = author;
        this.tempID = ID;
    }

    public static StructuresPacket fromCompound(CompoundNBT nbt, @Nullable File path, boolean network) {
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
        ListNBT structures = nbt.getList("Structures", 10);
        boolean is_included = nbt.getBoolean("Is Included");
        String author = nbt.getString("Author");
        int id = network ? nbt.getInt("ID") : IDcaller++;
        List<ResourceLocation> templates = Arrays.stream(nbt.getString("Templates").split(";")).map(ResourceLocation::new).collect(Collectors.toList());
        StructuresPacket packet = new StructuresPacket(packet_name, save_name, structures, templates, is_included, author, id);
        packet.possibleDimensions = Arrays.asList(nbt.getString("Possible Dimensions").split(";"));
        packet.hasIssues = nbt.getBoolean("HasIssues");
        return packet;
    }

    public static CompoundNBT toCompound(StructuresPacket packet) {
        CompoundNBT compoundnbt = new CompoundNBT();
        compoundnbt.putInt("Packet Version", StructureLoadUtils.PACKET_VERSION);
        compoundnbt.putString("Packet Name", packet.getDisplayName());
        compoundnbt.putString("Save Name", packet.getSaveName());
        ListNBT structures = new ListNBT();
        structures.addAll(packet.getStructures().stream().map(structure -> structure.write(new CompoundNBT()))
                .collect(Collectors.toList()));
        compoundnbt.put("Structures", structures);
        compoundnbt.putBoolean("Is Included", packet.isIncluded());
        compoundnbt.putString("Author", packet.getAuthor());
        compoundnbt.putInt("ID", packet.getTempID());// Only for network
        StringBuilder templates = new StringBuilder();
        for (ResourceLocation s : packet.templates) {
            templates.append(s).append(";");
        }
        compoundnbt.putString("Templates", templates.toString());
        StringBuilder dims = new StringBuilder();
        for (String s : StructureLoadUtils.getPossibleDimensions()) {
            dims.append(s).append(";");
        }
        compoundnbt.putString("Possible Dimensions", dims.toString());
        compoundnbt.putBoolean("HasIssues", packet.hasIssues);
        return compoundnbt;
    }

    public static void resetIDCaller() {
        StructuresPacket.IDcaller = 0;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Nonnull
    public String getSaveName() {
        return this.saveName != null ? this.saveName : this.displayName;
    }

    public void setSaveName(@Nullable String saveName) {
        this.saveName = saveName;
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

    public int getTempID() {
        return this.tempID;
    }

    @Override
    public int compareTo(StructuresPacket o) {
        int c = this.getDisplayName().compareTo(o.getDisplayName());
        return c != 0 ? c : this.getSaveName().compareTo(o.getSaveName());
    }

    public StructuresPacket copy() {
        return StructuresPacket.fromCompound(StructuresPacket.toCompound(this), null, false);
    }
}