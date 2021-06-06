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
package com.silverminer.shrines.client.gui.config;

import java.util.Random;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.client.gui.config.options.ConfigStructureScreen;
import com.silverminer.shrines.client.gui.config.settings.GeneralSettingsScreen;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author Silverminer
 *
 */
@OnlyIn(Dist.CLIENT)
public class ShrinesStructuresScreen extends Screen {
	protected final Screen lastScreen;
	private Button disableButton;
	private Button configureButton;
	private Button removeButton;
	protected TextFieldWidget searchBox;
	private StructuresList list;
	private static final TranslationTextComponent TITLE = new TranslationTextComponent("gui.shrines.structures.title");
	private static final TranslationTextComponent DISABLE = new TranslationTextComponent(
			"gui.shrines.structures.disable");
	private static final TranslationTextComponent ADD = new TranslationTextComponent("gui.shrines.structures.add");
	private static final TranslationTextComponent CONFIG = new TranslationTextComponent(
			"gui.shrines.structures.config");
	private static final TranslationTextComponent REMOVE = new TranslationTextComponent(
			"gui.shrines.structures.remove");
	private static final TranslationTextComponent SETTINGS = new TranslationTextComponent(
			"gui.shrines.structures.settings");

	public ShrinesStructuresScreen(Screen lastScreen) {
		super(TITLE);
		this.lastScreen = lastScreen;
	}

	public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double p_231043_5_) {
		return super.mouseScrolled(p_231043_1_, p_231043_3_, p_231043_5_);
	}

	public void tick() {
		this.searchBox.tick();
	}

	protected void init() {
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		this.searchBox = new TextFieldWidget(this.font, this.width / 2 - 100, 22, 200, 20, this.searchBox,
				new TranslationTextComponent("selectWorld.search"));
		this.searchBox.setResponder((search) -> {
			this.list.refreshList(() -> {
				return search;
			});
		});
		this.list = new StructuresList(this, this.minecraft, this.width, this.height, 48, this.height - 64, 36, () -> {
			return this.searchBox.getValue();
		}, this.list);
		this.children.add(this.searchBox);
		this.children.add(this.list);
		this.disableButton = this
				.addButton(new Button(this.width / 2 - 154, this.height - 52, 150, 20, DISABLE, (button) -> {
					this.list.getSelectedOpt().ifPresent(StructuresList.Entry::disable);
					this.list.refreshList(() -> this.searchBox.getValue());
					this.updateButtonStatus(false, false);
				}));
		this.addButton(new Button(this.width / 2 + 4, this.height - 52, 150, 20, ADD, (button) -> {
			this.minecraft.setScreen(new ConfigStructureScreen(this, new CustomStructureData("", new Random()), true));
		}));
		this.configureButton = this
				.addButton(new Button(this.width / 2 - 154, this.height - 28, 72, 20, CONFIG, (button) -> {
					this.list.getSelectedOpt().ifPresent(StructuresList.Entry::configure);
				}));
		// Cancel Button
		this.addButton(new Button(this.width / 2 - 76, this.height - 28, 152, 20, DialogTexts.GUI_CANCEL, (button) -> {
			Utils.saveStructures();
			this.minecraft.setScreen(this.lastScreen);
		}));
		this.removeButton = this
				.addButton(new Button(this.width / 2 + 82, this.height - 28, 72, 20, REMOVE, (remove) -> {
					this.list.getSelectedOpt().ifPresent(StructuresList.Entry::remove);
				}));
		// Settings Button
		this.addButton(new Button(this.width - 80, 8, 72, 20, SETTINGS, (button) -> {
			this.minecraft.setScreen(new GeneralSettingsScreen(this, Lists.newArrayList(Config.SERVER_SETTINGS_CONFIG)));
		}));
		this.updateButtonStatus(false, false);
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

	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.list.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		this.searchBox.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 8, 16777215);
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
	}

	public void updateButtonStatus(boolean flag, boolean removeable) {
		this.disableButton.active = flag;
		this.configureButton.active = flag;
		if (removeable) {
			this.removeButton.active = true;
		} else {
			this.removeButton.active = false;
		}
	}

	public void removed() {
		if (this.list != null) {
			this.list.children().forEach(StructuresList.Entry::close);
		}

	}
}