/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.stories;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.capabilities.CapabilityManager;
import com.silverminer.shrines.registries.TriggerTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BiomeTrigger implements Trigger {
   public static final Codec<BiomeTrigger> CODEC = RecordCodecBuilder.create(biomeTriggerInstance ->
         biomeTriggerInstance.group(
               ResourceLocation.CODEC.fieldOf("biome").forGetter(BiomeTrigger::getBiomes),
               Codec.BOOL.fieldOf("first").forGetter(BiomeTrigger::isFirst)
         ).apply(biomeTriggerInstance, BiomeTrigger::new));
   private final ResourceLocation biome;
   private final boolean first;

   public BiomeTrigger(ResourceLocation biome, boolean first) {
      this.biome = biome;
      this.first = first;
   }

   @Override
   public TriggerType getType() {
      return TriggerTypeRegistry.BIOME.get();
   }

   @Override
   public boolean matches(@NotNull Level level, @NotNull Player player) {
      return level.getBiome(player.blockPosition()).is(this.biome) &&
            player.getCapability(CapabilityManager.STORY).map(istorycapacibilty -> this.checkBiome(istorycapacibilty, player)).orElse(!this.first);
   }

   private boolean checkBiome(CapabilityManager.@NotNull IStoryCapability capability, @NotNull Player player) {
      ResourceLocation biomeKey = Shrines.location("biomes");
      if (capability.getSaves(biomeKey).stream().anyMatch(biome -> this.biome.toString().equals(biome))) {
         return !this.first;
      }
      capability.addSavedData(biomeKey, this.biome.toString());
      return true;
   }

   protected ResourceLocation getBiomes() {
      return biome;
   }

   protected boolean isFirst() {
      return first;
   }
}