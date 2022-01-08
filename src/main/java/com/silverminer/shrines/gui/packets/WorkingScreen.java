/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.gui.misc.ColorLoop;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class WorkingScreen extends Screen implements SkipableScreen {
   private final ColorLoop loop = new ColorLoop();
   private final Screen screenOnSuccess;
   private final Screen screenOnFail;
   private final Component info;

   public WorkingScreen(Screen screenOnSuccess, Screen screenOnFail, Component message, Component info) {
      super(message);
      this.screenOnSuccess = screenOnSuccess;
      this.screenOnFail = screenOnFail;
      this.info = info;
   }

   public void onClose() {
   }

   protected void init() {
   }

   public void render(@NotNull PoseStack matrixStack, int x, int y, float p_230430_4_) {
      this.loop.tick();
      this.renderDirtBackground(0);
      drawCenteredString(matrixStack, this.font, this.info,
            this.width / 2, this.height / 2, 0xffffff);
      drawCenteredString(matrixStack, this.font,
            new TranslatableComponent("gui.shrines.patient")
                  .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(this.loop.getRGB()))),
            this.width / 2, this.height / 2 + 15, 0xffffff);
      drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 0xffffff);
      super.render(matrixStack, x, y, p_230430_4_);
   }

   public Screen getLastScreen() {
      return screenOnSuccess;
   }

   public Screen getScreenOnFail() {
      return this.screenOnFail;
   }
}
