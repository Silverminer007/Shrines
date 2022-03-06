/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.data;

import com.silverminer.shrines.ShrinesMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

public class StructureSets {
    public static final StructureSet ABANDONED_WITCH_HOUSE;
    public static final StructureSet BALLOON;
    public static final StructureSet BEES;
    public static final StructureSet END_TEMPLE;
    public static final StructureSet FLOODED_TEMPLE;
    public static final StructureSet GUARDIAN_MEETING;
    public static final StructureSet HARBOUR;
    public static final StructureSet HIGH_TEMPLE;
    public static final StructureSet JUNGLE_TOWER;
    public static final StructureSet MINERAL_TEMPLE;
    public static final StructureSet NETHER_PYRAMID;
    public static final StructureSet NETHER_SHRINE;
    public static final StructureSet ORIENTAL_SANCTUARY;
    public static final StructureSet PLAYER_HOUSE;
    public static final StructureSet INFESTED_PRISON;
    public static final StructureSet SHRINE_OF_SAVANNA;
    public static final StructureSet SMALL_TEMPLE;
    public static final StructureSet TRADER_HOUSE;
    public static final StructureSet WATCH_TOWER;
    public static final StructureSet WATER_SHRINE;

    static {
        ABANDONED_WITCH_HOUSE = create("abandoned_witch_house", 60, 11, 1721882513);
        BALLOON = create("balloon", 50, 8, 143665);
        BEES = create("bees", 70, 12, 779806245);
        END_TEMPLE = create("end_temple", 60, 11, 32 ^ 478392);
        FLOODED_TEMPLE = create("flooded_temple", 50, 10, 54315143);
        GUARDIAN_MEETING = create("guardian_meeting", 70, 17, 1498473232);
        HARBOUR = create("harbour", 50, 8, 651398043);
        HIGH_TEMPLE = create("high_temple", 85, 18, 536987987);
        JUNGLE_TOWER = create("jungle_tower", 60, 11, 987531843);
        MINERAL_TEMPLE = create("mineral_temple", 50, 10, 576143753);
        NETHER_PYRAMID = create("nether_pyramid", 150, 50, 7428394);
        NETHER_SHRINE = create("nether_shrine", 80, 15, 653267);
        ORIENTAL_SANCTUARY = create("oriental_sanctuary", 50, 14, 143665);
        PLAYER_HOUSE = create("player_house", 80, 15, 751963298);
        INFESTED_PRISON = create("infested_prison", 60, 11, 567483014);
        SHRINE_OF_SAVANNA = create("shrine_of_savanna", 67, 11, 432333099);
        SMALL_TEMPLE = create("small_temple", 75, 13, 4765321);
        TRADER_HOUSE = create("trader_house", 60, 11, 760055678);
        WATCH_TOWER = create("watch_tower", 77, 16, 432189012);
        WATER_SHRINE = create("water_shrine", 80, 15, 643168754);
    }

    private static StructureSet create(String structureName, int spacing, int separation, int seed) {
        StructureSet element = new StructureSet(Holder.Reference.createStandAlone(
                BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
                ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, ShrinesMod.location(structureName))),
                new RandomSpreadStructurePlacement(spacing, separation, RandomSpreadType.LINEAR, seed));
        BuiltinRegistries.register(BuiltinRegistries.STRUCTURE_SETS, ShrinesMod.location(structureName), element);
        return element;
    }

    public static void bootstrap() {
    }
}