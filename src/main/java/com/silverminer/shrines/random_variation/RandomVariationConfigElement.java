/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.random_variation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.codec.Codecs;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;

public record RandomVariationConfigElement(
      HolderSet<RandomVariationMaterial> from,
      HolderSet<RandomVariationMaterial> to) {
   public static final Codec<RandomVariationConfigElement> OLD_CODEC = RecordCodecBuilder.create(randomVariationConfigElementInstance ->
         randomVariationConfigElementInstance.group(
               RegistryCodecs.homogeneousList(RandomVariationMaterial.REGISTRY, RandomVariationMaterial.DIRECT_CODEC, false).fieldOf("first").forGetter(RandomVariationConfigElement::from),
               RegistryCodecs.homogeneousList(RandomVariationMaterial.REGISTRY, RandomVariationMaterial.DIRECT_CODEC, false).fieldOf("second").forGetter(RandomVariationConfigElement::to)
         ).apply(randomVariationConfigElementInstance, RandomVariationConfigElement::new));
   public static final Codec<RandomVariationConfigElement> NEW_CODEC = RecordCodecBuilder.create(randomVariationConfigElementInstance ->
         randomVariationConfigElementInstance.group(
               RegistryCodecs.homogeneousList(RandomVariationMaterial.REGISTRY, RandomVariationMaterial.DIRECT_CODEC, false).fieldOf("from").forGetter(RandomVariationConfigElement::from),
               RegistryCodecs.homogeneousList(RandomVariationMaterial.REGISTRY, RandomVariationMaterial.DIRECT_CODEC, false).fieldOf("to").forGetter(RandomVariationConfigElement::to)
         ).apply(randomVariationConfigElementInstance, RandomVariationConfigElement::new));
   public static final Codec<RandomVariationConfigElement> CODEC = Codecs.alternativeCodec(NEW_CODEC, OLD_CODEC);
}
