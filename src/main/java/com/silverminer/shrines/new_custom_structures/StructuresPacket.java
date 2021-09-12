package com.silverminer.shrines.new_custom_structures;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class StructuresPacket {
	protected static final Logger LOGGER = LogManager.getLogger(StructuresPacket.class);
	protected String name;
	protected boolean isIncluded;

	protected List<StructureData> structures;

	public StructuresPacket(String name, ListNBT structures, boolean isIncluded) {
		this(name, structures.stream().map((inbt) -> {
			if (inbt.getId() == 10)
				return new StructureData((CompoundNBT) inbt);
			else
				return null;
		}).filter(nbt -> nbt != null).collect(Collectors.toList()), isIncluded);
	}

	public StructuresPacket(String name, List<StructureData> structures, boolean isIncluded) {
		this.name = name;
		this.structures = structures;
		this.isIncluded = isIncluded;
	}

	public static StructuresPacket fromCompound(CompoundNBT nbt, @Nullable File path) {
		if (nbt == null) {
			if (path != null)
				LOGGER.info("Failed to load custom structures packet: Unable to load structures.nbt file. Packet: {}",
						path);
			return null;
		}
		// Get the name of the packet and basic properties of the packet here
		if (!nbt.contains("Packet Version")) {
			if (path != null)
				Utils.warnInvalidStructureFile(path);
			return null;
		}
		int packet_version = nbt.getInt("Packet Version");
		if (packet_version != Utils.PACKET_VERSION) {
			if (packet_version < Utils.PACKET_VERSION) {
				LOGGER.info("Unable to load Structure Packet. This packet was made for an older version of this Mod");
			}
			if (packet_version > Utils.PACKET_VERSION) {
				LOGGER.info("Unable to load Structure Packet. This packet was made for an newer version of this Mod");
			}
			return null;
		}
		String packet_name = nbt.getString("Packet Name");
		ListNBT structures = nbt.getList("Structures", 10);
		boolean is_included = nbt.getBoolean("Is Included");
		StructuresPacket packet = new StructuresPacket(packet_name, structures, is_included);
		return packet;
	}

	public static CompoundNBT toCompound(StructuresPacket packet) {
		CompoundNBT compoundnbt = new CompoundNBT();
		compoundnbt.putInt("Packet Version", Utils.PACKET_VERSION);
		compoundnbt.putString("Packet Name", packet.getName());
		ListNBT structures = new ListNBT();
		structures.addAll(packet.getStructures().stream().map(structure -> structure.write(new CompoundNBT()))
				.collect(Collectors.toList()));
		compoundnbt.put("Structures", structures);
		compoundnbt.putBoolean("Is Included", packet.isIncluded());
		return compoundnbt;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<StructureData> getStructures() {
		return structures;
	}

	public void setStructures(ArrayList<StructureData> structures) {
		this.structures = structures;
	}

	public boolean isIncluded() {
		return isIncluded;
	}
}