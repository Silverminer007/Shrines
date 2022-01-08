/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.structures.load.options;

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