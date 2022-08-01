/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.registries;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.stories.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TriggerTypeRegistry {
   public static final DeferredRegister<TriggerType> REGISTRY = DeferredRegister.create(TriggerType.REGISTRY, Shrines.MODID);
   public static final Supplier<IForgeRegistry<TriggerType>> FORGE_REGISTRY_SUPPLIER = REGISTRY.makeRegistry(RegistryBuilder::new);
   public static final RegistryObject<TriggerType> STRUCTURE = REGISTRY.register("structure", () -> () -> StructureTrigger.CODEC);
   public static final RegistryObject<TriggerType> BIOME = REGISTRY.register("biome", () -> () -> BiomeTrigger.CODEC);
   public static final RegistryObject<TriggerType> DIMENSION = REGISTRY.register("dimension", () -> () -> DimensionTrigger.CODEC);
   public static final RegistryObject<TriggerType> ALL_MATCH = REGISTRY.register("all_match", () -> () -> AllMatchTrigger.CODEC);
}