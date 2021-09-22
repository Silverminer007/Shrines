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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.Utils;

import net.minecraft.world.gen.feature.structure.Structure;
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

	public static void load() {
	};

	private static ArrayList<StructureRegistryHolder> initStructures() {
		ArrayList<StructureRegistryHolder> structures = Lists.newArrayList();
		LOGGER.info("Registering custom structures");
		for (StructuresPacket packet : Utils.STRUCTURE_PACKETS) {
			for (StructureData structure : packet.getStructures()) {
				if (structure.successful) {
					String name = structure.getKey();

					structures.add(new StructureRegistryHolder(name, structure));
				}
			}
		}
		return structures;
	}

	@SubscribeEvent
	public static void regsiterStructures(RegistryEvent.Register<Structure<?>> event) {
		LOGGER.info("Registering {} structures of shrines Mod", STRUCTURES.size());
		for (StructureRegistryHolder structure : STRUCTURES) {
			Structure.STRUCTURES_REGISTRY.putIfAbsent(structure.getStructure().getConfig().getKey(),
					structure.getStructure());

			if (structure.getStructure().getConfig().getTransformLand()) {
				Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder()
						.addAll(Structure.NOISE_AFFECTING_FEATURES).add(structure.getStructure()).build();
			}

			event.getRegistry().register(structure.getStructure());
		}
	}
}