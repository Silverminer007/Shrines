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
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.structures.DefaultStructureConfig;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.custom_structures.Utils;

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
	public static final ImmutableList<StructureRegistryHolder> STRUCTURES = ImmutableList.<StructureRegistryHolder>builder().addAll(initStructures()).build();

	public static ArrayList<StructureRegistryHolder>  initStructures() {
		ArrayList<StructureRegistryHolder> structures = Lists.newArrayList();
		structures.add(new StructureRegistryHolder("abandoned_witch_house", DefaultStructureConfig.ABANDONEDWITCHHOUSE_CONFIG, true));
		structures.add(new StructureRegistryHolder("balloon", DefaultStructureConfig.BALLON_CONFIG, false));
		structures.add(new StructureRegistryHolder("bees", DefaultStructureConfig.BEES_CONFIG, true));
		structures.add(new StructureRegistryHolder("end_temple", DefaultStructureConfig.ENDTEMPLE_CONFIG, true));
		structures.add(new StructureRegistryHolder("flooded_temple", DefaultStructureConfig.FLOODEDTEMPLE_CONFIG, true));
		structures.add(new StructureRegistryHolder("guardian_meeting", DefaultStructureConfig.GUARDIANMEETING_CONFIG, true));
		structures.add(new StructureRegistryHolder("harbour", DefaultStructureConfig.HARBOUR_CONFIG, false));
		structures.add(new StructureRegistryHolder("high_temple", DefaultStructureConfig.HIGHTEMPLE_CONFIG, true));
		structures.add(new StructureRegistryHolder("infested_prison", DefaultStructureConfig.INFESTEDPRISON_CONFIG, true));
		structures.add(new StructureRegistryHolder("jungle_tower", DefaultStructureConfig.JUNGLETOWER_CONFIG, true));
		structures.add(new StructureRegistryHolder("mineral_temple", DefaultStructureConfig.MINERALTEMPLE_CONFIG, true));
		structures.add(new StructureRegistryHolder("nether_pyramid", DefaultStructureConfig.NETHERPYRAMID_CONFIG, true));
		structures.add(new StructureRegistryHolder("nether_shrine", DefaultStructureConfig.NETHERSHRINE_CONFIG, true));
		structures.add(new StructureRegistryHolder("oriental_sanctuary", DefaultStructureConfig.ORIENTALSANCTUARY_CONFIG, true));
		structures.add(new StructureRegistryHolder("player_house", DefaultStructureConfig.PLAYERHOUSE_CONFIG, true));
		structures.add(new StructureRegistryHolder("small_temple", DefaultStructureConfig.SMALLTEMPLE_CONFIG, true));
		structures.add(new StructureRegistryHolder("trader_house", DefaultStructureConfig.TRADER_HOUSE_CONFIG, true));
		structures.add(new StructureRegistryHolder("watchtower", DefaultStructureConfig.WATCHTOWER_CONFIG, true));
		structures.add(new StructureRegistryHolder("water_shrine", DefaultStructureConfig.WATERSHRINE_CONFIG, true));
		initCustomStructures(structures);
		return structures;
	}

	private static void initCustomStructures(ArrayList<StructureRegistryHolder> structures) {
		LOGGER.info("Registering custom structures");
		for (CustomStructureData csd : Utils.getStructures(true)) {
			String name = csd.getName().toLowerCase(Locale.ROOT);

			structures.add(new StructureRegistryHolder(name, csd, true, true));// TODO Add option to custom structures 'transform surrounding land'
		}
	}

	@SubscribeEvent
	public static void regsiterStructures(RegistryEvent.Register<Structure<?>> event) {
		for (StructureRegistryHolder structure : STRUCTURES) {
			if (!Structure.STRUCTURES_REGISTRY.containsValue(structure.getStructure())) {
				Structure.STRUCTURES_REGISTRY.putIfAbsent(structure.getName(), structure.getStructure());
			}

			if (structure.isTransformSurroundingLand()) {
				Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder()
						.addAll(Structure.NOISE_AFFECTING_FEATURES).add(structure.getStructure()).build();
			}

			event.getRegistry().register(structure.getStructure());
		}
	}
}