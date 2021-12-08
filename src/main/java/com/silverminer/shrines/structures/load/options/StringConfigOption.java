package com.silverminer.shrines.structures.load.options;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class StringConfigOption extends ConfigOption<String> {

	public StringConfigOption(String option, String value, String... comments) {
		super(option, value, comments);
	}

	public StringConfigOption(CompoundTag tag) {
		super(tag.getString("Value"), tag);
	}

	@Override
	protected Tag writeValue() {
		return StringTag.valueOf(this.getValue());
	}
}