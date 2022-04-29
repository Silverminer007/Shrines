/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.mixins;

import com.silverminer.shrines.Shrines;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(ConfiguredStructureFeature.class)
public class MixinConfiguredStructureFeature {
   @Inject(method = "generate", at = @At("HEAD"), cancellable = true)
   private void shrines_generate(RegistryAccess registryAccess, ChunkGenerator p_204709_, BiomeSource p_204710_, StructureManager p_204711_, long p_204712_, ChunkPos p_204713_, int p_204714_, LevelHeightAccessor p_204715_, Predicate<Holder<Biome>> p_204716_, CallbackInfoReturnable<StructureStart> cir) {
      if (!Shrines.checkStructure(registryAccess, (ConfiguredStructureFeature<?, ?>) (Object) this)) {
         cir.setReturnValue(StructureStart.INVALID_START);
      }
   }
}