/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.mixins;

import com.silverminer.shrines.config.ShrinesConfig;
import com.silverminer.shrines.structures.StructureUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(Structure.class)
public class MixinStructure {
   @Inject(method = "generate", at = @At("HEAD"), cancellable = true)
   private void shrines_generate(RegistryAccess registryAccess, ChunkGenerator p_226598_, BiomeSource p_226599_, RandomState p_226600_, StructureTemplateManager p_226601_, long p_226602_, ChunkPos p_226603_, int p_226604_, LevelHeightAccessor p_226605_, Predicate<Holder<Biome>> p_226606_, CallbackInfoReturnable<StructureStart> cir) {
      if (StructureUtils.invalidateStructure(registryAccess, StructureUtils.getStructureKey((Structure) (Object) this, registryAccess), ShrinesConfig.disabledStructures.get())) {
         cir.setReturnValue(StructureStart.INVALID_START);
      }
   }
}