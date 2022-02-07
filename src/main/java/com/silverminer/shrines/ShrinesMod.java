/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.utils.custom_structures.Utils;
import com.silverminer.shrines.utils.functions.IFunctionProvider;
import com.silverminer.shrines.utils.proxy.IProxy;

/**
 * @author Silverminer
 */
public abstract class ShrinesMod {
    public static final String MODID = "shrines";
    public static final Logger LOGGER = LogManager.getLogger(ShrinesMod.class);

    protected static ShrinesMod instance;

    protected IProxy proxy;
    protected IFunctionProvider functionProvider;

    /**
     * TODO 1.8.2 Change structure system to jigsaw (Aurelj)
     * TODO 1.8.2 Structures generation height fix -> InteJason -> All structures
     * TODO 1.8.2 Move color structure piece from superclass to object for support of multiple pieces having the same materials
     * TODO 1.8.2 #8
     * <p>
     * TODO 2.0.0 Mc1.17 Update -> Change names of structures -> Move #isAir to state only version
     * TODO 2.0.1 Preview for load
     * TODO 2.0.1 Improve spacing of structure (make check for other structures) -> #3
     * <p>
     * <p>
     * Releases:
     * - 1.8.2 27.06.2021
     * - 2.0.0 As fast as possible after Forge update
     * - 2.0.1 Unknown
     * <p>
     * NOTE: Test command: /execute positioned ~10000 ~ ~ run locate
     */
    public ShrinesMod() {
        instance = this;
        Utils.loadCustomStructures();
        this.registerConfig();
    }

    public static ShrinesMod getInstance() {
        return instance;
    }

    public abstract void registerConfig();

    public IProxy getProxy() {
        if (this.proxy == null) {
            this.setProxy();
        }
        return this.proxy;
    }

    public abstract void setProxy();

    public IFunctionProvider getFunctionProvider() {
        if (this.functionProvider == null) {
            this.setFunctionProvider();
        }
        return this.functionProvider;
    }

    public abstract void setFunctionProvider();
}