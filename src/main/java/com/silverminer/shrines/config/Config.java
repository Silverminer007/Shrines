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
package com.silverminer.shrines.config;

import java.io.File;

import org.apache.commons.lang3.tuple.Pair;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.silverminer.shrines.ShrinesMod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

public class Config {
	public static final ForgeConfigSpec SERVER_SETTINGS_CONFIG;
	public static final ShrinesSettingsConfig SETTINGS;

	static {
		final Pair<ShrinesSettingsConfig, ForgeConfigSpec> settingsPair = new ForgeConfigSpec.Builder()
				.configure(ShrinesSettingsConfig::new);
		SERVER_SETTINGS_CONFIG = settingsPair.getRight();
		SETTINGS = settingsPair.getLeft();
	}

	public static void loadConfig(ForgeConfigSpec config, String path) {
		final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).preserveInsertionOrder().sync()
				.autosave().writingMode(WritingMode.REPLACE).build();
		file.load();
		config.setConfig(file);
	}

	public static void register(final ModLoadingContext context) {
		context.registerConfig(ModConfig.Type.COMMON, SERVER_SETTINGS_CONFIG);
		Config.loadConfig(SERVER_SETTINGS_CONFIG, FMLPaths.CONFIGDIR.get().resolve(ShrinesMod.MODID + "-common.toml").toString());
	}
}