/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.misc;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.utils.ClientUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SingleSelectListScreen<T> extends Screen {
   private final Screen lastScreen;
   private final Supplier<List<T>> possibleValues;
   private final Consumer<T> valueUpdater;
   private final Function<T, String> valueToString;
   protected TextFieldWidget searchBox;
   protected SingleSelectList<T> singleSelectList;
   @Nullable
   private T value;

   public SingleSelectListScreen(Screen lastScreen, @NotNull ITextComponent pTitle, @Nullable T value, @NotNull Supplier<List<T>> possibleValues, @NotNull Consumer<T> valueUpdater, @NotNull Function<T, String> valueToString) {
      super(pTitle);
      this.lastScreen = lastScreen;
      this.value = value;
      this.possibleValues = possibleValues;
      this.valueUpdater = valueUpdater;
      this.valueToString = valueToString;
   }

   public void init() {
      Objects.requireNonNull(this.minecraft);
      this.singleSelectList = new SingleSelectList<>(minecraft, this.width, this.height, 26, this.height - 26, 12, this.value, this.possibleValues, this.valueToString, v -> this.value = v);
      this.searchBox = new TextFieldWidget(this.font, (this.width / 4) * 3, 3, 100, 20, this.searchBox, new StringTextComponent(""));
      this.searchBox.setResponder(singleSelectList::refreshList);
      this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), StringTextComponent.EMPTY));
      this.addButton(new Button(this.width / 2 - 50, this.height - 22, 100, 20,
            new TranslationTextComponent("gui.shrines.save"), (button) -> {
         this.valueUpdater.accept(this.singleSelectList.getValue());
         this.minecraft.setScreen(this.lastScreen);
      }));
      this.children.add(searchBox);
      this.children.add(this.singleSelectList);
      this.setInitialFocus(this.searchBox);
      this.singleSelectList.refreshList(this.searchBox.getValue());
   }

   @ParametersAreNonnullByDefault
   public void render(MatrixStack ms, int mouseX, int mouseY, float p_230430_4_) {
      this.singleSelectList.render(ms, mouseX, mouseY, p_230430_4_);
      drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
      super.render(ms, mouseX, mouseY, p_230430_4_);
   }
}