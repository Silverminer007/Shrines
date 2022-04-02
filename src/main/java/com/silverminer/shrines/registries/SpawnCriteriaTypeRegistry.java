/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.registries;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.structures.spawn_criteria.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SpawnCriteriaTypeRegistry {
   public static final DeferredRegister<SpawnCriteriaType> SPAWN_CRITERIA_TYPE_REGISTRY =
         DeferredRegister.create(SpawnCriteriaType.REGISTRY, ShrinesMod.MODID);

   public static final Supplier<IForgeRegistry<SpawnCriteriaType>> REGISTRY_SUPPLIER =
         SPAWN_CRITERIA_TYPE_REGISTRY.makeRegistry(SpawnCriteriaType.class, RegistryBuilder::new);

   public static final RegistryObject<SpawnCriteriaType> CLOSE_TO_STRUCTURE =
         SPAWN_CRITERIA_TYPE_REGISTRY.register("close_to_structure",
               () -> new SpawnCriteriaType(CloseToStructureSpawnCriteria.CODEC));

   public static final RegistryObject<SpawnCriteriaType> NOT_CLOSE_TO_STRUCTURE =
         SPAWN_CRITERIA_TYPE_REGISTRY.register("not_close_to_structure",
               () -> new SpawnCriteriaType(NotCloseToStructureSpawnCriteria.CODEC));

   public static final RegistryObject<SpawnCriteriaType> RANDOM_CHANCE =
         SPAWN_CRITERIA_TYPE_REGISTRY.register("random_chance",
               () -> new SpawnCriteriaType(RandomChanceSpawnCriteria.CODEC));

   public static final RegistryObject<SpawnCriteriaType> HEIGHT =
         SPAWN_CRITERIA_TYPE_REGISTRY.register("height",
               () -> new SpawnCriteriaType(HeightSpawnCriteria.CODEC));

   public static final RegistryObject<SpawnCriteriaType> GROUND_LEVEL_DELTA =
         SPAWN_CRITERIA_TYPE_REGISTRY.register("ground_level_delta",
               () -> new SpawnCriteriaType(GroundLevelDeltaSpawnCriteria.CODEC));
}