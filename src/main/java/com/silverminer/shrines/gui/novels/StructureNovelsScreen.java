package com.silverminer.shrines.gui.novels;

import java.util.ArrayList;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.gui.packets.StructuresPacketsScreen;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSPlayerJoinedQueuePacket;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StructureNovelsScreen extends Screen {
	protected final Screen lastScreen;
	private StructureNovelsList list;
	protected TextFieldWidget searchBox;
	private boolean isExpanded = false;
	private int itemSize = 50;
	private AbstractSlider sizeSlider;
	private Button opMode;
	private final ArrayList<StructuresPacket> packets;

	public StructureNovelsScreen(Screen lastScreen, ArrayList<StructuresPacket> packets) {
		super(new TranslationTextComponent("Shrines Structures Novels"));// TRANSLATION
		this.lastScreen = lastScreen;
		this.packets = packets;
	}

	protected void init() {
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true); // Why should I have this?
		this.searchBox = new TextFieldWidget(this.font, (this.width / 4) * 3 - 20, 3, 100, 20, this.searchBox,
				new StringTextComponent(""));
		this.searchBox.setResponder((string) -> {
			this.list.refreshList(() -> {
				return string;
			}, this.packets);
		});
		this.list = new StructureNovelsList(this.minecraft, this.width, this.height, this.isExpanded ? 52 : 26,
				this.height, this.itemSize, () -> {
					return this.searchBox.getValue();
				}, packets);
		this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20,
				new ResourceLocation(ShrinesMod.MODID, "textures/gui/widgets.png"), 256, 256, (button) -> {
					this.minecraft.setScreen(lastScreen);
				}, StringTextComponent.EMPTY));
		this.sizeSlider = this.addButton(new AbstractSlider((this.width / 4) * 3 - 20, 29, 100, 20,
				new TranslationTextComponent("gui.shrines.novels.item_size"),
				this.itemSize <= 50 ? 0.0D : (this.itemSize - 50) / 100.0D) {// TRANSLATION
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
		this.opMode = this.addButton(new Button((this.width / 2) - 50, 29, 100, 20,
				new TranslationTextComponent("Open OP Mode"), (button) -> {
					this.addPlayerToQueue();
				}));// TRANSLATION
		this.opMode.active = this.minecraft.player.hasPermissions(2);
		this.opMode.visible = this.isExpanded;
		this.addButton(new Button((this.width / 4) * 3 + 90, 4, 20, 20,
				new StringTextComponent(this.isExpanded ? "^" : "˅"), (button) -> {
					this.isExpanded = !this.isExpanded;
					this.minecraft.setScreen(this);
				}));
		this.children.add(this.searchBox);
		this.children.add(this.list);
		this.setInitialFocus(this.searchBox);
	}

	public void addPlayerToQueue() {
		this.minecraft.setScreen(new WorkingScreen());
		ShrinesPacketHandler.sendToServer(new CTSPlayerJoinedQueuePacket(this.minecraft.player.getUUID()));
	}

	public void openOpMode() {
		this.minecraft.setScreen(new StructuresPacketsScreen(this, this.packets));
	}

	public void refreshList() {
		this.list.refreshList(() -> this.searchBox.getValue(), this.packets);
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