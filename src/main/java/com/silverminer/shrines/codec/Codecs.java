/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Codecs {
   public static <F, S> Codec<Pair<F, S>> pairCodec(Codec<F> first, Codec<S> second) {// Mojang's Codec#pair doesn't work as expected, so here is a working implementation
      return RecordCodecBuilder.create(pairInstance -> pairInstance.group(
            first.fieldOf("first").forGetter(Pair::getFirst),
            second.fieldOf("second").forGetter(Pair::getSecond)
      ).apply(pairInstance, Pair::of));
   }

   public static <F, S> Codec<Pair<F, S>> namedPairCodec(Codec<F> first, String firstName, Codec<S> second, String secondName) {// Mojang's Codec#pair doesn't work as expected, so here is a working implementation
      return RecordCodecBuilder.create(pairInstance -> pairInstance.group(
            first.fieldOf(firstName).forGetter(Pair::getFirst),
            second.fieldOf(secondName).forGetter(Pair::getSecond)
      ).apply(pairInstance, Pair::of));
   }

   @Contract(value = "_, _ -> new", pure = true)
   @SafeVarargs
   public static <T> @NotNull Codec<T> alternativeCodec(Codec<T> first, Codec<T>... further) {
      return new Codec<>() {
         @Override
         public <T1> DataResult<Pair<T, T1>> decode(DynamicOps<T1> ops, T1 input) {
            DataResult<Pair<T, T1>> dataResult = first.decode(ops, input);
            if (dataResult.error().isEmpty()) {
               return dataResult;
            }
            for (Codec<T> codec : further) {
               DataResult<Pair<T, T1>> dataResult1 = codec.decode(ops, input);
               if (dataResult1.error().isEmpty()) {
                  return dataResult1;
               } else {
                  dataResult = dataResult.apply2((o1, o2) -> o2, dataResult1);
               }
            }
            return dataResult;
         }

         @Override
         public <T1> DataResult<T1> encode(T input, DynamicOps<T1> ops, T1 prefix) {
            DataResult<T1> dataResult = first.encode(input, ops, prefix);
            if (dataResult.error().isEmpty()) {
               return dataResult;
            }
            for (Codec<T> codec : further) {
               DataResult<T1> dataResult1 = codec.encode(input, ops, prefix);
               if (dataResult1.error().isEmpty()) {
                  return dataResult1;
               } else {
                  dataResult = dataResult.apply2((o1, o2) -> o2, dataResult1);
               }
            }
            return dataResult;
         }
      };
   }
}