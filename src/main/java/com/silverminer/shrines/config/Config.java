/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.utils.ClientUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;

public class Config {
   public static final ForgeConfigSpec SERVER_SETTINGS_CONFIG;
   public static final ShrinesSettingsConfig SETTINGS;

   static {
      final Pair<ShrinesSettingsConfig, ForgeConfigSpec> settingsPair = new ForgeConfigSpec.Builder()
            .configure(ShrinesSettingsConfig::new);
      SERVER_SETTINGS_CONFIG = settingsPair.getRight();
      SETTINGS = settingsPair.getLeft();
   }

   public static void register(final ModLoadingContext context) {
      context.registerConfig(ModConfig.Type.COMMON, SERVER_SETTINGS_CONFIG);
      Config.loadConfig(SERVER_SETTINGS_CONFIG, FMLPaths.CONFIGDIR.get().resolve(ShrinesMod.MODID + "-common.toml").toString());
      // Config UI Accessible trough forges mods overview
      DistExecutor.safeRunWhenOn(Dist.CLIENT, Config::registerConfigGuiHandler);
   }

   public static void loadConfig(ForgeConfigSpec config, String path) {
      final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).preserveInsertionOrder().sync()
            .autosave().writingMode(WritingMode.REPLACE).build();
      file.load();
      config.setConfig(file);
   }

   private static DistExecutor.SafeRunnable registerConfigGuiHandler() {
      return new DistExecutor.SafeRunnable() {

         private static final long serialVersionUID = 1L;

         @Override
         public void run() {
            ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                  () -> new ConfigGuiHandler.ConfigGuiFactory(ClientUtils::getConfigGui));
         }
      };
   }
}