/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets.edit.structures;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.gui.misc.buttons.BooleanValueButton;
import com.silverminer.shrines.gui.misc.screen.BiomeGenerationSettingsScreen;
import com.silverminer.shrines.gui.misc.screen.StringListOptionsScreen;
import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.configuration.ConfigOptions;
import com.silverminer.shrines.packages.datacontainer.StructureData;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ConfigureStructureList extends ObjectSelectionList<ConfigureStructureList.Entry<?>> {
   protected final StructuresPackageWrapper packet;
   protected StructureData structure;
   protected ConfigureStructureScreen screen;

   public ConfigureStructureList(Minecraft p_i45010_1_, int p_i45010_2_, int p_i45010_3_, int p_i45010_4_,
                                 int p_i45010_5_, int p_i45010_6_, ConfigureStructureScreen screen, StructureData structure, StructuresPackageWrapper packet) {
      super(p_i45010_1_, p_i45010_2_, p_i45010_3_, p_i45010_4_, p_i45010_5_, p_i45010_6_);
      this.screen = screen;
      this.structure = structure;
      this.packet = packet;
      this.refreshList();
   }

   public void refreshList() {
      this.clearEntries();
      // TODO Add Category Entry to expand and collapse child entries
      // Potential child entry are spawn configurations and variation configurations
      // TODO Add Variation configuration options
      this.addEntry(
            new StringEntry(ConfigOptions.LATEST.name(), this.structure::getName, this.structure::setName, 256));
      this.addEntry(new ResourceLocationEntry(ConfigOptions.LATEST.key(), this.structure::getKey, this.structure::setKey, 256));
      this.addEntry(
            new BooleanEntry(ConfigOptions.LATEST.generate(), this.structure::isGenerate, this.structure::setGenerate));
      this.addEntry(new BooleanEntry(ConfigOptions.LATEST.transformLand(), this.structure::isTransformLand,
            this.structure::setTransformLand));
      this.addEntry(new DoubleEntry(ConfigOptions.LATEST.spawnChance(), this.structure::getSpawn_chance,
            this.structure::setSpawn_chance));
      this.addEntry(
            new IntegerEntry(ConfigOptions.LATEST.distance(), this.structure::getDistance, this.structure::setDistance, false, false));
      this.addEntry(new IntegerEntry(ConfigOptions.LATEST.separation(), this.structure::getSeparation,
            this.structure::setSeparation, false, false));
      this.addEntry(new IntegerEntry(ConfigOptions.LATEST.seedModifier(), this.structure::getSeed_modifier,
            this.structure::setSeed_modifier, false, true));
      this.addEntry(new IntegerEntry(ConfigOptions.LATEST.heightOffset(), this.structure::getHeight_offset,
            this.structure::setHeight_offset, true, true));
      this.addEntry(new BiomeListsEntry(ConfigOptions.LATEST.biomeBlacklist(), this.structure::getBiome_blacklist,
            this.structure::setBiome_blacklist,
            this.structure::getBiome_category_whitelist, this.structure::setBiome_category_whitelist));
      this.addEntry(new StringListEntry(ConfigOptions.LATEST.dimensionWhitelist(), this.structure::getDimension_whitelist,
            this.structure::setDimension_whitelist, PackageManagerProvider.CLIENT.getAvailableDimensions()));
      this.addEntry(new PoolEntry(ConfigOptions.LATEST.startPool(), this.structure::getStart_pool, this.structure::setStart_pool));
      this.addEntry(new ResourceLocationEntry(ConfigOptions.LATEST.novel(), this.structure::getNovel, this.structure::setNovel, Integer.MAX_VALUE));
      this.addEntry(new IntegerEntry(ConfigOptions.LATEST.jigsawMaxDepth(), this.structure::getJigsawMaxDepth, this.structure::setJigsawMaxDepth, false, false));
      this.addEntry(new ResourceLocationEntry(ConfigOptions.LATEST.iconPath(), this.structure::getIconPath, this.structure::setIconPath, Integer.MAX_VALUE));
   }

   public int getRowWidth() {
      return this.width - 10;
   }

   protected int getScrollbarPosition() {
      return this.width - 5;
   }

   protected boolean isFocused() {
      return this.screen.getFocused() == this;
   }

   public Optional<ConfigureStructureList.Entry<?>> getSelectedOpt() {
      return Optional.ofNullable(this.getSelected());
   }

   @OnlyIn(Dist.CLIENT)
   public abstract class Entry<T>
         extends ObjectSelectionList.Entry<ConfigureStructureList.Entry<?>> implements ContainerEventHandler {
      protected final String option;
      protected final Consumer<T> setter;
      protected final Supplier<T> getter;
      protected Minecraft minecraft;
      protected ArrayList<GuiEventListener> children = Lists.newArrayList();
      protected T value;
      @Nullable
      private GuiEventListener focused;
      private boolean dragging;

      public Entry(String option, Supplier<T> value, Consumer<T> saver) {
         this.option = option;
         this.value = value.get();
         this.setter = saver;
         this.getter = value;
         this.minecraft = Minecraft.getInstance();
      }

      public void save() {
         this.setter.accept(this.value);
      }

      @Override
      @ParametersAreNonnullByDefault
      public void render(PoseStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                         boolean isHot, float partialTicks) {
         String description = new TranslatableComponent(ConfigOptions.LATEST.comments_prefix() + this.option).getString();
         int descriptionWidth = minecraft.font.width(description);
         int descriptionTop = top + (ConfigureStructureList.this.itemHeight - minecraft.font.lineHeight) / 2;
         minecraft.font.drawShadow(ms, description, left, descriptionTop, 16777215);

         if ((mouseX >= left) && (mouseX < (left + descriptionWidth))
               && (mouseY >= descriptionTop) && (mouseY < (descriptionTop + minecraft.font.lineHeight))) {
            ConfigureStructureList.this.screen.tooltip = new TranslatableComponent(ConfigOptions.LATEST.comments_prefix() + this.option + ConfigOptions.LATEST.comments_suffix());
         }
      }

      @Override
      @Nonnull
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
      public @NotNull Component getNarration() {
         return new TextComponent(this.option);
      }
   }

   public class TextFieldEntry<T> extends Entry<T> {
      protected final EditBox textField;

      public TextFieldEntry(String option, Supplier<T> value, Consumer<T> saver, Function<T, String> toStringMapper, Function<String, T> fromStringMapper, int maxLength) {
         this(option, value, saver, toStringMapper, fromStringMapper, Objects::nonNull, maxLength);
      }

      public TextFieldEntry(String option, Supplier<T> value, Consumer<T> saver, Function<T, String> toStringMapper, Function<String, T> fromStringMapper, Function<T, Boolean> validator, int maxLength) {
         super(option, value, saver);
         this.textField = new EditBox(this.minecraft.font, 0, 0, 200, 20, TextComponent.EMPTY);
         this.textField.setMaxLength(maxLength);
         this.textField.setValue(toStringMapper.apply(value.get()));
         this.textField.setResponder(text -> {
            try {
               this.textField.setTextColor(0xffffff);
               T newValue = fromStringMapper.apply(text);
               if (validator.apply(newValue)) {
                  this.setter.accept(newValue);
               }
            } catch (Throwable t) {
               this.textField.setTextColor(0xff0000);
            }
         });
         this.children.add(textField);
      }

      @Override
      @ParametersAreNonnullByDefault
      public void render(PoseStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                         boolean isHot, float partialTicks) {
         super.render(ms, index, top, left, width, height, mouseX, mouseY, isHot, partialTicks);
         this.textField.x = left + (width / 2);
         this.textField.y = top + (ConfigureStructureList.this.itemHeight - this.textField.getHeight()) / 2;
         this.textField.render(ms, mouseX, mouseY, partialTicks);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public class BooleanEntry extends Entry<Boolean> {
      protected final BooleanValueButton button;

      public BooleanEntry(String option, Supplier<Boolean> value, Consumer<Boolean> saver) {
         super(option, value, saver);
         this.button = new BooleanValueButton(0, 0, TextComponent.EMPTY, (button) -> {
            this.value = ((BooleanValueButton) button).value;
            this.setter.accept(this.value);
         }, this.value);
         this.children.add(this.button);
      }

      @Override
      @ParametersAreNonnullByDefault
      public void render(PoseStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                         boolean isHot, float partialTicks) {
         super.render(ms, index, top, left, width, height, mouseX, mouseY, isHot, partialTicks);
         this.button.x = left + (width / 2);
         this.button.y = top + (ConfigureStructureList.this.itemHeight - this.button.getHeight()) / 2;
         this.button.render(ms, mouseX, mouseY, partialTicks);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public class StringEntry extends TextFieldEntry<String> {
      public StringEntry(String option, Supplier<String> value, Consumer<String> saver, int maxLength) {
         super(option, value, saver, Objects::toString, string -> string, maxLength);
      }
   }

   public class ResourceLocationEntry extends TextFieldEntry<ResourceLocation> {
      public ResourceLocationEntry(String option, Supplier<ResourceLocation> value, Consumer<ResourceLocation> saver, int maxLength) {
         super(option, value, saver, Object::toString, ResourceLocation::new, maxLength);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public class DoubleEntry extends TextFieldEntry<Double> {
      public DoubleEntry(String option, Supplier<Double> value, Consumer<Double> saver) {
         super(option, value, saver, Object::toString, Double::valueOf, 32);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public class IntegerEntry extends TextFieldEntry<Integer> {
      public IntegerEntry(String option, Supplier<Integer> value, Consumer<Integer> saver,
                          boolean isNullAllowed, boolean isNegativeAllowed) {
         super(option, value, saver, Object::toString, Integer::parseInt, newValue -> !((!isNullAllowed && newValue == 0) || (!isNegativeAllowed && newValue < 0)), 32);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public class StringListEntry extends Entry<List<String>> {
      protected final Button button;

      public StringListEntry(String option, Supplier<List<String>> value,
                             Consumer<List<String>> saver, List<String> possibleValues) {
         super(option, value, saver);
         this.button = new Button(0, 0, 70, 20, new TranslatableComponent("gui.shrines.configure"), (button) -> this.minecraft.setScreen(new StringListOptionsScreen(ConfigureStructureList.this.screen, possibleValues,
               Lists.newArrayList(this.getter.get()), new TranslatableComponent(ConfigOptions.LATEST.comments_prefix() + this.option), saver)));
         this.children.add(this.button);
      }

      @Override
      @ParametersAreNonnullByDefault
      public void render(PoseStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                         boolean isHot, float partialTicks) {
         super.render(ms, index, top, left, width, height, mouseX, mouseY, isHot, partialTicks);
         this.button.x = left + (width / 2);
         this.button.y = top + (ConfigureStructureList.this.itemHeight - this.button.getHeight()) / 2;
         this.button.render(ms, mouseX, mouseY, partialTicks);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public class BiomeListsEntry extends Entry<List<String>> {
      protected final Button button;
      protected final Consumer<List<String>> categoriesSetter;
      protected final Supplier<List<String>> categoriesGetter;
      protected List<String> value;

      public BiomeListsEntry(String biomesOption, Supplier<List<String>> biomes,
                             Consumer<List<String>> biomeSaver,
                             Supplier<List<String>> categories, Consumer<List<String>> categoriesSaver) {
         super(biomesOption, biomes, biomeSaver);
         this.categoriesSetter = categoriesSaver;
         this.categoriesGetter = categories;
         this.button = new Button(0, 0, 70, 20, new TranslatableComponent("gui.shrines.configure"), (button) -> this.minecraft.setScreen(new BiomeGenerationSettingsScreen(screen, this.getter.get(),
               this.categoriesGetter.get(), new TranslatableComponent(ConfigOptions.LATEST.comments_prefix() + this.option), this.setter, this.categoriesSetter)));
         this.children.add(this.button);
      }

      @Override
      @ParametersAreNonnullByDefault
      public void render(PoseStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                         boolean isHot, float partialTicks) {
         super.render(ms, index, top, left, width, height, mouseX, mouseY, isHot, partialTicks);
         this.button.x = left + (width / 2);
         this.button.y = top + (ConfigureStructureList.this.itemHeight - this.button.getHeight()) / 2;
         this.button.render(ms, mouseX, mouseY, partialTicks);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public class PoolEntry extends Entry<String> {
      protected final Button button;

      public PoolEntry(String option, Supplier<String> value,
                       Consumer<String> setter) {
         super(option, value, setter);
         this.button = new Button(0, 0, 70, 20,
               value.get().isEmpty() ? new TranslatableComponent("gui.shrines.choose") : new TranslatableComponent("gui.shrines.change"),
               (button) -> this.minecraft.setScreen(new SelectPoolScreen(ConfigureStructureList.this.screen, ConfigureStructureList.this.packet, ConfigureStructureList.this.structure, new ResourceLocation(this.value))));
         this.children.add(this.button);
      }

      @Override
      @ParametersAreNonnullByDefault
      public void render(PoseStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                         boolean isHot, float partialTicks) {
         super.render(ms, index, top, left, width, height, mouseX, mouseY, isHot, partialTicks);
         this.button.x = left + (width / 2);
         this.button.y = top + (ConfigureStructureList.this.itemHeight - this.button.getHeight()) / 2;
         this.button.render(ms, mouseX, mouseY, partialTicks);
      }
   }
}
