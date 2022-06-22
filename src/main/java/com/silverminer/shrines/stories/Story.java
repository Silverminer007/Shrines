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
import com.silverminer.shrines.Shrines;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;

import java.util.List;

public record Story(List<Snippet> snippets, List<Trigger> triggers) {
   public static final ResourceKey<? extends Registry<Story>> REGISTRY = ResourceKey.createRegistryKey(Shrines.location("stories/story"));
   public static final Codec<Story> DIRECT_CODEC = RecordCodecBuilder.create(storyInstance ->
         storyInstance.group(
               Codec.list(Snippet.CODEC).fieldOf("snippets").forGetter(Story::snippets),
               Codec.list(Trigger.CODEC).fieldOf("trigger").forGetter(Story::triggers)
         ).apply(storyInstance, Story::new));
   public static final Codec<Holder<Story>> CODEC = RegistryFileCodec.create(REGISTRY, DIRECT_CODEC);
}