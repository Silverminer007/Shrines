/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.packets.edit.pools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.packages.datacontainer.TemplatePool;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ConfigurePoolEntryScreen extends Screen {
	protected Screen lastScreen;
	protected TemplatePool.Entry poolEntry;
	protected ConfigurePoolEntryList list;

	public ConfigurePoolEntryScreen(Screen lastScreen, TemplatePool.Entry poolEntry) {
		super(new TranslatableComponent("gui.shrines.configuration"));
		this.lastScreen = lastScreen;
		this.poolEntry = poolEntry;
	}

	public void onClose() {
		if(this.minecraft == null){
			return;
		}
		this.list.getSelectedOpt().ifPresent(ConfigurePoolEntryList.Entry::save);
		this.minecraft.setScreen(lastScreen);
	}

	protected void init() {
		this.list = new ConfigurePoolEntryList(minecraft, this.width, this.height, 24, height, 30, this,
				this.poolEntry);
		this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), TextComponent.EMPTY));
		this.addWidget(this.list);
	}

	@Override
	public void render(@NotNull PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.list.render(ms, mouseX, mouseY, partialTicks);
		drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
		super.render(ms, mouseX, mouseY, partialTicks);
	}
}