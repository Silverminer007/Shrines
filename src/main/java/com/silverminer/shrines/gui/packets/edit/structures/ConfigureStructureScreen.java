/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets.edit.structures;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.packages.datacontainer.StructureData;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import com.silverminer.shrines.utils.ClientUtils;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ConfigureStructureScreen extends Screen {
   protected Screen lastScreen;
   protected StructureData structure;
   protected ConfigureStructureList list;
   protected final StructuresPackageWrapper packet;
   protected Component tooltip;

   public ConfigureStructureScreen(Screen lastScreen, StructureData structure, StructuresPackageWrapper packet) {
      super(new TranslatableComponent("gui.shrines.configuration"));
      this.lastScreen = lastScreen;
      this.structure = structure;
      this.packet = packet;
   }

   public void onClose() {
      if (this.minecraft == null) {
         return;
      }
      this.list.getSelectedOpt().ifPresent(ConfigureStructureList.Entry::save);
      this.minecraft.setScreen(lastScreen);
   }

   protected void init() {
      this.list = new ConfigureStructureList(minecraft, this.width, this.height, 24, height, 30, this,
            this.structure, packet);
      this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20,
            ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), TextComponent.EMPTY));
      this.addWidget(this.list);
   }

   @Override
   public void render(@NotNull PoseStack ms, int mouseX, int mouseY, float partialTicks) {
      this.list.render(ms, mouseX, mouseY, partialTicks);
      drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
      super.render(ms, mouseX, mouseY, partialTicks);
      if (this.tooltip != null) {
         this.renderComponentTooltip(ms, Lists.newArrayList(this.tooltip), mouseX, mouseY);
         this.tooltip = null;
      }
   }
}