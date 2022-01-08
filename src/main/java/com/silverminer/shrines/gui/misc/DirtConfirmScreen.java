/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DirtConfirmScreen extends ConfirmScreen {

	public DirtConfirmScreen(BooleanConsumer callback, Component title1, Component title2, Component yes,
							 Component no) {
		super(callback, title1, title2, yes, no);
	}

	public void renderBackground(PoseStack p_238651_1_, int p_238651_2_) {
		this.renderDirtBackground(p_238651_2_);
	}
}