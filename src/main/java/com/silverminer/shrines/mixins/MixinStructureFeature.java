package com.silverminer.shrines.mixins;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.packages.datacontainer.VariationConfiguration;
import com.silverminer.shrines.worldgen.structures.RandomVariantsProcessor;
import com.silverminer.shrines.worldgen.structures.RandomVariationProcessable;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureFeature.class)
public abstract class MixinStructureFeature extends ForgeRegistryEntry implements RandomVariationProcessable {
   private RandomVariantsProcessor randomVariantsProcessor;

   @Inject(method = "<init>(Lcom/mojang/serialization/Codec;Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier;Lnet/minecraft/world/level/levelgen/structure/PostPlacementProcessor;)V", at = @At(value = "RETURN"))
   private void shrines_onStructureFeatureInit(Codec<?> codec, PieceGeneratorSupplier<?> pieceGeneratorSupplier, PostPlacementProcessor postPlacementProcessor, CallbackInfo ci) {
      if (postPlacementProcessor instanceof RandomVariantsProcessor randomVariantsProcessor) {
         this.randomVariantsProcessor = randomVariantsProcessor;
      } else {
         this.randomVariantsProcessor = new RandomVariantsProcessor(VariationConfiguration.ALL_ENABLED);
      }
   }

   @Override
   public RandomVariantsProcessor getRandomVariationProcessor() {
      return randomVariantsProcessor;
   }
}