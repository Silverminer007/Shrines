package com.silverminer.shrines.gui.packets.edit.templates;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.misc.IUpdatableScreen;
import com.silverminer.shrines.structures.load.StructuresPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TemplatesList extends ExtendedList<TemplatesList.Entry> {
    protected static final Logger LOGGER = LogManager.getLogger(TemplatesList.class);
    protected final IUpdatableScreen screen;
    protected final StructuresPacket packet;

    public TemplatesList(Minecraft p_i45010_1_, int p_i45010_2_, int p_i45010_3_, int p_i45010_4_, int p_i45010_5_, int p_i45010_6_, Supplier<String> search, StructuresPacket packet, IUpdatableScreen screen) {
        super(p_i45010_1_, p_i45010_2_, p_i45010_3_, p_i45010_4_, p_i45010_5_, p_i45010_6_);
        this.packet = packet;
        this.screen = screen;
        this.refreshList(search);
    }

    public void refreshList(Supplier<String> search) {
        this.clearEntries();
        List<String> templates = this.packet.getTemplates().stream().map(ResourceLocation::toString).sorted().collect(Collectors.toList());
        String s = search.get().toLowerCase(Locale.ROOT);

        for (String template : templates) {
            if (template.toLowerCase(Locale.ROOT).contains(s)) {
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

    public class Entry extends ExtendedList.AbstractListEntry<TemplatesList.Entry> {
        protected final Minecraft minecraft;
        protected final String template;

        public Entry(String template) {
            this.template = template;
            this.minecraft = Minecraft.getInstance();
        }

        @ParametersAreNonnullByDefault
        @Override
        public void render(MatrixStack ms, int p_230432_2_, int top, int left, int p_230432_5_, int p_230432_6_,
                           int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
            this.minecraft.font.draw(ms, template, left, top + 1, 0xffffff);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmount) {
            TemplatesList.this.setSelected(this);
            return true;
        }

        public void rename() {

        }

        public String getTemplate() {
            return template;
        }
    }
}
