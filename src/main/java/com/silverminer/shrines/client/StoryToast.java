/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StoryToast implements Toast {
   private final ResourceLocation storyID;
   private final Component title;
   private final List<FormattedCharSequence> snippetText;

   private final long displayTime;
   private final int width;

   private StoryToast(ResourceLocation storyID, List<FormattedCharSequence> snippetText, int width, long displayTime) {
      this.storyID = storyID;
      this.title = Component.translatable("gui.shrines.stories.unlock.title", storyID);// TODO Translation
      this.snippetText = snippetText;
      this.width = width;
      this.displayTime = displayTime;
   }

   private long lastChanged;
   private boolean changed;

   public static @NotNull StoryToast create(@NotNull Minecraft mc, ResourceLocation storyID, String snippetText) {
      Font font = mc.font;
      Component snippetTextTranslated = Component.translatable(snippetText);
      List<FormattedCharSequence> list = font.split(snippetTextTranslated, 200);
      int i = Math.max(200, list.stream().mapToInt(font::width).max().orElse(200));
      return new StoryToast(storyID, list, i + 30, snippetTextTranslated.getString().length() * 1000L);
   }

   public int width() {
      return this.width;
   }

   public Toast.@NotNull Visibility render(@NotNull PoseStack poseStack, @NotNull ToastComponent toastComponent, long tick) {
      if (this.changed) {
         this.lastChanged = tick;
         this.changed = false;
      }

      RenderSystem.setShaderTexture(0, TEXTURE);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      int i = this.width();
      int j = 12;
      if (i == 160 && this.snippetText.size() <= 1) {
         toastComponent.blit(poseStack, 0, 0, 0, 64, i, this.height());
      } else {
         int k = this.height() + Math.max(0, this.snippetText.size() - 1) * 12;
         int l = 28;
         int i1 = Math.min(4, k - 28);
         this.renderBackgroundRow(poseStack, toastComponent, i, 0, 0, 28);

         for (int j1 = 28; j1 < k - i1; j1 += 10) {
            this.renderBackgroundRow(poseStack, toastComponent, i, 16, j1, Math.min(16, k - j1 - i1));
         }

         this.renderBackgroundRow(poseStack, toastComponent, i, 32 - i1, k - i1, i1);
      }

      toastComponent.getMinecraft().font.draw(poseStack, this.title, 18.0F, 7.0F, -256);

      for (int k1 = 0; k1 < this.snippetText.size(); ++k1) {
         toastComponent.getMinecraft().font.draw(poseStack, this.snippetText.get(k1), 18.0F, (float) (18 + k1 * 12), -1);
      }

      return tick - this.lastChanged < this.displayTime ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
   }

   private void renderBackgroundRow(PoseStack poseStack, @NotNull ToastComponent toastComponent, int p_94839_, int p_94840_, int p_94841_, int p_94842_) {
      int i = p_94840_ == 0 ? 20 : 5;
      int j = Math.min(60, p_94839_ - i);
      toastComponent.blit(poseStack, 0, p_94841_, 0, 64 + p_94840_, i, p_94842_);

      for (int k = i; k < p_94839_ - j; k += 64) {
         toastComponent.blit(poseStack, k, p_94841_, 32, 64 + p_94840_, Math.min(64, p_94839_ - k - j), p_94842_);
      }

      toastComponent.blit(poseStack, p_94839_ - j, p_94841_, 160 - j, 64 + p_94840_, j, p_94842_);
   }

   public @NotNull ResourceLocation getToken() {
      return this.storyID;
   }
}