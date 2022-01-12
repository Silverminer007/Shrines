/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.StructureLoadUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * @author Silverminer
 */
@Mod(value = ShrinesMod.MODID)
public class ShrinesMod {
   public static final String MODID = "shrines";
   public static final Logger LOGGER = LogManager.getLogger(ShrinesMod.class);
   public static String VERSION = "N/A";

   /**
    * If you're looking for example code on how to make a structure Mod see
    * TelepathicGrunt's Example Mod on how to do that:
    * https://github.com/TelepathicGrunt/StructureTutorialMod
    * <p>
    * TODO 2.0.1 Use processors to perform Color Structure Piece's work
    * <p>
    * TODO 3.0.0 Mc1.17 Update -> Move #isAir to state only version
    * <p>
    * <p>
    * TODO 2.0.0 Test Caves & Cliffs Backport combat
    * <p>
    * TODO Fix screenshots for Novels Screen -> size
    * <p>
    * FIXME #22 https://github.com/Silverminer007/Shrines/issues/22
    * <p>
    * FIXME Add missing novels @pea_sh0ter
    * <p>
    * Releases: - 2.0.0 Bug fix update - 3.0.0 Mc1.17 Update - 3.0.1 Bugfixes of
    * 3.0.0 and some new features
    * <p>
    * NOTE: Test command: /execute positioned ~10000 ~ ~ run locate
    */
   public ShrinesMod() {
      ModList.get().getModContainerById(ShrinesMod.MODID)
            .ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());
      LOGGER.info("Shrines " + VERSION + " initialized");

      ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
            () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
      StructureLoadUtils.loadStructures();
      registerConfig();
   }

   public static void registerConfig() {
      // Config
      Config.register(ModLoadingContext.get());
      // Setup config UI
      DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
            () -> ClientUtils::getConfigGui));

   }

   public static File getMinecraftDirectory() {
      return FMLPaths.GAMEDIR.get().toFile();
   }
}