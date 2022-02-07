/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.client.gui.config.options;

import java.util.List;

import com.silverminer.shrines.config.IStructureConfig;

import net.minecraft.util.text.ITextComponent;

/**
 * @author Silverminer
 */
public class NormalListScreen extends StructureListOptionScreen {
    protected ConfigStructureScreen screen;

    /**
     * @param parent
     * @param title
     * @param possibleValues
     * @param activeValues
     * @param option
     */
    public NormalListScreen(ConfigStructureScreen parent, ITextComponent title, List<String> possibleValues,
                            List<? extends String> activeValues, String option) {
        super(parent, title, possibleValues, activeValues, option);
        this.screen = parent;
    }

    @Override
    protected void save() {
        IStructureConfig csd = this.screen.getConfig();
        csd.fromString(this.option, this.activeValues.toString());
        this.screen.setConfig(csd);
        this.parent = screen;
    }

}