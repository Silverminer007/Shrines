package com.silverminer.shrines.new_custom_structures.config_options;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;

public class StringConfigOption extends ConfigOption<String> {

	public StringConfigOption(String option, String value, String... comments) {
		super(option, value, comments);
	}

	public StringConfigOption(CompoundNBT tag) {
		super(tag.getString("Value"), tag);
	}

	@Override
	protected INBT writeValue() {
		return StringNBT.valueOf(this.getValue());
	}
}