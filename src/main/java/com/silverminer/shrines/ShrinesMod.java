/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines;

import com.mojang.logging.LogUtils;
import com.silverminer.shrines.registries.PlacementCalculatorTypeRegistry;
import com.silverminer.shrines.registries.SpawnCriteriaTypeRegistry;
import com.silverminer.shrines.registries.StructureInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import org.slf4j.Logger;

/**
 * @author Silverminer
 */
@Mod(value = ShrinesMod.MODID)
public class ShrinesMod {
   public static final String MODID = "shrines";
   public static final Logger LOGGER = LogUtils.getLogger();
   public static String VERSION = "N/A";

   /**
    * If you're looking for example code on how to make a structure Mod see
    * TelepathicGrunt's Example Mod on how to do that:
    * https://github.com/TelepathicGrunt/StructureTutorialMod
    * <p>
    * TODO Remove dragon heads from all shrines structures
    * TODO Re-add structure novels
    * TODO Re-add random variation
    * TODO Convert old structure packages to data-packs on first load
    */
   public ShrinesMod() {
      ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

      ModList.get().getModContainerById(ShrinesMod.MODID)
            .ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());
      LOGGER.info("Shrines " + VERSION + " initialized");

      var modBus = FMLJavaModLoadingContext.get().getModEventBus();
      StructureInit.STRUCTURES.register(modBus);
      SpawnCriteriaTypeRegistry.SPAWN_CRITERIA_TYPE_REGISTRY.register(modBus);
      PlacementCalculatorTypeRegistry.PLACEMENT_CALCULATOR_TYPE_DEFERRED_REGISTER.register(modBus);
   }

   public static ResourceLocation location(String path) {
      return new ResourceLocation(ShrinesMod.MODID, path);
   }
}