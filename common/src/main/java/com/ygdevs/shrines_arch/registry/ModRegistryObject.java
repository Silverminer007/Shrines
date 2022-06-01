/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.registry;

import com.ygdevs.shrines_arch.registries.Utils;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record ModRegistryObject<T>(ResourceKey<T> key) implements Supplier<T> {
   @Nullable
   public T get() {
      return this.getRegistry().get(this.key());
   }

   @NotNull
   public Holder<T> getAsHolder() {
      return Holder.Reference.createStandAlone(this.getRegistry(), this.key());
   }

   @NotNull
   private Registry<T> getRegistry() {
      return Utils.getRegistry(this.key().registry());
   }
}