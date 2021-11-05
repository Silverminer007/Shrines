package com.silverminer.shrines.gui.packets.edit.structures;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.misc.buttons.BooleanValueButton;
import com.silverminer.shrines.gui.misc.screen.BiomeGenerationSettingsScreen;
import com.silverminer.shrines.gui.misc.screen.StringListOptionsScreen;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.options.ConfigOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class ConfigureStructureList extends ExtendedList<ConfigureStructureList.Entry<?>> {
    protected StructureData structure;
    protected ConfigureStructureScreen screen;

    public ConfigureStructureList(Minecraft p_i45010_1_, int p_i45010_2_, int p_i45010_3_, int p_i45010_4_,
                                  int p_i45010_5_, int p_i45010_6_, ConfigureStructureScreen screen, StructureData structure) {
        super(p_i45010_1_, p_i45010_2_, p_i45010_3_, p_i45010_4_, p_i45010_5_, p_i45010_6_);
        this.screen = screen;
        this.structure = structure;
        this.refreshList();
    }

    public void refreshList() {
        this.clearEntries();
        this.addEntry(
                new StringEntry(this.structure.name, this.structure::getName, this.structure::setName, 256));
        this.addEntry(new StringEntry(this.structure.key, this.structure::getKey, this.structure::setKey, 256));
        this.addEntry(
                new BooleanEntry(this.structure.generate, this.structure::getGenerate, this.structure::setGenerate));
        this.addEntry(new BooleanEntry(this.structure.transformLand, this.structure::getTransformLand,
                this.structure::setTransformLand));
        this.addEntry(new DoubleEntry(this.structure.spawn_chance, this.structure::getSpawn_chance,
                this.structure::setSpawn_chance));
        this.addEntry(new BooleanEntry(this.structure.use_random_varianting, this.structure::getUse_random_varianting,
                this.structure::setUse_random_varianting));
        this.addEntry(
                new IntegerEntry(this.structure.distance, this.structure::getDistance, this.structure::setDistance, false, false));
        this.addEntry(new IntegerEntry(this.structure.seperation, this.structure::getSeperation,
                this.structure::setSeperation, false, false));
        this.addEntry(new IntegerEntry(this.structure.seed_modifier, this.structure::getSeed_modifier,
                this.structure::setSeed_modifier, false, true));
        this.addEntry(new IntegerEntry(this.structure.height_offset, this.structure::getHeight_offset,
                this.structure::setHeight_offset, true, true));
        this.addEntry(new BiomeListsEntry(this.structure.biome_blacklist, this.structure::getBiomeBlacklist,
                this.structure::setBiomeBlacklist, this.structure.biome_category_whitelist,
                this.structure::getBiomeCategoryWhitelist, this.structure::setBiomeCategoryWhitelist));
        this.addEntry(new StringListEntry(this.structure.dimension_whitelist, this.structure::getDimension_whitelist,
                this.structure::setDimension_whitelist, this.screen.possibleDimensions));
        this.addEntry(new StringEntry(this.structure.novel, this.structure::getNovel, this.structure::setNovel, 10000));
    }

    protected int getScrollbarPosition() {
        return this.width - 5;
    }

    public int getRowWidth() {
        return this.width - 10;
    }

    public Optional<ConfigureStructureList.Entry<?>> getSelectedOpt() {
        return Optional.ofNullable(this.getSelected());
    }

    protected boolean isFocused() {
        return this.screen.getFocused() == this;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract class Entry<T>
            extends ExtendedList.AbstractListEntry<ConfigureStructureList.Entry<?>> implements INestedGuiEventHandler {
        protected final ConfigOption<T> option;
        protected final Consumer<T> setter;
        protected final Supplier<T> getter;
        protected Minecraft minecraft;
        protected ArrayList<IGuiEventListener> children = Lists.newArrayList();
        protected T value;
        @Nullable
        private IGuiEventListener focused;
        private boolean dragging;

        public Entry(ConfigOption<T> option, Supplier<T> value, Consumer<T> saver) {
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
        public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                           boolean isHot, float partialTicks) {
            String description = this.option.getOption();
            int descriptionWidth = minecraft.font.width(description);
            int descriptionTop = top + (ConfigureStructureList.this.itemHeight - minecraft.font.lineHeight) / 2;
            minecraft.font.drawShadow(ms, description, left, descriptionTop, 16777215);

            if ((mouseX >= left) && (mouseX < (left + descriptionWidth))
                    && (mouseY >= descriptionTop) && (mouseY < (descriptionTop + minecraft.font.lineHeight))) {
                List<ITextComponent> comment = Arrays.stream(this.option.getComments())
                        .map(TranslationTextComponent::new).collect(Collectors.toList());
                ConfigureStructureList.this.screen.renderComponentTooltip(ms, comment, mouseX, mouseY);
            }
        }

        @Override
        @Nonnull
        public List<? extends IGuiEventListener> children() {
            return children;
        }

        public boolean isDragging() {
            return this.dragging;
        }

        public void setDragging(boolean p_231037_1_) {
            this.dragging = p_231037_1_;
        }

        @Nullable
        public IGuiEventListener getFocused() {
            return this.focused;
        }

        public void setFocused(@Nullable IGuiEventListener p_231035_1_) {
            this.focused = p_231035_1_;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class BooleanEntry extends Entry<Boolean> {
        protected final BooleanValueButton button;

        public BooleanEntry(ConfigOption<Boolean> option, Supplier<Boolean> value, Consumer<Boolean> saver) {
            super(option, value, saver);
            this.button = new BooleanValueButton(0, 0, StringTextComponent.EMPTY, (button) -> {
                this.value = ((BooleanValueButton) button).value;
                this.setter.accept(this.value);
            }, this.value);
            this.children.add(this.button);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                           boolean isHot, float partialTicks) {
            super.render(ms, index, top, left, width, height, mouseX, mouseY, isHot, partialTicks);
            this.button.x = left + (width / 2);
            this.button.y = top + (ConfigureStructureList.this.itemHeight - this.button.getHeight()) / 2;
            this.button.render(ms, mouseX, mouseY, partialTicks);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class StringEntry extends Entry<String> {
        protected final TextFieldWidget textField;

        public StringEntry(ConfigOption<String> option, Supplier<String> value, Consumer<String> saver, int maxLength) {
            super(option, value, saver);
            this.textField = new TextFieldWidget(this.minecraft.font, 0, 0, 200, 20, StringTextComponent.EMPTY);
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
        public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                           boolean isHot, float partialTicks) {
            super.render(ms, index, top, left, width, height, mouseX, mouseY, isHot, partialTicks);
            this.textField.x = left + (width / 2);
            this.textField.y = top + (ConfigureStructureList.this.itemHeight - this.textField.getHeight()) / 2;
            this.textField.render(ms, mouseX, mouseY, partialTicks);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class DoubleEntry extends Entry<Double> {
        protected final TextFieldWidget textField;

        public DoubleEntry(ConfigOption<Double> option, Supplier<Double> value, Consumer<Double> saver) {
            super(option, value, saver);
            this.textField = new TextFieldWidget(this.minecraft.font, 0, 0, 200, 20, StringTextComponent.EMPTY);
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
        public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                           boolean isHot, float partialTicks) {
            super.render(ms, index, top, left, width, height, mouseX, mouseY, isHot, partialTicks);
            this.textField.x = left + (width / 2);
            this.textField.y = top + (ConfigureStructureList.this.itemHeight - this.textField.getHeight()) / 2;
            this.textField.render(ms, mouseX, mouseY, partialTicks);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class IntegerEntry extends Entry<Integer> {
        protected final TextFieldWidget textField;
        protected final boolean isNullAllowed;
        protected boolean isNegativeAllowed;

        public IntegerEntry(ConfigOption<Integer> option, Supplier<Integer> value, Consumer<Integer> saver,
                            boolean isNullAllowed, boolean isNegativeAllowed) {
            super(option, value, saver);
            this.textField = new TextFieldWidget(this.minecraft.font, 0, 0, 200, 20, StringTextComponent.EMPTY);
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
        public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                           boolean isHot, float partialTicks) {
            super.render(ms, index, top, left, width, height, mouseX, mouseY, isHot, partialTicks);
            this.textField.x = left + (width / 2);
            this.textField.y = top + (ConfigureStructureList.this.itemHeight - this.textField.getHeight()) / 2;
            this.textField.render(ms, mouseX, mouseY, partialTicks);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class StringListEntry extends Entry<List<String>> {
        protected final Button button;

        public StringListEntry(ConfigOption<List<String>> option, Supplier<List<String>> value,
                               Consumer<List<String>> saver, List<String> possibleValues) {
            super(option, value, saver);
            this.button = new Button(0, 0, 70, 20, new TranslationTextComponent("Configure"), (button) -> this.minecraft.setScreen(new StringListOptionsScreen(ConfigureStructureList.this.screen, possibleValues,
                    Lists.newArrayList(this.getter.get()), this.option.getOption(), saver)));// TRANSLATION
            this.children.add(this.button);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
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
        protected final ConfigOption<List<String>> categoriesOption;
        protected final Consumer<List<String>> categoriesSetter;
        protected final Supplier<List<String>> categoriesGetter;
        protected List<String> value;

        public BiomeListsEntry(ConfigOption<List<String>> biomesOption, Supplier<List<String>> biomes,
                               Consumer<List<String>> biomeSaver, ConfigOption<List<String>> categoriesOption,
                               Supplier<List<String>> categories, Consumer<List<String>> categoriesSaver) {
            super(biomesOption, biomes, biomeSaver);
            this.categoriesOption = categoriesOption;
            this.categoriesSetter = categoriesSaver;
            this.categoriesGetter = categories;
            this.button = new Button(0, 0, 70, 20, new TranslationTextComponent("Configure"), (button) -> this.minecraft.setScreen(new BiomeGenerationSettingsScreen(screen, this.getter.get(),
                    this.categoriesGetter.get(), this.option.getOption(), this.setter, this.categoriesSetter)));// TRANSLATION
            this.children.add(this.button);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                           boolean isHot, float partialTicks) {
            super.render(ms, index, top, left, width, height, mouseX, mouseY, isHot, partialTicks);
            this.button.x = left + (width / 2);
            this.button.y = top + (ConfigureStructureList.this.itemHeight - this.button.getHeight()) / 2;
            this.button.render(ms, mouseX, mouseY, partialTicks);
        }
    }
}
