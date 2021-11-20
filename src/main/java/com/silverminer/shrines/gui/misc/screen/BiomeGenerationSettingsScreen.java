package com.silverminer.shrines.gui.misc.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.utils.ClientUtils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

@OnlyIn(Dist.CLIENT)
public class BiomeGenerationSettingsScreen extends Screen {
	protected Screen lastScreen;
	protected TextFieldWidget searchBox;
	protected BiomeGenerationSettingsList list;
	protected final ArrayList<String> selectedBiomes;
	protected final ArrayList<String> selectedBiomeCategories;
	protected final Consumer<List<String>> biomeSetter;
	protected final Consumer<List<String>> biomeCategorySetter;

	public BiomeGenerationSettingsScreen(Screen lastScreen, List<String> selectedBiomes,
										 List<String> selectedBiomeCategories, ITextComponent title, Consumer<List<String>> setter,
										 Consumer<List<String>> categoriesSetter) {
		super(title);
		this.lastScreen = lastScreen;
		this.selectedBiomes = Lists.newArrayList(selectedBiomes);
		this.selectedBiomeCategories = Lists.newArrayList(selectedBiomeCategories);
		this.biomeSetter = setter;
		this.biomeCategorySetter = categoriesSetter;
	}

	public void onClose() {
		this.biomeSetter.accept(selectedBiomes);
		this.biomeCategorySetter.accept(selectedBiomeCategories);
		this.minecraft.setScreen(this.lastScreen);
	}

	protected void init() {
		this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> {
			this.onClose();
		}, StringTextComponent.EMPTY));
		this.searchBox = new TextFieldWidget(this.font, (this.width / 4) * 3, 3, 100, 20, this.searchBox,
				new StringTextComponent(""));
		this.searchBox.setResponder((string) -> {
			this.list.refreshList(() -> {
				return string;
			});
		});
		this.list = new BiomeGenerationSettingsList(this, minecraft, this.width, this.height, 26, this.height, 26,
				() -> this.searchBox.getValue());
		this.children.add(searchBox);
		this.children.add(list);
	}

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.searchBox.render(ms, mouseX, mouseY, partialTicks);
		this.list.render(ms, mouseX, mouseY, partialTicks);
		drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
		super.render(ms, mouseX, mouseY, partialTicks);
	}
}