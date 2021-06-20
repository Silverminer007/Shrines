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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.ballon.BallonStructure;
import com.silverminer.shrines.structures.bees.BeesStructure;
import com.silverminer.shrines.structures.custom.CustomStructure;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.structures.end_temple.EndTempleStructure;
import com.silverminer.shrines.structures.flooded_temple.FloodedTempleStructure;
import com.silverminer.shrines.structures.guardian_meeting.GuardianMeetingStructure;
import com.silverminer.shrines.structures.harbour.HarbourStructure;
import com.silverminer.shrines.structures.high_tempel.HighTempelStructure;
import com.silverminer.shrines.structures.jungle_tower.JungleTowerStructure;
import com.silverminer.shrines.structures.mineral_temple.MineralTempleStructure;
import com.silverminer.shrines.structures.nether_pyramid.NetherPyramidStructure;
import com.silverminer.shrines.structures.nether_shrine.NetherShrineStructure;
import com.silverminer.shrines.structures.oriental_sanctuary.OrientalSanctuaryStructure;
import com.silverminer.shrines.structures.player_house.PlayerhouseStructure;
import com.silverminer.shrines.structures.prison.InfestedPrisonStructure;
import com.silverminer.shrines.structures.small_tempel.SmallTempelStructure;
import com.silverminer.shrines.structures.water_shrine.WaterShrineStructure;
import com.silverminer.shrines.structures.witch_house.AbandonedWitchHouseStructure;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
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
	public static final HashMap<String, AbstractStructure> STRUCTURES = Maps.newHashMap();

	public static void initStructures() {
		STRUCTURES.put("ballon", new BallonStructure(VillageConfig.CODEC));
		STRUCTURES.put("bees", new BeesStructure(VillageConfig.CODEC));
		STRUCTURES.put("end_temple", new EndTempleStructure(VillageConfig.CODEC));
		STRUCTURES.put("flooded_temple", new FloodedTempleStructure(VillageConfig.CODEC));
		STRUCTURES.put("guardian_meeting", new GuardianMeetingStructure(VillageConfig.CODEC));
		STRUCTURES.put("harbour", new HarbourStructure(VillageConfig.CODEC));
		STRUCTURES.put("high_tempel", new HighTempelStructure(VillageConfig.CODEC));
		STRUCTURES.put("jungle_tower", new JungleTowerStructure(VillageConfig.CODEC));
		STRUCTURES.put("mineral_temple", new MineralTempleStructure(VillageConfig.CODEC));
		STRUCTURES.put("nether_pyramid", new NetherPyramidStructure(VillageConfig.CODEC));
		STRUCTURES.put("nether_shrine", new NetherShrineStructure(VillageConfig.CODEC));
		STRUCTURES.put("oriental_sanctuary", new OrientalSanctuaryStructure(VillageConfig.CODEC));
		STRUCTURES.put("player_house", new PlayerhouseStructure(VillageConfig.CODEC));
		STRUCTURES.put("infested_prison", new InfestedPrisonStructure(VillageConfig.CODEC));
		STRUCTURES.put("small_tempel", new SmallTempelStructure(VillageConfig.CODEC));
		STRUCTURES.put("water_shrine", new WaterShrineStructure(VillageConfig.CODEC));
		STRUCTURES.put("witch_house", new AbandonedWitchHouseStructure(VillageConfig.CODEC));
		initCustomStructures();
	}

	public static void initCustomStructures() {
		LOGGER.debug("Registering custom structures");
		for (CustomStructureData csd : Utils.getStructures(true)) {
			String name = csd.getName().toLowerCase(Locale.ROOT);
			CustomStructure cS = new CustomStructure(VillageConfig.CODEC, name, csd);
			Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder()
					.addAll(Structure.NOISE_AFFECTING_FEATURES).add(cS).build();

			STRUCTURES.put(name, cS);
		}
	}

	@SubscribeEvent
	public static void regsiterStructures(RegistryEvent.Register<Structure<?>> event) {
		if (STRUCTURES.isEmpty()) {
			initStructures();
		}
		for (Entry<String, AbstractStructure> structure : STRUCTURES.entrySet()) {
			if (!Structure.STRUCTURES_REGISTRY.containsValue(structure.getValue())) {
				Structure.STRUCTURES_REGISTRY.putIfAbsent(
						new ResourceLocation(ShrinesMod.MODID, structure.getKey()).toString(), structure.getValue());
			}
			if (!Structure.STEP.containsValue(structure.getValue().step())) {
				Structure.STEP.putIfAbsent(structure.getValue(), structure.getValue().step());
			}

			Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder()
					.addAll(Structure.NOISE_AFFECTING_FEATURES).add(structure.getValue()).build();
			event.getRegistry().register(structure.getValue());
		}
	}
}