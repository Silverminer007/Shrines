/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets.edit.pools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.gui.packets.edit.EditStructurePacketScreen;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import com.silverminer.shrines.packages.datacontainer.TemplatePool;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.ParametersAreNonnullByDefault;

public class ConfigurePoolScreen extends EditStructurePacketScreen {
   private final TemplatePool pool;
   protected ConfigurePoolList poolsList;

   public ConfigurePoolScreen(Screen lastScreen, StructuresPackageWrapper packet, TemplatePool pool) {
      super(lastScreen, new TranslatableComponent("gui.shrines.pools.configure_pool"), packet, false);
      this.pool = pool;
   }

   @Override
   protected void init() {
      super.init();
      this.headerheight = 26;
      this.delete.setMessage(new TranslatableComponent("gui.shrines.remove"));
      this.poolsList = new ConfigurePoolList(minecraft, this.width, this.height, this.headerheight,
            this.bottomheight, 23, () -> this.searchBox.getValue(), this, pool);
      this.addWidget(this.poolsList);
      this.updateButtonStatus();
      this.refreshList(this.searchBox.getValue());
   }

   @Override
   protected void refreshList(String s) {
      this.poolsList.refreshList(() -> s);
   }

   @Override
   protected void add() {
      if (this.minecraft == null) {
         return;
      }
      this.minecraft.setScreen(new AddTemplateToPoolScreen(this, this.packet, this.pool));
   }

   @Override
   protected void delete() {
      this.poolsList.getSelectedOpt().ifPresent(entry -> this.pool.getEntries().remove(entry.getPoolEntry()));
      this.refreshList(this.searchBox.getValue());
   }

   @Override
   protected void renameOrConfigure() {
      this.poolsList.getSelectedOpt().ifPresent(ConfigurePoolList.Entry::configure);
   }

   @Override
   public void updateButtonStatus() {
      super.updateButtonStatus(this.poolsList.getSelectedOpt().isPresent());
   }

   @Override
   @ParametersAreNonnullByDefault
   public void render(PoseStack ms, int mouseX, int mouseY, float p_230430_4_) {
      this.poolsList.render(ms, mouseX, mouseY, p_230430_4_);
      super.render(ms, mouseX, mouseY, p_230430_4_);
   }
}
