package com.silverminer.shrines.gui.packets.edit.structures;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.ClientUtils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConfigureStructureScreen extends Screen {
	protected Screen lastScreen;
	protected StructureData structure;
	protected List<String> possibleDimensions;
	protected ConfigureStructureList list;
	protected final StructuresPacket packet;

	public ConfigureStructureScreen(Screen lastScreen, StructureData structure, List<String> possibleDimensions, StructuresPacket packet) {
		super(new TranslationTextComponent("gui.shrines.configuration"));
		this.lastScreen = lastScreen;
		this.structure = structure;
		this.possibleDimensions = possibleDimensions;
		this.packet = packet;
	}

	public void onClose() {
		this.list.getSelectedOpt().ifPresent(ConfigureStructureList.Entry::save);
		this.minecraft.setScreen(lastScreen);
	}

	protected void init() {
		this.list = new ConfigureStructureList(minecraft, this.width, this.height, 24, height, 30, this,
				this.structure, packet);
		this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> {
			this.onClose();
		}, StringTextComponent.EMPTY));
		this.children.add(list);
	}

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.list.render(ms, mouseX, mouseY, partialTicks);
		drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
		super.render(ms, mouseX, mouseY, partialTicks);
	}
}