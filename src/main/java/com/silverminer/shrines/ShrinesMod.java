/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.DynamicRegistriesRegistry;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Silverminer
 */
@Mod(value = ShrinesMod.MODID)
public class ShrinesMod {
    public static final String MODID = "shrines";
    public static final Logger LOGGER = LogManager.getLogger(ShrinesMod.class);
    public static final String WIKI_LINK = "https://silverminer007.github.io/ShrinesWiki/";
    public static final boolean debug = false;
    public static String VERSION = "N/A";

    /**
     * If you're looking for example code on how to make a structure Mod see
     * TelepathicGrunt's Example Mod on how to do that:
     * https://github.com/TelepathicGrunt/StructureTutorialMod
     */
    public ShrinesMod() {
        ModList.get().getModContainerById(ShrinesMod.MODID)
                .ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());
        LOGGER.info("Shrines " + VERSION + " initialized");
        Config.register(ModLoadingContext.get());
        DynamicRegistriesRegistry.NOVELS.register(FMLJavaModLoadingContext.get().getModEventBus());
        DynamicRegistriesRegistry.VARIATION_MATERIAL.register(FMLJavaModLoadingContext.get().getModEventBus());
        DynamicRegistriesRegistry.VARIATION_CONFIGURATION.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}