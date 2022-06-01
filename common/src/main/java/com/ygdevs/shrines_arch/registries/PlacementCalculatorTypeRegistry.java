/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.registries;

import com.ygdevs.shrines_arch.Shrines;
import com.ygdevs.shrines_arch.structures.placement_types.*;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

public class PlacementCalculatorTypeRegistry {
   public static final DeferredRegister<PlacementCalculatorType> REGISTRY =
         DeferredRegister.create(Shrines.MODID, PlacementCalculatorType.REGISTRY);

   public static final RegistrySupplier<PlacementCalculatorType> SIMPLE =
         REGISTRY.register("simple",
               () -> new PlacementCalculatorType(SimplePlacementCalculator.CODEC));

   public static final RegistrySupplier<PlacementCalculatorType> FIRST_FREE =
         REGISTRY.register("first_free",
               () -> new PlacementCalculatorType(FirstFreePlacementCalculator.CODEC));

   public static final RegistrySupplier<PlacementCalculatorType> FIXED =
         REGISTRY.register("fixed",
               () -> new PlacementCalculatorType(FixedPlacementCalculator.CODEC));

   public static final RegistrySupplier<PlacementCalculatorType> RELATIVE =
         REGISTRY.register("relative",
               () -> new PlacementCalculatorType(RelativePlacementCalculator.CODEC));
}