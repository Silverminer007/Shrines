/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.datacontainer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.Pair;

import java.util.ArrayList;
import java.util.List;

public class SimpleVariationConfiguration {
    public static final Codec<SimpleVariationConfiguration> CODEC = RecordCodecBuilder.create(variationConfigurationInstance -> variationConfigurationInstance.group(
            Codec.BOOL.fieldOf("is_wool_enabled").forGetter(SimpleVariationConfiguration::isWoolEnabled),
            Codec.BOOL.fieldOf("is_terracotta_enabled").forGetter(SimpleVariationConfiguration::isTerracottaEnabled),
            Codec.BOOL.fieldOf("is_glazed_terracotta").forGetter(SimpleVariationConfiguration::isGlazedTerracottaEnabled),
            Codec.BOOL.fieldOf("is_concrete_enabled").forGetter(SimpleVariationConfiguration::isConcreteEnabled),
            Codec.BOOL.fieldOf("is_concrete_powder_enabled").forGetter(SimpleVariationConfiguration::isConcretePowderEnabled),
            Codec.BOOL.fieldOf("are_planks_enabled").forGetter(SimpleVariationConfiguration::arePlanksEnabled),
            Codec.BOOL.fieldOf("are_ores_enabled").forGetter(SimpleVariationConfiguration::areOresEnabled),
            Codec.BOOL.fieldOf("are_stones_enabled").forGetter(SimpleVariationConfiguration::areStonesEnabled),
            Codec.BOOL.fieldOf("are_bees_enabled").forGetter(SimpleVariationConfiguration::areBeesEnabled)
    ).apply(variationConfigurationInstance, SimpleVariationConfiguration::new));
    public static final SimpleVariationConfiguration ALL_ENABLED = new SimpleVariationConfiguration(true, true, true, true, true, true, true, true, true);
    public static final SimpleVariationConfiguration ALL_DISABLED = new SimpleVariationConfiguration(false, false, false, false, false, false, false, false, false);
    private boolean isWoolEnabled;
    private boolean isTerracottaEnabled;
    private boolean isGlazedTerracottaEnabled;
    private boolean isConcreteEnabled;
    private boolean isConcretePowderEnabled;
    private boolean arePlanksEnabled;
    private boolean areOresEnabled;
    private boolean areStonesEnabled;
    private boolean areBeesEnabled;

    public SimpleVariationConfiguration(boolean isWoolEnabled, boolean isTerracottaEnabled, boolean isGlazedTerracottaEnabled, boolean isConcreteEnabled, boolean isConcretePowderEnabled, boolean arePlanksEnabled, boolean areOresEnabled, boolean areStonesEnabled, boolean areBeesEnabled) {
        this.isWoolEnabled = isWoolEnabled;
        this.isTerracottaEnabled = isTerracottaEnabled;
        this.isGlazedTerracottaEnabled = isGlazedTerracottaEnabled;
        this.isConcreteEnabled = isConcreteEnabled;
        this.isConcretePowderEnabled = isConcretePowderEnabled;
        this.arePlanksEnabled = arePlanksEnabled;
        this.areOresEnabled = areOresEnabled;
        this.areStonesEnabled = areStonesEnabled;
        this.areBeesEnabled = areBeesEnabled;
    }

    public boolean isWoolEnabled() {
        return isWoolEnabled;
    }

    public void setWoolEnabled(boolean woolEnabled) {
        isWoolEnabled = woolEnabled;
    }

    public boolean isTerracottaEnabled() {
        return isTerracottaEnabled;
    }

    public void setTerracottaEnabled(boolean terracottaEnabled) {
        isTerracottaEnabled = terracottaEnabled;
    }

    public boolean isGlazedTerracottaEnabled() {
        return isGlazedTerracottaEnabled;
    }

    public void setGlazedTerracottaEnabled(boolean glazedTerracottaEnabled) {
        isGlazedTerracottaEnabled = glazedTerracottaEnabled;
    }

    public boolean isConcreteEnabled() {
        return isConcreteEnabled;
    }

    public void setConcreteEnabled(boolean concreteEnabled) {
        isConcreteEnabled = concreteEnabled;
    }

    public boolean isConcretePowderEnabled() {
        return isConcretePowderEnabled;
    }

    public void setConcretePowderEnabled(boolean concretePowderEnabled) {
        isConcretePowderEnabled = concretePowderEnabled;
    }

    public boolean arePlanksEnabled() {
        return arePlanksEnabled;
    }

    public void setPlanksEnabled(boolean arePlanksEnabled) {
        this.arePlanksEnabled = arePlanksEnabled;
    }

    public boolean areOresEnabled() {
        return areOresEnabled;
    }

    public void setOresEnabled(boolean areOresEnabled) {
        this.areOresEnabled = areOresEnabled;
    }

    public boolean areStonesEnabled() {
        return areStonesEnabled;
    }

    public void setStonesEnabled(boolean areStonesEnabled) {
        this.areStonesEnabled = areStonesEnabled;
    }

    public boolean areBeesEnabled() {
        return areBeesEnabled;
    }

    public void setBeesEnabled(boolean areBeesEnabled) {
        this.areBeesEnabled = areBeesEnabled;
    }

    public Pair<List<String>, List<String>> getDisabledMaterials() {
        List<String> disabledMaterials = new ArrayList<>();
        List<String> disabledTypes = new ArrayList<>();
        if (!this.isWoolEnabled()) {
            disabledTypes.add("wool");
        }
        if (!this.isTerracottaEnabled()) {
            disabledTypes.add("terracotta");
        }
        if (!this.isGlazedTerracottaEnabled()) {
            disabledTypes.add("glazed_terracotta");
        }
        if (!this.isConcreteEnabled()) {
            disabledTypes.add("concrete");
        }
        if (!this.isConcretePowderEnabled()) {
            disabledTypes.add("concrete_powder");
        }
        if (!this.arePlanksEnabled()) {
            disabledTypes.add("planks");
        }
        if (!this.areOresEnabled()) {
            disabledMaterials.add("ores");
        }
        if (!this.areStonesEnabled()) {
            disabledMaterials.add("stone");
        }
        if (!this.areBeesEnabled()) {
            disabledMaterials.add("bees");
        }
        return Pair.of(disabledMaterials, disabledTypes);
    }
}