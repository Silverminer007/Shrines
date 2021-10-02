package com.silverminer.shrines.gui.packets.edit;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.config.DefaultStructureConfig;
import com.silverminer.shrines.gui.misc.DirtConfirmScreen;
import com.silverminer.shrines.gui.packets.edit.structures.EditStructuresList;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSEditedStructurePacketPacket;

import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class EditStructurePacketScreen extends Screen {
	protected final StructuresPacket packet;
	protected TextFieldWidget searchBox;
	protected Button structuresButton;
	protected Button templatesButton;
	protected Button poolsButton;
	protected Button delete;
	protected Button configure;
	protected Button add;
	protected Button rename;
	protected EditStructuresList structuresList;
	protected Mode editMode = Mode.STRUCTURES;
	protected int headerheight;
	protected int bottomheight;

	public EditStructurePacketScreen(StructuresPacket packet) {
		super(new TranslationTextComponent("Edit Structure Packet"));// TRANSLATION
		this.packet = packet;
	}

	public void onClose() {
		int IDtoDelete = this.packet.getTempID();
		StructuresPacket newPacket = this.packet.copy();
		this.minecraft.setScreen(new WorkingScreen());
		ShrinesPacketHandler.sendToServer(
				new CTSEditedStructurePacketPacket(newPacket, this.minecraft.player.getUUID(), IDtoDelete));
	}

	protected void init() {
		this.headerheight = 46;
		this.bottomheight = this.height - 26;
		this.searchBox = new TextFieldWidget(this.font, (this.width / 4) * 3, 3, 100, 20, this.searchBox,
				new StringTextComponent(""));
		this.searchBox.setResponder((string) -> {
			this.structuresList.refreshList(() -> {
				return string;
			});
		});
		this.structuresList = new EditStructuresList(this, minecraft, this.width, this.height, this.headerheight,
				this.bottomheight, 35, () -> {
					return this.searchBox.getValue();
				}, packet);
		this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20, ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> {
			this.onClose();
		}, StringTextComponent.EMPTY));
		this.structuresButton = this
				.addButton(new Button(2, 24, 150, 20, new TranslationTextComponent("Structures"), (button) -> {
					this.editMode = Mode.STRUCTURES;
					this.updateButtonStatus(this.structuresList.getSelectedOpt().isPresent());
				}));// TRANSLATION
		this.templatesButton = this
				.addButton(new Button(165, 24, 150, 20, new TranslationTextComponent("Templates"), (button) -> {
					this.editMode = Mode.TEMPLATES;
					this.updateButtonStatus(false);
				}));// TRANSLATION
		this.poolsButton = this
				.addButton(new Button(326, 24, 150, 20, new TranslationTextComponent("Pools"), (button) -> {
					this.editMode = Mode.POOLS;
					this.updateButtonStatus(false);
				}));// TRANSLATION
		this.delete = this.addButton(new Button(this.width / 2 - 40 - 80 - 3, this.height - 22, 80, 20,
				new TranslationTextComponent("Delete"), (button) -> {
					this.minecraft.setScreen(new DirtConfirmScreen((confirmed) -> {
						if (confirmed) {
							switch (this.editMode) {
							case STRUCTURES:
								this.structuresList.getSelectedOpt().ifPresent(entry -> {
									this.packet.getStructures().remove(entry.getStructure());
									this.structuresList.refreshList(() -> this.searchBox.getValue());
								});
								break;
							case TEMPLATES:
							case POOLS:
							default:
							}
						}
						this.minecraft.setScreen(this);
					}, new TranslationTextComponent("gui.shrines.structures.removeQuestion",
							this.structuresList.getSelected().getStructure().getName()),
							new TranslationTextComponent("gui.shrines.structures.removeWarning"),
							new TranslationTextComponent("gui.shrines.structures.deleteButton"),
							DialogTexts.GUI_CANCEL));

				}));// TRANSLATION
		this.configure = this.addButton(new Button(this.width / 2 - 40, this.height - 22, 80, 20,
				new TranslationTextComponent("Configure"), (button) -> {
					switch (this.editMode) {
					case STRUCTURES:
						this.structuresList.getSelectedOpt().ifPresent(EditStructuresList.Entry::configure);
						break;
					case TEMPLATES:
					case POOLS:
					default:
					}
				}));// TRANSLATION
		this.add = this.addButton(new Button(this.width / 2 + 40 + 3, this.height - 22, 80, 20,
				new TranslationTextComponent("Add"), (button) -> {
					switch (this.editMode) {
					case STRUCTURES:
						this.packet.getStructures().add(new StructureData(DefaultStructureConfig.CUSTOM));
						this.structuresList.refreshList(() -> this.searchBox.getValue());
						this.structuresList.setSelected(this.structuresList.children().get(0));
						this.structuresList.getSelectedOpt().ifPresent(EditStructuresList.Entry::configure);
						break;
					case TEMPLATES:
					case POOLS:
					default:
					}
				}));// TRANSLATION
		this.updateButtonStatus(false);
		this.children.add(structuresList);
		this.children.add(searchBox);
		this.setInitialFocus(this.searchBox);
	}

	public void updateButtonStatus(boolean hasSelected) {
		this.structuresButton.active = this.editMode != Mode.STRUCTURES;
		this.templatesButton.active = this.editMode != Mode.TEMPLATES;
		this.poolsButton.active = this.editMode != Mode.POOLS;
		this.delete.active = hasSelected;
		this.configure.active = hasSelected;
		/*
		 * if (this.children.contains(this.structuresList)) {
		 * this.children.remove(this.structuresList); } if (this.editMode ==
		 * Mode.STRUCTURES) { this.children.add(structuresList); }
		 */}

	public void render(MatrixStack ms, int mouseX, int mouseY, float p_230430_4_) {
		if (this.editMode == Mode.STRUCTURES)
			this.structuresList.render(ms, mouseX, mouseY, p_230430_4_);
		this.searchBox.render(ms, mouseX, mouseY, p_230430_4_);
		drawCenteredString(ms, this.font, this.title, this.width / 2, 8, 16777215);
		super.render(ms, mouseX, mouseY, p_230430_4_);
	}

	protected static enum Mode {
		STRUCTURES, TEMPLATES, POOLS;
	}
}