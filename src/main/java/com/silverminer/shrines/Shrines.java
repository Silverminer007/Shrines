package com.silverminer.shrines;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.StructureInit;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;

@Mod(value = Shrines.MODID)
public class Shrines {
	public static final String MODID = "shrines";

	public static final Logger LOGGER = LogManager.getLogger(Shrines.class);
	public static final boolean USECUSTOMSTRUCTURES = true;

	public static LinkedHashMap<String, List<String>> customStructures = new LinkedHashMap<String, List<String>>();
	public static ArrayList<String> customsToDelete = new ArrayList<String>();

	public Shrines() {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
				() -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		StructureInit.STRUCTURES.register(modEventBus);

		// Config
		Config.register(ModLoadingContext.get());
		if (Shrines.USECUSTOMSTRUCTURES)
			loadCustomStructures();
	}

	public void loadCustomStructures() {
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
			for (String n : names) {
				if (n.startsWith("#"))
					continue;
				File st = new File(f, "shrines");
				st = new File(st, n);
				if (!st.isDirectory()) {
					st.mkdirs();
				}
				st = new File(st, n + ".txt");
				if (!st.exists()) {
					st.createNewFile();
					FileWriter fw = new FileWriter(st);
					for (String s : getDefaultConfig(n)) {
						fw.write(s + "\n");
					}
					fw.close();
				}
				List<String> config = Files.readAllLines(st.toPath());
				customStructures.put(n, config);
			}
			LOGGER.info("Read structures from: {}", f);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static final ArrayList<String> defaultConfig = new ArrayList<String>();

	public static ArrayList<String> getDefaultConfig(String name) {
		return getDefaultConfig(name, new Random().nextInt(Integer.MAX_VALUE));
	}

	public static ArrayList<String> getDefaultConfig(String name, int seed) {
		if (defaultConfig.isEmpty()) {
			defaultConfig.add("Seed:" + String.valueOf(seed));
			defaultConfig.add("Generate:true");
			defaultConfig.add("Spawn Chance:0.6");
			defaultConfig.add("Needs Ground:true");
			defaultConfig.add("Distance:50");
			defaultConfig.add("Seperation:8");
			defaultConfig.add(
					"#Put in here all biomes that you want your structure to spawn in. DEFAULT and ALL are also supported; Sepperate them by \",\"");
			defaultConfig.add("Biome Categories:[DEFAULT]");
			defaultConfig
					.add("#Put here all biomes that you want your structure NOT to spawn in. Use minecraft name keys");
			defaultConfig.add("Biome Blacklist:[]");
			defaultConfig.add("Use Random Varianting:true");
			defaultConfig.add("Pieces:");
			defaultConfig.add("pieces");//Only place holder for later default piece
		}
		defaultConfig.set(defaultConfig.size() - 1, "[" + name + ", [0,0,0]]");
		return defaultConfig;
	}
}