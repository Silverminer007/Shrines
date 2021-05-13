package com.silverminer.shrines.utils;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.structures.custom.helper.ResourceData;

public class Utils {
	public static final Logger LOGGER = LogManager.getLogger(Utils.class);
	/**
	 * Logical Side: SERVER Never use on client! They are not sync
	 */
	public static ArrayList<String> customsToDelete = new ArrayList<String>();
	/**
	 * Logical Side: SERVER Never use on client! They are not sync
	 */
	public static ArrayList<CustomStructureData> customsStructs = new ArrayList<CustomStructureData>();

	/**
	 * Logical Side: CLIENT Never use on server! They are not sync
	 */
	public static final HashMap<String, ArrayList<ResourceData>> BOUNDS_TO_DRAW = Maps.newHashMap();

	public static File getLocationOf(String structure_name) {
		return FileUtils.getFile(Shrines.proxy.getBaseDir(), "shrines-saves", structure_name);
	}

	public static void loadCustomStructures() {
		try {
			String path = Shrines.proxy.getBaseDir().getAbsolutePath();
			File f = new File(path, "shrines-saves").getCanonicalFile();
			if (!f.exists())
				f.mkdirs();
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
				LOGGER.info("Read config of [{}] from file: {}", n, csd.toString());
			}
			LOGGER.info("Read structures from: {}", f);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static CustomStructureData getData(String name) {
		for (CustomStructureData csd : Utils.customsStructs) {
			if (csd.getName().equals(name)) {
				return csd;
			}
		}
		return null;
	}
}