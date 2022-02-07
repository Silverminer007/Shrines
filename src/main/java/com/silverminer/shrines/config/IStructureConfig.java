/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.config;

import java.util.List;
import java.util.Locale;

import com.silverminer.shrines.utils.custom_structures.OptionParsingResult;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.biome.Biome.Category;

/**
 * @author Silverminer
 */
public interface IStructureConfig extends Comparable<IStructureConfig> {
    public boolean getGenerate();

    public double getSpawnChance();

    public boolean getNeedsGround();

    public int getDistance();

    public int getSeparation();

    public int getSeed();

    public List<? extends Category> getWhitelist();

    public List<? extends String> getBlacklist();

    public List<? extends String> getDimensions();

    public boolean getUseRandomVarianting();

    public double getLootChance();

    public boolean getSpawnVillagers();

    public boolean isBuiltIn();

    default String getDataName() {
        return this.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_");
    }

    public String getName();

    public boolean getActive();

    public void setActive(boolean value);

    /**
     * @param option
     * @param string
     */
    default OptionParsingResult fromString(String option, String value) {
        for (IConfigOption<?> co : this.getAllOptions()) {
            if (co.getName().equals(option)) {
                OptionParsingResult res = co.fromString(value, this);
                return res;
            }
        }
        return new OptionParsingResult(false, new StringTextComponent("There is no such option as provided"));
    }

    public List<? extends IConfigOption<?>> getAllOptions();
}