/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.data;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.init.StructureInit;
import com.silverminer.shrines.worldgen.structures.ShrinesConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class ConfiguredStructureFeatures {
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> ABANDONED_WITCH_HOUSE;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> BALLOON;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> BEES;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> END_TEMPLE;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> FLOODED_TEMPLE;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> GUARDIANS_MEETING;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> HARBOUR;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> HIGH_TEMPLE;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> JUNGLE_TOWER;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> MINERAL_TEMPLE;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> NETHER_PYRAMID;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> NETHER_SHRINE;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> ORIENTAL_SANCTUARY;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> PLAYER_HOUSE;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> INFESTED_PRISON;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> SHRINE_OF_SAVANNA;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> SMALL_TEMPLE;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> TRADER_HOUSE;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> WATCH_TOWER;
    public static final ConfiguredStructureFeature<ShrinesConfiguration, ?> WATER_SHRINE;

    static {
        ABANDONED_WITCH_HOUSE = makeConfigured("abandoned_witch_house", ShrinesBiomeTagsProvider.ABANDONED_WITCH_HOUSE);// TODO Noise affecting
        BALLOON = makeConfigured("balloon", ShrinesBiomeTagsProvider.BALLOON);
        BEES = makeConfigured("bees", ShrinesBiomeTagsProvider.BEES);
        END_TEMPLE = makeConfigured("end_temple", ShrinesBiomeTagsProvider.END_TEMPLE);
        FLOODED_TEMPLE = makeConfigured("flooded_temple", ShrinesBiomeTagsProvider.FLOODED_TEMPLE);
        GUARDIANS_MEETING = makeConfigured("guardian_meeting", ShrinesBiomeTagsProvider.GUARDIANS_MEETING);
        HARBOUR = makeConfigured("harbour", ShrinesBiomeTagsProvider.HARBOUR);
        HIGH_TEMPLE = makeConfigured("high_temple", ShrinesBiomeTagsProvider.HIGH_TEMPLE);
        JUNGLE_TOWER = makeConfigured("jungle_tower", ShrinesBiomeTagsProvider.JUNGLE_TOWER);
        MINERAL_TEMPLE = makeConfigured("mineral_temple", ShrinesBiomeTagsProvider.MINERAL_TEMPLE);
        NETHER_PYRAMID = makeConfigured("nether_pyramid", ShrinesBiomeTagsProvider.NETHER_PYRAMID);
        NETHER_SHRINE = makeConfigured("nether_shrine", ShrinesBiomeTagsProvider.NETHER_SHRINE);
        ORIENTAL_SANCTUARY = makeConfigured("oriental_sanctuary", ShrinesBiomeTagsProvider.ORIENTAL_SANCTUARY);
        PLAYER_HOUSE = makeConfigured("player_house", ShrinesBiomeTagsProvider.PLAYER_HOUSE);
        INFESTED_PRISON = makeConfigured("infested_prison", ShrinesBiomeTagsProvider.INFESTED_PRISON);
        SHRINE_OF_SAVANNA = makeConfigured("shrine_of_savanna", ShrinesBiomeTagsProvider.SHRINE_OF_SAVANNA);
        SMALL_TEMPLE = makeConfigured("small_temple", ShrinesBiomeTagsProvider.SMALL_TEMPLE);
        TRADER_HOUSE = makeConfigured("trader_house", ShrinesBiomeTagsProvider.TRADER_HOUSE);
        WATCH_TOWER = makeConfigured("watch_tower", ShrinesBiomeTagsProvider.WATCH_TOWER);
        WATER_SHRINE = makeConfigured("water_shrine", ShrinesBiomeTagsProvider.BEES);
    }

    private static ConfiguredStructureFeature<ShrinesConfiguration, ?> makeConfigured(String structureName, TagKey<Biome> biomes) {
        ResourceKey<StructureTemplatePool> startPoolKey = ResourceKey.create(Registry.TEMPLATE_POOL_REGISTRY,
                ShrinesMod.location(structureName + "/start_pool"));
        Holder.Reference<StructureTemplatePool> holder = Holder.Reference.createStandAlone(BuiltinRegistries.TEMPLATE_POOL,
                startPoolKey);
        ConfiguredStructureFeature<ShrinesConfiguration, ?> element = StructureInit.SHRINES_NORMAL.get()
                .configured(new ShrinesConfiguration(holder), biomes);
        ResourceKey<ConfiguredStructureFeature<?, ?>> structureKey =
                ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, ShrinesMod.location(structureName));
        BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, structureKey, element);
        return element;
    }

    public static void bootstrap() {
    }
}