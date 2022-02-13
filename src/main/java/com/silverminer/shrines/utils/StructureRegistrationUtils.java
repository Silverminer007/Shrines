/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.utils;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.init.StructureRegistryHolder;
import com.silverminer.shrines.structures.ShrinesStructure;
import com.silverminer.shrines.structures.load.StructureData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Silverminer
 */
public class StructureRegistrationUtils {
    protected static final Logger LOGGER = LogManager.getLogger(StructureRegistrationUtils.class);
    private static Method GETCODEC_METHOD;

    public static boolean checkBiome(List<? extends String> blacklistedBiomes,
                                     List<? extends String> whitelistedBiomeCategories, ResourceLocation name, Biome.Category category) {
        if (!whitelistedBiomeCategories.isEmpty()) {
            if (blacklistedBiomes.isEmpty()) {
                return !blacklistedBiomes.contains(name.toString())
                        && whitelistedBiomeCategories.contains(category.toString());
            } else {
                return true;
            }
        }
        return false;
    }

    public static void setupWorldGen() {
        registerConfiguredStructureFeatures();
        registerStructureSeparationSettings();
    }

    public static void registerConfiguredStructureFeatures() {
        for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
            ShrinesStructure structure = holder.getStructure();
            ResourceLocation registryName = structure.getRegistryName();
            if (registryName == null) {
                continue;
            }
            holder.configure();
            WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE,
                    registryName.toString(), holder.getConfiguredStructure());
            FlatGenerationSettings.STRUCTURE_FEATURES.put(structure, holder.getConfiguredStructure());
            LOGGER.debug("Registered configured structure feature of {}", holder.getStructure().getConfig().getName());
        }
    }

    @Deprecated
    public static void registerStructureSeperationSettings() {
        registerStructureSeparationSettings();
    }

    public static void registerStructureSeparationSettings() {
        for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
            ShrinesStructure structure = holder.getStructure();

            StructureSeparationSettings structureSeparationSettings = new StructureSeparationSettings(
                    structure.getDistance(), structure.getSeparation(), structure.getConfig().getSeed_modifier());

            DimensionStructuresSettings.DEFAULTS = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                    .putAll(DimensionStructuresSettings.DEFAULTS).put(structure, structureSeparationSettings).build();

            WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
                Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings()
                        .structureConfig();
                if (structureMap instanceof ImmutableMap) {
                    Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                    tempMap.put(structure, structureSeparationSettings);
                    settings.getValue().structureSettings().structureConfig = tempMap;
                } else {
                    structureMap.put(structure, structureSeparationSettings);
                }
            });
            LOGGER.debug("Registered Structure Separation Settings for {}",
                    holder.getStructure().getConfig().getName());
        }
    }

    public static void addDimensionalSpacing(ServerWorld world) {

        /*
         * Skip Terraforged's chunk generator as they are a special case of a mod
         * locking down their chunkgenerator. They will handle your structure spacing
         * for your if you add to WorldGenRegistries.NOISE_GENERATOR_SETTINGS in your
         * structure's registration.
         */
        try {
            if (GETCODEC_METHOD == null)
                GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
            @SuppressWarnings("unchecked")
            // cgRL = chunk generator Resource Location
            ResourceLocation cgRL = Registry.CHUNK_GENERATOR
                    .getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(world.getChunkSource().generator));
            if (cgRL != null && cgRL.getNamespace().equals("terraforged"))
                return;
        } catch (Exception e) {
            LOGGER.error("Was unable to check if " + world.dimension().location()
                    + " is using Terraforged's ChunkGenerator.");
        }

        Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(
                world.getChunkSource().generator.getSettings().structureConfig());
        if (world.getChunkSource().getGenerator() instanceof FlatChunkGenerator
                && world.dimension().equals(World.OVERWORLD)) {
            NewStructureInit.STRUCTURES.stream().map(StructureRegistryHolder::getStructure)
                    .collect(Collectors.toList()).forEach(tempMap.keySet()::remove);
        } else {
            for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
                if (isAllowedForWorld(world, holder.getStructure().getConfig())) {
                    tempMap.putIfAbsent(holder.getStructure(),
                            DimensionStructuresSettings.DEFAULTS.get(holder.getStructure()));
                } else {
                    tempMap.remove(holder.getStructure());
                }
            }
        }
        world.getChunkSource().generator.getSettings().structureConfig = tempMap;
    }

    public static boolean isAllowedForWorld(ISeedReader currentWorld, StructureData config) {
        String worldID = currentWorld.getLevel().dimension().location().toString();
        return config.getDimension_whitelist().contains(worldID);
    }
}