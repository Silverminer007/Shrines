/*
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
package com.silverminer.shrines.gui.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Silverminer
 *
 */
@OnlyIn(Dist.CLIENT)
public class CoomingSoonScreen extends Screen {
	protected static final Logger LOGGER = LogManager.getLogger(CoomingSoonScreen.class);
	protected static final TranslatableComponent TITLE = new TranslatableComponent(
			"shrines.general.cooming_soon");

	public CoomingSoonScreen() {
		super(TITLE);
	}

	private final ColorLoop loop = new ColorLoop();

	@Override
	@ParametersAreNonnullByDefault
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		if(this.minecraft == null){
			return;
		}
		super.render(ms, mouseX, mouseY, partialTicks);
		this.loop.tick();
		try {
			String text = TITLE.getString();
			int tw = minecraft.font.width(text) / 2;
			int th = minecraft.font.lineHeight / 2;
			int color = this.loop.getRGB();
			int width = this.width / 4;
			int height = this.height / 4;
			for (int x = 1; x <= 3; x++) {
				for (int y = 1; y <= 3; y++) {
					minecraft.font.drawShadow(ms, TITLE, (width) * x - tw, (height) * y - th, color);
				}
			}
		} catch (IllegalArgumentException e) {
			LOGGER.info("RGB {}", this.loop.getRGB());
		}
	}
}