/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.structures.placement_types;

import com.mojang.serialization.Codec;
import com.ygdevs.shrines_arch.Shrines;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public record PlacementCalculatorType(
      Codec<? extends PlacementCalculator> codec) {
   public static final ResourceKey<Registry<PlacementCalculatorType>> REGISTRY =
         ResourceKey.createRegistryKey(Shrines.location("placement_calculator_type"));

}
