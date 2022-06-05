/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.gui.misc;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Silverminer
 */
@OnlyIn(Dist.CLIENT)
public class SingleSelectList<T> extends ExtendedList<SingleSelectList<T>.Entry> {
   protected static final Logger LOGGER = LogManager.getLogger(SingleSelectList.class);
   private final Supplier<List<T>> possibleValues;
   private final Function<T, String> valueToString;
   private final Consumer<T> valueUpdater;
   @org.jetbrains.annotations.Nullable
   private T value;

   public SingleSelectList(Minecraft mc, int width, int height, int headerSize, int footerSize, int elementSize, @Nullable T value, Supplier<List<T>> possibleValues, Function<T, String> valueToString, Consumer<T> valueUpdater) {
      super(mc, width, height, headerSize, footerSize, elementSize);
      this.value = value;
      this.possibleValues = possibleValues;
      this.valueToString = valueToString;
      this.valueUpdater = valueUpdater;
   }

   public void refreshList(@NotNull String search) {
      this.clearEntries();

      List<Pair<T, String>> pools = new ArrayList<>(this.possibleValues.get()).stream().map(v -> Pair.of(v, this.valueToString.apply(v))).sorted(Comparator.comparing(Pair::getSecond)).collect(Collectors.toList());
      String s = search.toLowerCase(Locale.ROOT);

      for (Pair<T, String> possibleValue : pools) {
         if (possibleValue.getSecond().toLowerCase(Locale.ROOT).contains(s)) {
            SingleSelectList<T>.Entry entry = new SingleSelectList<T>.Entry(possibleValue);
            this.addEntry(entry);
            if (Objects.equals(possibleValue.getFirst(), this.value)) {
               this.setSelected(entry);
            }
         }
      }
   }

   public void setSelected(@Nullable Entry entry) {
      if (entry != null) {
         this.value = entry.getValue().getFirst();
      } else {
         this.value = null;
      }
      this.valueUpdater.accept(this.value);
      super.setSelected(entry);
   }

   public @Nullable T getValue() {
      return this.value;
   }

   public class Entry extends ExtendedList.AbstractListEntry<SingleSelectList<T>.Entry> {
      protected final Minecraft minecraft;
      protected final Pair<T, String> value;

      public Entry(Pair<T, String> value) {
         this.value = value;
         this.minecraft = Minecraft.getInstance();
      }

      @ParametersAreNonnullByDefault
      public void render(MatrixStack ms, int p_230432_2_, int top, int left, int p_230432_5_, int p_230432_6_,
                         int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
         StringTextComponent header = new StringTextComponent(this.value.getSecond());

         this.minecraft.font.draw(ms, header, left, top + 1, 0xffffff);
      }

      public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmount) {
         SingleSelectList.this.setSelected(this);
         return true;
      }

      protected Pair<T, String> getValue() {
         return this.value;
      }
   }
}