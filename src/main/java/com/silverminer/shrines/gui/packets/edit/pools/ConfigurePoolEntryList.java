package com.silverminer.shrines.gui.packets.edit.pools;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.misc.buttons.BooleanValueButton;
import com.silverminer.shrines.gui.misc.screen.BiomeGenerationSettingsScreen;
import com.silverminer.shrines.gui.misc.screen.StringListOptionsScreen;
import com.silverminer.shrines.gui.packets.edit.structures.ConfigureStructureScreen;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.options.ConfigOption;
import com.silverminer.shrines.utils.TemplatePool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.screen.Screen;
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
public class ConfigurePoolEntryList extends ExtendedList<ConfigurePoolEntryList.Entry<?>> {
    protected Screen screen;
    protected final TemplatePool.Entry poolEntry;

    public ConfigurePoolEntryList(Minecraft p_i45010_1_, int p_i45010_2_, int p_i45010_3_, int p_i45010_4_,
                                  int p_i45010_5_, int p_i45010_6_, Screen screen, TemplatePool.Entry poolEntry) {
        super(p_i45010_1_, p_i45010_2_, p_i45010_3_, p_i45010_4_, p_i45010_5_, p_i45010_6_);
        this.screen = screen;
        this.poolEntry = poolEntry;
        this.refreshList();
    }

    public void refreshList() {
        this.clearEntries();
        this.addEntry(new IntegerEntry("Weight", this.poolEntry::getWeight, this.poolEntry::setWeight, false, false));
        this.addEntry(new BooleanEntry("Terrain Matching", this.poolEntry::isTerrain_matching, this.poolEntry::setTerrainMatching));
    }

    protected int getScrollbarPosition() {
        return this.width - 5;
    }

    public int getRowWidth() {
        return this.width - 10;
    }

    public Optional<ConfigurePoolEntryList.Entry<?>> getSelectedOpt() {
        return Optional.ofNullable(this.getSelected());
    }

    protected boolean isFocused() {
        return this.screen.getFocused() == this;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract class Entry<T>
            extends ExtendedList.AbstractListEntry<ConfigurePoolEntryList.Entry<?>> implements INestedGuiEventHandler {
        protected final String option;
        protected final Consumer<T> setter;
        protected final Supplier<T> getter;
        protected Minecraft minecraft;
        protected ArrayList<IGuiEventListener> children = Lists.newArrayList();
        protected T value;
        @Nullable
        private IGuiEventListener focused;
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
        public void render(MatrixStack ms, int index, int top, int left, int width, int height, int mouseX, int mouseY,
                           boolean isHot, float partialTicks) {
            String description = this.option;
            int descriptionTop = top + (ConfigurePoolEntryList.this.itemHeight - minecraft.font.lineHeight) / 2;
            minecraft.font.drawShadow(ms, description, left, descriptionTop, 16777215);
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

        public BooleanEntry(String option, Supplier<Boolean> value, Consumer<Boolean> saver) {
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
            this.button.y = top + (ConfigurePoolEntryList.this.itemHeight - this.button.getHeight()) / 2;
            this.button.render(ms, mouseX, mouseY, partialTicks);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class IntegerEntry extends Entry<Integer> {
        protected final TextFieldWidget textField;
        protected final boolean isNullAllowed;
        protected boolean isNegativeAllowed;

        public IntegerEntry(String option, Supplier<Integer> value, Consumer<Integer> saver,
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
            this.textField.y = top + (ConfigurePoolEntryList.this.itemHeight - this.textField.getHeight()) / 2;
            this.textField.render(ms, mouseX, mouseY, partialTicks);
        }
    }
}