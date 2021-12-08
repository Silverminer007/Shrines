package com.silverminer.shrines.structures.load.options;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class BooleanConfigOption extends ConfigOption<Boolean> {

	public BooleanConfigOption(String option, Boolean value, String... comments) {
		super(option, value, comments);
	}

	public BooleanConfigOption(CompoundTag tag) {
		super(tag.getBoolean("Value"), tag);
	}

	@Override
	protected Tag writeValue() {
		return ByteTag.valueOf(this.getValue());
	}
}