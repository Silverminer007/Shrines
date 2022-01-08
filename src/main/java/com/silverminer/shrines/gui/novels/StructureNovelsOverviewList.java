/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.novels;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.silverminer.shrines.gui.misc.IconList;
import com.silverminer.shrines.gui.novels.StructureNovelsOverviewList.StructureNovelsEntry;
import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.datacontainer.NovelsData;
import com.silverminer.shrines.packages.datacontainer.StructureData;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class StructureNovelsOverviewList extends IconList<StructureNovelsEntry> {
   protected static final Logger LOGGER = LogManager.getLogger(StructureNovelsOverviewList.class);

   public StructureNovelsOverviewList(Minecraft minecraft, int width, int height, int y0, int y1, int itemHeight,
                                      Supplier<String> search) {
      super(minecraft, width, height, y0, y1, itemHeight);
      this.refreshList(search);
   }

   public void refreshList(Supplier<String> filter) {
      this.clearEntries();
      String s = filter.get().toLowerCase(Locale.ROOT);
      for (StructuresPackageWrapper packet : PackageManagerProvider.CLIENT.getInitialPackages().getAsList()) {
         for (StructureData data : packet.getStructures().getAsIterable()) {
            if (data.getName().toLowerCase(Locale.ROOT).contains(s)
                  || data.getKey().toString().toLowerCase(Locale.ROOT).contains(s)) {
               if (PackageManagerProvider.CLIENT.getNovelsRegistryData().containsKey(data.getNovel())) {
                  this.addEntry(new StructureNovelsEntry(data));
               }
            }
         }
      }
   }

   @Override
   public @NotNull NarrationPriority narrationPriority() {
      return NarrationPriority.HOVERED;
   }

   @Override
   public void updateNarration(@NotNull NarrationElementOutput p_169152_) {

   }

   public class StructureNovelsEntry extends IconList.AbstractListEntry<StructureNovelsEntry> {
      private final Minecraft minecraft;
      private final StructureData data;
      private final ResourceLocation iconLocation;

      public StructureNovelsEntry(StructureData data) {
         this.data = data;
         this.minecraft = Minecraft.getInstance();
         this.iconLocation = new ResourceLocation(this.data.getIconPath().getNamespace(),
               "textures/structures/" + this.data.getIconPath().getPath() + ".png");
      }

      @Override
      public void render(PoseStack matrixStack, int x1, int x, int y, int y2, int p_230432_6_, int p_230432_7_,
                         int p_230432_8_, boolean isHovered, float p_230432_10_, int itemSize) {
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.setShaderTexture(0, this.iconLocation);
         RenderSystem.enableBlend();
         blit(matrixStack, y, x, 0.0F, 0.0F, itemSize, itemSize, itemSize, itemSize);
         if (isHovered || StructureNovelsOverviewList.this.getSelected() == this) {
            RenderSystem.disableTexture();
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuilder();
            RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 0.75F);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
            int j = itemSize - itemSize / 4;
            y = y + itemSize;
            bufferbuilder.vertex((double) y - itemSize, (double) x + j, 0.0D).endVertex();// A
            bufferbuilder.vertex((double) y - itemSize, (double) x + itemSize, 0.0D).endVertex();// D
            bufferbuilder.vertex(y, (double) x + itemSize, 0.0D).endVertex();// C
            bufferbuilder.vertex(y, (double) x + j, 0.0D).endVertex();// B
            tessellator.end();
            RenderSystem.enableTexture();
            String name = this.data.getName();
            int stringWidth = this.minecraft.font.width(name);
            if (stringWidth < itemSize || !name.contains(" "))
               this.minecraft.font.draw(matrixStack, name, y - (itemSize + stringWidth) / 2.0F, x + itemSize / 8.0F * 7, 0xffffff);
            else {
               int idx = name.indexOf(" ");
               String s1 = name.substring(0, idx);
               String s2 = name.substring(idx);
               stringWidth = this.minecraft.font.width(s1);
               this.minecraft.font.draw(matrixStack, s1, y - (itemSize + stringWidth) / 2.0F, x + itemSize / 16.0F * 13, 0xffffff);
               stringWidth = this.minecraft.font.width(s2);
               this.minecraft.font.draw(matrixStack, s2, y - (itemSize + stringWidth) / 2.0F, x + itemSize / 16.0F * 15, 0xffffff);
            }
         }
         RenderSystem.disableBlend();
      }

      public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmount) {
         if (StructureNovelsOverviewList.this.getSelected() == this) {
            this.minecraft.setScreen(new StructureNovelInsightsScreen(
                  this.minecraft.screen,
                  this.data,
                  Optional.ofNullable(
                              PackageManagerProvider.CLIENT.getNovels()
                                    .getByKey(this.data.getKey().toString()))
                        .map(NovelsData::getFoundStructuresCount)
                        .orElse(0)));
            return true;
         } else {
            StructureNovelsOverviewList.this.setSelected(this);
            return false;
         }
      }
   }
}