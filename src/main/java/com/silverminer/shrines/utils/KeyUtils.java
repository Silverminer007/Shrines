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
package com.silverminer.shrines.utils;

import net.minecraft.client.settings.KeyBinding;

/**
 * @author Silverminer
 *
 */
public class KeyUtils {
	/**
	 * This holds screen open keybind. It's initialised in ClientEvents (FMLClientSetupEvent) and used in Key Pressed Event. Allows key rebinds
	 */
	public static KeyBinding structuresScreen;
}