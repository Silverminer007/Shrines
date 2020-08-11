package com.silverminer.shrines.structures;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.silverminer.shrines.init.StructureInit;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.registries.ForgeRegistries;

public class Generator {
	public static final ArrayList<?> BLACKLISTED_BIOMES = Lists.newArrayList(Biomes.RIVER);
	public static void setupWorldGen() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			if (BLACKLISTED_BIOMES.contains(biome)) {
				continue;
			}
			if (biome.getCategory() != Category.NETHER && biome.getCategory() != Category.THEEND
					&& biome.getCategory() != Category.OCEAN) {
				addStructure(biome, StructureInit.NETHER_SHRINE.get());
				addStructure(biome, StructureInit.WATER_SHRINE.get());
				addStructure(biome, StructureInit.BEES.get());
				addStructure(biome, StructureInit.SMALL_TEMPEL.get());
				addStructure(biome, StructureInit.HIGH_TEMPEL.get());
				addStructure(biome, StructureInit.BALLON.get());
				addStructure(biome, StructureInit.NETHER_PYRAMID.get());
			}
		}
	}

	private static void addStructure(Biome biome, Structure<NoFeatureConfig> structure) {
		biome.func_235063_a_(structure.func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));
	}
}