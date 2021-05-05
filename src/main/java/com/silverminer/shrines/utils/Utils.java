package com.silverminer.shrines.utils;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.structures.custom.helper.CustomStructureData;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class Utils {
	public static final Logger LOGGER = LogManager.getLogger(Utils.class);
	public static ArrayList<String> customsToDelete = new ArrayList<String>();
	public static ArrayList<CustomStructureData> customsStructs = new ArrayList<CustomStructureData>();
	
	public static void loadCustomStructures() {
		try {
			DistExecutor.SafeSupplier<String> client = new DistExecutor.SafeSupplier<String>() {
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("resource")
				@Override
				public String get() {
					return Minecraft.getInstance().gameDirectory.getAbsolutePath();
				}
			};
			DistExecutor.SafeSupplier<String> server = new DistExecutor.SafeSupplier<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String get() {
					return ((MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER)).getFile("")
							.getAbsolutePath();
				}
			};
			String path = DistExecutor.safeRunForDist(() -> client, () -> server);
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
				for(String s : Files.readAllLines(st.toPath())) {
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