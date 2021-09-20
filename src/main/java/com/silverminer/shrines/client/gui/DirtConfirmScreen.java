package com.silverminer.shrines.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.util.text.ITextComponent;

public class DirtConfirmScreen extends ConfirmScreen {

	public DirtConfirmScreen(BooleanConsumer callback, ITextComponent title1, ITextComponent title2, ITextComponent yes,
			ITextComponent no) {
		super(callback, title1, title2, yes, no);
	}

	public void renderBackground(MatrixStack p_238651_1_, int p_238651_2_) {
		this.renderDirtBackground(p_238651_2_);
	}
}