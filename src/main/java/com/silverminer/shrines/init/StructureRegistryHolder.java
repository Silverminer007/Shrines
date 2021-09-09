package com.silverminer.shrines.init;

import com.silverminer.shrines.new_custom_structures.StructureData;
import com.silverminer.shrines.structures.ShrinesStructure;

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