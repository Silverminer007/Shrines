/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.utils.client;

import com.silverminer.shrines.client.gui.config.ShrinesStructuresScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;

/**
 * @author Silverminer
 */
public class ClientUtils {
   /**
    * This holds screen open keybind. It's initialised in ClientEvents (FMLClientSetupEvent) and used in Key Pressed Event. Allows key rebinds
    */
   public static KeyBinding structuresScreen;

   public static Screen getConfigGui(Minecraft mc, Screen parent) {
      return new ShrinesStructuresScreen(parent);
   }
}