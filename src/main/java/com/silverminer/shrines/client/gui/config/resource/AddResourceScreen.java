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

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.shrines.structures.custom.helper.ConfigOption;
import com.silverminer.shrines.structures.custom.helper.PieceData;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author Silverminer
 *
 */
public class AddResourceScreen extends Screen {
	protected static final Logger LOGGER = LogManager.getLogger(AddResourceScreen.class);
	protected static final ITextComponent TITLE = new TranslationTextComponent("gui.shrines.structures.resource.title");
	protected final ConfigOption<List<PieceData>> pieces;

	public AddResourceScreen(ConfigOption<List<PieceData>> pieces) {
		super(TITLE);
		this.pieces = pieces;
	}

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		super.render(ms, mouseX, mouseY, partialTicks);
		String text = "Cooming soon";
		int tw = minecraft.font.width(text);
		int th = minecraft.font.lineHeight;
		minecraft.font.drawShadow(ms, text, (this.width - tw) / 2, (this.height - th) / 2, 0xff0000);
	}
}