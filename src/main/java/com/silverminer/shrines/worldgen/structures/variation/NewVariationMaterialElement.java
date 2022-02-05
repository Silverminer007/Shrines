package com.silverminer.shrines.worldgen.structures.variation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record NewVariationMaterialElement(String typeID, ResourceLocation blockID) {
   public static final Codec<NewVariationMaterialElement> CODEC =
         RecordCodecBuilder.create(newVariationMaterialElementInstance ->
               newVariationMaterialElementInstance.group(
                     Codec.STRING.fieldOf("type_id").forGetter(NewVariationMaterialElement::typeID),
                     ResourceLocation.CODEC.fieldOf("block_id").forGetter(NewVariationMaterialElement::blockID))
                     .apply(newVariationMaterialElementInstance, NewVariationMaterialElement::new));
}