package com.silverminer.shrines.init;

import com.silverminer.shrines.Shrines;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;

public class ModStructureFeatures {
	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> NETHER_SHRINE = register(
			"nether_shrine", StructureInit.NETHER_SHRINE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> WATER_SHRINE = register(
			"water_shrine", StructureInit.WATER_SHRINE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> BEES = register("bees",
			StructureInit.BEES.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> SMALL_TEMPEL = register(
			"small_tempel", StructureInit.SMALL_TEMPEL.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> HIGH_TEMPEL = register(
			"high_tempel", StructureInit.HIGH_TEMPEL.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> BALLON = register(
			"ballon", StructureInit.BALLON.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> NETHER_PYRAMID = register(
			"nether_pyramid", StructureInit.NETHER_PYRAMID.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, F> register(String name,
			StructureFeature<FC, F> structureFeature) {
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE,
				new ResourceLocation(Shrines.MODID, name), structureFeature);
	}
}