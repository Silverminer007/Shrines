/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.registries;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.random_variation.RandomVariationConfig;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class RandomVariationConfigRegistry {
   public static final DeferredRegister<RandomVariationConfig> REGISTRY = DeferredRegister.create(RandomVariationConfig.REGISTRY, Shrines.MODID);
   public static final Supplier<IForgeRegistry<RandomVariationConfig>> FORGE_REGISTRY_SUPPLIER =
         REGISTRY.makeRegistry(RandomVariationConfig.class,
               () -> new RegistryBuilder<RandomVariationConfig>().dataPackRegistry(RandomVariationConfig.DIRECT_CODEC));
}