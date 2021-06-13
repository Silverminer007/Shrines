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

import java.util.Random;
import java.util.function.Function;

import com.silverminer.shrines.utils.custom_structures.OptionParsingResult;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author Silverminer
 *
 */
public interface IConfigOption<T> {

	/**
	 * @return
	 */
	public T getValue();

	/**
	 * @return
	 */
	public String getName();

	/**
	 * @return
	 */
	public T getDefaultValue();

	default OptionParsingResult fromString(String s, IStructureConfig data, boolean set) {
		if(data == null) {
			return new OptionParsingResult(false, new StringTextComponent("Failed to read structure from string, because config instance was null"));
		}
		if(s == null || s.replaceAll(" ", "").isEmpty()) {
			return new OptionParsingResult(false, null);
		}
		T v;
		if (s.startsWith(getName() + ":")) {
			v = this.getFromString(this.getName()).apply(s.replace(this.getName() + ":", ""));
		} else {
			v = this.getFromString(this.getName()).apply(s);
		}
		if (v == null) {
			return new OptionParsingResult(false,
					new TranslationTextComponent("commands.shrines.configure.failed.wrong_value", s, this.getName()));
		} else if (this.getName().equals("distance")) {
			if (((Integer) v) <= data.getSeparation()) {
				return new OptionParsingResult(false, new TranslationTextComponent(
						"commands.shrines.configure.failed.dist_larger_sep", v, data.getSeparation()));
			}
		} else if (this.getName().equals("seperation")) {
			if (((Integer) v) >= data.getDistance()) {
				return new OptionParsingResult(false, new TranslationTextComponent(
						"commands.shrines.configure.failed.sep_smaller_dist", v, data.getDistance()));
			}
		} else if (this.getName().equals("spawn_chance")) {
			Double d = (Double) v;
			if (d < 0.0 || d > 1.0) {
				return new OptionParsingResult(false,
						new TranslationTextComponent("commands.shrines.configure.failed.chance_out_of_range", v));
			}
		} else if (this.getName().equals("seed")) {
			Integer i = (Integer) v;
			if (i < 0) {
				return this.fromString(String.valueOf(i * -1), data, set).setMessage(
						new TranslationTextComponent("commands.shrines.configure.failed.seed_set_positive"));
			} else if (i == 0) {
				return this.fromString(String.valueOf(new Random().nextInt(Integer.MAX_VALUE)), data, set)
						.setMessage(new TranslationTextComponent("commands.shrines.configure.failed.seed_set_random"));
			}
		}
		if (set) {
			this.setValue(v, data.getName());
		}
		return new OptionParsingResult(true, null);
	}

	/**
	 * @return
	 */
	public Function<String, T> getFromString(String option);

	public void setValue(T v, String structure);

	/**
	 * @param text
	 * @param configSpecs
	 */
	default OptionParsingResult fromString(String text, IStructureConfig configSpecs) {
		return this.fromString(text, configSpecs, true);
	}
}