/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

public record CalculationError(String header, String text) {
   public static final CalculationError PLAYER_MISSING_PERMISSION = new CalculationError("Failed to run action on server", "Player doesn't have permission!");

   public CalculationError(String header, String text, @NotNull Throwable cause) {
      this(header, text, cause.getClass().getName() + ": " + cause.getMessage());
   }

   public CalculationError(String header, String text, Object... objects) {
      this(header, String.format(text, objects));
   }

   public CalculationError(CompoundTag tag) {
      this(tag.getString("header"), tag.getString("text"));
   }

   public String toString() {
      return header() + ". " + text();
   }

   public @NotNull CompoundTag save() {
      CompoundTag tag = new CompoundTag();
      tag.putString("header", header());
      tag.putString("text", text());
      return tag;
   }
}