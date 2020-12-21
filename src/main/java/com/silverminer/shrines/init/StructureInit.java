package com.silverminer.shrines.init;

import java.util.ArrayList;

import com.google.common.collect.ImmutableList;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.ballon.BallonStructure;
import com.silverminer.shrines.structures.bees.BeesStructure;
import com.silverminer.shrines.structures.high_tempel.HighTempelStructure;
import com.silverminer.shrines.structures.nether_pyramid.NetherPyramidStructure;
import com.silverminer.shrines.structures.nether_shrine.NetherShrineStructure;
import com.silverminer.shrines.structures.player_house.PlayerhouseStructure;
import com.silverminer.shrines.structures.small_tempel.SmallTempelStructure;
import com.silverminer.shrines.structures.water_shrine.WaterShrineStructure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StructureInit {
	public static final ArrayList<AbstractStructure<NoFeatureConfig>> STRUCTURES_LIST = new ArrayList<AbstractStructure<NoFeatureConfig>>();
	public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister
			.create(ForgeRegistries.STRUCTURE_FEATURES, Shrines.MODID);

	public static final RegistryObject<NetherShrineStructure> NETHER_SHRINE = register("nether_shrine",
			new NetherShrineStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<WaterShrineStructure> WATER_SHRINE = register("water_shrine",
			new WaterShrineStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<BeesStructure> BEES = register("bees",
			new BeesStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<SmallTempelStructure> SMALL_TEMPEL = register("small_tempel",
			new SmallTempelStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<HighTempelStructure> HIGH_TEMPEL = register("high_tempel",
			new HighTempelStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<BallonStructure> BALLON = register("ballon",
			new BallonStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<NetherPyramidStructure> NETHER_PYRAMID = register("nether_pyramid",
			new NetherPyramidStructure(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<PlayerhouseStructure> PLAYER_HOUSE = register("player_house",
			new PlayerhouseStructure(NoFeatureConfig.field_236558_a_));

	private static <T extends AbstractStructure<NoFeatureConfig>> RegistryObject<T> register(String name, T structure) {
		if (!Structure.NAME_STRUCTURE_BIMAP.containsValue(structure)) {
			Structure.NAME_STRUCTURE_BIMAP.putIfAbsent(new ResourceLocation(Shrines.MODID, name).toString(), structure);
		}
		if (!Structure.STRUCTURE_DECORATION_STAGE_MAP.containsValue(structure.getDecorationStage())) {
			Structure.STRUCTURE_DECORATION_STAGE_MAP.putIfAbsent(structure, structure.getDecorationStage());
		}

		Structure.field_236384_t_ = ImmutableList.<Structure<?>>builder().addAll(Structure.field_236384_t_)
				.add(structure).build();

		STRUCTURES_LIST.add(structure);

		return STRUCTURES.register(name, () -> structure);
	}
}