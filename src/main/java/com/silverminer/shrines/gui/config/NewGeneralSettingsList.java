package com.silverminer.shrines.gui.config;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.gui.misc.buttons.BooleanValueButton;
import com.silverminer.shrines.gui.misc.screen.StringListOptionsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class NewGeneralSettingsList extends ObjectSelectionList<NewGeneralSettingsList.Entry<?>> {
    protected NewGeneralSettingsScreen screen;

    public NewGeneralSettingsList(Minecraft p_i45010_1_, int p_i45010_2_, int p_i45010_3_, int p_i45010_4_,
                                  int p_i45010_5_, int p_i45010_6_, NewGeneralSettingsScreen screen) {
        super(p_i45010_1_, p_i45010_2_, p_i45010_3_, p_i45010_4_, p_i45010_5_, p_i45010_6_);
        this.screen = screen;
        this.refreshList();
    }

    public void refreshList() {
        this.clearEntries();
        this.addEntry(new DoubleEntry("distance_factor", Config.SETTINGS.DISTANCE_FACTOR::get, Config.SETTINGS.DISTANCE_FACTOR::set));
        this.addEntry(new DoubleEntry("seperation_factor", Config.SETTINGS.SEPERATION_FACTOR::get, Config.SETTINGS.SEPERATION_FACTOR::set));
        this.addEntry(new IntegerEntry("structure_min_distance", Config.SETTINGS.STRUCTURE_MIN_DISTANCE::get, Config.SETTINGS.STRUCTURE_MIN_DISTANCE::set, false, false));
        this.addEntry(new StringListEntry("blacklist", () -> Config.SETTINGS.BLACKLISTED_BIOMES.get().stream().map(String::toString).collect(Collectors.toList()), Config.SETTINGS.BLACKLISTED_BIOMES::set, ForgeRegistries.BIOMES.getValues().stream().map(o -> o.getRegistryName() != null ? o.getRegistryName().toString() : null).filter(Objects::nonNull).collect(Collectors.toList())));
        this.addEntry(new BooleanEntry("advanced_logging", Config.SETTINGS.ADVANCED_LOGGING::get, Config.SETTINGS.ADVANCED_LOGGING::set));
        this.addEntry(new StringListEntry("banned_blocks", () -> Config.SETTINGS.BANNED_BLOCKS.get().stream().map(String::toString).collect(Collectors.toList()), Config.SETTINGS.BANNED_BLOCKS::set, ForgeRegistries.BLOCKS.getValues().stream().map(o -> o.getRegistryName() != null ? o.getRegistryName().toString() : null).filter(Objects::nonNull).collect(Collectors.toList())));
        this.addEntry(new StringListEntry("banned_entities", () -> Config.SETTINGS.BANNED_ENTITIES.get().stream().map(String::toString).collect(Collectors.toList()), Config.SETTINGS.BANNED_ENTITIES::set, ForgeRegistries.ENTITIES.getValues().stream().map(o -> o.getRegistryName() != null ? o.getRegistryName().toString() : null).filter(Objects::nonNull).collect(Collectors.toList())));
        this.addEntry(new IntegerEntry("needed_novels", Config.SETTINGS.NEEDED_NOVELS::get, Config.SETTINGS.NEEDED_NOVELS::set, true, false));
    }

    protected int getScrollbarPosition() {
        return this.width - 5;
    }

    public int getRowWidth() {
        return this.width - 10;
    }

    public Optional<NewGeneralSettingsList.Entry<?>> getSelectedOpt() {
        return Optional.ofNullable(this.getSelected());
    }

    protected boolean isFocused() {
        return this.screen.getFocused() == this;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract class Entry<T>
            extends ObjectSelectionList.Entry<NewGeneralSettingsList.Entry<?>> implements ContainerEventHandler {
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
            Component option = new TranslatableComponent("config.shrines." + this.option);
            int descriptionWidth = this.minecraft.font.width(option);
            int descriptionTop = top + (NewGeneralSettingsList.this.itemHeight - minecraft.font.lineHeight) / 2;
            minecraft.font.drawShadow(ms, option, left, descriptionTop, 16777215);
            if ((mouseX >= left) && (mouseX < (left + descriptionWidth))
                    && (mouseY >= descriptionTop) && (mouseY < (descriptionTop + minecraft.font.lineHeight))) {
                List<Component> comment = Lists.newArrayList(new TranslatableComponent("config.shrines." + this.option + ".tooltip"));
                NewGeneralSettingsList.this.screen.renderComponentTooltip(ms, comment, mouseX, mouseY);
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
        public Component getNarration() {
            return new TextComponent(option);
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
            this.button.y = top + (NewGeneralSettingsList.this.itemHeight - this.button.getHeight()) / 2;
            this.button.render(ms, mouseX, mouseY, partialTicks);
        }
    }

    @SuppressWarnings("unused")
    @OnlyIn(Dist.CLIENT)
    public class StringEntry extends Entry<String> {
        protected final EditBox textField;

        public StringEntry(String option, Supplier<String> value, Consumer<String> saver, int maxLength) {
            super(option, value, saver);
            this.textField = new EditBox(this.minecraft.font, 0, 0, 200, 20, TextComponent.EMPTY);
            this.textField.setMaxLength(maxLength);
            this.textField.setValue(value.get());
            this.textField.setResponder(text -> {
                try {
                    this.textField.setTextColor(0xffffff);
                    this.setter.accept(text);
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
            this.textField.y = top + (NewGeneralSettingsList.this.itemHeight - this.textField.getHeight()) / 2;
            this.textField.render(ms, mouseX, mouseY, partialTicks);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class DoubleEntry extends Entry<Double> {
        protected final EditBox textField;

        public DoubleEntry(String option, Supplier<Double> value, Consumer<Double> saver) {
            super(option, value, saver);
            this.textField = new EditBox(this.minecraft.font, 0, 0, 200, 20, TextComponent.EMPTY);
            this.textField.setValue(value.get().toString());
            this.textField.setResponder(text -> {
                try {
                    this.textField.setTextColor(0xffffff);
                    Double v = Double.valueOf(text);
                    this.setter.accept(v);
                } catch (NumberFormatException t) {
                    this.textField.setTextColor(0xff0000);
                    this.setter.accept(this.getter.get());
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
            this.textField.y = top + (NewGeneralSettingsList.this.itemHeight - this.textField.getHeight()) / 2;
            this.textField.render(ms, mouseX, mouseY, partialTicks);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class IntegerEntry extends Entry<Integer> {
        protected final EditBox textField;
        protected final boolean isNullAllowed;
        protected boolean isNegativeAllowed;

        public IntegerEntry(String option, Supplier<Integer> value, Consumer<Integer> saver,
                            boolean isNullAllowed, boolean isNegativeAllowed) {
            super(option, value, saver);
            this.textField = new EditBox(this.minecraft.font, 0, 0, 200, 20, TextComponent.EMPTY);
            this.textField.setValue(value.get().toString());
            this.isNullAllowed = isNullAllowed;
            this.isNegativeAllowed = isNegativeAllowed;
            this.textField.setResponder(text -> {
                try {
                    this.textField.setTextColor(0xffffff);
                    int v = Integer.parseInt(text);
                    if (!(!this.isNullAllowed && v == 0 || !this.isNegativeAllowed && v < 0)) {
                        this.setter.accept(v);
                    }
                } catch (NumberFormatException t) {
                    this.textField.setTextColor(0xff0000);
                    this.setter.accept(this.getter.get());
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
            this.textField.y = top + (NewGeneralSettingsList.this.itemHeight - this.textField.getHeight()) / 2;
            this.textField.render(ms, mouseX, mouseY, partialTicks);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class StringListEntry extends Entry<List<String>> {
        protected final Button button;

        public StringListEntry(String option, Supplier<List<String>> value,
                               Consumer<List<String>> saver, List<String> possibleValues) {
            super(option, value, saver);
            this.button = new Button(0, 0, 70, 20, new TranslatableComponent("gui.shrines.configure"), (button) -> this.minecraft.setScreen(new StringListOptionsScreen(NewGeneralSettingsList.this.screen, possibleValues,
                    Lists.newArrayList(this.getter.get()), new TextComponent(this.option), saver)));
            this.children.add(this.button);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void render(PoseStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                           boolean isHot, float partialTicks) {
            super.render(ms, index, top, left, width, height, mouseX, mouseY, isHot, partialTicks);
            this.button.x = left + (width / 2);
            this.button.y = top + (NewGeneralSettingsList.this.itemHeight - this.button.getHeight()) / 2;
            this.button.render(ms, mouseX, mouseY, partialTicks);
        }
    }
}
