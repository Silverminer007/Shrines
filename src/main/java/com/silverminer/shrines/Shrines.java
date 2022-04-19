/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines;

import com.mojang.logging.LogUtils;
import com.silverminer.shrines.commands.VariationCommand;
import com.silverminer.shrines.config.ShrinesConfig;
import com.silverminer.shrines.random_variation.RandomVariationConfig;
import com.silverminer.shrines.random_variation.RandomVariationMaterial;
import com.silverminer.shrines.registries.RandomVariationConfigRegistry;
import com.silverminer.shrines.registries.RandomVariationMaterialRegistry;
import com.silverminer.shrines.registries.*;
import com.silverminer.shrines.registry.Utils;
import com.silverminer.shrines.update.Updater;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.datafix.fixes.StructuresBecomeConfiguredFix;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegisterStructureConversionsEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.registries.NewRegistryEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * @author Silverminer
 */
@Mod(value = Shrines.MODID)
public class Shrines {
   public static final String MODID = "shrines";
   public static final Logger LOGGER = LogUtils.getLogger();
   public static String VERSION = "N/A";

   /**
    * If you're looking for example code on how to make a structure Mod see
    * TelepathicGrunt's Example Mod on how to do that:
    * https://github.com/TelepathicGrunt/StructureTutorialMod
    */
   public Shrines() {
      this.registerExtensionPoint();
      this.printShrinesVersion();
      this.registerEvents();
      this.registerRegistries();
      this.registerConfig();
      this.runUpdater();
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

   private void runUpdater() {
      Path minecraftDir = FMLPaths.GAMEDIR.get();
      Path oldPackDir = minecraftDir.resolve("shrines-data").resolve("3.x.x").resolve("Packets");
      Path newPackDir = minecraftDir.resolve("datapacks");
      if (Files.isDirectory(oldPackDir) && !Files.exists(newPackDir)) {
         Updater.updateAll(oldPackDir, newPackDir);
      }
   }

   private void registerEvents() {
      var modBus = FMLJavaModLoadingContext.get().getModEventBus();
      modBus.addListener((NewRegistryEvent event) -> {
         Utils.createRegistry(RandomVariationMaterial.REGISTRY, RandomVariationMaterial.DIRECT_CODEC);
         Utils.createRegistry(RandomVariationConfig.REGISTRY, RandomVariationConfig.DIRECT_CODEC);
      });
      modBus.addListener((AddPackFindersEvent event) -> {
         PackSource source = PackSource.decorating("pack.source.shrines");
         event.addRepositorySource(new FolderRepositorySource(FMLPaths.GAMEDIR.get().resolve("datapacks").toFile(), source));
      });
      MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> VariationCommand.register(event.getDispatcher()));
      MinecraftForge.EVENT_BUS.addListener(Shrines::registerStructureConversions);
   }

   private void registerRegistries() {
      var modBus = FMLJavaModLoadingContext.get().getModEventBus();
      StructureInit.STRUCTURES.register(modBus);
      SpawnCriteriaTypeRegistry.SPAWN_CRITERIA_TYPE_REGISTRY.register(modBus);
      PlacementCalculatorTypeRegistry.PLACEMENT_CALCULATOR_TYPE_DEFERRED_REGISTER.register(modBus);
      ConfiguredStructureFeatureRegistry.REGISTRY.register(modBus);
      TemplatePoolRegistry.REGISTRY.register(modBus);
      StructureSetRegistry.REGISTRY.register(modBus);
      RandomVariationMaterialRegistry.REGISTRY.register();
      RandomVariationConfigRegistry.REGISTRY.register();
   }

   private static void registerStructureConversions(@NotNull RegisterStructureConversionsEvent event) {
      event.register("shrines:ballon", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:balloon"));
      event.register("shrines:high_tempel", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:high_temple"));
      event.register("shrines:small_tempel", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:small_temple"));
      for (String structure : ShrinesConfig.removedStructures.get()) {
         try {
            event.register(structure, StructuresBecomeConfiguredFix.Conversion.trivial(ConfiguredStructureFeatureRegistry.DELETED_STRUCTURE.getId().toString()));
         } catch(NullPointerException | IllegalArgumentException e) {
            throw new RuntimeException("You've miss-configured shrines removed structures: " + e);
         }
      }
   }

   @NotNull
   public static ResourceLocation location(@NotNull String path) {
      return new ResourceLocation(Shrines.MODID, path);
   }
}