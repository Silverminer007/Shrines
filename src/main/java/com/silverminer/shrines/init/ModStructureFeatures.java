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

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.structures.custom.CustomStructure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;

public class ModStructureFeatures {
	public static final ArrayList<StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>>> USERS_STRUCTURES = new ArrayList<StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>>>();

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> NETHER_SHRINE = register(
			"nether_shrine", StructureInit.NETHER_SHRINE.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> WATER_SHRINE = register(
			"water_shrine", StructureInit.WATER_SHRINE.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> BEES = register("bees",
			StructureInit.BEES.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> SMALL_TEMPEL = register(
			"small_tempel", StructureInit.SMALL_TEMPEL.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> HIGH_TEMPEL = register(
			"high_tempel", StructureInit.HIGH_TEMPEL.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> BALLON = register(
			"ballon", StructureInit.BALLON.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> NETHER_PYRAMID = register(
			"nether_pyramid", StructureInit.NETHER_PYRAMID.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> PLAYER_HOUSE = register(
			"player_house", StructureInit.PLAYER_HOUSE.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> MINERAL_TEMPLE = register(
			"mineral_temple", StructureInit.MINERAL_TEMPLE.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> FLOODED_TEMPLE = register(
			"flooded_temple", StructureInit.FLOODED_TEMPLE.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> HARBOUR = register(
			"harbour", StructureInit.HARBOUR.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> INFESTED_PRISON = register(
			"infested_prison", StructureInit.INFESTED_PRISON.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> WITCH_HOUSE = register(
			"witch_house", StructureInit.WITCH_HOUSE.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> JUNGLE_TOWER = register(
			"jungle_tower", StructureInit.JUNGLE_TOWER.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> END_TEMPLE = register(
			"end_temple", StructureInit.END_TEMPLE.get().configured(IFeatureConfig.NONE));

	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> GUARDIAN_MEETING = register(
			"guardian_meeting",
			StructureInit.GUARDIAN_MEETING.get().configured(IFeatureConfig.NONE));

	private static boolean hasReadUsersStructures = false;

	private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, F> register(String name,
			StructureFeature<FC, F> structureFeature) {
		if (!hasReadUsersStructures && CustomStructureInit.areStructuresRegistered) {
			hasReadUsersStructures = true;
			loadUsersStructures();
		}
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE,
				new ResourceLocation(Shrines.MODID, name), structureFeature);
	}

	private static void loadUsersStructures() {
		for (RegistryObject<CustomStructure> s : CustomStructureInit.USERS_STRUCTURES) {
			USERS_STRUCTURES.add(register(s.getId().getNamespace(), s.get().configured(IFeatureConfig.NONE)));
		}
	}
}