/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.registries;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.random_variation.RandomVariationMaterial;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class RandomVariationMaterialRegistry {
   public static final DeferredRegister<RandomVariationMaterial> REGISTRY = DeferredRegister.create(RandomVariationMaterial.REGISTRY, Shrines.MODID);
   public static final Supplier<IForgeRegistry<RandomVariationMaterial>> FORGE_REGISTRY_SUPPLIER = REGISTRY.makeRegistry(RandomVariationMaterial.class,
         () -> new RegistryBuilder<RandomVariationMaterial>().dataPackRegistry(RandomVariationMaterial.DIRECT_CODEC));
}