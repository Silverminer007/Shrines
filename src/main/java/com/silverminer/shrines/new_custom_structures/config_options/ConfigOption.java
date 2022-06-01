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
package com.silverminer.shrines.new_custom_structures.config_options;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

public abstract class ConfigOption<T> {
	protected static final Logger LOGGER = LogManager.getLogger(ConfigOption.class);
	private final String option;
	private T value;
	private String[] comments;

	public ConfigOption(String option, T value, String... comments) {
		this.option = option;
		this.value = value;
		this.comments = comments;
	}

	public ConfigOption(T value, CompoundNBT tag) {
		this(tag.getString("Option"), value, tag.getList("Comments", 0).stream().map(inbt -> inbt.getAsString()).toArray(String[]::new));
	}

	public CompoundNBT write() {
		CompoundNBT tag = new CompoundNBT();
		tag.putString("Option", option);
		ListNBT comments = new ListNBT();
		comments.addAll(Arrays.asList(this.comments).stream().map(s -> StringNBT.valueOf(s)).collect(Collectors.toList()));
		tag.put("Comments", comments);
		tag.put("Value", this.writeValue());
		return tag;
	}

	protected abstract INBT writeValue();

	public void setValue(T v) {
		this.value = v;
	}

	public T getValue() {
		return value;
	}

	public String getOption() {
		return this.option;
	}

	public String[] getComments() {
		return comments;
	}

	public void setComments(String[] comments) {
		this.comments = comments;
	}
}