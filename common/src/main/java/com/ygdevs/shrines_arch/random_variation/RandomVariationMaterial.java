/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.random_variation;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ygdevs.shrines_arch.Shrines;
import com.ygdevs.shrines_arch.codec.Codecs;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record RandomVariationMaterial(List<Pair<Block, ResourceLocation>> elements) {
   public static final ResourceKey<Registry<RandomVariationMaterial>> REGISTRY = ResourceKey.createRegistryKey(Shrines.location("random_variation/material"));
   public static final Codec<RandomVariationMaterial> DIRECT_CODEC = RecordCodecBuilder.create(randomVariationMaterialInstance ->
         randomVariationMaterialInstance.group(
               Codec.list(Codecs.pairCodec(Registry.BLOCK.byNameCodec(), ResourceLocation.CODEC)).fieldOf("elements").forGetter(RandomVariationMaterial::elements)
         ).apply(randomVariationMaterialInstance, RandomVariationMaterial::new));
   public static final Codec<Holder<RandomVariationMaterial>> CODEC = RegistryFileCodec.create(REGISTRY, DIRECT_CODEC);
}