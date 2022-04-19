/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ModRegistrar<T> {// TODO Tags
   private final String modid;
   private final ResourceKey<? extends Registry<T>> registryKey;
   private final Map<ResourceKey<T>, Supplier<T>> defaultValues = new HashMap<>();

   protected ModRegistrar(String modid, ResourceKey<? extends Registry<T>> registryKey) {
      this.modid = modid;
      this.registryKey = registryKey;
   }

   public static <E> ModRegistrar<E> create(String modid, ResourceKey<? extends Registry<E>> registryKey) {
      return new ModRegistrar<>(modid, registryKey);
   }

   public ModRegistryObject<T> register(String id, @NotNull Supplier<T> defaultValue) {
      ResourceKey<T> resourceKey = this.createResourceKey(id);
      this.defaultValues.put(resourceKey, defaultValue);
      return this.register(resourceKey);
   }

   public ModRegistryObject<T> register(String id) {
      return this.register(this.createResourceKey(id));
   }

   public ModRegistryObject<T> register(ResourceKey<T> resourceKey) {
      return new ModRegistryObject<>(resourceKey);
   }

   private ResourceKey<T> createResourceKey(String id) {
      return ResourceKey.create(this.registryKey, new ResourceLocation(this.modid, id));
   }

   public void register() {
      MinecraftForge.EVENT_BUS.register(new EventDispatcher<>(this));
   }

   public Set<ResourceLocation> getKeys() {
      return Utils.getRegistry(this.registryKey.location()).keySet();
   }

   @SuppressWarnings({"unchecked", "rawtypes"})
   public Set<Map.Entry<ResourceKey<T>,T>> getEntries() {
      return (Set<Map.Entry<ResourceKey<T>,T>>)(Set) Utils.getRegistry(this.registryKey.location()).entrySet();
   }

   @SuppressWarnings("unchecked")
   public T get(ResourceLocation key) {
      return (T) Utils.getRegistry(this.registryKey.location()).get(key);
   }

   private record EventDispatcher<T>(ModRegistrar<T> registrar) {
      @SubscribeEvent(priority = EventPriority.HIGH)
      public void registerValues(ModRegistryRegisterEvent event) {
         for (Map.Entry<ResourceKey<T>, Supplier<T>> entry : this.registrar().defaultValues.entrySet()) {
            event.register(this.registrar().registryKey, entry.getKey(), entry.getValue());
         }
      }
   }
}