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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

public class RandomVariationConfig extends ForgeRegistryEntry<RandomVariationConfig> {
   public static final ResourceKey<? extends Registry<RandomVariationConfig>> REGISTRY =
         ResourceKey.createRegistryKey(Shrines.location("random_variation/config"));
   public static final Codec<RandomVariationConfig> DIRECT_CODEC = RecordCodecBuilder.create(randomVariationConfigInstance ->
         randomVariationConfigInstance.group(
               Codec.list(Codec.list(RandomVariationConfigElement.CODEC))
                     .fieldOf("remaps").forGetter(RandomVariationConfig::remaps)
         ).apply(randomVariationConfigInstance, RandomVariationConfig::new));
   public static final Codec<Holder<RandomVariationConfig>> CODEC = RegistryFileCodec.create(REGISTRY, DIRECT_CODEC);

   private final List<List<RandomVariationConfigElement>> remaps;

   public RandomVariationConfig(List<List<RandomVariationConfigElement>> remaps) {
      this.remaps = remaps;
   }

   public List<List<RandomVariationConfigElement>> remaps() {
      return this.remaps;
   }

   public void process(@NotNull WorldGenLevel worldGenLevel, @NotNull Random random, @NotNull BoundingBox chunkBounds,
                       @NotNull BoundingBox structureBounds, Predicate<BlockPos> isInside) {
      var unmappedRemaps = random(this.remaps(), random);
      if (unmappedRemaps == null) {
         return;
      }
      List<Pair<RandomVariationMaterial, RandomVariationMaterial>> mappedRemaps = new ArrayList<>();
      for (RandomVariationConfigElement e : unmappedRemaps) {
         for (Holder<RandomVariationMaterial> holder : e.from()) {
            e.to().getRandomElement(random)
                  .ifPresent(destinationHolder -> mappedRemaps.add(Pair.of(holder.value(), destinationHolder.value())));
         }
      }

      // Iterate over the positions first to prevent double overrides of one BlockPos
      for (int x = chunkBounds.minX(); x <= chunkBounds.maxX(); x++) {
         for (int y = chunkBounds.minY(); y <= chunkBounds.maxY(); y++) {
            for (int z = chunkBounds.minZ(); z <= chunkBounds.maxZ(); z++) {
               for (var remapPair : mappedRemaps) {
                  if (remapPair.getSecond() == null) {
                     continue;
                  }
                  BlockPos position = new BlockPos(x, y, z);
                  if (worldGenLevel.isEmptyBlock(position) || !structureBounds.isInside(position) || !isInside.test(position)) {
                     continue;
                  }
                  BlockState blockStateAtPos = worldGenLevel.getBlockState(position);
                  Optional<ResourceLocation> id = find(remapPair.getFirst().elements(), elementPair ->
                        elementPair.getFirst().equals(blockStateAtPos.getBlock())).map(Pair::getSecond);// Don't use streams here, because order is important
                  if (id.isEmpty()) {
                     continue;
                  }
                  Optional<BlockState> newBlockState = find(remapPair.getSecond().elements(), elementPair ->
                        elementPair.getSecond().equals(id.get())).map(Pair::getFirst).map(block -> block.withPropertiesOf(blockStateAtPos));
                  if (newBlockState.isEmpty()) {
                     continue;
                  }
                  worldGenLevel.setBlock(position, newBlockState.get(), 18);//Block.UPDATE_CLIENTS);
                  break;
               }
            }
         }
      }
   }

   @Nullable
   private <T> T random(@NotNull List<T> list, Random r) {
      if (list.isEmpty()) {
         return null;
      }
      int size = list.size();
      return list.get(r.nextInt(size));
   }

   private <T> @NotNull Optional<T> find(@NotNull List<T> list, Predicate<T> matcher) {
      for (T t : list) {
         if (matcher.test(t)) {
            return Optional.ofNullable(t);
         }
      }
      return Optional.empty();
   }
}