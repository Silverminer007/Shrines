/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.data;

import com.mojang.datafixers.util.Pair;
import com.silverminer.shrines.ShrinesMod;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Arrays;

public class TemplatePools {
    public static final StructureTemplatePool ABANDONED_WITCH_HOUSE;
    public static final StructureTemplatePool BALLOON;
    public static final StructureTemplatePool BEES;
    public static final StructureTemplatePool END_TEMPLE;
    public static final StructureTemplatePool FLOODED_TEMPLE;
    public static final StructureTemplatePool GUARDIANS_MEETING;
    public static final StructureTemplatePool HARBOUR;
    public static final StructureTemplatePool HARBOUR_BUILDINGS;
    public static final StructureTemplatePool HARBOUR_STREET;
    public static final StructureTemplatePool HARBOUR_WARE;
    public static final StructureTemplatePool HIGH_TEMPLE;
    public static final StructureTemplatePool JUNGLE_TOWER;
    public static final StructureTemplatePool MINERAL_TEMPLE;
    public static final StructureTemplatePool NETHER_PYRAMID;
    public static final StructureTemplatePool NETHER_SHRINE;
    public static final StructureTemplatePool ORIENTAL_SANCTUARY;
    public static final StructureTemplatePool PLAYER_HOUSE;
    public static final StructureTemplatePool PLAYER_HOUSE_TABLE;
    public static final StructureTemplatePool INFESTED_PRISON;
    public static final StructureTemplatePool SHRINE_OF_SAVANNA;
    public static final StructureTemplatePool SMALL_TEMPLE;
    public static final StructureTemplatePool TRADER_HOUSE;
    public static final StructureTemplatePool WATCH_TOWER;
    public static final StructureTemplatePool WATER_SHRINE;

    static {
        ABANDONED_WITCH_HOUSE = makeStartPool("abandoned_witch_house");
        BALLOON = makeStartPool("balloon", "balloon_1", "balloon_2", "balloon_3", "balloon_4",
                "balloon_5", "balloon_6", "balloon_7", "balloon2_1", "balloon2_2", "balloon2_3", "balloon2_4");
        BEES = makeStartPool("bees");
        END_TEMPLE = makeStartPool("end_temple");
        FLOODED_TEMPLE = makeStartPool("flooded_temple");
        GUARDIANS_MEETING = makeStartPool("guardian_meeting");
        HARBOUR = makeStartPool("harbour", "start");
        HARBOUR_BUILDINGS = makePool("harbour", "buildings", "buildings/crane/crane_1",
                "buildings/crane/crane_2", "buildings/houses/house_1", "buildings/warehouses/warehouse_big",
                "buildings/warehouses/warehouse_small_1", "buildings/warehouses/warehouse_small_2");
        HARBOUR_STREET = makePool("harbour", "street", "street/curve_1", "street/curve_2",
                "street/cross", "street/straight_1", "street/straight_2");
        HARBOUR_WARE = makePool("harbour", "ware", "ware/ware_1");
        HIGH_TEMPLE = makeStartPool("high_temple");
        JUNGLE_TOWER = makeStartPool("jungle_tower");
        MINERAL_TEMPLE = makeStartPool("mineral_temple");
        NETHER_PYRAMID = makeStartPool("nether_pyramid");
        NETHER_SHRINE = makeStartPool("nether_shrine");
        ORIENTAL_SANCTUARY = makeStartPool("oriental_sanctuary");
        PLAYER_HOUSE = makeStartPool("player_house");
        PLAYER_HOUSE_TABLE = makePool("player_house", "table", "table");
        INFESTED_PRISON = makeStartPool("infested_prison");
        SHRINE_OF_SAVANNA = makeStartPool("shrine_of_savanna");
        SMALL_TEMPLE = makeStartPool("small_temple");
        TRADER_HOUSE = makeStartPool("trader_house");
        WATCH_TOWER = makeStartPool("watch_tower");
        WATER_SHRINE = makeStartPool("water_shrine");
    }

    private static StructureTemplatePool makeStartPool(String structureName, String... templates) {
        return makePool(structureName, "start_pool", templates);
    }

    private static StructureTemplatePool makePool(String structureName, String poolName, String... templates) {
        ResourceLocation id = ShrinesMod.location(structureName + "/" + poolName);
        if (templates.length == 0) {
            templates = new String[]{structureName};
        }
        StructureTemplatePool element = new StructureTemplatePool(id, new ResourceLocation("empty"),
                Arrays.stream(templates).map(template ->
                                Pair.of((StructurePoolElement) StructurePoolElement.single(
                                        ShrinesMod.location(structureName + "/" + template).toString(),
                                        ProcessorLists.SHRINES).apply(StructureTemplatePool.Projection.RIGID), 1))
                        .toList()
        );
        BuiltinRegistries.register(BuiltinRegistries.TEMPLATE_POOL, id, element);
        return element;
    }

    public static void bootstrap() {
    }
}