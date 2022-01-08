/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets.edit.pools;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.gui.misc.DirtConfirmScreen;
import com.silverminer.shrines.gui.misc.SetNameScreen;
import com.silverminer.shrines.gui.packets.edit.EditStructurePacketScreen;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import com.silverminer.shrines.packages.datacontainer.TemplatePool;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

public class EditPoolsScreen extends EditStructurePacketScreen {
   protected EditPoolsList poolsList;

   public EditPoolsScreen(Screen lastScreen, StructuresPackageWrapper packet) {
      super(lastScreen, packet);
   }

   protected void init() {
      super.init();
      this.poolsList = new EditPoolsList(minecraft, this.width, this.height, this.headerheight,
            this.bottomheight, 23, packet, this);
      this.addWidget(this.poolsList);
      this.updateButtonStatus();
      this.refreshList(searchBox.getValue());
   }

   @Override
   protected void refreshList(String s) {
      this.poolsList.refreshList(() -> s);
   }

   @Override
   protected void add() {
      if (this.minecraft != null) {
         this.minecraft.setScreen(new SetNameScreen(this, new TranslatableComponent("gui.shrines.pools.enter_name"), TextComponent.EMPTY, TextComponent.EMPTY,
               (value) -> this.packet.getPools().add(new TemplatePool(new ResourceLocation(value), Lists.newArrayList())),
               (value) -> !value.isEmpty() && ResourceLocation.isValidResourceLocation(value) && this.packet.getPools().getAsList().stream().map(pool -> pool.getName().toString()).noneMatch(pool -> pool.equals(value))
         ));
      }
   }

   @Override
   protected void delete() {
      if (this.minecraft != null && this.poolsList.getSelected() != null) {
         this.minecraft.setScreen(new DirtConfirmScreen((confirmed) -> {
            if (confirmed) {
               this.poolsList.getSelectedOpt().ifPresent(entry -> {
                  TemplatePool pool = entry.getPool();
                  this.packet.getPools().remove(pool.getSaveName());
               });
            }
            this.minecraft.setScreen(this);
         }, new TranslatableComponent("gui.shrines.removeQuestion",
               this.poolsList.getSelected().getPool().getName().toString()),
               new TranslatableComponent("gui.shrines.removeWarning"),
               new TranslatableComponent("gui.shrines.delete"),
               CommonComponents.GUI_CANCEL));
      }

   }

   @Override
   protected void renameOrConfigure() {
      this.poolsList.getSelectedOpt().ifPresent(EditPoolsList.Entry::configure);
   }

   public void updateButtonStatus() {
      this.structuresButton.active = true;
      this.templatesButton.active = true;
      this.poolsButton.active = false;
      super.updateButtonStatus(this.poolsList.getSelectedOpt().isPresent());
   }

   @ParametersAreNonnullByDefault
   public void render(PoseStack ms, int mouseX, int mouseY, float p_230430_4_) {
      this.poolsList.render(ms, mouseX, mouseY, p_230430_4_);
      super.render(ms, mouseX, mouseY, p_230430_4_);
   }

   @Override
   public Screen getScreen() {
      return this;
   }
}
