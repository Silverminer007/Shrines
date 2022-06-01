/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.registries;

import com.ygdevs.shrines_arch.Shrines;
import com.ygdevs.shrines_arch.structures.spawn_criteria.*;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

public class SpawnCriteriaTypeRegistry {
   public static final DeferredRegister<SpawnCriteriaType> REGISTRY =
         DeferredRegister.create(Shrines.MODID, SpawnCriteriaType.REGISTRY);

   public static final RegistrySupplier<SpawnCriteriaType> CLOSE_TO_STRUCTURE =
         REGISTRY.register("close_to_structure",
               () -> new SpawnCriteriaType(CloseToStructureSpawnCriteria.CODEC));

   public static final RegistrySupplier<SpawnCriteriaType> NOT_CLOSE_TO_STRUCTURE =
         REGISTRY.register("not_close_to_structure",
               () -> new SpawnCriteriaType(NotCloseToStructureSpawnCriteria.CODEC));

   public static final RegistrySupplier<SpawnCriteriaType> RANDOM_CHANCE =
         REGISTRY.register("random_chance",
               () -> new SpawnCriteriaType(RandomChanceSpawnCriteria.CODEC));

   public static final RegistrySupplier<SpawnCriteriaType> HEIGHT =
         REGISTRY.register("height",
               () -> new SpawnCriteriaType(HeightSpawnCriteria.CODEC));

   public static final RegistrySupplier<SpawnCriteriaType> GROUND_LEVEL_DELTA =
         REGISTRY.register("ground_level_delta",
               () -> new SpawnCriteriaType(GroundLevelDeltaSpawnCriteria.CODEC));

   public static final RegistrySupplier<SpawnCriteriaType> MIN_STRUCTURE_DISTANCE =
         REGISTRY.register("min_structure_distance",
               () -> new SpawnCriteriaType(MinStructureDistanceSpawnCriteria.CODEC));
}