/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.codec;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

public class ReferenceNamedHolderSet<T> extends HolderSet.Named<T> {
   private final ResourceKey<? extends Registry<T>> resourceKey;

   public ReferenceNamedHolderSet(ResourceKey<? extends Registry<T>> resourceKey, TagKey<T> tagKey) {
      super(null, tagKey);
      this.resourceKey = resourceKey;
   }

   public boolean isValidInRegistry(@NotNull Registry<T> registry) {
      return this.resourceKey.equals(registry.key());// Is there any valid reason vanilla verifies against the actual registry instead of the key?
   }
}
