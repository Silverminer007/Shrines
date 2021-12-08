package com.silverminer.shrines.gui.packets.edit.structures;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.ClientUtils;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
		super(new TranslatableComponent("gui.shrines.configuration"));
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
		this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> {
			this.onClose();
		}, TextComponent.EMPTY));
		this.addWidget(this.list);
	}

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.list.render(ms, mouseX, mouseY, partialTicks);
		drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
		super.render(ms, mouseX, mouseY, partialTicks);
	}
}