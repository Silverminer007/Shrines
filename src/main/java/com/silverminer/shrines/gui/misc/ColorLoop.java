/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

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
package com.silverminer.shrines.gui.misc;

import java.awt.Color;

/**
 * @author Silverminer
 *
 */
public class ColorLoop {
	private int red = 0;
	private int green = 0;
	private int blue = 0;

	public ColorLoop() {
	}

	public int getRed() {
		return this.red;
	}

	public int getGreen() {
		return this.green;
	}

	public int getBlue() {
		return this.blue;
	}

	public Color getColor() {
		return new Color(red % 256, green % 256, blue % 256);
	}

	public int getRGB() {
		return this.getColor().getRGB();
	}

	public void tick() {
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