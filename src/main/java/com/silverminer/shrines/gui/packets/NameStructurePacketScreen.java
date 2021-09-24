package com.silverminer.shrines.gui.packets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.ShrinesMod;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class NameStructurePacketScreen extends Screen {
	protected final Screen lastScreen;
	protected TextFieldWidget nameField;
	protected Button confirmButton;

	protected NameStructurePacketScreen(Screen lastScreen) {
		super(new TranslationTextComponent("Add Structures packet"));// TRANSLATION
		this.lastScreen = lastScreen;
	}

	protected void init() {
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true); // Why should I have this?
		String message = this.nameField == null ? this.defaultNameFieldString().getString() : this.nameField.getValue();
		this.nameField = new TextFieldWidget(font, this.width / 2 - 75, this.height / 2 - 40, 150, 20,
				StringTextComponent.EMPTY);
		this.nameField.setValue(message);
		this.nameField.setResponder(s -> {
			this.confirmButton.active = !s.replaceAll(" ", "").isEmpty();
		});
		this.confirmButton = this.addButton(new Button(this.width / 2 - 70, this.height / 2 + 20, 140, 20,
				new TranslationTextComponent("Confirm"), (button) -> {
					this.done();
				}));// TRANSLATION
		this.confirmButton.active = !this.nameField.getValue().isEmpty();
		this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20,
				new ResourceLocation(ShrinesMod.MODID, "textures/gui/widgets.png"), 256, 256, (button) -> {
					this.minecraft.setScreen(lastScreen);
				}, StringTextComponent.EMPTY));
		this.children.add(nameField);
	}

	public abstract ITextComponent defaultNameFieldString();

	public void render(MatrixStack matrixStack, int x, int y, float p_230430_4_) {
		this.renderDirtBackground(0);
		this.nameField.render(matrixStack, x, y, p_230430_4_);
		drawCenteredString(matrixStack, font,
				new TranslationTextComponent("The name may change if there is already a package with this name"),
				this.width / 2, this.height / 2 - 16, 0x999999);// TRANSLATION
		drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 0xffffff);
		super.render(matrixStack, x, y, p_230430_4_);
	}

	public abstract void done();
}