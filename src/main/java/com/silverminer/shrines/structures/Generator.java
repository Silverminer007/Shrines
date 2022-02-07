/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.structures;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.init.NewStructureInit;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

public class Generator {

    public static final Logger LOGGER = LogManager.getLogger(Generator.class);

    public static void setupWorldGen() {
        LOGGER.debug("Generating {} Structures", NewStructureInit.STRUCTURES.size());
        for (AbstractStructure<?> structure : NewStructureInit.STRUCTURES.values()) {
            try {
                for (String dim : structure.getDimensions()) {
                    register(RegistryKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation(dim)),
                            structure, new StructureSeparationSettings(structure.getDistance(),
                                    structure.getSeparation(), structure.getSeedModifier()));
                }
            } catch (Throwable t) {
                LOGGER.error("Structures of Shrines can't be registered correctly because exception was thrown\n {}",
                        t);
            }
        }
    }

    public static void register(RegistryKey<DimensionSettings> dimension, Structure<?> structure,
                                StructureSeparationSettings separationSettings) {
        DimensionSettings DS = WorldGenRegistries.NOISE_GENERATOR_SETTINGS.get(dimension);
        if (DS == null) {
            LOGGER.error("You've added an non existing dimension to shrines config. This dimension will be ignored", dimension);
            return;
        }
        DimensionStructuresSettings structuresSettings = DS.structureSettings();
        structuresSettings.structureConfig().put(structure, separationSettings);
    }
}