/**
 * Silverminer (and Team)
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * 
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.utils.custom_structures;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.new_custom_structures.StructureData;
import com.silverminer.shrines.new_custom_structures.StructuresPacket;
import com.silverminer.shrines.new_custom_structures.StructuresPacket.Mode;
import com.silverminer.shrines.structures.DefaultStructureConfig;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.FileUtil;

public class Utils {
	protected static final Logger LOGGER = LogManager.getLogger(Utils.class);

	public static final int PACKET_VERSION = 1;

	public static ImmutableList<StructuresPacket> STRUCTURE_PACKETS;

	public static File getSaveLocation() {
		return FileUtils.getFile(ShrinesMod.getMinecraftDirectory(), "shrines-saves");
	}

	/**
	 * Read here the structure of the structure packets from disk. Structure is as
	 * follows:
	 * 
	 * .minecraft/shrines-saves/StructurePakets |-> <Name of Packet> |-> | ->
	 * Structures.nbt // This file contains any necessary information about the
	 * Packet. This file allows the mod to detect the LOGGER.error("Couldn't load
	 * structure from {}", path, ioexception);directory as packet. |-> | -> | ->
	 * List of all structures // Based on this list, the structure are initialized
	 * |-> | -> | -> | -> <List of all options of this structure with values and
	 * comments> |-> | -> <Folder for each structure in the structures list above>
	 * |-> | -> | -> <Subfolders for further data like loot tables or stuff>
	 * 
	 * .minecraft/shrines-saves/StructureDataPakets // First of all a placeholder
	 * for more data about the structures that should be loaded as datapack, so they
	 * could be reloaded during runtime. Here is the place for .nbt files and
	 * template pools and loot tables
	 */
	public static void loadStructures(boolean initialLoad) {
		try {
			ArrayList<StructuresPacket> structure_packets = Lists.newArrayList();

			File shrines_saves = new File(ShrinesMod.getMinecraftDirectory(), "shrines-saves").getCanonicalFile();
			if (!shrines_saves.exists()) {
				shrines_saves.mkdirs();
			}

			// Get the list of all directories in shrines saves folder as possible structure
			// packets
			List<File> files_in_shrines_saves = Arrays.asList(shrines_saves.listFiles()).stream()
					.filter(file -> file.isDirectory()).collect(Collectors.toList());
			for (File p_packet : files_in_shrines_saves) {
				File structures_file = new File(p_packet, "structures.nbt");
				if (!structures_file.exists()) {
					continue;
				}
				// Read the file here in CompoundNBT tagfalse
				CompoundNBT data_structure = readNBTFile(structures_file);
				// Create an instance of a packet datastructure and write it to an read/write
				// array
				StructuresPacket packet = StructuresPacket.fromCompound(data_structure, p_packet,
						initialLoad ? Mode.DISK : Mode.REFRESH);
				if (packet == null) {
					continue;
				}
				structure_packets.add(packet);
			}

			// Initialize here all datapacks that should be loaded later when worlds starts

			// Check if the included structures are already loaded or need to be defaulted
			boolean has_included_structures = false;
			for (StructuresPacket packet : structure_packets) {
				if (packet.isIncluded()) {
					has_included_structures = true;
					break;
				}
			}
			if (!has_included_structures && initialLoad) {
				structure_packets.add(Utils.getIncludedStructures());
			}
			// Save Structure Packets for later use in immutable list
			STRUCTURE_PACKETS = ImmutableList.copyOf(structure_packets);
			Utils.saveStructures(true);

			HashMap<String, StructuresPacket> temp = Maps.newHashMap();
			// Check for duplicated structure names and warn/stop loading the structure and
			// open GUI after start
			for (StructuresPacket packet : Utils.STRUCTURE_PACKETS) {
				String packet_name = packet.getName();
				for (StructureData data : packet.getStructures()) {
					String key = data.getKey();
					if (temp.containsKey(key)) {
						// TODO Make use of successful when GUIs are ready and throw user in GUI as soon
						// as Minecraft is ready to do so
						LOGGER.error(
								"A conflict was detected when loading structures. There are two or more structures with the same name. Key [{}], Packet 1 [{}], Packet 2 [{}]",
								key, packet_name, temp.get(key).getName());
						data.successful = false;
					}
					temp.put(key, packet);
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	protected static String getSavePath(String name) {
		String resultName = "";
		resultName = name.trim();
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
		StructuresPacket included_packet = new StructuresPacket("Included Structures", structures, true, "Silverm7ner");
		return included_packet;
	}

	public static void warnInvalidStructureFile(File packet) {
		LOGGER.info("Unable to load Structure Packet, because the structure of structures.nbt is wrong. Packet: {}",
				packet);
	}

	private static CompoundNBT readNBTFile(File path) {
		try (InputStream inputstream = new FileInputStream(path)) {
			return CompressedStreamTools.readCompressed(inputstream);
		} catch (FileNotFoundException filenotfoundexception) {
			return null;
		} catch (IOException ioexception) {
			return null;
		}
	}

	public static void saveStructures(boolean initialSave) {
		saveStructures(initialSave, -1, null);
	}

	public static void saveStructures(boolean initialSave, StructuresPacket newPacket) {
		saveStructures(initialSave, -1, newPacket);
	}

	public static void saveStructures(boolean initialSave, int IDtoDelete) {
		saveStructures(initialSave, IDtoDelete, null);
	}

	public static void saveStructures(boolean initialSave, int IDtoDelete, StructuresPacket newPacket) {
		try {
			File shrines_saves = new File(ShrinesMod.getMinecraftDirectory(), "shrines-saves").getCanonicalFile();
			if (!shrines_saves.exists()) {
				shrines_saves.mkdirs();
			}

			ArrayList<File> usedPath = Lists.newArrayList();
			for (StructuresPacket packet : Utils.STRUCTURE_PACKETS) {
				if (IDtoDelete != packet.getTempID()) {
					usedPath.add(savePacket(shrines_saves, packet, usedPath));
				} else {
					try {
						File packet_path = new File(shrines_saves, packet.getName());
						File[] subFiles = packet_path.listFiles();
						for (File subFile : subFiles) {
							subFile.delete();
						}
						packet_path.delete();
					} catch (Throwable e) {
						LOGGER.warn("Failed to delete Structure Package {}", packet.getName());
					}
				}
			}
			if (newPacket != null) {
				savePacket(shrines_saves, newPacket, usedPath);
			}
			if (!initialSave) {
				loadStructures(false);
			}
		} catch (Throwable t) {

		}
	}

	public static File savePacket(File shrines_saves, StructuresPacket packet, ArrayList<File> usedPath) {
		File packet_path = new File(shrines_saves, packet.getName());
		if (usedPath.contains(packet_path)) {
			packet_path = new File(shrines_saves, getSavePath(packet_path.getName()));
		}
		if (!packet_path.exists()) {
			packet_path.mkdirs();
		}
		File structures_file = new File(packet_path, "structures.nbt");

		CompoundNBT compoundnbt = StructuresPacket.toCompound(packet);
		try (OutputStream outputstream = new FileOutputStream(structures_file)) {
			CompressedStreamTools.writeCompressed(compoundnbt, outputstream);
		} catch (Throwable throwable) {
			LOGGER.error("Failed to save structures packet [{}] on path {}, {}", packet.getName(), structures_file,
					throwable);
		}
		return packet_path;
	}
}