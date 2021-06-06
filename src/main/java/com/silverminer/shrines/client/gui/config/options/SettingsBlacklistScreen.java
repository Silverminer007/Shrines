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
package com.silverminer.shrines.client.gui.config.options;

import java.util.List;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Silverminer
 *
 */
public class SettingsBlacklistScreen extends StructureListOptionScreen {

	/**
	 * @param parent
	 * @param title
	 * @param possibleValues
	 * @param activeValues
	 * @param option
	 */
	public SettingsBlacklistScreen(Screen parent, ITextComponent title,
			List<? extends String> activeValues) {
		super(parent, title, CustomStructureData.getPossibleValuesForKey("blacklist"), activeValues, "blacklist");
	}

	@Override
	protected void save() {
		Config.SETTINGS.BLACKLISTED_BIOMES.set(activeValues);
		Config.SETTINGS.BLACKLISTED_BIOMES.save();
	}

}