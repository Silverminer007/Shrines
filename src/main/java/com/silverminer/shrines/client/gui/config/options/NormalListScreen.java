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

import com.silverminer.shrines.config.IStructureConfig;

import net.minecraft.util.text.ITextComponent;

/**
 * @author Silverminer
 *
 */
public class NormalListScreen extends StructureListOptionScreen {
	protected ConfigStructureScreen screen;
	/**
	 * @param parent
	 * @param title
	 * @param possibleValues
	 * @param activeValues
	 * @param option
	 */
	public NormalListScreen(ConfigStructureScreen parent, ITextComponent title, List<String> possibleValues,
			List<? extends String> activeValues, String option) {
		super(parent, title, possibleValues, activeValues, option);
		this.screen = parent;
	}

	@Override
	protected void save() {
		IStructureConfig csd = this.screen.getConfig();
		csd.fromString(this.option, this.activeValues.toString());
		this.screen.setConfig(csd);
		this.parent = screen;
	}

}