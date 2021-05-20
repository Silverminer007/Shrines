/**
 * Silverminer (and Team)
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * 
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.core.structures;

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
					register(DimensionSettings.END, structure, new StructureSeparationSettings(
							structure.getDistance(), structure.getSeparation(), structure.getSeedModifier()));
				}
				if (structure.isNetherStructure()) {
					register(DimensionSettings.NETHER, structure, new StructureSeparationSettings(
							structure.getDistance(), structure.getSeparation(), structure.getSeedModifier()));
				}
				register(DimensionSettings.OVERWORLD, structure, new StructureSeparationSettings(
						structure.getDistance(), structure.getSeparation(), structure.getSeedModifier()));
			} catch (Throwable t) {
				LOGGER.error("Structures of Shrines can't be registered correctly because exception was thrown\n {}",
						t);
			}
		}
	}

	public static void register(RegistryKey<DimensionSettings> dimension, Structure<?> structure,
			StructureSeparationSettings separationSettings) {
		DimensionSettings DS = WorldGenRegistries.NOISE_GENERATOR_SETTINGS.get(dimension);
		if (DS == null) {
			LOGGER.error("Something went wrong in structure registerting to dimensions. Please report this issue");
			return;
		}
		DimensionStructuresSettings structuresSettings = DS.structureSettings();
		structuresSettings.structureConfig().put(structure, separationSettings);
	}
}