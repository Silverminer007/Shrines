/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.registries;

import com.mojang.datafixers.util.Pair;
import com.silverminer.shrines.Shrines;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;

public class TemplatePoolRegistry {
   public static final DeferredRegister<StructureTemplatePool> REGISTRY = DeferredRegister.create(Registry.TEMPLATE_POOL_REGISTRY, Shrines.MODID);

   public static final RegistryObject<StructureTemplatePool> ABANDONED_VILLA = makeStartPool("abandoned_villa");
   public static final RegistryObject<StructureTemplatePool> ABANDONED_WITCH_HOUSE = makeStartPool("abandoned_witch_house");
   public static final RegistryObject<StructureTemplatePool> AZALEA_PAVILION = makeStartPool("azalea_pavilion");
   public static final RegistryObject<StructureTemplatePool> BALLOON = makeStartPool("balloon", "balloon_1", "balloon_2", "balloon_3", "balloon_4",
         "balloon_5", "balloon_6", "balloon_7", "balloon2_1", "balloon2_2", "balloon2_3", "balloon2_4");
   public static final RegistryObject<StructureTemplatePool> BEES = makeStartPool("bees");
   public static final RegistryObject<StructureTemplatePool> END_TEMPLE = makeStartPool("end_temple");
   public static final RegistryObject<StructureTemplatePool> FLOODED_TEMPLE = makeStartPool("flooded_temple");
   public static final RegistryObject<StructureTemplatePool> GUARDIANS_MEETING = makeStartPool("guardian_meeting");
   public static final RegistryObject<StructureTemplatePool> HARBOUR = makeStartPool("harbour", "start");
   public static final RegistryObject<StructureTemplatePool> HARBOUR_BUILDINGS = makePool("harbour", "buildings", "buildings/crane/crane_1",
         "buildings/crane/crane_2", "buildings/houses/house_1", "buildings/warehouses/warehouse_big",
         "buildings/warehouses/warehouse_small_1", "buildings/warehouses/warehouse_small_2");
   public static final RegistryObject<StructureTemplatePool> HARBOUR_STREET = makePool("harbour", "street", "street/curve_1", "street/curve_2",
         "street/cross", "street/straight_1", "street/straight_2");
   public static final RegistryObject<StructureTemplatePool> HARBOUR_WARE = makePool("harbour", "ware", "ware/ware_1");
   public static final RegistryObject<StructureTemplatePool> HIGH_TEMPLE = makeStartPool("high_temple");
   public static final RegistryObject<StructureTemplatePool> JUNGLE_TOWER = makeStartPool("jungle_tower");
   public static final RegistryObject<StructureTemplatePool> LUXURY_VILLA = makeStartPool("luxury_villa");
   public static final RegistryObject<StructureTemplatePool> MINERAL_TEMPLE = makeStartPool("mineral_temple");
   public static final RegistryObject<StructureTemplatePool> OASIS_SHRINE = makeStartPool("oasis_shrine");
   public static final RegistryObject<StructureTemplatePool> NETHER_PYRAMID = makeStartPool("nether_pyramid");
   public static final RegistryObject<StructureTemplatePool> NETHER_SHRINE = makeStartPool("nether_shrine",
         "nether_shrine_001", "nether_shrine_002", "nether_shrine_003", "nether_shrine_004", "nether_shrine_005", "nether_shrine_006", "nether_shrine_007",
         "nether_shrine_008", "nether_shrine_009", "nether_shrine_011", "nether_shrine_sandstone");
   public static final RegistryObject<StructureTemplatePool> MODERN_VILLA = makeStartPool("modern_villa");
   public static final RegistryObject<StructureTemplatePool> ORIENTAL_SANCTUARY = makeStartPool("oriental_sanctuary");
   public static final RegistryObject<StructureTemplatePool> SMALL_PLAYER_HOUSE = makeStartPool("small_player_house");
   public static final RegistryObject<StructureTemplatePool> INFESTED_PRISON = makeStartPool("infested_prison");
   public static final RegistryObject<StructureTemplatePool> SHRINE_OF_SAVANNA = makeStartPool("shrine_of_savanna");
   public static final RegistryObject<StructureTemplatePool> SMALL_TEMPLE = makeStartPool("small_temple");
   public static final RegistryObject<StructureTemplatePool> TALL_PLAYER_HOUSE = makeStartPool("tall_player_house", "tall_player_house_1", "tall_player_house_2", "tall_player_house_3");
   public static final RegistryObject<StructureTemplatePool> TRADER_HOUSE = makeStartPool("trader_house");
   public static final RegistryObject<StructureTemplatePool> WATCH_TOWER = makeStartPool("watch_tower");
   public static final RegistryObject<StructureTemplatePool> WATER_SHRINE = makeStartPool("water_shrine");
   public static final RegistryObject<StructureTemplatePool> WORLD_TREE_MANOR = makeStartPool("world_tree_manor");

   private static RegistryObject<StructureTemplatePool> makeStartPool(String structureName) {
      return makePool(structureName, "start_pool", structureName);
   }

   private static RegistryObject<StructureTemplatePool> makeStartPool(String structureName, String... templates) {
      return makePool(structureName, "start_pool", templates);
   }

   private static RegistryObject<StructureTemplatePool> makePool(String structureName, String poolName, String... templates) {
      ResourceLocation id = Shrines.location(structureName + "/" + poolName);
      return REGISTRY.register(id.getPath(), () -> new StructureTemplatePool(id, new ResourceLocation("empty"),
            Arrays.stream(templates).map(template ->
                        Pair.of((StructurePoolElement) StructurePoolElement.single(
                              Shrines.location(structureName + "/" + template).toString(),
                              ProcessorLists.EMPTY).apply(StructureTemplatePool.Projection.RIGID), 1))
                  .toList()
      ));
   }
}
