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
import com.silverminer.shrines.registries.TriggerTypeRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class AllMatchTrigger implements Trigger {
   public static final Codec<AllMatchTrigger> CODEC = Codec.list(Trigger.CODEC).comapFlatMap(to -> DataResult.success(new AllMatchTrigger(to)), from -> from.triggerList);
   private final List<Trigger> triggerList;

   public AllMatchTrigger(List<Trigger> triggerList) {
      this.triggerList = triggerList;
   }

   @Override
   public TriggerType getType() {
      return TriggerTypeRegistry.ALL_MATCH.get();
   }

   @Override
   public boolean matches(Level level, Player player) {
      return this.triggerList.stream().allMatch(trigger -> trigger.matches(level, player));
   }
}