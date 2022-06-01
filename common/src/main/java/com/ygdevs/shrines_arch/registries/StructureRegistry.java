/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.registries;

import com.ygdevs.shrines_arch.Shrines;
import com.ygdevs.shrines_arch.structures.ShrinesStructure;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

/**
 * @author Silverminer
 */
public class StructureRegistry {
   public static final DeferredRegister<StructureFeature<?>> REGISTRY = DeferredRegister.create(Shrines.MODID, Registry.STRUCTURE_FEATURE_REGISTRY);
   public static final RegistrySupplier<ShrinesStructure> SURFACE = REGISTRY.register("surface", () -> new ShrinesStructure(GenerationStep.Decoration.SURFACE_STRUCTURES));
   @SuppressWarnings("unused")
   public static final RegistrySupplier<ShrinesStructure> UNDERGROUND = REGISTRY.register("underground", () -> new ShrinesStructure(GenerationStep.Decoration.UNDERGROUND_STRUCTURES));
}