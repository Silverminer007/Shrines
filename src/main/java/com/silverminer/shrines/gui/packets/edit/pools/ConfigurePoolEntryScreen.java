package com.silverminer.shrines.gui.packets.edit.pools;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.gui.packets.edit.structures.ConfigureStructureList;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.TemplatePool;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ConfigurePoolEntryScreen extends Screen {
	protected Screen lastScreen;
	protected TemplatePool.Entry poolEntry;
	protected ConfigurePoolEntryList list;

	public ConfigurePoolEntryScreen(Screen lastScreen, TemplatePool.Entry poolEntry) {
		super(new TranslationTextComponent("gui.shrines.configuration"));
		this.lastScreen = lastScreen;
		this.poolEntry = poolEntry;
	}

	public void onClose() {
		this.list.getSelectedOpt().ifPresent(ConfigurePoolEntryList.Entry::save);
		this.minecraft.setScreen(lastScreen);
	}

	protected void init() {
		this.list = new ConfigurePoolEntryList(minecraft, this.width, this.height, 24, height, 30, this,
				this.poolEntry);
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