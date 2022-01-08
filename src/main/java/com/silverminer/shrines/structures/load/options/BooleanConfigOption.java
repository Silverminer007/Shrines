/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.structures.load.options;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class BooleanConfigOption extends ConfigOption<Boolean> {

	public BooleanConfigOption(String option, Boolean value, String... comments) {
		super(option, value, comments);
	}

	public BooleanConfigOption(CompoundNBT tag) {
		super(tag.getBoolean("Value"), tag);
	}

	@Override
	protected INBT writeValue() {
		return ByteNBT.valueOf(this.getValue());
	}
}