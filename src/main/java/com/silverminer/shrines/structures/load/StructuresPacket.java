package com.silverminer.shrines.structures.load;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.utils.Utils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class StructuresPacket implements Comparable<StructuresPacket> {
	protected static final Logger LOGGER = LogManager.getLogger(StructuresPacket.class);
	protected String name;
	protected boolean isIncluded;
	protected final String author;
	protected boolean isDeleted = false;
	protected final int tempID;
	private static int IDcaller = 0;

	protected List<StructureData> structures;

	public StructuresPacket(String name, ListNBT structures, boolean isIncluded, String author, int ID) {
		this(name, structures.stream().map((inbt) -> {
			if (inbt.getId() == 10)
				return new StructureData((CompoundNBT) inbt);
			else
				return null;
		}).filter(nbt -> nbt != null).collect(Collectors.toList()), isIncluded, author, ID);
	}

	public StructuresPacket(String name, List<StructureData> structures, boolean isIncluded, String author) {
		this(name, structures, isIncluded, author, IDcaller++);
	}

	public StructuresPacket(String name, List<StructureData> structures, boolean isIncluded, String author, int ID) {
		this.name = name;
		this.structures = structures;
		this.isIncluded = isIncluded;
		this.author = author;
		this.tempID = ID;
	}

	public static StructuresPacket fromCompound(CompoundNBT nbt, @Nullable File path, Mode mode) {
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
		String packet_name = mode == Mode.NETWORK || path == null ? nbt.getString("Packet Name") : path.getName();
		ListNBT structures = nbt.getList("Structures", 10);
		boolean is_included = nbt.getBoolean("Is Included");
		String author = nbt.getString("Author");
		int id = mode == Mode.NETWORK ? nbt.getInt("ID") : IDcaller++;
		StructuresPacket packet = new StructuresPacket(packet_name, structures, is_included, author, id);
		return packet;
	}

	public static CompoundNBT toCompound(StructuresPacket packet) {
		CompoundNBT compoundnbt = new CompoundNBT();
		compoundnbt.putInt("Packet Version", Utils.PACKET_VERSION);
		compoundnbt.putString("Packet Name", packet.getName());// Only for network
		ListNBT structures = new ListNBT();
		structures.addAll(packet.getStructures().stream().map(structure -> structure.write(new CompoundNBT()))
				.collect(Collectors.toList()));
		compoundnbt.put("Structures", structures);
		compoundnbt.putBoolean("Is Included", packet.isIncluded());
		compoundnbt.putString("Author", packet.getAuthor());
		compoundnbt.putInt("ID", packet.getTempID());// Only for network
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

	public String getAuthor() {
		return author;
	}

	public int getTempID() {
		return this.tempID;
	}

	@Override
	public int compareTo(StructuresPacket o) {
		return this.getName().compareTo(o.getName());
	}

	public StructuresPacket copy() {
		return StructuresPacket.fromCompound(StructuresPacket.toCompound(this), null, Mode.REFRESH);
	}

	public static enum Mode {
		NETWORK, DISK, REFRESH;
	}
}