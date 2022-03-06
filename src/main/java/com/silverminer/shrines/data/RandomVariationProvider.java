/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.packages.datacontainer.NewVariationConfiguration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record RandomVariationProvider(DataGenerator generator) implements DataProvider {
    public static final NewVariationConfiguration ABANDONED_WITCH_HOUSE;
    public static final NewVariationConfiguration BALLOON;
    public static final NewVariationConfiguration BEES;
    public static final NewVariationConfiguration END_TEMPLE;
    public static final NewVariationConfiguration FLOODED_TEMPLE;
    public static final NewVariationConfiguration GUARDIAN_MEETING;
    public static final NewVariationConfiguration HARBOUR;
    public static final NewVariationConfiguration HIGH_TEMPLE;
    public static final NewVariationConfiguration JUNGLE_TOWER;
    public static final NewVariationConfiguration MINERAL_TEMPLE;
    public static final NewVariationConfiguration NETHER_PYRAMID;
    public static final NewVariationConfiguration NETHER_SHRINE;
    public static final NewVariationConfiguration ORIENTAL_SANCTUARY;
    public static final NewVariationConfiguration PLAYER_HOUSE;
    public static final NewVariationConfiguration INFESTED_PRISON;
    public static final NewVariationConfiguration SHRINE_OF_SAVANNA;
    public static final NewVariationConfiguration SMALL_TEMPLE;
    public static final NewVariationConfiguration TRADER_HOUSE;
    public static final NewVariationConfiguration WATCH_TOWER;
    public static final NewVariationConfiguration WATER_SHRINE;
    private static final Logger LOGGER = LogManager.getLogger(RandomVariationProvider.class);
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final Map<ResourceLocation, NewVariationConfiguration> ELEMENTS = new HashMap<>();

    static {
        ABANDONED_WITCH_HOUSE = create("abandoned_witch_house");
        BALLOON = create("balloon");
        BEES = create("bees");
        END_TEMPLE = create("end_temple");
        FLOODED_TEMPLE = create("flooded_temple");
        GUARDIAN_MEETING = create("guardian_meeting");
        HARBOUR = create("harbour");
        HIGH_TEMPLE = create("high_temple");
        JUNGLE_TOWER = create("jungle_tower");
        MINERAL_TEMPLE = create("mineral_temple");
        NETHER_PYRAMID = create("nether_pyramid");
        NETHER_SHRINE = create("nether_shrine");
        ORIENTAL_SANCTUARY = create("oriental_sanctuary");
        PLAYER_HOUSE = create("player_house");
        INFESTED_PRISON = create("infested_prison");
        SHRINE_OF_SAVANNA = create("shrine_of_savanna");
        SMALL_TEMPLE = create("small_temple");
        TRADER_HOUSE = create("trader_house");
        WATCH_TOWER = create("watch_tower");
        WATER_SHRINE = create("water_shrine");
    }

    private static @NotNull Map<ResourceLocation, NewVariationConfiguration> getElements() {
        return ELEMENTS;
    }

    private static NewVariationConfiguration create(String structureName) {
        NewVariationConfiguration element = new NewVariationConfiguration(true);
        ELEMENTS.put(ShrinesMod.location(structureName), element);
        return element;
    }

    @Override
    public void run(@NotNull HashCache pCache) throws IOException {
        Path output = this.generator.getOutputFolder();

        Map<ResourceLocation, NewVariationConfiguration> elements = getElements();
        for (ResourceLocation key : elements.keySet()) {
            DataResult<JsonElement> dataResult = NewVariationConfiguration.CODEC.encode(elements.get(key), JsonOps.INSTANCE, new JsonObject());
            Optional<JsonElement> optional = dataResult.resultOrPartial(LOGGER::error);
            if (optional.isPresent()) {
                ResourceLocation registryPath = NewVariationConfiguration.REGISTRY.location();
                DataProvider.save(GSON, pCache, optional.get(), output.resolve("data").resolve(key.getNamespace()).resolve(registryPath.getPath()).resolve(key.getPath() + ".json"));
            }
        }
    }

    @Override
    public @NotNull String getName() {
        return "Random variation";
    }
}
