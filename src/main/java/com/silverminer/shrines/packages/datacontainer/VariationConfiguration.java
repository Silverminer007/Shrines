/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.datacontainer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.Pair;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;

public class VariationConfiguration extends ForgeRegistryEntry<VariationConfiguration> {
    public static final Codec<VariationConfiguration> CODEC = RecordCodecBuilder.create(variationConfigurationInstance -> variationConfigurationInstance.group(
            Codec.BOOL.fieldOf("is_enabled").forGetter(VariationConfiguration::isEnabled),
            SimpleVariationConfiguration.CODEC.optionalFieldOf("simple", SimpleVariationConfiguration.ALL_DISABLED).forGetter(VariationConfiguration::getSimpleVariationConfiguration),
            NestedVariationConfiguration.CODEC.optionalFieldOf("nested", NestedVariationConfiguration.ALL_DISABLED).forGetter(VariationConfiguration::getNestedVariationConfiguration)
    ).apply(variationConfigurationInstance, VariationConfiguration::new));
    private final SimpleVariationConfiguration simpleVariationConfiguration;
    private final NestedVariationConfiguration nestedVariationConfiguration;
    private boolean isEnabled;

    public VariationConfiguration(boolean isEnabled, SimpleVariationConfiguration simpleVariationConfiguration, NestedVariationConfiguration nestedVariationConfiguration) {
        this.isEnabled = isEnabled;
        this.simpleVariationConfiguration = simpleVariationConfiguration;
        this.nestedVariationConfiguration = nestedVariationConfiguration;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public SimpleVariationConfiguration getSimpleVariationConfiguration() {
        return simpleVariationConfiguration;
    }

    public NestedVariationConfiguration getNestedVariationConfiguration() {
        return nestedVariationConfiguration;
    }

    public NewVariationConfiguration toNewConfiguration() {
        Pair<List<String>, List<String>> pairDisabledMaterialsTypes = this.simpleVariationConfiguration.getDisabledMaterials();
        List<String> disabledTypes = this.nestedVariationConfiguration.getDisabledTypes();
        disabledTypes.addAll(pairDisabledMaterialsTypes.second());
        return new NewVariationConfiguration(this.isEnabled, pairDisabledMaterialsTypes.first(), disabledTypes);
    }
}