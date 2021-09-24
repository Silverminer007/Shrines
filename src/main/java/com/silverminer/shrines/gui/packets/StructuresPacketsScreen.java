package com.silverminer.shrines.gui.packets;

import java.util.ArrayList;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSPlayerLeftQueuePacket;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StructuresPacketsScreen extends Screen {
	protected final Screen lastScreen;
	protected TextFieldWidget searchBox;
	protected StructurePacketsList list;
	protected Button delete;
	protected Button configure;
	protected Button add;
	protected Button rename;
	private final ArrayList<StructuresPacket> packets;

	public StructuresPacketsScreen(Screen lastScreen, ArrayList<StructuresPacket> packets) {
		super(new TranslationTextComponent("Structure Packets"));// TRANSLATION
		this.lastScreen = lastScreen;
		this.packets = packets;
	}

	protected void init() {
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true); // Why should I have this?
		this.searchBox = new TextFieldWidget(this.font, (this.width / 4) * 3, 3, 100, 20, this.searchBox,
				new StringTextComponent(""));
		this.searchBox.setResponder((string) -> {
			this.list.refreshList(() -> {
				return string;
			});
		});
		this.list = new StructurePacketsList(this, this.minecraft, this.width, this.height, 26, this.height - 26, 36,
				() -> {
					return this.searchBox.getValue();
				}, this.packets);
		this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20,
				new ResourceLocation(ShrinesMod.MODID, "textures/gui/widgets.png"), 256, 256, (button) -> {
					this.minecraft.setScreen(this.lastScreen);
					ShrinesPacketHandler.sendToServer(new CTSPlayerLeftQueuePacket(this.minecraft.player.getUUID()));
				}, StringTextComponent.EMPTY));
		this.delete = this.addButton(new Button(this.width / 2 - 80 - 80 - 9, this.height - 22, 80, 20,
				new TranslationTextComponent("Delete"), (button) -> {
					this.list.getSelectedOpt().ifPresent(StructurePacketsList.Entry::remove);
				}));// TRANSLATION
		this.configure = this.addButton(new Button(this.width / 2 - 80 - 3, this.height - 22, 80, 20,
				new TranslationTextComponent("Configure"), (button) -> {
					this.list.getSelectedOpt().ifPresent(StructurePacketsList.Entry::configure);
				}));// TRANSLATION
		this.add = this.addButton(new Button(this.width / 2 + 3, this.height - 22, 80, 20,
				new TranslationTextComponent("Add"), (button) -> {
					this.minecraft.setScreen(new AddStructurePacketScreen(this));
				}));// TRANSLATION
		this.rename = this.addButton(new Button(this.width / 2 + 80 + 9, this.height - 22, 80, 20,
				new TranslationTextComponent("Rename"), (button) -> {
					this.list.getSelectedOpt().ifPresent(StructurePacketsList.Entry::rename);
				}));// TRANSLATION
		this.updateButtonStatus(false);
		this.children.add(this.searchBox);
		this.children.add(this.list);
		this.setInitialFocus(this.searchBox);
	}

	public void render(MatrixStack matrixStack, int x, int y, float p_230430_4_) {
		this.list.render(matrixStack, x, y, p_230430_4_);
		this.searchBox.render(matrixStack, x, y, p_230430_4_);
		drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 16777215);
		super.render(matrixStack, x, y, p_230430_4_);
	}

	public void updateButtonStatus(boolean hasSelected) {
		this.delete.active = hasSelected;
		this.configure.active = hasSelected;
		this.rename.active = hasSelected;
	}
}