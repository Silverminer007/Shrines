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

import java.awt.Color;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author Silverminer
 *
 */
public class CoomingSoonScreen extends Screen {
	protected static final Logger LOGGER = LogManager.getLogger(CoomingSoonScreen.class);
	protected static final TranslationTextComponent TITLE = new TranslationTextComponent(
			"shrines.general.cooming_soon");

	public CoomingSoonScreen() {
		super(TITLE);
	}

	private int red = 0;
	private int green = 0;
	private int blue = 0;

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		super.render(ms, mouseX, mouseY, partialTicks);
		this.runColorLoop();
		try {
			String text = TITLE.getString();
			int tw = minecraft.font.width(text) / 2;
			int th = minecraft.font.lineHeight / 2;
			int color = new Color(red % 256, green % 256, blue % 256).getRGB();
			int width = this.width / 4;
			int height = this.height / 4;
			for (int x = 1; x <= 3; x++) {
				for (int y = 1; y <= 3; y++) {
					minecraft.font.drawShadow(ms, TITLE, (width) * x - tw, (height) * y - th, color);
				}
			}
		} catch (IllegalArgumentException e) {
			LOGGER.info("RGB {}, {}, {}", red, green, blue);
		}
	}

	private void runColorLoop() {
		int speed = 3;
		if (blue < 255 && red <= 0 && green <= 0) {
			blue += speed;
			return;
		}
		if (red < 255 && blue >= 255 && green <= 0) {
			red += speed;
			return;
		}
		if (blue > 0 && red >= 255 && green <= 0) {
			blue -= speed;
			return;
		}
		if (green < 255 && blue <= 0 && red >= 255) {
			green += speed;
			return;
		}
		if (red > 0 && blue <= 0 && green >= 255) {
			red -= speed;
			return;
		}
		if (blue < 255 && red <= 0 && green >= 255) {
			blue += speed;
			return;
		}
		if (green > 0 && red <= 0 && blue >= 255) {
			green -= speed;
			return;
		}
	}
}