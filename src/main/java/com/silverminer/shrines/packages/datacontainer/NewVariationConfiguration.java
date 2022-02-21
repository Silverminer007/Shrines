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
import com.silverminer.shrines.ShrinesMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.ForgeRegistryEntry;
import silverminer.dynamicregistries.registry.RegistryAccessExtension;

import java.util.ArrayList;
import java.util.List;

public class NewVariationConfiguration extends ForgeRegistryEntry<NewVariationConfiguration> {
    public static final ResourceKey<Registry<NewVariationConfiguration>> REGISTRY = RegistryAccessExtension.createRegistryKey(ShrinesMod.MODID, "worldgen/variation_configuration");
    public static final Codec<NewVariationConfiguration> CODEC =
            RecordCodecBuilder.create(newVariationConfigurationInstance ->
                    newVariationConfigurationInstance.group(
                                    Codec.BOOL.fieldOf("active").forGetter(NewVariationConfiguration::isEnabled),
                                    Codec.list(Codec.STRING).fieldOf("disabled_materials").forGetter(NewVariationConfiguration::getDisabledMaterials),
                                    Codec.list(Codec.STRING).fieldOf("disabled_types").forGetter(NewVariationConfiguration::getDisabledTypes))
                            .apply(newVariationConfigurationInstance, NewVariationConfiguration::new));

    private boolean isEnabled;
    private List<String> disabledMaterials;
    private List<String> disabledTypes;

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

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
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

    public void setDisabledMaterials(List<String> disabledMaterials) {
        this.disabledMaterials = disabledMaterials;
    }

    public List<String> getDisabledTypes() {
        return disabledTypes;
    }

    public void setDisabledTypes(List<String> disabledTypes) {
        this.disabledTypes = disabledTypes;
    }
}