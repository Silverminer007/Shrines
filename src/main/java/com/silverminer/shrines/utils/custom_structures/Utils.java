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
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.network.CCustomStructuresPacket;
import com.silverminer.shrines.utils.network.IPacket;
import com.silverminer.shrines.utils.network.SCustomStructuresPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.saves.BoundSaveData;

public class Utils {
	protected static final Logger LOGGER = LogManager.getLogger(Utils.class);
	/**
	 * Logical Side: SERVER Never use on client! They are not sync
	 */
	private static ArrayList<String> customsToDelete = new ArrayList<String>();
	/**
	 * Logical Side: SERVER Never use on client! They are not sync
	 */
	private static ArrayList<CustomStructureData> customsStructs = new ArrayList<CustomStructureData>();

	/**
	 * Logical Side: CLIENT Never use on server! They are not sync
	 */
	private static ArrayList<CustomStructureData> DATAS_FROM_SERVER = Lists.newArrayList();

	private static boolean send = true;

	public static CustomStructureProperties properties;

	public static BoundSaveData boundDataSave;

	public static File getSaveLocation() {
		return FileUtils.getFile(ShrinesMod.getInstance().getProxy().getBaseDir(), "shrines-saves");
	}

	public static File getLocationOf(String structure) {
		return FileUtils.getFile(ShrinesMod.getInstance().getProxy().getBaseDir(), "shrines-saves", "shrines",
				structure);
	}

	public static ArrayList<CustomStructureData> getStructures(boolean server) {
		if (server) {
			return Utils.customsStructs;
		} else {
			return Utils.DATAS_FROM_SERVER;
		}
	}

	public static void setStructures(ArrayList<CustomStructureData> structures, boolean server) {
		if (server) {
			Utils.customsStructs = structures;
		} else {
			Utils.DATAS_FROM_SERVER = structures;
		}
	}

	public static boolean addStructure(CustomStructureData csd, boolean server) {
		if (getData(csd.getName(), server) != null) {
			return false;
		} else {
			boolean flag;
			if (server) {
				flag = Utils.customsStructs.add(csd);
			} else {
				flag = Utils.DATAS_FROM_SERVER.add(csd);
			}
			Utils.onChanged(server);
			return flag;
		}
	}

	public static boolean remove(String name, boolean delete, boolean server) {
		return Utils.remove(Utils.getData(name, server), delete, server);
	}

	public static boolean remove(CustomStructureData structure, boolean delete, boolean server) {
		if (structure == null) {
			return false;
		}
		if (delete)
			Utils.customsToDelete.add(structure.getName());
		boolean flag;
		if (server) {
			flag = Utils.customsStructs.remove(structure);
		} else {
			flag = Utils.DATAS_FROM_SERVER.remove(structure);
		}
		Utils.onChanged(server);
		return flag;
	}

	public static void loadCustomStructures() {
		try {
			File f = new File(ShrinesMod.getInstance().getProxy().getBaseDir(), "shrines-saves").getCanonicalFile();
			if (!f.exists())
				f.mkdirs();

			// PROPERTIES START
			File properties = new File(f, "shrines.properties");
			if (!properties.exists())
				properties.createNewFile();
			Utils.properties = CustomStructureProperties.load(properties.toPath());
			if (Utils.properties != null) {
				Utils.properties.save();
			}
			// PROPERTIES END

			File structures = new File(f, "structures.txt");
			if (!structures.exists())
				structures.createNewFile();
			List<String> names = Files.readAllLines(structures.toPath());
			Random rand = new Random();
			for (String n : names) {
				if (n.startsWith("#"))
					continue;
				File st = new File(f, "shrines");
				st = new File(st, n);
				if (!st.isDirectory()) {
					st.mkdirs();
				}
				st = new File(st, n + ".txt");
				CustomStructureData csd = new CustomStructureData(n, rand);
				if (!st.exists()) {
					st.createNewFile();
					FileWriter fw = new FileWriter(st);
					fw.write(csd.toString().replaceAll(";", ";\n"));
					fw.close();
				}
				String data = "";
				for (String s : Files.readAllLines(st.toPath())) {
					data += s + "\n";
				}
				csd.fromString(data);
				Utils.customsStructs.add(csd);
				if (Config.SETTINGS.ADVANCED_LOGGING.get())
					LOGGER.info("Read config of [{}] from file: {}", n, csd.toString());
			}
			if (Config.SETTINGS.ADVANCED_LOGGING.get())
				LOGGER.info("Read structures from: {}", f);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void saveStructures() {
		File path = ShrinesMod.getInstance().getProxy().getBaseDir();
		if (Utils.properties != null) {
			Utils.properties.save();
		}
		try {
			path = new File(path, "shrines-saves").getCanonicalFile();
			if (Config.SETTINGS.ADVANCED_LOGGING.get())
				LOGGER.info("Saving config options on path: {}", path);
			if (!path.exists())
				path.mkdirs();
			File structures = new File(path, "structures.txt");
			if (!structures.exists()) {
				structures.createNewFile();
			}
			for (String key : Utils.customsToDelete) {
				File st = new File(path, "shrines");
				st = new File(st, key);
				if (!st.isDirectory()) {
					continue;
				}
				for (File f : st.listFiles()) {
					f.delete();
				}
				st.delete();
				if (Config.SETTINGS.ADVANCED_LOGGING.get())
					LOGGER.info("Deleted {} from disk", st);
			}
			FileWriter fw = new FileWriter(structures);
			for (CustomStructureData data : Utils.customsStructs) {
				String key = data.getName();
				if (Config.SETTINGS.ADVANCED_LOGGING.get())
					LOGGER.info("Writing config options of custom structure with name {}", key);
				fw.write(key + "\n");
				File st = new File(path, "shrines");
				st = new File(st, key);
				if (!st.isDirectory()) {
					st.mkdirs();
				}
				st = new File(st, key + ".txt");
				if (!st.exists()) {
					st.createNewFile();
				}
				FileWriter cfw = new FileWriter(st);
				cfw.write(data.toStringReadAble());
				cfw.close();
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static CustomStructureData getData(String name, boolean server) {
		if (server) {
			for (CustomStructureData csd : Utils.customsStructs) {
				if (csd.getName().equals(name)) {
					return csd;
				}
			}
			return null;
		} else {
			for (CustomStructureData csd : Utils.DATAS_FROM_SERVER) {
				if (csd.getName().equals(name)) {
					return csd;
				}
			}
			return null;
		}
	}

	public static boolean replace(CustomStructureData csd, boolean server) {
		CustomStructureData oldData = getData(csd.getName(), server);
		if (oldData == null) {
			return false;
		}
		if (server) {
			int idx = Utils.customsStructs.indexOf(oldData);
			Utils.customsStructs.set(idx, csd);
		} else {
			int idx = Utils.DATAS_FROM_SERVER.indexOf(oldData);
			Utils.DATAS_FROM_SERVER.set(idx, csd);
		}
		Utils.onChanged(server);
		return true;
	}

	public static void onChanged(boolean server) {
		if (!send) {
			return;
		}
		if (server) {
			sendToClients();
		} else {
			sendToServer();
		}
	}

	public static void setSend(boolean send) {
		Utils.send = send;
	}

	private static void sendToClients() {
		ShrinesPacketHandler.sendToAll(toPacket(false));
	}

	private static void sendToServer() {
		ShrinesPacketHandler.sendToServer(toPacket(true));
	}

	private static IPacket toPacket(boolean server) {
		if (server)
			return new CCustomStructuresPacket(Utils.DATAS_FROM_SERVER);
		else
			return new SCustomStructuresPacket(Utils.customsStructs);
	}
}