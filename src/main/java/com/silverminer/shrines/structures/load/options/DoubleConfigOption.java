package com.silverminer.shrines.structures.load.options;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;

public class DoubleConfigOption extends ConfigOption<Double> {

	public DoubleConfigOption(String option, Double value, String... comments) {
		super(option, value, comments);
	}

	public DoubleConfigOption(CompoundNBT tag) {
		super(tag.getDouble("Value"), tag);
	}

	@Override
	protected INBT writeValue() {
		return DoubleNBT.valueOf(this.getValue());
	}
}