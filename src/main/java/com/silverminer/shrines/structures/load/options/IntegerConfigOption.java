/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.structures.load.options;

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