/**
 * Silverminer (and Team)
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * 
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.client.gui.config.resource;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.structures.custom.helper.ResourceData;
import com.silverminer.shrines.utils.custom_structures.Utils;
import com.silverminer.shrines.utils.network.CSaveCustomStructuresPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author Silverminer
 *
 */
public class AddResourceScreen extends Screen {
	protected static final Logger LOGGER = LogManager.getLogger(AddResourceScreen.class);
	protected static final ITextComponent TITLE = new TranslationTextComponent("gui.shrines.structures.resource.title");
	public static AddResourceScreen isInPiecesScreen = null;
	protected final Screen parent;
	protected final CustomStructureData data;
	protected final ArrayList<ResourceData> originData;
	protected TextFieldWidget x0;
	protected TextFieldWidget y0;
	protected TextFieldWidget z0;
	protected TextFieldWidget x1;
	protected TextFieldWidget y1;
	protected TextFieldWidget z1;
	protected static final ITextComponent DEFAULT_MESSAGE = new TranslationTextComponent(
			"gui.shrines.structures.resources.choose");
	protected ITextComponent message = DEFAULT_MESSAGE;
	protected boolean error = false;
	protected Button saveButton;

	public AddResourceScreen(Screen parent, CustomStructureData csd) {
		super(TITLE);
		this.data = csd;
		this.originData = csd.PIECES_ON_FLY;
		this.parent = parent;
		isInPiecesScreen = this;
	}

	public void init(Minecraft ms, int width, int height) {
		super.init(ms, width, height);
		MutableBoundingBox mbb = MutableBoundingBox.getUnknownBox();
		if (!this.data.PIECES_ON_FLY.isEmpty())
			for (ResourceData rd : this.data.PIECES_ON_FLY) {
				mbb.expand(rd.getBounds());
			}
		else
			mbb = MutableBoundingBox.createProper(0, 0, 0, 0, 0, 0);
		int i = (this.width) / 2;
		int j = (this.height) / 2;
		this.x0 = new TextFieldWidget(this.font, i + 62, j - 100, 50, 20, new StringTextComponent("x0"));
		this.y0 = new TextFieldWidget(this.font, i + 116, j - 100, 50, 20, new StringTextComponent("y0"));
		this.z0 = new TextFieldWidget(this.font, i + 170, j - 100, 50, 20, new StringTextComponent("z0"));
		this.x1 = new TextFieldWidget(this.font, i + 62, j - 75, 50, 20, new StringTextComponent("x1"));
		this.y1 = new TextFieldWidget(this.font, i + 116, j - 75, 50, 20, new StringTextComponent("y1"));
		this.z1 = new TextFieldWidget(this.font, i + 170, j - 75, 50, 20, new StringTextComponent("z1"));

		this.x0.insertText(String.valueOf(mbb.x0));
		this.y0.insertText(String.valueOf(mbb.y0));
		this.z0.insertText(String.valueOf(mbb.z0));
		this.x1.insertText(String.valueOf(mbb.x1));
		this.y1.insertText(String.valueOf(mbb.y1));
		this.z1.insertText(String.valueOf(mbb.z1));

		this.z0.setResponder(this::onPosChanged);
		this.x0.setResponder(this::onPosChanged);
		this.y0.setResponder(this::onPosChanged);
		this.x1.setResponder(this::onPosChanged);
		this.y1.setResponder(this::onPosChanged);
		this.z1.setResponder(this::onPosChanged);

		this.children.add(x0);
		this.children.add(y0);
		this.children.add(z0);
		this.children.add(x1);
		this.children.add(y1);
		this.children.add(z1);

		this.saveButton = this.addButton(new Button(i + 144, j - 50, 77, 20,
				new TranslationTextComponent("gui.shrines.structures.save"), (button) -> {
					LOGGER.info("Started Saving structure {}", this.data.PIECES_ON_FLY);
					Utils.replace(data, false);
					Utils.onChanged(false);
					ShrinesPacketHandler.sendToServer(new CSaveCustomStructuresPacket(this.data.getName(),
							this.minecraft.player.getName().getString(), true,
							this.minecraft.level.dimension().location()));// TODO Add include entities option
					WorkingScreen ws = new WorkingScreen();
					this.minecraft.setScreen(ws);
					ws.progressStart(new TranslationTextComponent("gui.shrines.structures.resource.save.start"));
					isInPiecesScreen = this;
				}));
		this.addButton(new Button(i + 62, j - 50, 77, 20, new TranslationTextComponent("gui.shrines.structures.back"),
				(button) -> {
					data.PIECES_ON_FLY.clear();
					data.PIECES_ON_FLY.addAll(originData);
					Utils.replace(data, false);
					isInPiecesScreen = null;
					this.minecraft.setScreen(parent);
				}));
		this.addButton(new Button(i + 62, j - 27, 159, 20, new TranslationTextComponent("gui.shrines.structures.world"),
				(button) -> {
					Utils.onChanged(false);
					this.minecraft.setScreen(null);
				}));
		this.onPosChanged("");
	}

	/**
	 * Called from networking to wait for save complete
	 */
	public void onSaveComplete() {
		isInPiecesScreen = null;
		this.minecraft.setScreen(parent);
	}

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		super.render(ms, mouseX, mouseY, partialTicks);
		this.x0.setAlpha(0.0f);
		this.x0.render(ms, mouseX, mouseY, partialTicks);
		this.y0.render(ms, mouseX, mouseY, partialTicks);
		this.z0.render(ms, mouseX, mouseY, partialTicks);
		this.x1.render(ms, mouseX, mouseY, partialTicks);
		this.y1.render(ms, mouseX, mouseY, partialTicks);
		this.z1.render(ms, mouseX, mouseY, partialTicks);
		String text = this.message.getString();
		int tw = this.minecraft.font.width(text) / 2;
		this.minecraft.font.drawShadow(ms, text, (this.width / 2) - tw, 10, this.error ? 0xff0000 : 0xff00ff);
	}

	public void onPosChanged(String text) {
		try {
			Utils.setSend(false);
			String x0T = this.x0.getValue();
			String y0T = this.y0.getValue();
			String z0T = this.z0.getValue();
			String x1T = this.x1.getValue();
			String y1T = this.y1.getValue();
			String z1T = this.z1.getValue();
			boolean x0rel = false;
			if (x0T.startsWith("~")) {
				x0T = x0T.substring(1);
				x0rel = true;
			}
			boolean y0rel = false;
			if (y0T.startsWith("~")) {
				y0T = y0T.substring(1);
				y0rel = true;
			}
			boolean z0rel = false;
			if (z0T.startsWith("~")) {
				z0T = z0T.substring(1);
				z0rel = true;
			}
			boolean x1rel = false;
			if (x1T.startsWith("~")) {
				x1T = x1T.substring(1);
				x1rel = true;
			}
			boolean y1rel = false;
			if (y1T.startsWith("~")) {
				y1T = y1T.substring(1);
				y1rel = true;
			}
			boolean z1rel = false;
			if (z1T.startsWith("~")) {
				z1T = z1T.substring(1);
				z1rel = true;
			}
			int x0 = !x0T.isEmpty() ? Integer.valueOf(x0T) : 0;
			int y0 = !y0T.isEmpty() ? Integer.valueOf(y0T) : 0;
			int z0 = !z0T.isEmpty() ? Integer.valueOf(z0T) : 0;
			int x1 = !x1T.isEmpty() ? Integer.valueOf(x1T) : 0;
			int y1 = !y1T.isEmpty() ? Integer.valueOf(y1T) : 0;
			int z1 = !z1T.isEmpty() ? Integer.valueOf(z1T) : 0;
			BlockPos playerPos = BlockPos.ZERO;
			if (this.minecraft.player != null)
				playerPos = this.minecraft.player.blockPosition();
			if (x0rel) {
				x0 = x0 + playerPos.getX();
			}
			if (y0rel) {
				y0 = y0 + playerPos.getY();
			}
			if (z0rel) {
				z0 = z0 + playerPos.getZ();
			}
			if (x1rel) {
				x1 = x1 + playerPos.getX();
			}
			if (y1rel) {
				y1 = y1 + playerPos.getY();
			}
			if (z1rel) {
				z1 = z1 + playerPos.getZ();
			}
			boolean flag = this.data.calculateBounds(new BlockPos(x0, y0, z0), new BlockPos(x1, y1, z1),
					this.minecraft.level.dimension());
			Utils.replace(data, false);
			Utils.setSend(true);
			Utils.onChanged(false);
			if (!flag) {
				this.error = true;
				this.message = new TranslationTextComponent("gui.shrines.structures.resources.out_of_range");
			} else {
				this.error = false;
				this.message = DEFAULT_MESSAGE;
			}
			this.saveButton.active = flag;
		} catch (NumberFormatException e) {
			this.saveButton.active = false;
			this.error = true;
			this.message = new TranslationTextComponent("gui.shrines.structures.resources.invalid_char");
		}
		Utils.setSend(true);
	}
}