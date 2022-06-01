/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.forge.test;

import com.ygdevs.shrines_arch.Shrines;
import com.ygdevs.shrines_arch.registry.ModRegistrar;
import com.ygdevs.shrines_arch.registry.ModRegistryObject;
import net.minecraft.data.worldgen.DesertVillagePools;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;

public class TestRegistry {
   public static final ModRegistrar<Test> REGISTRY = ModRegistrar.create(Shrines.MODID, Test.REGISTRY);
   public static final ModRegistryObject<Test> TEST_A = REGISTRY.register("test_a", () ->
         new Test(42, "goodbye", Blocks.DIAMOND_BLOCK,
               PlainVillagePools.START, Optional.empty(), Optional.empty()));
   public static final ModRegistryObject<Test> TEST_B = REGISTRY.register("test_b");
   public static final ModRegistryObject<Test> TEST_C = REGISTRY.register("test_c", () ->
         new Test(9100, "good morning", Blocks.DIAMOND_BLOCK,
               DesertVillagePools.START, Optional.ofNullable(TEST_A.get()), Optional.empty()));
}