/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.gui.packets.edit.structures;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.packets.edit.EditStructurePacketScreen;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author Silverminer
 */
@OnlyIn(Dist.CLIENT)
public class EditStructuresList extends ExtendedList<EditStructuresList.Entry> {
   protected static final Logger LOGGER = LogManager.getLogger(EditStructuresList.class);
   private final EditStructurePacketScreen screen;
   private final StructuresPacket packet;

   public EditStructuresList(Minecraft mc, int p_i49846_3_, int p_i49846_4_,
                             int p_i49846_5_, int p_i49846_6_, int p_i49846_7_, Supplier<String> search, StructuresPacket packet, EditStructurePacketScreen screen) {
      super(mc, p_i49846_3_, p_i49846_4_, p_i49846_5_, p_i49846_6_, p_i49846_7_);
      this.screen = screen;
      this.packet = packet;

      this.refreshList(search);
   }

   public void refreshList(Supplier<String> search) {
      this.clearEntries();

      List<StructureData> structures = packet.getStructures();
      Collections.sort(structures);
      String s = search.get().toLowerCase(Locale.ROOT);

      for (StructureData packet : structures) {
         if (packet.getName().toLowerCase(Locale.ROOT).contains(s)) {
            this.addEntry(new EditStructuresList.Entry(packet));
         }
      }

   }

   public int getRowWidth() {
      return super.getRowWidth() + 160;
   }

   public void setSelected(@Nullable EditStructuresList.Entry entry) {
      this.screen.updateButtonStatus(true);
      super.setSelected(entry);
   }

   protected int getScrollbarPosition() {
      return super.getScrollbarPosition() + 60;
   }

   protected boolean isFocused() {
      return this.screen.getFocused() == this;
   }

   public Optional<EditStructuresList.Entry> getSelectedOpt() {
      return Optional.ofNullable(this.getSelected());
   }

   @OnlyIn(Dist.CLIENT)
   public final class Entry extends ExtendedList.AbstractListEntry<EditStructuresList.Entry> {
      private final Minecraft minecraft;
      private final StructureData structure;
      private long lastClickTime;

      public Entry(StructureData structure) {
         this.structure = structure;
         this.minecraft = Minecraft.getInstance();
      }

      @ParametersAreNonnullByDefault
      public void render(MatrixStack ms, int p_230432_2_, int top, int left, int p_230432_5_, int p_230432_6_,
                         int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
         StringTextComponent header = new StringTextComponent(structure.getName());
         header.withStyle(Style.EMPTY.withItalic(!this.structure.registered));
         String headerRight = " (" + structure.getKey() + ")";
         String s1 = "Dimensions: " + this.structure.getDimension_whitelist().toString();
         String s2 = "Distance: " + this.structure.getDistance() + "  Seperation: " + this.structure.getSeperation();

         this.minecraft.font.draw(ms, header, left, top + 1, this.structure.successful ? 0xffffff : 0xff0000);
         this.minecraft.font.draw(ms, headerRight, left + this.minecraft.font.width(header), top + 1, 0xc0c0c0);
         this.minecraft.font.draw(ms, s1, left, top + 9 + 3, 0x808080);
         this.minecraft.font.draw(ms, s2, left, top + 9 + 9 + 3, 0x808080);
      }

      public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmount) {
         EditStructuresList.this.setSelected(this);
         if (Util.getMillis() - this.lastClickTime < 250L) {
            this.configure();
            return true;
         } else {
            this.lastClickTime = Util.getMillis();
            return false;
         }
      }

      public void configure() {
         this.minecraft.setScreen(new ConfigureStructureScreen(screen, structure, packet.possibleDimensions, packet));
      }

      public StructureData getStructure() {
         return this.structure;
      }
   }
}