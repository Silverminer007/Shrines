package com.silverminer.shrines.utils;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

public record CalculationError(String header, String text) {
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