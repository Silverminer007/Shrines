/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets.edit.templates;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.gui.misc.IUpdatableScreen;
import com.silverminer.shrines.packages.datacontainer.StructureTemplate;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

public class TemplatesList extends ObjectSelectionList<TemplatesList.Entry> {
   protected static final Logger LOGGER = LogManager.getLogger(TemplatesList.class);
   protected final IUpdatableScreen screen;
   protected final StructuresPackageWrapper packet;

   public TemplatesList(Minecraft p_i45010_1_, int p_i45010_2_, int p_i45010_3_, int p_i45010_4_, int p_i45010_5_, int p_i45010_6_, Supplier<String> search, StructuresPackageWrapper packet, IUpdatableScreen screen) {
      super(p_i45010_1_, p_i45010_2_, p_i45010_3_, p_i45010_4_, p_i45010_5_, p_i45010_6_);
      this.packet = packet;
      this.screen = screen;
      this.refreshList(search);
   }

   public void refreshList(Supplier<String> search) {
      this.clearEntries();
      List<StructureTemplate> templates = this.packet.getTemplates().getElementsAsList();
      templates.sort(Comparator.comparing(StructureTemplate::getTemplateLocation));
      String s = search.get().toLowerCase(Locale.ROOT);

      for (StructureTemplate template : templates) {
         if (template.getTemplateLocation().toString().toLowerCase(Locale.ROOT).contains(s)) {
            this.addEntry(new Entry(template));
         }
      }
   }

   protected int getScrollbarPosition() {
      return super.getScrollbarPosition() + 60;
   }

   public int getRowWidth() {
      return super.getRowWidth() + 160;
   }

   protected boolean isFocused() {
      return this.screen.getFocused() == this;
   }

   public void setSelected(@Nullable TemplatesList.Entry entry) {
      this.screen.updateButtonStatus(true);
      super.setSelected(entry);
   }

   public Optional<TemplatesList.Entry> getSelectedOpt() {
      return Optional.ofNullable(this.getSelected());
   }

   public class Entry extends ObjectSelectionList.Entry<TemplatesList.Entry> {
      protected final Minecraft minecraft;
      protected final StructureTemplate template;

      public Entry(StructureTemplate template) {
         this.template = template;
         this.minecraft = Minecraft.getInstance();
      }

      @ParametersAreNonnullByDefault
      @Override
      public void render(PoseStack ms, int p_230432_2_, int top, int left, int p_230432_5_, int p_230432_6_,
                         int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
         this.minecraft.font.draw(ms, template.getTemplateLocation().toString(), left, top + 1, 0xffffff);
      }

      public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmount) {
         TemplatesList.this.setSelected(this);
         return true;
      }

      public void rename() {

      }

      public StructureTemplate getTemplate() {
         return template;
      }

      @Override
      public @NotNull Component getNarration() {
         return new TextComponent(this.template.getTemplateLocation().toString());
      }
   }
}
