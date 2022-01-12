/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.misc.ColorLoop;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSPlayerLeftQueuePacket;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WaitInQueueScreen extends Screen {
	private final int position;
	private final ColorLoop loop;

	public WaitInQueueScreen(int position) {
		super(new TranslationTextComponent("gui.shrines.queue"));
		this.position = position;
		this.loop = new ColorLoop();
	}

	public void onClose() {
		this.minecraft.setScreen(new WorkingScreen());
		ShrinesPacketHandler.sendToServer(new CTSPlayerLeftQueuePacket());
	}

	protected void init() {
		this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> {
			this.onClose();
		}, StringTextComponent.EMPTY));
	}

	public void render(MatrixStack matrixStack, int x, int y, float p_230430_4_) {
		this.loop.tick();
		this.renderDirtBackground(0);
		drawCenteredString(matrixStack, this.font,
				new TranslationTextComponent(
						"gui.shrines.queue.info", this.position),
				this.width / 2, this.height / 2, 0xffffff);
		drawCenteredString(matrixStack, this.font,
				new TranslationTextComponent("gui.shrines.queue.patient")
						.setStyle(Style.EMPTY.withColor(Color.fromRgb(this.loop.getRGB()))),
				this.width / 2, this.height / 2 + 15, 0xffffff);
		drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 0xffffff);
		super.render(matrixStack, x, y, p_230430_4_);
	}
}