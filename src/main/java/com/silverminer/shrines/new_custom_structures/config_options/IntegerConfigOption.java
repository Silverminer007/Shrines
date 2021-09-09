package com.silverminer.shrines.new_custom_structures.config_options;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;

public class IntegerConfigOption extends ConfigOption<Integer> {

	public IntegerConfigOption(String option, Integer value, String... comments) {
		super(option, value, comments);
	}

	public IntegerConfigOption(CompoundNBT tag) {
		super(tag.getInt("Value"), tag);
	}

	@Override
	protected INBT writeValue() {
		return IntNBT.valueOf(this.getValue());
	}
}