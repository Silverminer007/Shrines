/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.client.gui.config.widgets.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ValidationStatusButton extends Button {
   private boolean valid;

   public ValidationStatusButton(int x, int y, IPressable clickHandler) {
      super(x, y, 15, 15, new StringTextComponent(""), clickHandler);

      this.valid = true;
   }

   public void setValid() {
      this.valid = true;
   }

   public void setInvalid() {
      this.valid = false;
   }

   public boolean isValid() {
      return this.valid;
   }

   public void setValid(boolean isValid) {
      this.valid = isValid;
   }

   @SuppressWarnings("deprecation")
   @Override
   public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
      Minecraft.getInstance().getTextureManager().bind(Button.WIDGETS_LOCATION);
      GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
      Icon icon = (this.valid) ? Icon.VALID : Icon.INVALID;

      this.blit(ms, this.x, this.y, icon.getX(), icon.getY(), this.width, this.height);
   }

   @Override
   public boolean changeFocus(boolean forward) {
      return false;
   }

   enum Icon {
      VALID(208, 0),
      INVALID(192, 0);

      private final int x;
      private final int y;

      Icon(int x, int y) {
         this.x = x;
         this.y = y;
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }
   }
}
