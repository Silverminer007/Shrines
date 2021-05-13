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
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.ballon.BallonStructure;
import com.silverminer.shrines.structures.bees.BeesStructure;
import com.silverminer.shrines.structures.end_temple.EndTempleStructure;
import com.silverminer.shrines.structures.flooded_temple.FloodedTempleStructure;
import com.silverminer.shrines.structures.guardian_meeting.GuardianMeetingStructure;
import com.silverminer.shrines.structures.harbour.HarbourStructure;
import com.silverminer.shrines.structures.high_tempel.HighTempelStructure;
import com.silverminer.shrines.structures.jungle_tower.JungleTowerStructure;
import com.silverminer.shrines.structures.mineral_temple.MineralTempleStructure;
import com.silverminer.shrines.structures.nether_pyramid.NetherPyramidStructure;
import com.silverminer.shrines.structures.nether_shrine.NetherShrineStructure;
import com.silverminer.shrines.structures.player_house.PlayerhouseStructure;
import com.silverminer.shrines.structures.prison.InfestedPrisonStructure;
import com.silverminer.shrines.structures.small_tempel.SmallTempelStructure;
import com.silverminer.shrines.structures.water_shrine.WaterShrineStructure;
import com.silverminer.shrines.structures.witch_house.AbandonedWitchHouseStructure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StructureInit {
	protected static final Logger LOGGER = LogManager.getLogger(StructureInit.class);
	public static final ArrayList<AbstractStructure<NoFeatureConfig>> STRUCTURES_LIST = new ArrayList<AbstractStructure<NoFeatureConfig>>();
	public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister
			.create(ForgeRegistries.STRUCTURE_FEATURES, Shrines.MODID);

	public static final RegistryObject<NetherShrineStructure> NETHER_SHRINE = register("nether_shrine",
			new NetherShrineStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<WaterShrineStructure> WATER_SHRINE = register("water_shrine",
			new WaterShrineStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<BeesStructure> BEES = register("bees",
			new BeesStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<SmallTempelStructure> SMALL_TEMPEL = register("small_tempel",
			new SmallTempelStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<HighTempelStructure> HIGH_TEMPEL = register("high_tempel",
			new HighTempelStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<BallonStructure> BALLON = register("ballon",
			new BallonStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<NetherPyramidStructure> NETHER_PYRAMID = register("nether_pyramid",
			new NetherPyramidStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<PlayerhouseStructure> PLAYER_HOUSE = register("player_house",
			new PlayerhouseStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<MineralTempleStructure> MINERAL_TEMPLE = register("mineral_temple",
			new MineralTempleStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<FloodedTempleStructure> FLOODED_TEMPLE = register("flooded_temple",
			new FloodedTempleStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<HarbourStructure> HARBOUR = register("harbour",
			new HarbourStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<InfestedPrisonStructure> INFESTED_PRISON = register("infested_prison",
			new InfestedPrisonStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<AbandonedWitchHouseStructure> WITCH_HOUSE = register("witch_house",
			new AbandonedWitchHouseStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<JungleTowerStructure> JUNGLE_TOWER = register("jungle_tower",
			new JungleTowerStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<EndTempleStructure> END_TEMPLE = register("end_temple",
			new EndTempleStructure(NoFeatureConfig.CODEC));

	public static final RegistryObject<GuardianMeetingStructure> GUARDIAN_MEETING = register("guardian_meeting",
			new GuardianMeetingStructure(NoFeatureConfig.CODEC));

	private static <T extends AbstractStructure<NoFeatureConfig>> RegistryObject<T> register(String name, T structure) {
		if (!Structure.STRUCTURES_REGISTRY.containsValue(structure)) {
			Structure.STRUCTURES_REGISTRY.putIfAbsent(new ResourceLocation(Shrines.MODID, name).toString(), structure);
		}
		if (!Structure.STEP.containsValue(structure.step())) {
			Structure.STEP.putIfAbsent(structure, structure.step());
		}

		Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder().addAll(Structure.NOISE_AFFECTING_FEATURES)
				.add(structure).build();

		STRUCTURES_LIST.add(structure);

		return STRUCTURES.register(name, () -> structure);
	}
}