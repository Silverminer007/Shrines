/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets.edit.pools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.packages.datacontainer.TemplatePool;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Silverminer
 */
@OnlyIn(Dist.CLIENT)
public class ConfigurePoolList extends ObjectSelectionList<ConfigurePoolList.Entry> {
   protected static final Logger LOGGER = LogManager.getLogger(ConfigurePoolList.class);
   private final ConfigurePoolScreen screen;
   private final TemplatePool templatePool;

   public ConfigurePoolList(Minecraft mc, int p_i49846_3_, int p_i49846_4_,
                            int p_i49846_5_, int p_i49846_6_, int p_i49846_7_, Supplier<String> search, ConfigurePoolScreen screen, TemplatePool templatePool) {
      super(mc, p_i49846_3_, p_i49846_4_, p_i49846_5_, p_i49846_6_, p_i49846_7_);
      this.screen = screen;
      this.templatePool = templatePool;

      this.refreshList(search);
   }

   public void refreshList(Supplier<String> search) {
      this.clearEntries();

      List<TemplatePool.Entry> templatePoolEntries = this.templatePool.getEntries();
      Collections.sort(templatePoolEntries);
      String s = search.get().toLowerCase(Locale.ROOT);

      for (TemplatePool.Entry entry : templatePoolEntries) {
         if (entry.getTemplate().toString().toLowerCase(Locale.ROOT).contains(s)) {
            this.addEntry(new ConfigurePoolList.Entry(entry));
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

   public void setSelected(@Nullable ConfigurePoolList.Entry entry) {
      this.screen.updateButtonStatus(true);
      super.setSelected(entry);
   }

   public Optional<ConfigurePoolList.Entry> getSelectedOpt() {
      return Optional.ofNullable(this.getSelected());
   }

   @OnlyIn(Dist.CLIENT)
   public final class Entry extends ObjectSelectionList.Entry<ConfigurePoolList.Entry> {
      private final Minecraft minecraft;
      private final TemplatePool.Entry entry;
      private long lastClickTime;

      public Entry(TemplatePool.Entry entry) {
         this.entry = entry;
         this.minecraft = Minecraft.getInstance();
      }

      @ParametersAreNonnullByDefault
      public void render(PoseStack ms, int p_230432_2_, int top, int left, int p_230432_5_, int p_230432_6_,
                         int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
         TextComponent header = new TextComponent(entry.getTemplate().toString());
         String s1 = "Weight: " + this.entry.getWeight() + "    " + (this.entry.isTerrain_matching() ? "Terrain Matching" : "Rigid");

         this.minecraft.font.draw(ms, header, left, top + 1, 0xffffff);
         this.minecraft.font.draw(ms, s1, left, top + 9 + 3, 0x808080);
      }

      public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmount) {
         ConfigurePoolList.this.setSelected(this);
         if (Util.getMillis() - this.lastClickTime < 250L) {
            this.configure();
            return true;
         } else {
            this.lastClickTime = Util.getMillis();
            return false;
         }
      }

      public void configure() {
         this.minecraft.setScreen(new ConfigurePoolEntryScreen(ConfigurePoolList.this.screen, this.getPoolEntry()));
      }

      public TemplatePool.Entry getPoolEntry() {
         return this.entry;
      }

      @Override
      public @NotNull
      Component getNarration() {
         return new TextComponent(this.entry.getTemplate().toString());
      }
   }
}