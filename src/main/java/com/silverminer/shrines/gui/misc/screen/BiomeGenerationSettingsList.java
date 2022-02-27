/*
 * Silverminer007
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Silverminer
 */
@OnlyIn(Dist.CLIENT)
public class BiomeGenerationSettingsList extends ObjectSelectionList<BiomeGenerationSettingsList.Entry> {
   protected static final Logger LOGGER = LogManager.getLogger();
   private final BiomeGenerationSettingsScreen screen;

   public BiomeGenerationSettingsList(BiomeGenerationSettingsScreen screen, Minecraft mc, int p_i49846_3_,
                                      int p_i49846_4_, int p_i49846_5_, int p_i49846_6_, int p_i49846_7_, Supplier<String> search) {
      super(mc, p_i49846_3_, p_i49846_4_, p_i49846_5_, p_i49846_6_, p_i49846_7_);
      this.screen = screen;

      this.refreshList(search);
   }

   public void refreshList(Supplier<String> search) {
      this.clearEntries();

      ArrayList<Biome.BiomeCategory> possibleCategories = Lists.newArrayList(Biome.BiomeCategory.values());
      ArrayList<String> selectedCategories = this.screen.selectedBiomeCategories;
      ArrayList<Biome> possibleBiomes = ForgeRegistries.BIOMES.getEntries().stream()
            .map(Map.Entry::getValue).collect(Collectors.toCollection(ArrayList::new));
      ArrayList<String> selectedBiomes = this.screen.selectedBiomes;

      String s = search.get().toLowerCase(Locale.ROOT);

      for (Biome.BiomeCategory opt : possibleCategories) {
         if (opt.getName().toLowerCase(Locale.ROOT).contains(s)) {
            boolean selected = selectedCategories.contains(opt.toString());
            this.addEntry(new BiomeGenerationSettingsList.CategoryEntry(opt, selected));
            if (selected) {
               for (Biome opt1 : possibleBiomes.stream().filter(biome -> biome.getBiomeCategory().equals(opt)).toList()) {
                  boolean selected1 = !selectedBiomes.contains(opt1.toString());
                  this.addEntry(new BiomeGenerationSettingsList.BiomeEntry(opt1, selected1));
               }
            }
         }
      }

   }

   public int getRowWidth() {
      return this.width - 10;
   }

   public void setSelected(@Nullable BiomeGenerationSettingsList.Entry entry) {
      super.setSelected(entry);
   }

   protected int getScrollbarPosition() {
      return this.width - 5;
   }

   protected boolean isFocused() {
      return this.screen.getFocused() == this;
   }

   public Optional<BiomeGenerationSettingsList.Entry> getSelectedOpt() {
      return Optional.ofNullable(this.getSelected());
   }

   @OnlyIn(Dist.CLIENT)
   public abstract class Entry extends ObjectSelectionList.Entry<BiomeGenerationSettingsList.Entry>
         implements ContainerEventHandler {
      protected final Minecraft minecraft;
      protected final BooleanValueButton button;
      protected final String opt;
      protected ArrayList<GuiEventListener> children = Lists.newArrayList();
      @Nullable
      private GuiEventListener focused;
      private boolean dragging;

      public Entry(String option, boolean active) {
         this.opt = option;
         this.minecraft = Minecraft.getInstance();
         this.button = new BooleanValueButton(0, 0, TextComponent.EMPTY, (button) -> {
            this.updateList(((BooleanValueButton) button).value);
            BiomeGenerationSettingsList.this
                  .refreshList(() -> BiomeGenerationSettingsList.this.screen.searchBox.getValue());
         }, active);
         this.children.add(this.button);
      }

      public abstract void updateList(boolean active);

      @Override
      public void render(@NotNull PoseStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                         boolean isHot, float partialTicks) {
         int descriptionTop = top + (BiomeGenerationSettingsList.this.itemHeight - minecraft.font.lineHeight) / 2;
         minecraft.font.drawShadow(ms, this.opt, left, descriptionTop, this.isCategoryOption() ? 0xffffff : 0x999999);
         this.button.x = left + (width / 2);
         this.button.y = top + (BiomeGenerationSettingsList.this.itemHeight - this.button.getHeight()) / 2;
         this.button.render(ms, mouseX, mouseY, partialTicks);
      }

      public abstract boolean isCategoryOption();

      @Override
      public @NotNull List<? extends GuiEventListener> children() {
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
      public @NotNull Component getNarration() {
         return new TextComponent(this.opt);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public class CategoryEntry extends Entry {

      public CategoryEntry(Biome.BiomeCategory option, boolean active) {
         super(option.toString(), active);
      }

      @Override
      public void updateList(boolean active) {
         if (active) {
            BiomeGenerationSettingsList.this.screen.selectedBiomeCategories.add(this.opt);
            for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
               if (biome.getBiomeCategory().toString().equals(this.opt)) {
                  ResourceLocation resourceLocation = biome.getRegistryName();
                  if (resourceLocation != null) {
                     BiomeGenerationSettingsList.this.screen.selectedBiomes.remove(resourceLocation.toString());
                  }
               }
            }
         } else {
            BiomeGenerationSettingsList.this.screen.selectedBiomeCategories.remove(this.opt);
         }
      }

      @Override
      public boolean isCategoryOption() {
         return true;
      }

   }

   @OnlyIn(Dist.CLIENT)
   public class BiomeEntry extends Entry {

      public BiomeEntry(Biome option, boolean active) {
         super(Optional.ofNullable(option.getRegistryName()).orElse(new ResourceLocation("error", "unknown_biome")).toString(), active);
      }

      @Override
      public void updateList(boolean active) {
         if (active) {
            BiomeGenerationSettingsList.this.screen.selectedBiomes.remove(this.opt);
         } else {
            BiomeGenerationSettingsList.this.screen.selectedBiomes.add(this.opt);
         }
      }

      @Override
      public boolean isCategoryOption() {
         return false;
      }

   }
}