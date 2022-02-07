/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines;

import org.apache.commons.lang3.tuple.Pair;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.utils.client.ClientUtils;
import com.silverminer.shrines.utils.functions.ForgeFunctionProvider;
import com.silverminer.shrines.utils.proxy.ClientProxy;
import com.silverminer.shrines.utils.proxy.ForgeServerProxy;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;

@Mod(value = ShrinesMod.MODID)
public class ForgeShrines extends ShrinesMod {

    public ForgeShrines() {
        super();
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
                () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    @Override
    public void registerConfig() {
        // Make sure structures are initialized before config will be loaded
        NewStructureInit.initStructures();
        // Config
        Config.register(ModLoadingContext.get());
        // Setup config UI
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
                    () -> ClientUtils::getConfigGui);
        });
    }

    @Override
    public void setProxy() {
        this.proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ForgeServerProxy::new);
    }

    @Override
    public void setFunctionProvider() {
        this.functionProvider = new ForgeFunctionProvider();
    }
}