/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.structures.load.options;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

public class StringListConfigOption extends ConfigOption<List<String>> {

	public StringListConfigOption(String option, List<String> value, String... comments) {
		super(option, value, comments);
	}

	public StringListConfigOption(CompoundNBT tag) {
		super(tag.getList("Value", 8).stream().map(inbt -> inbt.getAsString()).collect(Collectors.toList()), tag);
		// Type 8 means String
	}

	@Override
	protected INBT writeValue() {
		ListNBT value = new ListNBT();
		value.addAll(this.getValue().stream().map(s -> StringNBT.valueOf(s)).collect(Collectors.toList()));
		return value;
	}
}