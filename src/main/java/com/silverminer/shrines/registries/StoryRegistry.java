/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.registries;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.stories.Snippet;
import com.silverminer.shrines.stories.Story;
import com.silverminer.shrines.stories.StructureTrigger;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class StoryRegistry {
   public static final DeferredRegister<Story> REGISTRY = DeferredRegister.create(Story.REGISTRY, Shrines.MODID);
   public static final Supplier<IForgeRegistry<Story>> FORGE_REGISTRY_SUPPLIER = REGISTRY.makeRegistry(() -> new RegistryBuilder<Story>().dataPackRegistry(Story.DIRECT_CODEC).disableSync().disableSaving());
   public static final RegistryObject<Story> ABANDONED_WITCH_HOUSE = REGISTRY.register("abandoned_witch_house", () -> new Story(List.of(
         new Snippet("stories.shrines.abandoned_witch_house.part_1", 1),
         new Snippet("stories.shrines.abandoned_witch_house.part_2", 2),
         new Snippet("stories.shrines.abandoned_witch_house.part_3", 3)
   ), List.of(new StructureTrigger(StructureRegistry.ABANDONED_WITCH_HOUSE.getHolder().orElseThrow()))));
   public static final RegistryObject<Story> END_TEMPLE = REGISTRY.register("end_temple", () -> new Story(List.of(
         new Snippet("stories.shrines.end_temple.part_1", 1),
         new Snippet("stories.shrines.end_temple.part_2", 2),
         new Snippet("stories.shrines.end_temple.part_3", 3)
   ), List.of(new StructureTrigger(StructureRegistry.END_TEMPLE.getHolder().orElseThrow()))));
   public static final RegistryObject<Story> GUARDIAN_MEETING = REGISTRY.register("guardian_meeting", () -> new Story(List.of(
         new Snippet("stories.shrines.guardian_meeting.part_1", 1),
         new Snippet("stories.shrines.guardian_meeting.part_2", 2),
         new Snippet("stories.shrines.guardian_meeting.part_3", 3)
   ), List.of(new StructureTrigger(StructureRegistry.GUARDIAN_MEETING.getHolder().orElseThrow()))));
   public static final RegistryObject<Story> HIGH_TEMPLE = REGISTRY.register("high_temple", () -> new Story(List.of(
         new Snippet("stories.shrines.high_temple.part_1", 1),
         new Snippet("stories.shrines.high_temple.part_2", 2),
         new Snippet("stories.shrines.high_temple.part_3", 3),
         new Snippet("stories.shrines.high_temple.part_4", 4)
   ), List.of(new StructureTrigger(StructureRegistry.HIGH_TEMPLE.getHolder().orElseThrow()))));
   public static final RegistryObject<Story> MINERAL_TEMPLE = REGISTRY.register("mineral_temple", () -> new Story(List.of(
         new Snippet("stories.shrines.mineral_temple.part_1", 1),
         new Snippet("stories.shrines.mineral_temple.part_2", 2),
         new Snippet("stories.shrines.mineral_temple.part_3", 3)
   ), List.of(new StructureTrigger(StructureRegistry.MINERAL_TEMPLE.getHolder().orElseThrow()))));
   public static final RegistryObject<Story> MODERN_VILLA = REGISTRY.register("modern_villa", () -> new Story(List.of(
         new Snippet("stories.shrines.modern_villa.part_1", 1),
         new Snippet("stories.shrines.modern_villa.part_2", 2),
         new Snippet("stories.shrines.modern_villa.part_3", 3)
   ), List.of(new StructureTrigger(StructureRegistry.MODERN_VILLA.getHolder().orElseThrow()))));
   public static final RegistryObject<Story> NETHER_PYRAMID = REGISTRY.register("nether_pyramid", () -> new Story(List.of(
         new Snippet("stories.shrines.nether_pyramid.part_1", 1),
         new Snippet("stories.shrines.nether_pyramid.part_2", 2),
         new Snippet("stories.shrines.nether_pyramid.part_3", 3)
   ), List.of(
         new StructureTrigger(StructureRegistry.NETHER_PYRAMID_NETHER.getHolder().orElseThrow()),
         new StructureTrigger(StructureRegistry.NETHER_PYRAMID_OVERWORLD.getHolder().orElseThrow())
   )));
   public static final RegistryObject<Story> PLAYER_HOUSE_AND_WATER_SHRINE = REGISTRY.register("player_house_and_water_shrine", () -> new Story(List.of(
         new Snippet("stories.shrines.player_house_and_water_shrine.part_1", 1),
         new Snippet("stories.shrines.player_house_and_water_shrine.part_2", 2),
         new Snippet("stories.shrines.player_house_and_water_shrine.part_3", 3),
         new Snippet("stories.shrines.player_house_and_water_shrine.part_4", 4)
   ), List.of(
         new StructureTrigger(StructureRegistry.SMALL_PLAYER_HOUSE.getHolder().orElseThrow()),
         new StructureTrigger(StructureRegistry.TALL_PLAYER_HOUSE.getHolder().orElseThrow()),
         new StructureTrigger(StructureRegistry.WATER_SHRINE.getHolder().orElseThrow())
   )));
   public static final RegistryObject<Story> SHRINE_OF_SAVANNA = REGISTRY.register("shrine_of_savanna", () -> new Story(List.of(
         new Snippet("stories.shrines.shrine_of_savanna.part_1", 1),
         new Snippet("stories.shrines.shrine_of_savanna.part_2", 2),
         new Snippet("stories.shrines.shrine_of_savanna.part_3", 3),
         new Snippet("stories.shrines.shrine_of_savanna.part_4", 4)
   ), List.of(new StructureTrigger(StructureRegistry.SHRINE_OF_SAVANNA.getHolder().orElseThrow()))));
   public static final RegistryObject<Story> SMALL_TEMPLE = REGISTRY.register("small_temple", () -> new Story(List.of(
         new Snippet("stories.shrines.small_temple.part_1", 1),
         new Snippet("stories.shrines.small_temple.part_2", 2),
         new Snippet("stories.shrines.small_temple.part_3", 3)
   ), List.of(new StructureTrigger(StructureRegistry.SMALL_TEMPLE.getHolder().orElseThrow()))));
}