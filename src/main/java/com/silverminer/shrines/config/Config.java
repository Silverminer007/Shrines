/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
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

   public static final ForgeConfigSpec SERVER_STRUCTURES_CONFIG;
   public static final StructureConfig STRUCTURES;
   public static final ForgeConfigSpec SERVER_SETTINGS_CONFIG;
   public static final ShrinesSettingsConfig SETTINGS;

   static {
      final Pair<StructureConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
            .configure(StructureConfig::new);
      final Pair<ShrinesSettingsConfig, ForgeConfigSpec> settingsPair = new ForgeConfigSpec.Builder()
            .configure(ShrinesSettingsConfig::new);
      SERVER_STRUCTURES_CONFIG = specPair.getRight();
      STRUCTURES = specPair.getLeft();
      SERVER_SETTINGS_CONFIG = settingsPair.getRight();
      SETTINGS = settingsPair.getLeft();
   }

   public static void register(final ModLoadingContext context) {
      context.registerConfig(ModConfig.Type.SERVER, SERVER_STRUCTURES_CONFIG);
      Config.loadConfig(SERVER_STRUCTURES_CONFIG, FMLPaths.CONFIGDIR.get().resolve(ShrinesMod.MODID + "-server.toml").toString());
      context.registerConfig(ModConfig.Type.COMMON, SERVER_SETTINGS_CONFIG);
      Config.loadConfig(SERVER_SETTINGS_CONFIG, FMLPaths.CONFIGDIR.get().resolve(ShrinesMod.MODID + "-common.toml").toString());
   }

   public static void loadConfig(ForgeConfigSpec config, String path) {
      final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).preserveInsertionOrder().sync()
            .autosave().writingMode(WritingMode.REPLACE).build();
      file.load();
      config.setConfig(file);
   }
}