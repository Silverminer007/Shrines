/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.datacontainer;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public class NewVariationConfiguration extends ForgeRegistryEntry<NewVariationConfiguration> {
    public static final Codec<NewVariationConfiguration> CODEC =
            RecordCodecBuilder.create(newVariationConfigurationInstance ->
                    newVariationConfigurationInstance.group(
                                    Codec.BOOL.fieldOf("active").forGetter(NewVariationConfiguration::isEnabled),
                                    Codec.list(Codec.STRING).fieldOf("disabled_materials").forGetter(NewVariationConfiguration::getDisabledMaterials),
                                    Codec.list(Codec.STRING).fieldOf("disabled_types").forGetter(NewVariationConfiguration::getDisabledTypes))
                            .apply(newVariationConfigurationInstance, NewVariationConfiguration::new));

    private final boolean isEnabled;
    private final List<String> disabledMaterials;
    private final List<String> disabledTypes;

    public NewVariationConfiguration(boolean isEnabled) {
        this(isEnabled, new ArrayList<>(), new ArrayList<>());
    }

    public NewVariationConfiguration(boolean isEnabled, List<String> disabledMaterials, List<String> disabledTypes) {
        this.isEnabled = isEnabled;
        this.disabledMaterials = ImmutableList.copyOf(disabledMaterials);
        this.disabledTypes = ImmutableList.copyOf(disabledTypes);
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public boolean isTypeEnabled(String typeID) {
        return !this.disabledTypes.contains(typeID);
    }

    public boolean isMaterialEnabled(String materialID) {
        return !this.disabledMaterials.contains(materialID);
    }

    public List<String> getDisabledMaterials() {
        return disabledMaterials;
    }

    public List<String> getDisabledTypes() {
        return disabledTypes;
    }
}