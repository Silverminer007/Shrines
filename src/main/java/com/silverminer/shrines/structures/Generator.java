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
		LOGGER.debug("Generating {} Structures", StructureInit.STRUCTURES_LIST.size());
		for (AbstractStructure<?> structure : StructureInit.STRUCTURES_LIST) {
			try {
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
			} catch (Throwable t) {
				LOGGER.error("Structures of Shrines can't be registered correctly because exception was thrown\n {}", t);
			}
		}
	}

	public static void register(RegistryKey<DimensionSettings> dimension, Structure<?> structure,
			StructureSeparationSettings separationSettings) {
		DimensionSettings DS = WorldGenRegistries.NOISE_SETTINGS.getValueForKey(dimension);
		if (DS == null) {
			LOGGER.error("Something went wrong in structure registerting to dimensions. Please report this issue");
			return;
		}
		DimensionStructuresSettings structuresSettings = DS.getStructures();
		structuresSettings.func_236195_a_().put(structure, separationSettings);
	}
}