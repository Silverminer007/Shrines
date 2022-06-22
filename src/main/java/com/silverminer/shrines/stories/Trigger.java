/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.stories;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.registries.TriggerTypeRegistry;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface Trigger {
   Codec<Trigger> CODEC = ExtraCodecs.lazyInitializedCodec(() -> TriggerTypeRegistry.FORGE_REGISTRY_SUPPLIER.get().getCodec()).dispatch("type", Trigger::getType, TriggerType::codec);

   TriggerType getType();

   boolean matches(Level level, Player player);
}