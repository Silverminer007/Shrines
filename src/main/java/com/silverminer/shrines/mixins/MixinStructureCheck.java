package com.silverminer.shrines.mixins;

import com.silverminer.shrines.Shrines;
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
      if (!Shrines.checkStructure(registryAccess, configuredStructureFeature)) {
         cir.setReturnValue(false);
      }
   }
}