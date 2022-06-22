/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.stories;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Snippet(String text, int id) {
   public static final Codec<Snippet> CODEC = RecordCodecBuilder.create(snippetInstance ->
         snippetInstance.group(
               Codec.STRING.fieldOf("text").forGetter(Snippet::text),
               Codec.INT.fieldOf("id").forGetter(Snippet::id)
         ).apply(snippetInstance, Snippet::new));
}