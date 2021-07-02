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
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.abandoned_witch_house.AbandonedWitchHouseStructure;
import com.silverminer.shrines.structures.balloon.BalloonStructure;
import com.silverminer.shrines.structures.bees.BeesStructure;
import com.silverminer.shrines.structures.custom.CustomStructure;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.structures.end_temple.EndTempleStructure;
import com.silverminer.shrines.structures.flooded_temple.FloodedTempleStructure;
import com.silverminer.shrines.structures.guardian_meeting.GuardianMeetingStructure;
import com.silverminer.shrines.structures.harbour.HarbourStructure;
import com.silverminer.shrines.structures.high_temple.HighTempleStructure;
import com.silverminer.shrines.structures.jungle_tower.JungleTowerStructure;
import com.silverminer.shrines.structures.mineral_temple.MineralTempleStructure;
import com.silverminer.shrines.structures.nether_pyramid.NetherPyramidStructure;
import com.silverminer.shrines.structures.nether_shrine.NetherShrineStructure;
import com.silverminer.shrines.structures.oriental_sanctuary.OrientalSanctuaryStructure;
import com.silverminer.shrines.structures.player_house.PlayerhouseStructure;
import com.silverminer.shrines.structures.prison.InfestedPrisonStructure;
import com.silverminer.shrines.structures.small_temple.SmallTempleStructure;
import com.silverminer.shrines.structures.trader_house.TraderHouseStructure;
import com.silverminer.shrines.structures.water_shrine.WaterShrineStructure;
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
		STRUCTURES.put(BalloonStructure.NAME, new BalloonStructure(VillageConfig.CODEC));
		STRUCTURES.put(BeesStructure.NAME, new BeesStructure(VillageConfig.CODEC));
		STRUCTURES.put(EndTempleStructure.NAME, new EndTempleStructure(VillageConfig.CODEC));
		STRUCTURES.put(FloodedTempleStructure.NAME, new FloodedTempleStructure(VillageConfig.CODEC));
		STRUCTURES.put(GuardianMeetingStructure.NAME, new GuardianMeetingStructure(VillageConfig.CODEC));
		STRUCTURES.put(HarbourStructure.NAME, new HarbourStructure(VillageConfig.CODEC));
		STRUCTURES.put(HighTempleStructure.NAME, new HighTempleStructure(VillageConfig.CODEC));
		STRUCTURES.put(JungleTowerStructure.NAME, new JungleTowerStructure(VillageConfig.CODEC));
		STRUCTURES.put(MineralTempleStructure.NAME, new MineralTempleStructure(VillageConfig.CODEC));
		STRUCTURES.put(NetherPyramidStructure.NAME, new NetherPyramidStructure(VillageConfig.CODEC));
		STRUCTURES.put(NetherShrineStructure.NAME, new NetherShrineStructure(VillageConfig.CODEC));
		STRUCTURES.put(OrientalSanctuaryStructure.NAME, new OrientalSanctuaryStructure(VillageConfig.CODEC));
		STRUCTURES.put(PlayerhouseStructure.NAME, new PlayerhouseStructure(VillageConfig.CODEC));
		STRUCTURES.put(InfestedPrisonStructure.NAME, new InfestedPrisonStructure(VillageConfig.CODEC));
		STRUCTURES.put(SmallTempleStructure.NAME, new SmallTempleStructure(VillageConfig.CODEC));
		STRUCTURES.put(WaterShrineStructure.NAME, new WaterShrineStructure(VillageConfig.CODEC));
		STRUCTURES.put(AbandonedWitchHouseStructure.NAME, new AbandonedWitchHouseStructure(VillageConfig.CODEC));
		STRUCTURES.put(TraderHouseStructure.NAME, new TraderHouseStructure(VillageConfig.CODEC));
		initCustomStructures();
	}

	public static void initCustomStructures() {
		if (Config.SETTINGS.ADVANCED_LOGGING.get())
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