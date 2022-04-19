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
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Codecs {
   public static <F, S> Codec<Pair<F, S>> pairCodec(Codec<F> first, Codec<S> second) {// Mojang's Codec#pair doesn't work as expected, so here is a working implementation
      return RecordCodecBuilder.create(pairInstance -> pairInstance.group(
            first.fieldOf("first").forGetter(Pair::getFirst),
            second.fieldOf("second").forGetter(Pair::getSecond)
      ).apply(pairInstance, Pair::of));
   }
}