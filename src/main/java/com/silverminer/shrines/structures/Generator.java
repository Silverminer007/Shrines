package com.silverminer.shrines.structures;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.silverminer.shrines.init.StructureInit;

import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

public class Generator {

	public static final Logger LOGGER = LogManager.getLogger(Generator.class);

	public static void setupWorldGen() {
		LOGGER.info("Generating Structures");
		StructureInit.STRUCTURES_LIST.forEach(structure -> {
			DimensionStructuresSettings.field_236191_b_ = // Default structures
					ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
							.putAll(DimensionStructuresSettings.field_236191_b_)
							.put(structure, new StructureSeparationSettings(structure.getDistance(),
									structure.getSeparation(), structure.getSeedModifier()))
							.build();

			DimensionSettings.field_242740_q.getStructures().field_236193_d_.put(structure,
					new StructureSeparationSettings(structure.getDistance(), structure.getSeparation(),
							structure.getSeedModifier()));
			if (structure.isEndStructure())
				WorldGenRegistries.NOISE_SETTINGS.forEach(dim -> {
					if (dim.func_242744_a(DimensionSettings.field_242737_f)) {
						dim.getStructures().field_236193_d_.put(structure, new StructureSeparationSettings(
								structure.getDistance(), structure.getSeparation(), structure.getSeedModifier()));
					}
				});
		});
	}
}