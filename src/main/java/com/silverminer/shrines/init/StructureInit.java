package com.silverminer.shrines.init;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.structures.NetherShrineStructure;

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
}