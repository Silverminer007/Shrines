/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.init;

import com.silverminer.shrines.worldgen.structures.ShrinesStructure;
import com.silverminer.shrines.packages.datacontainer.StructureData;

import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StructureRegistryHolder {
    protected static final Logger LOGGER = LogManager.getLogger(StructureRegistryHolder.class);
    private final ShrinesStructure structure;
    private ConfiguredStructureFeature<?, ?> configuredStructure;

    public StructureRegistryHolder(ResourceLocation name, StructureData config) {
        this.structure = new ShrinesStructure(name, config);
        this.configure();
    }

    public ShrinesStructure getStructure() {
        return structure;
    }

    public ConfiguredStructureFeature<?, ?> getConfiguredStructure() {
        return configuredStructure;
    }

    private void configure() {
        this.configuredStructure = structure.configured(new JigsawConfiguration(() -> PlainVillagePools.START, 1));
    }
}