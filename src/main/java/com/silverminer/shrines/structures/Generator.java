package com.silverminer.shrines.structures;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.init.StructureInit;

import net.minecraft.util.RegistryKey;
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
			if (structure.isEndStructure()) {
				register(DimensionSettings.field_242737_f, structure, new StructureSeparationSettings(
						structure.getDistance(), structure.getSeparation(), structure.getSeedModifier()));
			} else if (structure.isNetherStructure()) {
				register(DimensionSettings.field_242736_e, structure, new StructureSeparationSettings(
						structure.getDistance(), structure.getSeparation(), structure.getSeedModifier()));
			} else {
				register(DimensionSettings.field_242734_c, structure, new StructureSeparationSettings(
						structure.getDistance(), structure.getSeparation(), structure.getSeedModifier()));
			}
		});
	}

	public static void register(RegistryKey<DimensionSettings> dimension, Structure<?> structure,
			StructureSeparationSettings separationSettings) {
		WorldGenRegistries.NOISE_SETTINGS.getOptionalValue(dimension).ifPresent(dimensionSettings -> {
			DimensionStructuresSettings structuresSettings = dimensionSettings.getStructures();
			structuresSettings.func_236195_a_().put(structure, separationSettings);
		});
	}
}