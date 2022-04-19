/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;
import java.util.function.Supplier;

public class ModRegistryRegisterEvent extends Event {
   private final ResourceKey<? extends Registry<?>> currentRegistry;

   public ModRegistryRegisterEvent(ResourceKey<? extends Registry<?>> currentRegistry) {
      this.currentRegistry = currentRegistry;
   }

   public <T> void register(ResourceKey<? extends Registry<T>> registryKey, ResourceKey<T> key, Supplier<T> value) {
      if (!this.currentRegistry.equals(registryKey)) {
         return;
      }
      if (this.getRegistry(registryKey) instanceof WritableRegistry<T> writableRegistry) {
         writableRegistry.registerOrOverride(OptionalInt.empty(), key, value.get(), Lifecycle.experimental());
      } else {
         throw new IllegalArgumentException("You can only register to Mutable registries");
      }
   }

   @NotNull
   private <T> Registry<T> getRegistry(ResourceKey<? extends Registry<T>> registryKey) {
      return Utils.getRegistry(registryKey.location());
   }
}