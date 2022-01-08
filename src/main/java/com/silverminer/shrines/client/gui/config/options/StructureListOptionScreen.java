/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.client.gui.config.options;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.silverminer.shrines.client.gui.config.widgets.buttons.CheckboxButtonEx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public abstract class StructureListOptionScreen extends Screen {
   protected static final Logger LOG = LogManager.getLogger(StructureListOptionScreen.class);
   private static final int PADDING = 5;
   protected Screen parent;
   protected ModOptionList optionList;
   protected List<String> possibleValues;
   protected List<String> activeValues = Lists.newArrayList();
   protected String option;

   public StructureListOptionScreen(Screen parent, ITextComponent title, List<String> possibleValues,
                                    List<? extends String> activeValues, String option) {
      super(title);

      this.parent = parent;
      this.possibleValues = possibleValues;
      this.activeValues = Lists.newArrayList(activeValues);
      this.option = option;
   }

   @Override
   public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(ms);
      this.optionList.render(ms, mouseX, mouseY, partialTicks);
      RenderSystem.disableLighting(); // Rendering the tooltip enables lighting but buttons etc. assume lighting to be
      // disabled.
      super.render(ms, mouseX, mouseY, partialTicks);
      minecraft.font.draw(ms, title.getString(), PADDING, PADDING, 16777215);
   }

   @Override
   public void init(Minecraft mc, int width, int height) {
      super.init(mc, width, height);

      int titleHeight = mc.font.wordWrapHeight(title.getString(), width - 2 * PADDING);
      int paddedTitleHeight = titleHeight + PADDING * 2;

      addButton(width - 120 - 2 * PADDING, 0, 60, paddedTitleHeight, new StringTextComponent("Back"),
            button -> mc.setScreen(parent));
      addButton(width - 60 - PADDING, 0, 60, paddedTitleHeight, new StringTextComponent("Save"), button -> {
         this.optionList.commitChanges();
         this.save();
         mc.setScreen(parent);
      });

      int optionListHeaderHeight = titleHeight + 2 * PADDING;
      this.optionList = new ModOptionList(this.possibleValues, this.activeValues, minecraft, width, height,
            optionListHeaderHeight, height - optionListHeaderHeight, 26);
      this.children.add(optionList);
   }

   private void addButton(int x, int y, int width, int height, ITextComponent label, Button.IPressable pressHandler) {
      Button button = new ExtendedButton(x, y, width, height, label, pressHandler);

      children.add(button);
      buttons.add(button);
   }

   protected abstract void save();

   @Override
   public void tick() {
      super.tick();
      optionList.tick();
   }

   @OnlyIn(Dist.CLIENT)
   public class ModOptionList extends AbstractOptionList<ModOptionList.Entry> {
      private static final int LEFT_RIGHT_BORDER = 30;

      public ModOptionList(List<String> possibleValues, List<String> activeOptions, Minecraft mc, int width,
                           int height, int top, int bottom, int itemHeight) {
         super(mc, width, height, top, bottom, itemHeight);

         for (String v : possibleValues) {
            this.addEntry(new OptionEntry(v, activeOptions.contains(v) || activeOptions.contains(v.replaceAll("minecraft:", ""))));
         }
      }

      public void tick() {
         for (IGuiEventListener child : this.children()) {
            ((Entry) child).tick();
         }
      }

      @Override
      public int getRowWidth() {
         return width - LEFT_RIGHT_BORDER * 2;
      }

      @Override
      public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
         super.render(ms, mouseX, mouseY, partialTicks);

         String tooltip = null;

         for (Entry entry : this.children()) {
            tooltip = entry.getTooltip();

            if (!StringUtils.isNullOrEmpty(tooltip)) {
               List<ITextComponent> comment = Arrays.asList(tooltip.split("\n")).stream()
                     .map(s -> new StringTextComponent(s)).collect(Collectors.toList());
               renderComponentTooltip(ms, comment, mouseX, mouseY);

               break;
            }
         }
      }

      @Override
      protected int getScrollbarPosition() {
         return width - LEFT_RIGHT_BORDER;
      }

      @Override
      public boolean mouseClicked(double x, double y, int button) {
         if (super.mouseClicked(x, y, button)) {
            IGuiEventListener focusedChild = getFocused();

            for (IGuiEventListener child : this.children()) {
               if (child != focusedChild)
                  ((Entry) child).clearFocus();
            }

            return true;
         }

         return false;
      }

      public void commitChanges() {
         StructureListOptionScreen.this.activeValues.clear();
         for (Entry entry : this.children()) {
            entry.commitChanges();
         }
      }

      @OnlyIn(Dist.CLIENT)
      public abstract class Entry extends AbstractOptionList.Entry<StructureListOptionScreen.ModOptionList.Entry> {
         public abstract void clearFocus();

         public abstract void commitChanges();

         public abstract void tick();

         public abstract String getTooltip();
      }

      @OnlyIn(Dist.CLIENT)
      public class OptionEntry extends Entry {
         private CheckboxButtonEx checkBox;
         private ITextComponent entry;
         private String entryS;

         public OptionEntry(String entry, boolean active) {
            this.entry = new StringTextComponent(entry);
            this.entryS = entry;
            this.checkBox = new CheckboxButtonEx(0, 0, 10, 20, new StringTextComponent(""), active);
         }

         @Override
         public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX,
                            int mouseY, boolean isHot, float partialTicks) {
            if (this.checkBox != null) {
               this.checkBox.x = left + (width / 2) + PADDING;
               this.checkBox.y = top;
               this.checkBox.render(ms, mouseX, mouseY, partialTicks);
            }
            String description = this.entry.getString();
            int descriptionWidth = minecraft.font.width(description);
            int descriptionLeft = left + (width / 2) - descriptionWidth - PADDING;
            int descriptionTop = top + (itemHeight / 2) - PADDING - minecraft.font.lineHeight / 2 + 2;
            minecraft.font.drawShadow(ms, description, descriptionLeft, descriptionTop, 16777215);
         }

         @Override
         public List<? extends IGuiEventListener> children() {
            return Lists.newArrayList(this.checkBox);
         }

         @Override
         public void clearFocus() {
         }

         @Override
         public void commitChanges() {
            String s = this.entryS;
            if (this.checkBox.getValue()) {
               if (!StructureListOptionScreen.this.activeValues.contains(s)) {
                  StructureListOptionScreen.this.activeValues.add(s);
               }
            }
         }

         @Override
         public void tick() {
         }

         @Override
         public String getTooltip() {
            return "";
         }
      }
   }
}
