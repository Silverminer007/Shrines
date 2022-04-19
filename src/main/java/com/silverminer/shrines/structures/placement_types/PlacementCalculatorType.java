/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.structures.placement_types;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.Shrines;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class PlacementCalculatorType extends ForgeRegistryEntry<PlacementCalculatorType> {
   public static final ResourceKey<Registry<PlacementCalculatorType>> REGISTRY =
         ResourceKey.createRegistryKey(Shrines.location("placement_calculator_type"));
   private final Codec<? extends PlacementCalculator> codec;

   public PlacementCalculatorType(Codec<? extends PlacementCalculator> codec) {
      this.codec = codec;
   }

   public Codec<? extends PlacementCalculator> codec() {
      return this.codec;
   }
}
