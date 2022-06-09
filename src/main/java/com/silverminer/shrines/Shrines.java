/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines;

import com.mojang.logging.LogUtils;
import com.silverminer.shrines.commands.LocateInBiomeCommand;
import com.silverminer.shrines.commands.VariationCommand;
import com.silverminer.shrines.config.ShrinesConfig;
import com.silverminer.shrines.registries.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.datafix.fixes.StructuresBecomeConfiguredFix;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegisterStructureConversionsEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;
import net.minecraftforge.network.NetworkConstants;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

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

   private void registerEvents() {
      var modBus = FMLJavaModLoadingContext.get().getModEventBus();
      modBus.addListener((AddPackFindersEvent event) -> {
         PackSource source = PackSource.decorating("pack.source.shrines");
         event.addRepositorySource(new FolderRepositorySource(FMLPaths.GAMEDIR.get().resolve("datapacks").toFile(), source));
      });
      MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
         VariationCommand.register(event.getDispatcher());
         LocateInBiomeCommand.register(event.getDispatcher());
      });
      MinecraftForge.EVENT_BUS.addListener(Shrines::registerStructureConversions);
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
   }

   private static void registerStructureConversions(@NotNull RegisterStructureConversionsEvent event) {
      event.register("shrines:ballon", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:balloon"));
      event.register("shrines:high_tempel", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:high_temple"));
      event.register("shrines:small_tempel", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:small_temple"));
      event.register("shrines:player_house", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:tall_player_house"));
      for (String structure : ShrinesConfig.removedStructures.get()) {
         try {
            event.register(structure, StructuresBecomeConfiguredFix.Conversion.trivial(StructureRegistry.DELETED_STRUCTURE.getId().toString()));
         } catch (NullPointerException | IllegalArgumentException e) {
            throw new RuntimeException("You've miss-configured shrines removed structures: " + e);
         }
      }
   }

   @NotNull
   public static ResourceLocation location(@NotNull String path) {
      return new ResourceLocation(Shrines.MODID, path);
   }
}