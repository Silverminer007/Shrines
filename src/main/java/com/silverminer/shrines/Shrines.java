/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines;

import com.mojang.logging.LogUtils;
import com.silverminer.shrines.config.ShrinesConfig;
import com.silverminer.shrines.network.NetworkManager;
import com.silverminer.shrines.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;
import net.minecraftforge.network.NetworkConstants;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(value = Shrines.MODID)
public class Shrines {
   public static final String MODID = "shrines";
   public static final Logger LOGGER = LogUtils.getLogger();
   public static String VERSION = "N/A";

   /**
    * If you're looking for example code on how to make a structure Mod see
    * TelepathicGrunt's Example Mod on how to do that:
    * <a href="https://github.com/TelepathicGrunt/StructureTutorialMod">StructureTutorialMod</a>
    */
   public Shrines() {
      this.registerExtensionPoint();
      this.printShrinesVersion();
      this.registerRegistries();
      this.registerConfig();
      NetworkManager.registerPackets();
   }

   private void registerExtensionPoint() {
      ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
   }

   private void printShrinesVersion() {
      ModList.get().getModContainerById(Shrines.MODID)
            .ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());
      LOGGER.info("Shrines " + VERSION + " initialized");
   }

   private void registerConfig() {
      FileUtils.getOrCreateDirectory(FMLPaths.CONFIGDIR.get().resolve(MODID), MODID);
      ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ShrinesConfig.GENERAL_SPEC, MODID + "/settings.toml");
   }

   private void registerRegistries() {
      var modBus = FMLJavaModLoadingContext.get().getModEventBus();
      StructureTypeRegistry.STRUCTURES.register(modBus);
      SpawnCriteriaTypeRegistry.SPAWN_CRITERIA_TYPE_REGISTRY.register(modBus);
      StructureRegistry.REGISTRY.register(modBus);
      TemplatePoolRegistry.REGISTRY.register(modBus);
      StructureSetRegistry.REGISTRY.register(modBus);
      RandomVariationMaterialRegistry.REGISTRY.register(modBus);
      RandomVariationConfigRegistry.REGISTRY.register(modBus);
      StoryRegistry.REGISTRY.register(modBus);
      TriggerTypeRegistry.REGISTRY.register(modBus);
   }

   @NotNull
   public static ResourceLocation location(@NotNull String path) {
      return new ResourceLocation(Shrines.MODID, path);
   }
}