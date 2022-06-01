/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.registries;

import com.mojang.serialization.Lifecycle;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class Utils {
   @ExpectPlatform
   public static <T> Registry<T> getRegistry(ResourceLocation registryKey) {
      throw new AssertionError();
   }

   @SuppressWarnings({"unchecked", "raw"})
   public static <T> void createRegistry(ResourceKey<Registry<T>> resourceKey) {
      ((WritableRegistry) Registry.REGISTRY).register(resourceKey, new MappedRegistry<>(resourceKey, Lifecycle.stable(), null), Lifecycle.stable());
   }
}
