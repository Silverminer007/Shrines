/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.forge.registry;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import com.ygdevs.shrines_arch.registry.ModRegistryAccess;
import com.ygdevs.shrines_arch.Shrines;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.NewRegistryEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class Utils {
   private static final List<DataPackRegistry<?>> DATA_PACK_REGISTRIES = new ArrayList<>();

   @SuppressWarnings({"unchecked", "rawtypes"})
   public static <T> void createRegistry(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec) {
      DATA_PACK_REGISTRIES.add(new DataPackRegistry<>(registryKey, codec));
      WritableRegistry registry = (WritableRegistry) BuiltinRegistries.REGISTRY;
      registry.register(registryKey,
            new ModRegistry<>(registryKey, Lifecycle.experimental(), null),
            Lifecycle.experimental());
   }

   public static <T> Codec<T> getCodec(ResourceKey<? extends Registry<T>> registryKey) {
      return ResourceLocation.CODEC.flatXmap((key) -> {
         Registry<T> registry = Utils.getRegistry(registryKey.location());
         return Optional.ofNullable(registry.get(key)).map(DataResult::success).orElseGet(() ->
               DataResult.error("Unknown registry key in " + registryKey + ": " + key));
      }, (value) -> {
         Registry<T> registry = Utils.getRegistry(registryKey.location());
         return registry.getResourceKey(value).map(ResourceKey::location).map(DataResult::success).orElseGet(() ->
               DataResult.error("Unknown registry element in " + registryKey + ":" + value));
      });
   }

   @SuppressWarnings("unchecked")
   public static <T> Registry<T> getRegistry(ResourceLocation registryKey) {
      return Optional.ofNullable((Registry<T>) Registry.REGISTRY.get(registryKey)).or(() ->
            Optional.ofNullable((Registry<T>) BuiltinRegistries.REGISTRY.get(registryKey))
      ).orElseThrow(() -> new IllegalStateException("You can't register to an unregistered Registry"));
   }

   @Mod.EventBusSubscriber(modid = Shrines.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
   static class ModEvents {
      @SubscribeEvent
      public static void onRegister(RegistryEvent.@NotNull Register<?> event) {
         MinecraftForge.EVENT_BUS.post(new ModRegistryRegisterEvent(ResourceKey.createRegistryKey(event.getRegistry().getRegistryName())));
      }

      @SubscribeEvent(priority = EventPriority.HIGHEST)
      public static void onRegisterNewRegistries(NewRegistryEvent event) {
         DATA_PACK_REGISTRIES.clear();
      }
   }

   @Mod.EventBusSubscriber(modid = Shrines.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
   static class ForgeEvents {
      @SubscribeEvent
      public static void addReloadListener(AddReloadListenerEvent event) {
         for (DataPackRegistry<?> dataPackRegistry : DATA_PACK_REGISTRIES) {
            event.addListener(new DataPackReader<>(dataPackRegistry));
         }
      }
   }

   static class DataPackReader<T> extends SimpleJsonResourceReloadListener {
      static final Logger LOGGER = LogUtils.getLogger();
      private final DataPackRegistry<T> registry;
      private static final Gson GSON = new Gson();

      public DataPackReader(@NotNull DataPackRegistry<T> registry) {
         super(GSON, createRegistryPath(registry.registryKey().location()));
         this.registry = registry;
      }

      private static @NotNull String createRegistryPath(@NotNull ResourceLocation registryKey) {
         return registryKey.getNamespace() + "/" + registryKey.getPath();
      }

      @Override
      protected void apply(@NotNull Map<ResourceLocation, JsonElement> elementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
         if ((Utils.getRegistry(this.registry.registryKey().location())) instanceof ModRegistry modRegistry) {
            modRegistry.clear();
         }
         ModRegistryRegisterEvent event = new ModRegistryRegisterEvent(this.registry.registryKey());
         MinecraftForge.EVENT_BUS.post(event);
         try {
            elementMap.entrySet().stream().map(entry ->
                  Pair.of(
                        entry.getKey(),
                        this.registry.codec().decode(RegistryOps.create(JsonOps.INSTANCE, new ModRegistryAccess()), entry.getValue())
                              .resultOrPartial(LOGGER::error)
                              .map(Pair::getFirst).orElse(null)
                  )).filter(pair -> pair.getSecond() != null).forEach(pair -> event.register(this.registry.registryKey(),
                  ResourceKey.create(this.registry.registryKey(), pair.getFirst()),
                  pair::getSecond));
         } catch (Exception e) {
            LOGGER.error("Failed to decode and register Data Pack Registry Elements for unknown reason", e);
         }
      }
   }
}
