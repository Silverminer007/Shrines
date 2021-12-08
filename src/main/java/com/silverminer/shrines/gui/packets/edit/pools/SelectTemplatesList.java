package com.silverminer.shrines.gui.packets.edit.pools;

import com.google.common.collect.Lists;
import com.silverminer.shrines.gui.misc.IDoubleClickScreen;
import com.silverminer.shrines.gui.packets.edit.templates.TemplatesList;
import com.silverminer.shrines.structures.load.StructuresPacket;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SelectTemplatesList extends TemplatesList {
    protected final List<ResourceLocation> templateFilter;
    protected final IDoubleClickScreen doubleClickScreen;
    protected final ArrayList<SelectTemplatesList.MultipleSelectEntry> selectEntries = Lists.newArrayList();

    public SelectTemplatesList(Minecraft p_i45010_1_, int p_i45010_2_, int p_i45010_3_, int p_i45010_4_, int p_i45010_5_, int p_i45010_6_, Supplier<String> search, StructuresPacket packet, IDoubleClickScreen screen, List<ResourceLocation> templateFilter) {
        super(p_i45010_1_, p_i45010_2_, p_i45010_3_, p_i45010_4_, p_i45010_5_, p_i45010_6_, search, packet, screen);
        this.templateFilter = templateFilter;
        this.doubleClickScreen = screen;
        this.refreshList(search);
    }

    @Override
    public void refreshList(Supplier<String> search) {
        if (this.templateFilter == null) {
            return;
        }
        this.clearEntries();
        List<String> templates = this.packet.getTemplates()
                .stream().filter(template ->
                        !this.templateFilter.contains(template))
                .map(ResourceLocation::toString)
                .sorted()
                .collect(Collectors.toList());
        String s = search.get().toLowerCase(Locale.ROOT);

        for (String template : templates) {
            if (template.toLowerCase(Locale.ROOT).contains(s)) {
                this.addEntry(new MultipleSelectEntry(template));
            }
        }
    }

    public ArrayList<SelectTemplatesList.MultipleSelectEntry> getSelectEntries() {
        return this.selectEntries;
    }

    @Override
    public void setSelected(@Nullable TemplatesList.Entry entry) {
        if (entry instanceof SelectTemplatesList.MultipleSelectEntry) {
            SelectTemplatesList.MultipleSelectEntry multipleSelectEntry = (MultipleSelectEntry) entry;
            if (this.selectEntries.contains(multipleSelectEntry)) {
                this.selectEntries.remove(multipleSelectEntry);
                multipleSelectEntry.selected = false;
            } else {
                this.selectEntries.add(multipleSelectEntry);
                multipleSelectEntry.selected = true;
            }
            this.screen.updateButtonStatus(this.selectEntries.size() > 0);
        }
    }

    @Override
    protected boolean isSelectedItem(int p_230957_1_) {
        return this.selectEntries.contains(this.children().get(p_230957_1_));
    }

    public class MultipleSelectEntry extends TemplatesList.Entry {
        private long lastClickTime;
        private boolean selected = false;

        public MultipleSelectEntry(String template) {
            super(template);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmount) {
            SelectTemplatesList.this.setSelected(this);
            if (Util.getMillis() - this.lastClickTime < 250L && this.selected && SelectTemplatesList.this.getSelectEntries().size() == 1) {
                SelectTemplatesList.this.doubleClickScreen.onEntryDoubleClicked();
                return true;
            } else {
                this.lastClickTime = Util.getMillis();
                return false;
            }
        }
    }
}
