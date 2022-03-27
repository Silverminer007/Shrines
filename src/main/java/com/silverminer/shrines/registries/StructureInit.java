/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.registries;

import com.mojang.logging.LogUtils;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.worldgen.structures.ShrinesStructure;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

/**
 * @author Silverminer
 */
public class StructureInit {
   public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, ShrinesMod.MODID);
   public static final RegistryObject<ShrinesStructure> SURFACE = STRUCTURES.register("surface", () -> new ShrinesStructure(GenerationStep.Decoration.SURFACE_STRUCTURES));
   @SuppressWarnings("unused")
   public static final RegistryObject<ShrinesStructure> UNDERGROUND = STRUCTURES.register("underground", () -> new ShrinesStructure(GenerationStep.Decoration.UNDERGROUND_STRUCTURES));
   protected static final Logger LOGGER = LogUtils.getLogger();
}