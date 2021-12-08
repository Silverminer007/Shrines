package com.silverminer.shrines.structures.load.options;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;

public class IntegerConfigOption extends ConfigOption<Integer> {

	public IntegerConfigOption(String option, Integer value, String... comments) {
		super(option, value, comments);
	}

	public IntegerConfigOption(CompoundTag tag) {
		super(tag.getInt("Value"), tag);
	}

	@Override
	protected Tag writeValue() {
		return IntTag.valueOf(this.getValue());
	}
}