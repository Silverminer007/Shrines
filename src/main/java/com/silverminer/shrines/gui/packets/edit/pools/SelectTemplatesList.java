package com.silverminer.shrines.gui.packets.edit.pools;

import com.google.common.collect.Lists;
import com.silverminer.shrines.gui.misc.IDoubleClickScreen;
import com.silverminer.shrines.gui.packets.edit.templates.TemplatesList;
import com.silverminer.shrines.packages.datacontainer.StructureTemplate;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class SelectTemplatesList extends TemplatesList {
   protected final List<ResourceLocation> templateFilter;
   protected final IDoubleClickScreen doubleClickScreen;
   protected final ArrayList<SelectTemplatesList.MultipleSelectEntry> selectEntries = Lists.newArrayList();

   public SelectTemplatesList(Minecraft p_i45010_1_, int p_i45010_2_, int p_i45010_3_, int p_i45010_4_, int p_i45010_5_, int p_i45010_6_, Supplier<String> search, StructuresPackageWrapper packet, IDoubleClickScreen screen, List<ResourceLocation> templateFilter) {
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
      List<StructureTemplate> templates = this.packet.getTemplates().getElementsAsList();
      templates.removeIf(template -> this.templateFilter.contains(template.getTemplateLocation()));
      templates.sort(Comparator.comparing(StructureTemplate::getTemplateLocation));
      String s = search.get().toLowerCase(Locale.ROOT);

      for (StructureTemplate template : templates) {
         if (template.getTemplateLocation().toString().toLowerCase(Locale.ROOT).contains(s)) {
            this.addEntry(new MultipleSelectEntry(template));
         }
      }
   }

   public ArrayList<SelectTemplatesList.MultipleSelectEntry> getSelectEntries() {
      return this.selectEntries;
   }

   @Override
   public void setSelected(@Nullable TemplatesList.Entry entry) {
      if (entry instanceof MultipleSelectEntry multipleSelectEntry) {
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
   protected boolean isSelectedItem(int pos) {
      TemplatesList.Entry entry = this.children().get(pos);
      return this.selectEntries.stream().map(MultipleSelectEntry::getTemplate).toList().contains(entry.getTemplate());
   }

   public class MultipleSelectEntry extends TemplatesList.Entry {
      private long lastClickTime;
      private boolean selected = false;

      public MultipleSelectEntry(StructureTemplate template) {
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
