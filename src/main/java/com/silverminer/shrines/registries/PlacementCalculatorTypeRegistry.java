/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.registries;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.worldgen.structures.placement_types.FirstFreePlacementCalculator;
import com.silverminer.shrines.worldgen.structures.placement_types.FixedPlacementCalculator;
import com.silverminer.shrines.worldgen.structures.placement_types.PlacementCalculatorType;
import com.silverminer.shrines.worldgen.structures.placement_types.SimplePlacementCalculator;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class PlacementCalculatorTypeRegistry {
   public static final DeferredRegister<PlacementCalculatorType> PLACEMENT_CALCULATOR_TYPE_DEFERRED_REGISTER =
         DeferredRegister.create(PlacementCalculatorType.REGISTRY, ShrinesMod.MODID);

   public static final Supplier<IForgeRegistry<PlacementCalculatorType>> REGISTRY_SUPPLIER =
         PLACEMENT_CALCULATOR_TYPE_DEFERRED_REGISTER.makeRegistry(PlacementCalculatorType.class, RegistryBuilder::new);

   public static final RegistryObject<PlacementCalculatorType> SIMPLE =
         PLACEMENT_CALCULATOR_TYPE_DEFERRED_REGISTER.register("simple",
               () -> new PlacementCalculatorType(SimplePlacementCalculator.CODEC));

   public static final RegistryObject<PlacementCalculatorType> FIRST_FREE =
         PLACEMENT_CALCULATOR_TYPE_DEFERRED_REGISTER.register("first_free",
               () -> new PlacementCalculatorType(FirstFreePlacementCalculator.CODEC));

   public static final RegistryObject<PlacementCalculatorType> FIXED =
         PLACEMENT_CALCULATOR_TYPE_DEFERRED_REGISTER.register("fixed",
               () -> new PlacementCalculatorType(FixedPlacementCalculator.CODEC));
}