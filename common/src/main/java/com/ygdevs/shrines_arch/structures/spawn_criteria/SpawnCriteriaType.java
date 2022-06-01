/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.structures.spawn_criteria;

import com.mojang.serialization.Codec;
import com.ygdevs.shrines_arch.Shrines;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public record SpawnCriteriaType(
      Codec<? extends SpawnCriteria> codec) {
   public static final ResourceKey<Registry<SpawnCriteriaType>> REGISTRY =
         ResourceKey.createRegistryKey(Shrines.location("spawn_criteria_type"));

}