/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.random_variation;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.codec.Codecs;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RandomVariationMaterial extends ForgeRegistryEntry<RandomVariationMaterial> {
   public static final ResourceKey<? extends Registry<RandomVariationMaterial>> REGISTRY = ResourceKey.createRegistryKey(Shrines.location("random_variation/material"));
   public static final Codec<RandomVariationMaterial> DIRECT_CODEC = RecordCodecBuilder.create(randomVariationMaterialInstance ->
         randomVariationMaterialInstance.group(
               Codec.list(Codecs.pairCodec(ForgeRegistries.BLOCKS.getCodec(), ResourceLocation.CODEC)).fieldOf("elements").forGetter(RandomVariationMaterial::elements)
         ).apply(randomVariationMaterialInstance, RandomVariationMaterial::new));
   public static final Codec<Holder<RandomVariationMaterial>> CODEC = RegistryFileCodec.create(REGISTRY, DIRECT_CODEC);

   private final List<Pair<Block, ResourceLocation>> elements;

   public RandomVariationMaterial(List<Pair<Block, ResourceLocation>> elements) {
      this.elements = elements;
   }

   public List<Pair<Block, ResourceLocation>> elements() {
      return this.elements;
   }
}