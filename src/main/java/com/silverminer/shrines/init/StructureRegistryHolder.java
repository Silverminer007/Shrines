package com.silverminer.shrines.init;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.config.IStructureConfig;
import com.silverminer.shrines.structures.ShrinesStructure;
import com.silverminer.shrines.structures.custom.CustomStructure;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.common.ForgeConfigSpec;

public class StructureRegistryHolder {
	private final ShrinesStructure structure;
	private StructureFeature<?, ?> configuredStructure;
	private final boolean transformSurroundingLand;
	private final boolean isCustom;
	private final String name;

	public StructureRegistryHolder(String name, IStructureConfig config, boolean transformSurroundingLand) {
		this(name, config, transformSurroundingLand, false);
	}

	public StructureRegistryHolder(String name, IStructureConfig config, boolean transformSurroundingLand,
			boolean isCustom) {
		this.isCustom = isCustom;
		if (this.isCustom) {
			this.structure = new CustomStructure(name, (CustomStructureData) config);
		} else {
			this.structure = new ShrinesStructure(name, config);
		}
		this.name = new ResourceLocation(ShrinesMod.MODID, name).toString();
		this.transformSurroundingLand = transformSurroundingLand;
		ShrinesMod.LOGGER.debug("Registering structures for shrines mod. Current structure: {}", this.name);
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

	public boolean isTransformSurroundingLand() {
		return transformSurroundingLand;
	}

	public String getName() {
		return name;
	}

	public boolean isCustom() {
		return isCustom;
	}

	public void buildConfig(final ForgeConfigSpec.Builder BUILDER) {
		this.structure.buildConfig(BUILDER);
	}

}