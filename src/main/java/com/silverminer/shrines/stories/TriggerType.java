/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.stories;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.Shrines;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface TriggerType {
   ResourceKey<? extends Registry<TriggerType>> REGISTRY = ResourceKey.createRegistryKey(Shrines.location("stories/trigger"));
   Codec<? extends Trigger> codec();
}
