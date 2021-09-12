package com.silverminer.shrines.client.gui;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.new_custom_structures.StructuresPacket;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class StructureNovelsScreen extends Screen {
	protected final Screen lastScreen;
	private StructureNovelsList list;
	protected TextFieldWidget searchBox;
	private boolean isExpanded = false;
	private int itemSize = 50;
	private AbstractSlider sizeSlider;
	private final List<StructuresPacket> packets;

	public StructureNovelsScreen(Screen lastScreen, List<StructuresPacket> packets) {
		super(new TranslationTextComponent("Shrines Structures Novels"));// TODO Create Translations
		this.lastScreen = lastScreen;
		this.packets = packets;
	}

	protected void init() {
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true); // Why should I have this?
		this.searchBox = new TextFieldWidget(this.font, (this.width / 4) * 3 - 20, 3, 100, 20, this.searchBox,
				new TranslationTextComponent(""));
		this.searchBox.setResponder((string) -> {
			this.list.refreshList(() -> {
				return string;
			}, this.packets);
		});
		this.list = new StructureNovelsList(this.minecraft, this.width, this.height, this.isExpanded ? 52 : 26,
				this.height, this.itemSize, () -> {
					return this.searchBox.getValue();
				}, packets, this);
		this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20,
				new ResourceLocation(ShrinesMod.MODID, "textures/gui/widgets.png"), 256, 256, (button) -> {
					this.minecraft.setScreen(lastScreen);
				}, StringTextComponent.EMPTY));
		this.sizeSlider = this.addButton(new AbstractSlider((this.width / 4) * 3 - 20, 29, 100, 20,
				StringTextComponent.EMPTY, this.itemSize <= 50 ? 0.0D : (this.itemSize - 50) / 100.0D) {
			{
				this.updateMessage();
			}

			protected void updateMessage() {
				this.setMessage(
						new StringTextComponent(String.valueOf(StructureNovelsScreen.this.list.getEntrySize())));
			}

			protected void applyValue() {
				int res = MathHelper.floor(MathHelper.clampedLerp(50.0D, 150.0D, this.value));
				StructureNovelsScreen.this.itemSize = res;
				StructureNovelsScreen.this.minecraft.setScreen(StructureNovelsScreen.this);
			}
		});
		this.sizeSlider.visible = this.isExpanded;
		this.addButton(new Button((this.width / 4) * 3 + 90, 4, 20, 20,
				new StringTextComponent(this.isExpanded ? "^" : "˅"), (button) -> {
					this.isExpanded = !this.isExpanded;
					this.minecraft.setScreen(this);
				}));
		this.children.add(this.searchBox);
		this.children.add(this.list);
		this.setInitialFocus(this.searchBox);
	}

	public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_) ? true
				: this.searchBox.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
	}

	public void onClose() {
		this.minecraft.setScreen(this.lastScreen);
	}

	public boolean charTyped(char p_231042_1_, int p_231042_2_) {
		return this.searchBox.charTyped(p_231042_1_, p_231042_2_);
	}

	public void render(MatrixStack matrixStack, int x, int y, float p_230430_4_) {
		this.list.render(matrixStack, x, y, p_230430_4_);
		this.searchBox.render(matrixStack, x, y, p_230430_4_);
		drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 16777215);
		super.render(matrixStack, x, y, p_230430_4_);
	}
}