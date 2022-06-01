/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.mixin;

import com.ygdevs.shrines_arch.Shrines;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureCheck;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureCheck.class)
public class MixinStructureCheck {
   @Shadow
   @Final
   private RegistryAccess registryAccess;

   @Inject(method = "canCreateStructure", at = @At("HEAD"), cancellable = true)
   private void shrines_canGenerateStructure(ChunkPos chunkPos, ConfiguredStructureFeature<?, ?> configuredStructureFeature, CallbackInfoReturnable<Boolean> cir) {
      if (Shrines.invalidateStructure(registryAccess, configuredStructureFeature)) {
         cir.setReturnValue(false);
      }
   }
}