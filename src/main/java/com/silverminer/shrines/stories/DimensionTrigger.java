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
import com.silverminer.shrines.capabilities.CapabilityManager;
import com.silverminer.shrines.registries.TriggerTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DimensionTrigger implements Trigger {
   public static final Codec<DimensionTrigger> CODEC = RecordCodecBuilder.create(dimensionTriggerInstance ->
         dimensionTriggerInstance.group(
               ResourceLocation.CODEC.fieldOf("dimension").forGetter(DimensionTrigger::getDimension),
               Codec.BOOL.fieldOf("first").forGetter(DimensionTrigger::isFirst)
         ).apply(dimensionTriggerInstance, DimensionTrigger::new));
   private final ResourceLocation dimension;
   private final boolean first;

   public DimensionTrigger(ResourceLocation dimension, boolean first) {
      this.dimension = dimension;
      this.first = first;
   }


   @Override
   public TriggerType getType() {
      return TriggerTypeRegistry.DIMENSION.get();
   }

   @Override
   public boolean matches(@NotNull Level level, @NotNull Player player) {
      return level.dimension().location().equals(this.dimension) &&
            player.getCapability(CapabilityManager.STORY).map(istorycapacibilty -> this.checkDimension(istorycapacibilty, player)).orElse(!this.first);
   }

   private boolean checkDimension(CapabilityManager.@NotNull IStoryCapability capability, @NotNull Player player) {
      ResourceLocation dimensionKey = Shrines.location("dimensions");
      if (capability.getSaves(dimensionKey).stream().anyMatch(biome -> this.dimension.toString().equals(biome))) {
         return !this.first;
      }
      capability.addSavedData(dimensionKey, this.dimension.toString());
      return true;
   }

   protected ResourceLocation getDimension() {
      return dimension;
   }

   protected boolean isFirst() {
      return first;
   }
}