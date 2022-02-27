/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.misc.buttons;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.ShrinesMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BooleanValueButton extends Button {
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(ShrinesMod.MODID,
            "textures/gui/buttons_boolean.png");
    public boolean value;

    public BooleanValueButton(int posX, int posY, Component text, Button.OnPress onPress, boolean value) {
        super(posY, posY, 40, 20, text, onPress);
        this.value = value;
    }

    public BooleanValueButton(int posX, int posY, Component text, Button.OnPress onPress, Button.OnTooltip tooltip,
                              boolean value) {
        super(posY, posY, 40, 20, text, onPress, tooltip);
        this.value = value;
    }

    public void onClick(double mouseX, double mouseY) {
        this.value = !this.value;
        super.onClick(mouseX, mouseY);
    }

    @Override
    public void render(@NotNull PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        boolean isHovered = this.isMouseOver(mouseX, mouseY);
        if (this.visible) {
            Minecraft minecraft = Minecraft.getInstance();
            RenderSystem.setShaderTexture(0, BUTTON_TEXTURE);
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            int buttonH = isHovered ? 20 : 0;
            buttonH += this.value ? 40 : 0;
            blit(ms, this.x, this.y, 0, buttonH, this.width, this.height);
            this.renderBg(ms, minecraft, mouseX, mouseY);
        }
    }
}