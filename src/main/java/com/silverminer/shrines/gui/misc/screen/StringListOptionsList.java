/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.gui.misc.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.gui.misc.buttons.BooleanValueButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author Silverminer
 */
@OnlyIn(Dist.CLIENT)
public class StringListOptionsList extends ObjectSelectionList<StringListOptionsList.Entry> {
   protected static final Logger LOGGER = LogManager.getLogger();
   private final StringListOptionsScreen screen;

   public StringListOptionsList(StringListOptionsScreen screen, Minecraft mc, int p_i49846_3_, int p_i49846_4_,
                                int p_i49846_5_, int p_i49846_6_, int p_i49846_7_, Supplier<String> search) {
      super(mc, p_i49846_3_, p_i49846_4_, p_i49846_5_, p_i49846_6_, p_i49846_7_);
      this.screen = screen;

      this.refreshList(search);
   }

   public void refreshList(Supplier<String> search) {
      this.clearEntries();

      List<String> options = this.screen.possibleValues;
      Collections.sort(options);
      String s = search.get().toLowerCase(Locale.ROOT);

      for (String opt : options) {
         if (opt.toLowerCase(Locale.ROOT).contains(s)) {
            this.addEntry(new StringListOptionsList.Entry(opt, this.screen.selectedValues.contains(opt)));
         }
      }

   }

   protected int getScrollbarPosition() {
      return this.width - 5;
   }

   public int getRowWidth() {
      return this.width - 10;
   }

   protected boolean isFocused() {
      return this.screen.getFocused() == this;
   }

   public void setSelected(@Nullable StringListOptionsList.Entry entry) {
      super.setSelected(entry);
   }

   public Optional<StringListOptionsList.Entry> getSelectedOpt() {
      return Optional.ofNullable(this.getSelected());
   }

   @OnlyIn(Dist.CLIENT)
   public final class Entry extends ObjectSelectionList.Entry<StringListOptionsList.Entry>
         implements ContainerEventHandler {
      private final ArrayList<GuiEventListener> children = Lists.newArrayList();
      private final Minecraft minecraft;
      private final BooleanValueButton button;
      private final String opt;
      @Nullable
      private GuiEventListener focused;
      private boolean dragging;
      private boolean active;

      public Entry(String option, boolean active) {
         this.opt = option;
         this.active = active;
         this.minecraft = Minecraft.getInstance();
         this.button = new BooleanValueButton(0, 0, TextComponent.EMPTY, (button) -> {
            this.setActive(((BooleanValueButton) button).value);
            if (this.active) {
               if (!StringListOptionsList.this.screen.selectedValues.contains(opt))
                  StringListOptionsList.this.screen.selectedValues.add(opt);
            } else {
               StringListOptionsList.this.screen.selectedValues.remove(opt);
            }
         }, active);
         this.children.add(this.button);
      }

      private void setActive(boolean active) {
         this.active = active;
      }

      @ParametersAreNonnullByDefault
      @Override
      public void render(PoseStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                         boolean isHot, float partialTicks) {
         int descriptionTop = top + (StringListOptionsList.this.itemHeight - minecraft.font.lineHeight) / 2;
         minecraft.font.drawShadow(ms, this.opt, left, descriptionTop, 16777215);
         this.button.x = left + (width / 2);
         this.button.y = top + (StringListOptionsList.this.itemHeight - this.button.getHeight()) / 2;
         this.button.render(ms, mouseX, mouseY, partialTicks);
      }

      @Nonnull
      @Override
      public List<? extends GuiEventListener> children() {
         return children;
      }

      public boolean isDragging() {
         return this.dragging;
      }

      public void setDragging(boolean p_231037_1_) {
         this.dragging = p_231037_1_;
      }

      @Nullable
      public GuiEventListener getFocused() {
         return this.focused;
      }

      public void setFocused(@Nullable GuiEventListener p_231035_1_) {
         this.focused = p_231035_1_;
      }

      @Override
      public Component getNarration() {
         return new TextComponent(this.opt);
      }
   }
}