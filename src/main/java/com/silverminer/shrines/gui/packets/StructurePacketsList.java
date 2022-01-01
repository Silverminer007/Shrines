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
package com.silverminer.shrines.gui.packets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.gui.misc.DirtConfirmScreen;
import com.silverminer.shrines.gui.packets.edit.structures.EditStructuresScreen;
import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author Silverminer
 */
@OnlyIn(Dist.CLIENT)
public class StructurePacketsList extends ObjectSelectionList<StructurePacketsList.Entry> {
   protected static final Logger LOGGER = LogManager.getLogger();
   private final StructuresPacketsScreen screen;

   public StructurePacketsList(StructuresPacketsScreen screen, Minecraft mc, int p_i49846_3_, int p_i49846_4_,
                               int p_i49846_5_, int p_i49846_6_, int p_i49846_7_, Supplier<String> search) {
      super(mc, p_i49846_3_, p_i49846_4_, p_i49846_5_, p_i49846_6_, p_i49846_7_);
      this.screen = screen;

      this.refreshList(search);
   }

   public void refreshList(Supplier<String> search) {
      this.clearEntries();

      List<StructuresPackageWrapper> wrappers = PackageManagerProvider.CLIENT.getPackages().getAsList();
      Collections.sort(wrappers);
      String s = search.get().toLowerCase(Locale.ROOT);

      for (StructuresPackageWrapper packet : wrappers) {
         if (packet.getStructuresPacketInfo().getDisplayName().toLowerCase(Locale.ROOT).contains(s)) {
            this.addEntry(new StructurePacketsList.Entry(packet));
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

   public void setSelected(@Nullable StructurePacketsList.Entry entry) {
      super.setSelected(entry);
      this.screen.updateButtonStatus(entry != null);
   }

   public Optional<StructurePacketsList.Entry> getSelectedOpt() {
      return Optional.ofNullable(this.getSelected());
   }

   @OnlyIn(Dist.CLIENT)
   public final class Entry extends ObjectSelectionList.Entry<StructurePacketsList.Entry> {
      private final Minecraft minecraft;
      private final StructuresPackageWrapper packet;
      private long lastClickTime;

      public Entry(StructuresPackageWrapper packet) {
         this.packet = packet;
         this.minecraft = Minecraft.getInstance();
      }

      @ParametersAreNonnullByDefault
      public void render(PoseStack ms, int p_230432_2_, int top, int left, int p_230432_5_, int p_230432_6_,
                         int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
         String header = packet.getStructuresPacketInfo().getDisplayName();
         String s1 = new TranslatableComponent("gui.shrines.author").getString() + ": " + this.packet.getStructuresPacketInfo().getAuthor();
         String s2 = new TranslatableComponent("gui.shrines.structures").getString() + ": " + this.packet.getStructures().getSize()
               + " " + new TranslatableComponent("gui.shrines.templates").getString() + ": " + this.packet.getTemplates().getSize()
               + " " + new TranslatableComponent("gui.shrines.pools").getString() + ": " + this.getPacket().getPools().getAsList().size();

         this.minecraft.font.draw(ms, header, left, top + 1, 0xffffff);
         this.minecraft.font.draw(ms, s1, left, top + 9 + 3, 8421504);
         this.minecraft.font.draw(ms, s2, left, top + 9 + 9 + 3, 8421504);
      }

      public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmount) {
         StructurePacketsList.this.setSelected(this);
         if (Util.getMillis() - this.lastClickTime < 250L) {
            this.configure();
            return true;
         } else {
            this.lastClickTime = Util.getMillis();
            return false;
         }
      }

      public void configure() {
         this.minecraft.setScreen(new EditStructuresScreen(this.minecraft.screen, this.packet));
      }

      public void remove() {
         this.minecraft.setScreen(new DirtConfirmScreen((confirmed) -> {
            if (confirmed) {
               PackageManagerProvider.CLIENT.getPackages().remove(this.packet.getPackageID());
            }

            this.minecraft.setScreen(StructurePacketsList.this.screen);
         }, new TranslatableComponent("gui.shrines.removeQuestion", this.packet.getStructuresPacketInfo().getDisplayName()),
               new TranslatableComponent("gui.shrines.removeWarning"),
               new TranslatableComponent("gui.shrines.delete"), CommonComponents.GUI_CANCEL));
      }

      public StructuresPackageWrapper getPacket() {
         return this.packet;
      }

      @Override
      public @NotNull Component getNarration() {
         return new TextComponent(this.packet.getStructuresPacketInfo().getDisplayName());
      }
   }
}