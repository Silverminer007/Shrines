/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.client.gui.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.utils.client.ColorLoop;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author Silverminer
 */
public class CoomingSoonScreen extends Screen {
    protected static final Logger LOGGER = LogManager.getLogger(CoomingSoonScreen.class);
    protected static final TranslationTextComponent TITLE = new TranslationTextComponent(
            "shrines.general.cooming_soon");
    private ColorLoop loop = new ColorLoop();

    public CoomingSoonScreen() {
        super(TITLE);
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        super.render(ms, mouseX, mouseY, partialTicks);
        this.loop.tick();
        try {
            String text = TITLE.getString();
            int tw = minecraft.font.width(text) / 2;
            int th = minecraft.font.lineHeight / 2;
            int color = this.loop.getRGB();
            int width = this.width / 4;
            int height = this.height / 4;
            for (int x = 1; x <= 3; x++) {
                for (int y = 1; y <= 3; y++) {
                    minecraft.font.drawShadow(ms, TITLE, (width) * x - tw, (height) * y - th, color);
                }
            }
        } catch (IllegalArgumentException e) {
            LOGGER.info("RGB {}", this.loop.getRGB());
        }
    }
}