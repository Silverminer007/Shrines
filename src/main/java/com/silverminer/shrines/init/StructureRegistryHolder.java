/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.init;

import com.silverminer.shrines.structures.ShrinesStructure;
import com.silverminer.shrines.structures.load.StructureData;

import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class StructureRegistryHolder {
	private final ShrinesStructure structure;
	private StructureFeature<?, ?> configuredStructure;

	public StructureRegistryHolder(String name, StructureData config) {
		this.structure = new ShrinesStructure(name, config);
	}

	public ShrinesStructure getStructure() {
		return structure;
	}

	public StructureFeature<?, ?> getConfiguredStructure() {
		return configuredStructure;
	}

	public void configure() {
		this.configuredStructure = structure.configured(IFeatureConfig.NONE);
	}
}