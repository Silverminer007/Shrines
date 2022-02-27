/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets.edit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.gui.misc.IUpdatableScreen;
import com.silverminer.shrines.gui.packets.edit.pools.EditPoolsScreen;
import com.silverminer.shrines.gui.packets.edit.structures.EditStructuresScreen;
import com.silverminer.shrines.gui.packets.edit.templates.EditTemplatesScreen;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import com.silverminer.shrines.utils.ClientUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class EditStructurePacketScreen extends Screen implements IUpdatableScreen {
   protected final StructuresPackageWrapper packet;
   protected final Screen lastScreen;
   protected EditBox searchBox;
   protected Button structuresButton;
   protected Button templatesButton;
   protected Button poolsButton;
   protected Button delete;
   protected Button configure;
   protected Button add;
   protected int headerheight;
   protected int bottomheight;
   protected boolean showHeader;

   public EditStructurePacketScreen(Screen lastScreen, StructuresPackageWrapper packet) {
      this(lastScreen, new TranslatableComponent("gui.shrines.edit_packet"), packet, true);
   }

   public EditStructurePacketScreen(Screen lastScreen, Component title, StructuresPackageWrapper packet, boolean showHeader) {
      super(title);
      this.lastScreen = lastScreen;
      this.packet = packet;
      this.showHeader = showHeader;
   }

   public void onClose() {
      if (this.minecraft != null) {
         this.minecraft.setScreen(this.lastScreen);
      }
   }

   protected void init() {
      if (this.minecraft == null) {
         return;
      }
      this.headerheight = 46;
      this.bottomheight = this.height - 26;
      this.searchBox = new EditBox(this.font, (this.width / 4) * 3, 3, 100, 20, this.searchBox,
            new TextComponent(""));
      this.searchBox.setResponder(this::refreshList);
      this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20,
            ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), TextComponent.EMPTY));
      if (this.showHeader) {
         this.structuresButton = this
               .addRenderableWidget(new Button(2, 24, 150, 20,
                     new TranslatableComponent("gui.shrines.structures"), (button) -> this.minecraft.setScreen(new EditStructuresScreen(this.lastScreen, this.packet))));
         this.templatesButton = this
               .addRenderableWidget(new Button(165, 24, 150, 20,
                     new TranslatableComponent("gui.shrines.templates"), (button) -> this.minecraft.setScreen(new EditTemplatesScreen(this.lastScreen, this.packet))));
         this.poolsButton = this
               .addRenderableWidget(new Button(326, 24, 150, 20,
                     new TranslatableComponent("gui.shrines.pools"), (button) -> this.minecraft.setScreen(new EditPoolsScreen(this.lastScreen, this.packet))));
      }
      this.delete = this.addRenderableWidget(new Button(this.width / 2 - 40 - 80 - 3, this.height - 22, 80, 20,
            new TranslatableComponent("gui.shrines.delete"), (button) -> this.delete()));
      this.configure = this.addRenderableWidget(new Button(this.width / 2 - 40, this.height - 22, 80, 20,
            new TranslatableComponent("gui.shrines.configure"), (button) -> this.renameOrConfigure()));
      this.add = this.addRenderableWidget(new Button(this.width / 2 + 40 + 3, this.height - 22, 80, 20,
            new TranslatableComponent("gui.shrines.add"), (button) -> this.add()));
      this.addWidget(this.searchBox);
      this.setInitialFocus(this.searchBox);
   }

   protected abstract void refreshList(String s);

   protected abstract void add();

   protected abstract void delete();

   protected abstract void renameOrConfigure();

   public abstract void updateButtonStatus();

   public void updateButtonStatus(boolean hasSelected) {
      this.delete.active = hasSelected;
      this.configure.active = hasSelected;
   }

   @ParametersAreNonnullByDefault
   public void render(PoseStack ms, int mouseX, int mouseY, float p_230430_4_) {
      this.searchBox.render(ms, mouseX, mouseY, p_230430_4_);
      drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
      super.render(ms, mouseX, mouseY, p_230430_4_);
   }

   @Override
   public Screen getScreen() {
      return this;
   }
}