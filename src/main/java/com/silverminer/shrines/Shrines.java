/*
 * Silverminer007
 * Copyright (c) 2023.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;
import org.slf4j.Logger;

@Mod(value = Shrines.MODID)
public class Shrines {
   public static final String MODID = "shrines";
   public static final Logger LOGGER = LogUtils.getLogger();
   public static String VERSION = "N/A";

   /**
    * If you're looking for example code on how to make a structure Mod see
    * TelepathicGrunt's Example Mod on how to do that:
    * <a href="https://github.com/TelepathicGrunt/StructureTutorialMod">...</a>
    */
   public Shrines() {
      this.registerExtensionPoint();
      this.printShrinesVersion();
   }

   private void registerExtensionPoint() {
      ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
   }

   private void printShrinesVersion() {
      ModList.get().getModContainerById(Shrines.MODID)
            .ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());
      LOGGER.info("Shrines " + VERSION + " initialized");
   }
}