/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.registry;

import com.ygdevs.shrines_arch.registries.Utils;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ModRegistryAccess implements RegistryAccess.Writable {
   @Override
   public <E> Optional<WritableRegistry<E>> ownedWritableRegistry(ResourceKey<? extends Registry<? extends E>> resourceKey) {
      return Optional.ofNullable((WritableRegistry<E>) Utils.getRegistry(resourceKey.location()));
   }

   @Override
   public <E> Optional<Registry<E>> ownedRegistry(ResourceKey<? extends Registry<? extends E>> resourceKey) {
      return Optional.ofNullable(Utils.getRegistry(resourceKey.location()));
   }

   @Override
   public Stream<RegistryEntry<?>> ownedRegistries() {
      return BuiltinRegistries.REGISTRY.entrySet().stream().map(ModRegistryAccess::fromMapEntry);
   }

   private static <T, R extends Registry<? extends T>> RegistryEntry<T> fromMapEntry(Map.Entry<? extends ResourceKey<? extends Registry<?>>, R> p_206242_) {
      return fromUntyped(p_206242_.getKey(), p_206242_.getValue());
   }

   @NotNull
   @SuppressWarnings("unchecked")
   private static <T> RegistryAccess.RegistryEntry<T> fromUntyped(ResourceKey<? extends Registry<?>> p_206244_, Registry<?> p_206245_) {
      return new RegistryEntry<>((ResourceKey<? extends Registry<T>>) p_206244_, (Registry<T>) p_206245_);
   }
}