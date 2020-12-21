package com.silverminer.shrines.config;

import java.io.File;

import org.apache.commons.lang3.tuple.Pair;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.silverminer.shrines.Shrines;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

public class Config {

	private static final ForgeConfigSpec SERVER_CONFIG;
	public static final StructureConfig STRUCTURES;

	static {
		final Pair<StructureConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
				.configure(StructureConfig::new);
		SERVER_CONFIG = specPair.getRight();
		STRUCTURES = specPair.getLeft();
	}

	public static void loadConfig(ForgeConfigSpec config, String path) {
		final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).preserveInsertionOrder().sync()
				.autosave().writingMode(WritingMode.REPLACE).build();
		file.load();
		config.setConfig(file);
	}

	public static void register(final ModLoadingContext context) {
		context.registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
		Config.loadConfig(SERVER_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Shrines.MODID + "-server.toml").toString());
	}
}