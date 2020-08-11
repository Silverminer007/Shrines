package com.silverminer.shrines.init;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.structures.ballon.BallonStructure;
import com.silverminer.shrines.structures.bees.BeesStructure;
import com.silverminer.shrines.structures.high_tempel.HighTempelStructure;
import com.silverminer.shrines.structures.nether_pyramid.NetherPyramidStructure;
import com.silverminer.shrines.structures.nether_shrine.NetherShrineStructure;
import com.silverminer.shrines.structures.small_tempel.SmallTempelStructure;
import com.silverminer.shrines.structures.water_shrine.WaterShrineStructure;

import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StructureInit {
	public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES,
			Shrines.MODID);

	public static final RegistryObject<Structure<NoFeatureConfig>> NETHER_SHRINE = STRUCTURES.register("nether_shrine",
			() -> new NetherShrineStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<Structure<NoFeatureConfig>> WATER_SHRINE = STRUCTURES.register("water_shrine",
			() -> new WaterShrineStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<Structure<NoFeatureConfig>> BEES = STRUCTURES.register("bees",
			() -> new BeesStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<Structure<NoFeatureConfig>> SMALL_TEMPEL = STRUCTURES.register("small_tempel",
			() -> new SmallTempelStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<Structure<NoFeatureConfig>> HIGH_TEMPEL = STRUCTURES.register("high_tempel",
			() -> new HighTempelStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<Structure<NoFeatureConfig>> BALLON = STRUCTURES.register("ballon",
			() -> new BallonStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<Structure<NoFeatureConfig>> NETHER_PYRAMID = STRUCTURES.register("nether_pyramid",
			() -> new NetherPyramidStructure(NoFeatureConfig.field_236558_a_));
}