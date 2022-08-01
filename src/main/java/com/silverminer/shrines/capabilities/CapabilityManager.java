/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.capabilities;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.codec.Codecs;
import com.silverminer.shrines.stories.Snippet;
import com.silverminer.shrines.stories.Story;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Mod.EventBusSubscriber(modid = Shrines.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityManager {
   public static final Capability<IStoryCapability> STORY = net.minecraftforge.common.capabilities.CapabilityManager.get(new CapabilityToken<>() {
   });

   @SubscribeEvent
   public static void attachCapabilities(final @NotNull AttachCapabilitiesEvent<Entity> event) {
      if (!(event.getObject() instanceof Player)) return;

      ShrinesStoryCapability capability = new ShrinesStoryCapability();
      LazyOptional<IStoryCapability> optional = LazyOptional.of(() -> capability);

      ICapabilityProvider provider = new ICapabilitySerializable<CompoundTag>() {
         @Override
         public CompoundTag serializeNBT() {
            return capability.serializeNBT();
         }

         @Override
         public void deserializeNBT(CompoundTag nbt) {
            capability.deserializeNBT(nbt);
         }

         @Override
         public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (cap == STORY) {
               return optional.cast();
            }
            return LazyOptional.empty();
         }
      };
      event.addCapability(Shrines.location("novels"), provider);
   }

   @SubscribeEvent
   public static void registerCaps(@NotNull RegisterCapabilitiesEvent event) {
      event.register(IStoryCapability.class);
   }

   public interface IStoryCapability extends INBTSerializable<CompoundTag> {
      @NotNull List<Pair<ResourceLocation, Integer>> getUnlockedSnippets();

      String unlockSnippet(Story story, Level level);

      void addSavedData(ResourceLocation key, String data);

      List<String> getSaves(ResourceLocation key);
   }

   public static class ShrinesStoryCapability implements IStoryCapability {
      private static final Codec<Map<ResourceLocation, List<String>>> SAVES_CODEC = Codec.list(Codecs.pairCodec(ResourceLocation.CODEC, Codec.list(Codec.STRING)))
            .comapFlatMap(to -> {
               Map<ResourceLocation, List<String>> result = new HashMap<>();
               to.forEach(pair -> result.put(pair.getFirst(), pair.getSecond()));
               return DataResult.success(result);
            }, from -> from.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())).toList());
      private static final Codec<List<Pair<ResourceLocation, Integer>>> UNLOCKED_STORIES_CODEC = Codec.list(Codecs.pairCodec(ResourceLocation.CODEC, Codec.INT));
      private Map<ResourceLocation, Integer> snippets = new HashMap<>();
      private Map<ResourceLocation, List<String>> saves = new HashMap<>();

      @Override
      public @NotNull List<Pair<ResourceLocation, Integer>> getUnlockedSnippets() {
         return this.snippets.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())).toList();
      }

      @Override
      public String unlockSnippet(Story story, @NotNull Level level) {
         ResourceLocation storyID = level.registryAccess().ownedRegistryOrThrow(Story.REGISTRY).getKey(story);
         int snippetID = this.snippets.containsKey(storyID) ? this.snippets.get(storyID) + 1 : 1;
         this.snippets.put(storyID, snippetID);
         return story.snippets().stream().filter(snippet -> snippet.id() == snippetID).findFirst().map(Snippet::text).orElse("");
      }

      @Override
      public void addSavedData(ResourceLocation key, String data) {
         this.saves.computeIfAbsent(key, k -> new ArrayList<>()).add(data);
      }

      @Override
      public List<String> getSaves(ResourceLocation key) {
         return Collections.unmodifiableList(this.saves.computeIfAbsent(key, k -> new ArrayList<>()));
      }

      @Override
      public CompoundTag serializeNBT() {
         CompoundTag compoundTag = new CompoundTag();
         UNLOCKED_STORIES_CODEC
               .encodeStart(NbtOps.INSTANCE, this.getUnlockedSnippets())
               .resultOrPartial(Shrines.LOGGER::error).ifPresent(tag ->
                     compoundTag.put("unlockedStorySnippets", tag));
         SAVES_CODEC.encodeStart(NbtOps.INSTANCE, this.saves).resultOrPartial(Shrines.LOGGER::error).ifPresent(tag ->
               compoundTag.put("structureBounds", tag));
         return compoundTag;
      }

      @Override
      public void deserializeNBT(@NotNull CompoundTag nbt) {
         this.snippets = new HashMap<>();
         UNLOCKED_STORIES_CODEC.decode(NbtOps.INSTANCE, nbt.get("unlockedStorySnippets")).resultOrPartial(Shrines.LOGGER::error).map(Pair::getFirst).ifPresent(list ->
               list.forEach(pair -> this.snippets.put(pair.getFirst(), pair.getSecond())));
         this.saves = SAVES_CODEC.decode(NbtOps.INSTANCE, nbt.get("structureBounds")).resultOrPartial(Shrines.LOGGER::error).map(Pair::getFirst).map(HashMap::new).orElse(new HashMap<>());
      }
   }
}
