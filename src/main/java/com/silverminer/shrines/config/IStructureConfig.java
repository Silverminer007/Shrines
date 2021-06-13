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
package com.silverminer.shrines.config;

import java.util.List;
import java.util.Locale;

import com.silverminer.shrines.utils.custom_structures.OptionParsingResult;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.biome.Biome.Category;

/**
 * @author Silverminer
 *
 */
public interface IStructureConfig extends Comparable<IStructureConfig> {
	public String getName();

	public boolean getGenerate();

	public double getSpawnChance();

	public boolean getNeedsGround();

	public int getDistance();

	public int getSeparation();

	public int getSeed();

	public List<? extends Category> getWhitelist();

	public List<? extends String> getBlacklist();

	public List<? extends String> getDimensions();

	public boolean getUseRandomVarianting();

	public double getLootChance();

	public boolean getSpawnVillagers();

	public boolean isBuiltIn();

	default String getDataName() {
		return this.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_");
	}

	public boolean getActive();

	public void setActive(boolean value);

	public List<? extends IConfigOption<?>> getAllOptions();

	/**
	 * @param option
	 * @param string
	 */
	default OptionParsingResult fromString(String option, String value) {
		for (IConfigOption<?> co : this.getAllOptions()) {
			if (co.getName().equals(option)) {
				OptionParsingResult res = co.fromString(value, this);
				return res;
			}
		}
		return new OptionParsingResult(false, new StringTextComponent("There is no such option as provided"));
	}
}