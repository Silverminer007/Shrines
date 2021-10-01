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
package com.silverminer.shrines.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.structures.ShrinesStructure;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.StructureLoadUtils;

import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * @author Silverminer
 *
 */
@EventBusSubscriber(modid = ShrinesMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NewStructureInit {
	protected static final Logger LOGGER = LogManager.getLogger(NewStructureInit.class);
	public static final ImmutableList<StructureRegistryHolder> STRUCTURES = ImmutableList
			.<StructureRegistryHolder>builder().addAll(initStructures()).build();

	private static ArrayList<StructureRegistryHolder> initStructures() {
		ArrayList<StructureRegistryHolder> structures = Lists.newArrayList();
		LOGGER.info("Registering shrines structures");
		for (StructuresPacket packet : StructureLoadUtils.STRUCTURE_PACKETS) {
			for (StructureData structure : packet.getStructures()) {
				if (structure.successful) {
					String name = structure.getKey();

					structures.add(new StructureRegistryHolder(name, structure));
					structure.registered = true;
				} else {
					structure.registered = false;
				}
			}
		}
		return structures;
	}

	@SubscribeEvent
	public static void regsiterStructures(RegistryEvent.Register<Structure<?>> event) {
		LOGGER.info("Registering {} structures of shrines Mod", STRUCTURES.size());
		for (StructureRegistryHolder holder : STRUCTURES) {
			Structure.STRUCTURES_REGISTRY.putIfAbsent(holder.getStructure().getConfig().getKey(),
					holder.getStructure());

			if (holder.getStructure().getConfig().getTransformLand()) {
				Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder()
						.addAll(Structure.NOISE_AFFECTING_FEATURES).add(holder.getStructure()).build();
			}

			ShrinesStructure structure = holder.getStructure();

			StructureSeparationSettings structureSeparationSettings = new StructureSeparationSettings(
					structure.getDistance(), structure.getSeparation(), structure.getSeedModifier());

			DimensionStructuresSettings.DEFAULTS = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
					.putAll(DimensionStructuresSettings.DEFAULTS).put(structure, structureSeparationSettings).build();

			WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
				Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings()
						.structureConfig();
				if (structureMap instanceof ImmutableMap) {
					Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
					tempMap.put(structure, structureSeparationSettings);
					settings.getValue().structureSettings().structureConfig = tempMap;
				} else {
					structureMap.put(structure, structureSeparationSettings);
				}
			});

			event.getRegistry().register(structure.getStructure());
		}
	}
}