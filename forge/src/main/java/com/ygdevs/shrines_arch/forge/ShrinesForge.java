/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.forge;

import com.ygdevs.shrines_arch.Shrines;
import com.ygdevs.shrines_arch.commands.LocateInBiomeCommand;
import com.ygdevs.shrines_arch.commands.VariationCommand;
import com.ygdevs.shrines_arch.config.forge.ConfigImpl;
import com.ygdevs.shrines_arch.random_variation.RandomVariationConfig;
import com.ygdevs.shrines_arch.random_variation.RandomVariationMaterial;
import com.ygdevs.shrines_arch.registries.ConfiguredStructureFeatureRegistry;
import com.ygdevs.shrines_arch.forge.registry.Utils;
import dev.architectury.platform.forge.EventBuses;
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
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.registries.NewRegistryEvent;
import org.jetbrains.annotations.NotNull;

import static com.ygdevs.shrines_arch.Shrines.*;

/**
 * @author Silverminer
 */
@Mod(value = MODID)
public class ShrinesForge {

   public ShrinesForge() {
      EventBuses.registerModEventBus(MODID, FMLJavaModLoadingContext.get().getModEventBus());
      Shrines.init();
      this.registerExtensionPoint();
      this.printShrinesVersion();
      this.registerEvents();
      this.registerConfig();
   }

   private void registerExtensionPoint() {
      ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
   }

   private void printShrinesVersion() {
      ModList.get().getModContainerById(MODID)
            .ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());
      LOGGER.info("Shrines " + VERSION + " initialized");
   }

   private void registerConfig() {
      FileUtils.getOrCreateDirectory(FMLPaths.CONFIGDIR.get().resolve(MODID), MODID);
      ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigImpl.GENERAL_SPEC, MODID + "/settings.toml");
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
      modBus.addListener((FMLLoadCompleteEvent event) -> event.enqueueWork(Shrines::runUpdater));
      MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
         VariationCommand.register(event.getDispatcher());
         LocateInBiomeCommand.register(event.getDispatcher());
      });
      MinecraftForge.EVENT_BUS.addListener(ShrinesForge::registerStructureConversions);
   }

   private static void registerStructureConversions(@NotNull RegisterStructureConversionsEvent event) {
      event.register("shrines:ballon", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:balloon"));
      event.register("shrines:high_tempel", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:high_temple"));
      event.register("shrines:small_tempel", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:small_temple"));
      event.register("shrines:player_house", StructuresBecomeConfiguredFix.Conversion.trivial("shrines:tall_player_house"));
      for (String structure : ConfigImpl.removedStructures.get()) {
         try {
            event.register(structure, StructuresBecomeConfiguredFix.Conversion.trivial(ConfiguredStructureFeatureRegistry.DELETED_STRUCTURE.getId().toString()));
         } catch (NullPointerException | IllegalArgumentException e) {
            throw new RuntimeException("You've miss-configured shrines removed structures: " + e);
         }
      }
   }
}