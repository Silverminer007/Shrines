/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.client.gui.config.options;

import java.util.List;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Silverminer
 */
public class SettingsBlacklistScreen extends StructureListOptionScreen {

   /**
    * @param parent
    * @param title
    * @param possibleValues
    * @param activeValues
    * @param option
    */
   public SettingsBlacklistScreen(Screen parent, ITextComponent title,
                                  List<? extends String> activeValues) {
      super(parent, title, CustomStructureData.getPossibleValuesForKey("blacklist"), activeValues, "blacklist");
   }

   @Override
   protected void save() {
      Config.SETTINGS.BLACKLISTED_BIOMES.set(activeValues);
      Config.SETTINGS.BLACKLISTED_BIOMES.save();
   }

}