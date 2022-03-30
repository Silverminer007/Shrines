/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.data;

import com.mojang.datafixers.util.Pair;
import com.silverminer.shrines.ShrinesMod;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Arrays;

public class TemplatePools {
   public static final StructureTemplatePool ABANDONED_VILLA = makeStartPool("abandoned_villa");
   public static final StructureTemplatePool ABANDONED_WITCH_HOUSE = makeStartPool("abandoned_witch_house");
   public static final StructureTemplatePool BALLOON = makeStartPool("balloon", "balloon_1", "balloon_2", "balloon_3", "balloon_4",
         "balloon_5", "balloon_6", "balloon_7", "balloon2_1", "balloon2_2", "balloon2_3", "balloon2_4");
   public static final StructureTemplatePool BEES = makeStartPool("bees");
   public static final StructureTemplatePool END_TEMPLE = makeStartPool("end_temple");
   public static final StructureTemplatePool FLOODED_TEMPLE = makeStartPool("flooded_temple");
   public static final StructureTemplatePool GUARDIANS_MEETING = makeStartPool("guardian_meeting");
   public static final StructureTemplatePool HARBOUR = makeStartPool("harbour", "start");
   public static final StructureTemplatePool HARBOUR_BUILDINGS = makePool("harbour", "buildings", "buildings/crane/crane_1",
         "buildings/crane/crane_2", "buildings/houses/house_1", "buildings/warehouses/warehouse_big",
         "buildings/warehouses/warehouse_small_1", "buildings/warehouses/warehouse_small_2");
   public static final StructureTemplatePool HARBOUR_STREET = makePool("harbour", "street", "street/curve_1", "street/curve_2",
         "street/cross", "street/straight_1", "street/straight_2");
   public static final StructureTemplatePool HARBOUR_WARE = makePool("harbour", "ware", "ware/ware_1");
   public static final StructureTemplatePool HIGH_TEMPLE = makeStartPool("high_temple");
   public static final StructureTemplatePool JUNGLE_TOWER = makeStartPool("jungle_tower");
   public static final StructureTemplatePool MINERAL_TEMPLE = makeStartPool("mineral_temple");
   public static final StructureTemplatePool OASIS_SHRINE = makeStartPool("oasis_shrine");
   public static final StructureTemplatePool NETHER_PYRAMID = makeStartPool("nether_pyramid");
   public static final StructureTemplatePool NETHER_SHRINE = makeStartPool("nether_shrine");
   public static final StructureTemplatePool MODERN_VILLA = makeStartPool("modern_villa");
   public static final StructureTemplatePool ORIENTAL_SANCTUARY = makeStartPool("oriental_sanctuary");
   public static final StructureTemplatePool PLAYER_HOUSE = makeStartPool("player_house");
   public static final StructureTemplatePool PLAYER_HOUSE_TABLE = makePool("player_house", "table", "table");
   public static final StructureTemplatePool INFESTED_PRISON = makeStartPool("infested_prison");
   public static final StructureTemplatePool SHRINE_OF_SAVANNA = makeStartPool("shrine_of_savanna");
   public static final StructureTemplatePool SMALL_TEMPLE = makeStartPool("small_temple");
   public static final StructureTemplatePool TRADER_HOUSE = makeStartPool("trader_house");
   public static final StructureTemplatePool WATCH_TOWER = makeStartPool("watch_tower");
   public static final StructureTemplatePool WATER_SHRINE = makeStartPool("water_shrine");
   public static final StructureTemplatePool WORLD_TREE_MANOR = makeStartPool("world_tree_manor");

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
                              ProcessorLists.EMPTY).apply(StructureTemplatePool.Projection.RIGID), 1))
                  .toList()
      );
      BuiltinRegistries.register(BuiltinRegistries.TEMPLATE_POOL, id, element);
      return element;
   }

   public static void bootstrap() {
   }
}