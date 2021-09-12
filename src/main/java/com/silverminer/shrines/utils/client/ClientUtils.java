package com.silverminer.shrines.utils.client;

import com.google.common.collect.Lists;
import com.silverminer.shrines.client.gui.config.legacy.settings.GeneralSettingsScreen;
import com.silverminer.shrines.config.Config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;

public class ClientUtils {
	/**
	 * This holds screen open keybind. It's initialised in ClientEvents (FMLClientSetupEvent) and used in Key Pressed Event. Allows key rebinds
	 */
	public static KeyBinding structuresScreen;

	public static Screen getConfigGui(Minecraft mc, Screen parent) {
		return new GeneralSettingsScreen(parent, Lists.newArrayList(Config.SERVER_SETTINGS_CONFIG));
	}
}