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
package com.silverminer.shrines.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.silverminer.shrines.ShrinesMod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.template.ProcessorLists;

/**
 * @author Silverminer
 *
 */
public class StructurePools {
	public static final JigsawPattern BALLOON = JigsawPatternRegistry.register(new JigsawPattern(
			new ResourceLocation(ShrinesMod.MODID, "balloon"), new ResourceLocation("empty"),
			ImmutableList.of(
					Pair.of(JigsawPiece.legacy(new ResourceLocation(ShrinesMod.MODID, "balloon/balloon_1").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.legacy(new ResourceLocation(ShrinesMod.MODID, "balloon/balloon_2").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.legacy(new ResourceLocation(ShrinesMod.MODID, "balloon/balloon_3").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.legacy(new ResourceLocation(ShrinesMod.MODID, "balloon/balloon_4").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.legacy(new ResourceLocation(ShrinesMod.MODID, "balloon/balloon_5").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.legacy(new ResourceLocation(ShrinesMod.MODID, "balloon/balloon_6").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.legacy(new ResourceLocation(ShrinesMod.MODID, "balloon/balloon_7").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.legacy(new ResourceLocation(ShrinesMod.MODID, "balloon/balloon2_1").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.legacy(new ResourceLocation(ShrinesMod.MODID, "balloon/balloon2_2").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.legacy(new ResourceLocation(ShrinesMod.MODID, "balloon/balloon2_3").toString(),
							ProcessorLists.EMPTY), 1),
					Pair.of(JigsawPiece.legacy(new ResourceLocation(ShrinesMod.MODID, "balloon/balloon2_4").toString(),
							ProcessorLists.EMPTY), 1)),
			JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern BALLOON_RAINBOW = JigsawPatternRegistry.register(
			new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "balloon"), new ResourceLocation("empty"),
					ImmutableList.of(Pair.of(JigsawPiece.legacy(
							new ResourceLocation(ShrinesMod.MODID, "balloon/balloon_rainbow").toString(),
							ProcessorLists.EMPTY), 1)),
					JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern BEES = JigsawPatternRegistry
			.register(new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "bees"), new ResourceLocation("empty"),
					ImmutableList.of(
							Pair.of(JigsawPiece.legacy(new ResourceLocation(ShrinesMod.MODID, "bees/bees").toString(),
									ProcessorLists.EMPTY), 1)),
					JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern END_TEMPLE = JigsawPatternRegistry.register(
			new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "end_temple"), new ResourceLocation("empty"),
					ImmutableList.of(Pair.of(JigsawPiece.legacy(
							new ResourceLocation(ShrinesMod.MODID, "end_temple/end_temple").toString(),
							ProcessorLists.EMPTY), 1)),
					JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern FLOODED_TEMPLE = JigsawPatternRegistry.register(
			new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "flooded_temple"), new ResourceLocation("empty"),
					ImmutableList.of(Pair.of(JigsawPiece.legacy(
							new ResourceLocation(ShrinesMod.MODID, "flooded_temple/flooded_temple").toString(),
							ProcessorLists.EMPTY), 1)),
					JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern GUARDIAN_MEETING = JigsawPatternRegistry.register(
			new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "guardian_meeting"), new ResourceLocation("empty"),
					ImmutableList.of(Pair.of(JigsawPiece.legacy(
							new ResourceLocation(ShrinesMod.MODID, "guardian_meeting/guardian_meeting").toString(),
							ProcessorLists.EMPTY), 1)),
					JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern HIGH_TEMPLE = JigsawPatternRegistry.register(
			new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "high_temple"), new ResourceLocation("empty"),
					ImmutableList.of(Pair.of(JigsawPiece.legacy(
							new ResourceLocation(ShrinesMod.MODID, "high_temple/high_temple").toString(),
							ProcessorLists.EMPTY), 1)),
					JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern JUNGLE_TOWER = JigsawPatternRegistry.register(
			new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "jungle_tower"), new ResourceLocation("empty"),
					ImmutableList.of(Pair.of(JigsawPiece.legacy(
							new ResourceLocation(ShrinesMod.MODID, "jungle_tower/jungle_tower").toString(),
							ProcessorLists.EMPTY), 1)),
					JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern MINERAL_TEMPLE = JigsawPatternRegistry.register(
			new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "mineral_temple"), new ResourceLocation("empty"),
					ImmutableList.of(Pair.of(JigsawPiece.legacy(
							new ResourceLocation(ShrinesMod.MODID, "mineral_temple/mineral_temple").toString(),
							ProcessorLists.EMPTY), 1)),
					JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern NETHER_PYRAMID = JigsawPatternRegistry.register(
			new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "nether_pyramid"), new ResourceLocation("empty"),
					ImmutableList.of(Pair.of(JigsawPiece.legacy(
							new ResourceLocation(ShrinesMod.MODID, "nether_pyramid/nether_pyramid").toString(),
							ProcessorLists.EMPTY), 1)),
					JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern NETHER_SHRINE = JigsawPatternRegistry.register(
			new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "nether_shrine"), new ResourceLocation("empty"),
					ImmutableList.of(
							Pair.of(JigsawPiece
									.legacy(new ResourceLocation(ShrinesMod.MODID, "nether_shrine/nether_shrine_001")
											.toString(), ProcessorLists.EMPTY),
									1),
							Pair.of(JigsawPiece
									.legacy(new ResourceLocation(ShrinesMod.MODID, "nether_shrine/nether_shrine_002")
											.toString(), ProcessorLists.EMPTY),
									1),
							Pair.of(JigsawPiece
									.legacy(new ResourceLocation(ShrinesMod.MODID, "nether_shrine/nether_shrine_003")
											.toString(), ProcessorLists.EMPTY),
									1),
							Pair.of(JigsawPiece
									.legacy(new ResourceLocation(ShrinesMod.MODID, "nether_shrine/nether_shrine_004")
											.toString(), ProcessorLists.EMPTY),
									1),
							Pair.of(JigsawPiece
									.legacy(new ResourceLocation(ShrinesMod.MODID, "nether_shrine/nether_shrine_005")
											.toString(), ProcessorLists.EMPTY),
									1),
							Pair.of(JigsawPiece
									.legacy(new ResourceLocation(ShrinesMod.MODID, "nether_shrine/nether_shrine_006")
											.toString(), ProcessorLists.EMPTY),
									1),
							Pair.of(JigsawPiece
									.legacy(new ResourceLocation(ShrinesMod.MODID, "nether_shrine/nether_shrine_007")
											.toString(), ProcessorLists.EMPTY),
									1),
							Pair.of(JigsawPiece
									.legacy(new ResourceLocation(ShrinesMod.MODID, "nether_shrine/nether_shrine_008")
											.toString(), ProcessorLists.EMPTY),
									1),
							Pair.of(JigsawPiece
									.legacy(new ResourceLocation(ShrinesMod.MODID, "nether_shrine/nether_shrine_009")
											.toString(), ProcessorLists.EMPTY),
									1),
							Pair.of(JigsawPiece
									.legacy(new ResourceLocation(ShrinesMod.MODID, "nether_shrine/nether_shrine_010")
											.toString(), ProcessorLists.EMPTY),
									1),
							Pair.of(JigsawPiece
									.legacy(new ResourceLocation(ShrinesMod.MODID, "nether_shrine/nether_shrine_011")
											.toString(), ProcessorLists.EMPTY),
									1),
							Pair.of(JigsawPiece.legacy(
									new ResourceLocation(ShrinesMod.MODID, "nether_shrine/nether_shrine_sandstone")
											.toString(),
									ProcessorLists.EMPTY), 1)),
					JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern ORIENTAL_SANCTUARY = JigsawPatternRegistry.register(new JigsawPattern(
			new ResourceLocation(ShrinesMod.MODID, "oriental_sanctuary"), new ResourceLocation("empty"),
			ImmutableList.of(Pair.of(JigsawPiece.legacy(
					new ResourceLocation(ShrinesMod.MODID, "oriental_sanctuary/oriental_sanctuary_0").toString(),
					ProcessorLists.EMPTY), 1)),
			JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern PLAYER_HOUSE = JigsawPatternRegistry
			.register(
					new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "player_house"),
							new ResourceLocation("empty"),
							ImmutableList.of(
									Pair.of(JigsawPiece.legacy(
											new ResourceLocation(ShrinesMod.MODID, "player_house/player_house_1")
													.toString(),
											ProcessorLists.EMPTY), 1),
									Pair.of(JigsawPiece.legacy(
											new ResourceLocation(ShrinesMod.MODID, "player_house/player_house_2")
													.toString(),
											ProcessorLists.EMPTY), 1),
									Pair.of(JigsawPiece.legacy(
											new ResourceLocation(ShrinesMod.MODID, "player_house/player_house_3")
													.toString(),
											ProcessorLists.EMPTY), 1),
									Pair.of(JigsawPiece.legacy(
											new ResourceLocation(ShrinesMod.MODID, "player_house/player_house_v2_1")
													.toString(),
											ProcessorLists.EMPTY), 1),
									Pair.of(JigsawPiece.legacy(
											new ResourceLocation(ShrinesMod.MODID, "player_house/player_house_v2_2")
													.toString(),
											ProcessorLists.EMPTY), 1),
									Pair.of(JigsawPiece.legacy(
											new ResourceLocation(ShrinesMod.MODID, "player_house/player_house_v2_3")
													.toString(),
											ProcessorLists.EMPTY), 1)),
							JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern INFESTED_PRISON = JigsawPatternRegistry.register(
			new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "infested_prison"), new ResourceLocation("empty"),
					ImmutableList.of(Pair.of(JigsawPiece.legacy(
							new ResourceLocation(ShrinesMod.MODID, "infested_prison/infested_prison").toString(),
							ProcessorLists.EMPTY), 1)),
					JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern SMALL_TEMPLE = JigsawPatternRegistry.register(
			new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "small_temple"), new ResourceLocation("empty"),
					ImmutableList.of(Pair.of(JigsawPiece.legacy(
							new ResourceLocation(ShrinesMod.MODID, "small_temple/small_temple").toString(),
							ProcessorLists.EMPTY), 1)),
					JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern WATER_SHRINE = JigsawPatternRegistry.register(
			new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "water_shrine"), new ResourceLocation("empty"),
					ImmutableList.of(Pair.of(JigsawPiece.legacy(
							new ResourceLocation(ShrinesMod.MODID, "water_shrine/water_shrine").toString(),
							ProcessorLists.EMPTY), 1)),
					JigsawPattern.PlacementBehaviour.RIGID));

	public static final JigsawPattern ABANDONED_WITCH_HOUSE = JigsawPatternRegistry.register(new JigsawPattern(
			new ResourceLocation(ShrinesMod.MODID, "abandoned_witch_house"), new ResourceLocation("empty"),
			ImmutableList.of(Pair.of(JigsawPiece.legacy(
					new ResourceLocation(ShrinesMod.MODID, "abandoned_witch_house/abandoned_witch_house").toString(),
					ProcessorLists.EMPTY), 1)),
			JigsawPattern.PlacementBehaviour.RIGID));

	public static void load() {
		// Player house table
		JigsawPatternRegistry
				.register(new JigsawPattern(new ResourceLocation(ShrinesMod.MODID, "player_house_table"),
						new ResourceLocation("empty"),
						ImmutableList.of(Pair.of(JigsawPiece.legacy(
								new ResourceLocation(ShrinesMod.MODID, "player_house/table").toString(),
								ProcessorLists.EMPTY), 1)),
						JigsawPattern.PlacementBehaviour.RIGID));
	}
}