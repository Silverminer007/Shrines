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
