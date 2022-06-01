/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.registries;

import com.mojang.datafixers.util.Pair;
import com.ygdevs.shrines_arch.Shrines;
import com.ygdevs.shrines_arch.random_variation.RandomVariationMaterial;
import com.ygdevs.shrines_arch.registry.ModRegistrar;
import com.ygdevs.shrines_arch.registry.ModRegistryObject;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class RandomVariationMaterialRegistry {
   public static final ModRegistrar<RandomVariationMaterial> REGISTRY = ModRegistrar.create(Shrines.MODID, RandomVariationMaterial.REGISTRY);
   public static final ModRegistryObject<RandomVariationMaterial> DARK_OAK = REGISTRY.register("dark_oak", () -> new RandomVariationMaterial(List.of(
         Pair.of(Blocks.DARK_OAK_PLANKS, Shrines.location("planks"))
   )));
   public static final ModRegistryObject<RandomVariationMaterial> BIRCH = REGISTRY.register("birch", () -> new RandomVariationMaterial(List.of(
         Pair.of(Blocks.BIRCH_PLANKS, Shrines.location("planks"))
   )));
   public static final ModRegistryObject<RandomVariationMaterial> OAK = REGISTRY.register("oak", () -> new RandomVariationMaterial(List.of(
         Pair.of(Blocks.OAK_PLANKS, Shrines.location("planks"))
   )));
}