/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.worldgen.structures.spawn_criteria;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.ShrinesMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class SpawnCriteriaType extends ForgeRegistryEntry<SpawnCriteriaType> {
   public static final ResourceKey<Registry<SpawnCriteriaType>> REGISTRY =
         ResourceKey.createRegistryKey(ShrinesMod.location("spawn_criteria_type"));
   private final Codec<? extends SpawnCriteria> codec;

   public SpawnCriteriaType(Codec<? extends SpawnCriteria> codec) {
      this.codec = codec;
   }

   public Codec<? extends SpawnCriteria> codec() {
      return this.codec;
   }
}