/*
 * Silverminer (and Team)
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 *
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.gui.packets.edit.structures;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.gui.packets.edit.EditStructurePacketScreen;
import com.silverminer.shrines.packages.datacontainer.StructureData;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Silverminer
 */
@OnlyIn(Dist.CLIENT)
public class EditStructuresList extends ObjectSelectionList<EditStructuresList.Entry> {
   protected static final Logger LOGGER = LogManager.getLogger(EditStructuresList.class);
   private final EditStructurePacketScreen screen;
   private final StructuresPackageWrapper packet;

   public EditStructuresList(Minecraft mc, int p_i49846_3_, int p_i49846_4_,
                             int p_i49846_5_, int p_i49846_6_, int p_i49846_7_, Supplier<String> search, StructuresPackageWrapper packet, EditStructurePacketScreen screen) {
      super(mc, p_i49846_3_, p_i49846_4_, p_i49846_5_, p_i49846_6_, p_i49846_7_);
      this.screen = screen;
      this.packet = packet;

      this.refreshList(search);
   }

   public void refreshList(Supplier<String> search) {
      this.clearEntries();

      List<StructureData> structures = packet.getStructures().getAsList();
      Collections.sort(structures);
      String s = search.get().toLowerCase(Locale.ROOT);

      for (StructureData packet : structures) {
         if (packet.getName().toLowerCase(Locale.ROOT).contains(s)) {
            this.addEntry(new EditStructuresList.Entry(packet));
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

   public void setSelected(@Nullable EditStructuresList.Entry entry) {
      this.screen.updateButtonStatus(true);
      super.setSelected(entry);
   }

   public Optional<EditStructuresList.Entry> getSelectedOpt() {
      return Optional.ofNullable(this.getSelected());
   }

   @OnlyIn(Dist.CLIENT)
   public final class Entry extends ObjectSelectionList.Entry<EditStructuresList.Entry> {
      private final Minecraft minecraft;
      private final StructureData structure;
      private long lastClickTime;

      public Entry(StructureData structure) {
         this.structure = structure;
         this.minecraft = Minecraft.getInstance();
      }

      @ParametersAreNonnullByDefault
      public void render(PoseStack ms, int p_230432_2_, int top, int left, int p_230432_5_, int p_230432_6_,
                         int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
         TextComponent header = new TextComponent(structure.getName());
         String headerRight = " (" + structure.getKey() + ")";
         String s1 = "Dimensions: " + this.structure.getDimension_whitelist().toString();
         String s2 = "Distance: " + this.structure.getDistance() + "  Seperation: " + this.structure.getSeparation();

         this.minecraft.font.draw(ms, header, left, top + 1, 0xffffff);
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
         this.minecraft.setScreen(new ConfigureStructureScreen(screen, structure, packet));
      }

      public StructureData getStructure() {
         return this.structure;
      }

      @Override
      public @NotNull Component getNarration() {
         return new TextComponent(this.structure.getName());
      }
   }
}