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
import com.silverminer.shrines.packages.datacontainer.*;
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
public class ConfigureStructureList extends ObjectSelectionList<ConfigureStructureList.Entry> {
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
      this.addEntry(
            new StringEntry(ConfigOptions.LATEST.name(), this.structure::getName, this.structure::setName, 256, 0));
      this.addEntry(new ResourceLocationEntry(ConfigOptions.LATEST.key(), this.structure::getKey, this.structure::setKey, 256, 0));
      this.addEntry(new ResourceLocationEntry(ConfigOptions.LATEST.novel(), this.structure::getNovel, this.structure::setNovel, Integer.MAX_VALUE, 0));
      this.addEntry(new ResourceLocationEntry(ConfigOptions.LATEST.iconPath(), this.structure::getIconPath, this.structure::setIconPath, Integer.MAX_VALUE, 0));

      SpawnConfiguration spawnConfiguration = this.structure.getSpawnConfiguration();
      List<Entry> spawnConfigurationOptions = new ArrayList<>();
      spawnConfigurationOptions.add(new BooleanEntry(ConfigOptions.LATEST.generate(), spawnConfiguration::isGenerate, spawnConfiguration::setGenerate, 1));
      spawnConfigurationOptions.add(new BooleanEntry(ConfigOptions.LATEST.transformLand(), spawnConfiguration::isTransformLand, spawnConfiguration::setTransformLand, 1));
      spawnConfigurationOptions.add(new DoubleEntry(ConfigOptions.LATEST.spawnChance(), spawnConfiguration::getSpawn_chance, spawnConfiguration::setSpawn_chance, 1));
      spawnConfigurationOptions.add(new IntegerEntry(ConfigOptions.LATEST.distance(), spawnConfiguration::getDistance, spawnConfiguration::setDistance,
            false, false, 1));
      spawnConfigurationOptions.add(new IntegerEntry(ConfigOptions.LATEST.separation(), spawnConfiguration::getSeparation, spawnConfiguration::setSeparation,
            false, false, 1));
      spawnConfigurationOptions.add(new IntegerEntry(ConfigOptions.LATEST.seedModifier(), spawnConfiguration::getSeed_modifier, spawnConfiguration::setSeed_modifier,
            false, true, 1));
      spawnConfigurationOptions.add(new IntegerEntry(ConfigOptions.LATEST.heightOffset(), spawnConfiguration::getHeight_offset, spawnConfiguration::setHeight_offset,
            true, true, 1));
      spawnConfigurationOptions.add(new BiomeListsEntry(ConfigOptions.LATEST.biomeBlacklist(), spawnConfiguration::getBiome_blacklist, spawnConfiguration::setBiome_blacklist,
            spawnConfiguration::getBiome_category_whitelist, spawnConfiguration::setBiome_category_whitelist, 1));
      spawnConfigurationOptions.add(new StringListEntry(ConfigOptions.LATEST.dimensionWhitelist(), spawnConfiguration::getDimension_whitelist,
            spawnConfiguration::setDimension_whitelist, PackageManagerProvider.CLIENT.getAvailableDimensions(), 1));
      spawnConfigurationOptions.add(new PoolEntry(ConfigOptions.LATEST.startPool(), spawnConfiguration::getStart_pool, spawnConfiguration::setStart_pool, 1));
      spawnConfigurationOptions.add(new IntegerEntry(ConfigOptions.LATEST.jigsawMaxDepth(), spawnConfiguration::getJigsawMaxDepth, spawnConfiguration::setJigsawMaxDepth,
            false, false, 1));
      this.addEntry(new CategoryEntry("spawn_configuration_options", spawnConfigurationOptions, 0));

      VariationConfiguration variationConfiguration = null;// TODO this.structure.getVariationConfiguration(); -> New Config UI
      List<Entry> variationOptions = new ArrayList<>();
      variationOptions.add(new BooleanEntry("enable", variationConfiguration::isEnabled, variationConfiguration::setEnabled, 1));

      SimpleVariationConfiguration simpleVariationConfiguration = variationConfiguration.getSimpleVariationConfiguration();
      List<Entry> simpleVariationOptions = new ArrayList<>();
      simpleVariationOptions.add(new BooleanEntry("wool_variation", simpleVariationConfiguration::isWoolEnabled, simpleVariationConfiguration::setWoolEnabled, 2));
      simpleVariationOptions.add(new BooleanEntry("terracotta_variation", simpleVariationConfiguration::isTerracottaEnabled, simpleVariationConfiguration::setTerracottaEnabled,
            2));
      simpleVariationOptions.add(new BooleanEntry("glazed_terracotta_variation", simpleVariationConfiguration::isGlazedTerracottaEnabled,
            simpleVariationConfiguration::setGlazedTerracottaEnabled, 2));
      simpleVariationOptions.add(new BooleanEntry("concrete_variation", simpleVariationConfiguration::isConcreteEnabled, simpleVariationConfiguration::setConcreteEnabled, 2));
      simpleVariationOptions.add(new BooleanEntry("concrete_powder_variation", simpleVariationConfiguration::isConcretePowderEnabled,
            simpleVariationConfiguration::setConcretePowderEnabled, 2));
      simpleVariationOptions.add(new BooleanEntry("planks_variation", simpleVariationConfiguration::arePlanksEnabled, simpleVariationConfiguration::setPlanksEnabled, 2));
      simpleVariationOptions.add(new BooleanEntry("ores_variation", simpleVariationConfiguration::areOresEnabled, simpleVariationConfiguration::setOresEnabled, 2));
      simpleVariationOptions.add(new BooleanEntry("stones_variation", simpleVariationConfiguration::areStonesEnabled, simpleVariationConfiguration::setStonesEnabled, 2));
      simpleVariationOptions.add(new BooleanEntry("bees_variation", simpleVariationConfiguration::areBeesEnabled, simpleVariationConfiguration::setBeesEnabled, 2));

      NestedVariationConfiguration nestedVariationConfiguration = variationConfiguration.getNestedVariationConfiguration();
      List<Entry> nestedVariationOptions = new ArrayList<>();
      nestedVariationOptions.add(new BooleanEntry("slab_variation", nestedVariationConfiguration::isAreSlabsEnabled, nestedVariationConfiguration::setAreSlabsEnabled, 2));
      nestedVariationOptions.add(new BooleanEntry("button_variation", nestedVariationConfiguration::isButtonEnabled, nestedVariationConfiguration::setButtonEnabled, 2));
      nestedVariationOptions.add(new BooleanEntry("fence_variation", nestedVariationConfiguration::isFenceEnabled, nestedVariationConfiguration::setFenceEnabled, 2));
      nestedVariationOptions.add(new BooleanEntry("normal_log_variation", nestedVariationConfiguration::isAreNormalLogsEnabled,
            nestedVariationConfiguration::setAreNormalLogsEnabled, 2));
      nestedVariationOptions.add(new BooleanEntry("stripped_log_variation", nestedVariationConfiguration::isAreStrippedLogsEnabled,
            nestedVariationConfiguration::setAreStrippedLogsEnabled, 2));
      nestedVariationOptions.add(new BooleanEntry("trapdoor_variation", nestedVariationConfiguration::isAreTrapdoorsEnabled, nestedVariationConfiguration::setAreTrapdoorsEnabled,
            2));
      nestedVariationOptions.add(new BooleanEntry("door_variation", nestedVariationConfiguration::isAreDoorsEnabled, nestedVariationConfiguration::setAreDoorsEnabled, 2));
      nestedVariationOptions.add(new BooleanEntry("stair_variation", nestedVariationConfiguration::isStairEnabled, nestedVariationConfiguration::setStairEnabled, 2));
      nestedVariationOptions.add(new BooleanEntry("standing_sign_variation", nestedVariationConfiguration::isStandingSignEnabled,
            nestedVariationConfiguration::setStandingSignEnabled, 2));
      nestedVariationOptions.add(new BooleanEntry("wall_sign_variation", nestedVariationConfiguration::isWallSignEnabled, nestedVariationConfiguration::setWallSignEnabled, 2));

      variationOptions.add(new CategoryEntry("simple", simpleVariationOptions, 1));
      variationOptions.add(new CategoryEntry("nested", nestedVariationOptions, 1));

      this.addEntry(new CategoryEntry("random_variation_options", variationOptions, 0));
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

   public Optional<ConfigureStructureList.Entry> getSelectedOpt() {
      return Optional.ofNullable(this.getSelected());
   }

   @OnlyIn(Dist.CLIENT)
   public abstract class Entry extends ObjectSelectionList.Entry<ConfigureStructureList.Entry> implements ContainerEventHandler {
      protected final String option;
      protected final int childDepth;
      protected Minecraft minecraft;
      protected ArrayList<GuiEventListener> children = Lists.newArrayList();
      @Nullable
      private GuiEventListener focused;
      private boolean dragging;

      public Entry(String option) {
         this(option, 0);
      }

      public Entry(String option, int childDepth) {
         this.option = option;
         this.minecraft = Minecraft.getInstance();
         this.childDepth = childDepth;
      }

      public abstract void save();

      @Override
      @ParametersAreNonnullByDefault
      public void render(PoseStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                         boolean isHot, float partialTicks) {
         left += (15 * this.childDepth);
         String description = new TranslatableComponent(ConfigOptions.LATEST.comments_prefix() + this.option).getString();
         int descriptionWidth = minecraft.font.width(description);
         int descriptionTop = top + (ConfigureStructureList.this.itemHeight - minecraft.font.lineHeight) / 2;
         minecraft.font.drawShadow(ms, description, left, descriptionTop, 0xffffff - (this.childDepth * 0x555555));

         if ((mouseX >= left) && (mouseX < (left + descriptionWidth)) && (mouseY >= descriptionTop) && (mouseY < (descriptionTop + minecraft.font.lineHeight))) {
            ConfigureStructureList.this.screen.tooltip =
                  new TranslatableComponent(ConfigOptions.LATEST.comments_prefix() + this.option + ConfigOptions.LATEST.comments_suffix());
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

   public abstract class ElementEntry<T> extends Entry {
      protected final Consumer<T> setter;
      protected final Supplier<T> getter;
      protected T value;

      public ElementEntry(String option, Supplier<T> value, Consumer<T> saver, int childDepth) {
         super(option, childDepth);
         this.value = value.get();
         this.setter = saver;
         this.getter = value;
      }

      public void save() {
         this.setter.accept(this.value);
      }
   }

   public class CategoryEntry extends Entry {
      protected final List<Entry> children;
      protected final Button toggleButton;
      protected boolean expanded = false;

      public CategoryEntry(String option, List<Entry> children, int childDepth) {
         super(option, childDepth);
         this.children = children;
         this.toggleButton = new Button(0, 0, 20, 20, new TextComponent(expanded ? "-" : "+"), (button) -> {
            this.expanded = !expanded;
            button.setMessage(new TextComponent(expanded ? "-" : "+"));
            if (this.expanded) {
               this.addChildren();
            } else {
               this.removeChildren();
            }
         });
         super.children.add(this.toggleButton);
      }

      private void addChildren() {
         List<Entry> activeEntries = ConfigureStructureList.this.children();
         int categoryPosition = activeEntries.indexOf(this);
         if (categoryPosition >= 0) {
            activeEntries.addAll(categoryPosition + 1, this.children);
         }
      }

      private void removeChildren() {
         List<Entry> activeEntries = ConfigureStructureList.this.children();
         for (Entry entry : this.children) {
            activeEntries.remove(entry);
            if (entry instanceof CategoryEntry categoryEntry) {
               categoryEntry.removeChildren();
            }
         }
      }

      @Override
      public void save() {
         this.children.forEach(Entry::save);
      }

      @Override
      @ParametersAreNonnullByDefault
      public void render(PoseStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                         boolean isHot, float partialTicks) {
         super.render(ms, index, top, left, width, height, mouseX, mouseY, isHot, partialTicks);
         this.toggleButton.x = left + (width / 2);
         this.toggleButton.y = top + (ConfigureStructureList.this.itemHeight - this.toggleButton.getHeight()) / 2;
         this.toggleButton.render(ms, mouseX, mouseY, partialTicks);
      }
   }

   public class TextFieldEntry<T> extends ElementEntry<T> {
      protected final EditBox textField;

      public TextFieldEntry(String option, Supplier<T> value, Consumer<T> saver, Function<T, String> toStringMapper, Function<String, T> fromStringMapper, int maxLength,
                            int childDepth) {
         this(option, value, saver, toStringMapper, fromStringMapper, Objects::nonNull, maxLength, childDepth);
      }

      public TextFieldEntry(String option, Supplier<T> value, Consumer<T> saver, Function<T, String> toStringMapper, Function<String, T> fromStringMapper,
                            Function<T, Boolean> validator, int maxLength, int childDepth) {
         super(option, value, saver, childDepth);
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
   public class BooleanEntry extends ElementEntry<Boolean> {
      protected final BooleanValueButton button;

      public BooleanEntry(String option, Supplier<Boolean> value, Consumer<Boolean> saver, int childDepth) {
         super(option, value, saver, childDepth);
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
      public StringEntry(String option, Supplier<String> value, Consumer<String> saver, int maxLength, int childDepth) {
         super(option, value, saver, Objects::toString, string -> string, maxLength, childDepth);
      }
   }

   public class ResourceLocationEntry extends TextFieldEntry<ResourceLocation> {
      public ResourceLocationEntry(String option, Supplier<ResourceLocation> value, Consumer<ResourceLocation> saver, int maxLength, int childDepth) {
         super(option, value, saver, Object::toString, ResourceLocation::new, maxLength, childDepth);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public class DoubleEntry extends TextFieldEntry<Double> {
      public DoubleEntry(String option, Supplier<Double> value, Consumer<Double> saver, int childDepth) {
         super(option, value, saver, Object::toString, Double::valueOf, 32, childDepth);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public class IntegerEntry extends TextFieldEntry<Integer> {
      public IntegerEntry(String option, Supplier<Integer> value, Consumer<Integer> saver,
                          boolean isNullAllowed, boolean isNegativeAllowed, int childDepth) {
         super(option, value, saver, Object::toString, Integer::parseInt, newValue -> !((!isNullAllowed && newValue == 0) || (!isNegativeAllowed && newValue < 0)), 32, childDepth);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public class StringListEntry extends ElementEntry<List<String>> {
      protected final Button button;

      public StringListEntry(String option, Supplier<List<String>> value,
                             Consumer<List<String>> saver, List<String> possibleValues, int childDepth) {
         super(option, value, saver, childDepth);
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
   public class BiomeListsEntry extends ElementEntry<List<String>> {
      protected final Button button;
      protected final Consumer<List<String>> categoriesSetter;
      protected final Supplier<List<String>> categoriesGetter;
      protected List<String> value;

      public BiomeListsEntry(String biomesOption, Supplier<List<String>> biomes,
                             Consumer<List<String>> biomeSaver,
                             Supplier<List<String>> categories, Consumer<List<String>> categoriesSaver, int childDepth) {
         super(biomesOption, biomes, biomeSaver, childDepth);
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
   public class PoolEntry extends ElementEntry<String> {
      protected final Button button;

      public PoolEntry(String option, Supplier<String> value, Consumer<String> setter, int childDepth) {
         super(option, value, setter, childDepth);
         this.button = new Button(0, 0, 70, 20,
               value.get().isEmpty() ? new TranslatableComponent("gui.shrines.choose") : new TranslatableComponent("gui.shrines.change"),
               (button) -> this.minecraft.setScreen(
                     new SelectPoolScreen(
                           ConfigureStructureList.this.screen, ConfigureStructureList.this.packet, ConfigureStructureList.this.structure, new ResourceLocation(this.value))));
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
