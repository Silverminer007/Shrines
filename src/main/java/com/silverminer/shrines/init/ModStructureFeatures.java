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

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> PLAYER_HOUSE = register(
			"player_house", StructureInit.PLAYER_HOUSE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> MINERAL_TEMPLE = register(
			"mineral_temple", StructureInit.MINERAL_TEMPLE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> FLOODED_TEMPLE = register(
			"flooded_temple", StructureInit.FLOODED_TEMPLE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> HARBOUR = register(
			"harbour", StructureInit.HARBOUR.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> INFESTED_PRISON = register(
			"infested_prison", StructureInit.INFESTED_PRISON.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> WITCH_HOUSE = register(
			"witch_house", StructureInit.WITCH_HOUSE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> JUNGLE_TOWER = register(
			"jungle_tower", StructureInit.JUNGLE_TOWER.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> END_TEMPLE = register(
			"end_temple", StructureInit.END_TEMPLE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

	private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, F> register(String name,
			StructureFeature<FC, F> structureFeature) {
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE,
				new ResourceLocation(Shrines.MODID, name), structureFeature);
	}
}