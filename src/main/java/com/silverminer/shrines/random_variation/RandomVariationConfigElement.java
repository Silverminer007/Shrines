package com.silverminer.shrines.random_variation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;

public record RandomVariationConfigElement(HolderSet<RandomVariationMaterial> from,
                                           HolderSet<RandomVariationMaterial> to) {

   public static final Codec<RandomVariationConfigElement> CODEC = RecordCodecBuilder.create(randomVariationConfigElementInstance ->
         randomVariationConfigElementInstance.group(
               RegistryCodecs.homogeneousList(RandomVariationMaterial.REGISTRY, RandomVariationMaterial.DIRECT_CODEC, false).fieldOf("first").forGetter(RandomVariationConfigElement::from),
               RegistryCodecs.homogeneousList(RandomVariationMaterial.REGISTRY, RandomVariationMaterial.DIRECT_CODEC, false).fieldOf("second").forGetter(RandomVariationConfigElement::to)
         ).apply(randomVariationConfigElementInstance, RandomVariationConfigElement::new));
}
