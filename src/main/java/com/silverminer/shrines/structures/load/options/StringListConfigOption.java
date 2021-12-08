package com.silverminer.shrines.structures.load.options;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class StringListConfigOption extends ConfigOption<List<String>> {

	public StringListConfigOption(String option, List<String> value, String... comments) {
		super(option, value, comments);
	}

	public StringListConfigOption(CompoundTag tag) {
		super(tag.getList("Value", 8).stream().map(inbt -> inbt.getAsString()).collect(Collectors.toList()), tag);
		// Type 8 means String
	}

	@Override
	protected Tag writeValue() {
		ListTag value = new ListTag();
		value.addAll(this.getValue().stream().map(s -> StringTag.valueOf(s)).collect(Collectors.toList()));
		return value;
	}
}