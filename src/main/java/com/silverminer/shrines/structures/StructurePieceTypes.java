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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.structures.ballon.BallonPiece;
import com.silverminer.shrines.structures.bees.BeesPiece;
import com.silverminer.shrines.structures.custom.CustomPiece;
import com.silverminer.shrines.structures.end_temple.EndTemplePiece;
import com.silverminer.shrines.structures.flooded_temple.FloodedTemplePiece;
import com.silverminer.shrines.structures.guardian_meeting.GuardianMeetingPiece;
import com.silverminer.shrines.structures.harbour.HarbourPieces;
import com.silverminer.shrines.structures.harbour.HarbourPieces.HarbourBuildingPiece;
import com.silverminer.shrines.structures.harbour.test.BetterHarbourPieces;
import com.silverminer.shrines.structures.high_tempel.HighTempelPiece;
import com.silverminer.shrines.structures.jungle_tower.JungleTowerPiece;
import com.silverminer.shrines.structures.mineral_temple.MineralTemplePiece;
import com.silverminer.shrines.structures.nether_pyramid.NetherPyramidPiece;
import com.silverminer.shrines.structures.nether_shrine.NetherShrinePiece;
import com.silverminer.shrines.structures.player_house.PlayerhousePiece;
import com.silverminer.shrines.structures.prison.InfestedPrisonPiece;
import com.silverminer.shrines.structures.small_tempel.SmallTempelPiece;
import com.silverminer.shrines.structures.water_shrine.WaterShrinePiece;
import com.silverminer.shrines.structures.witch_house.AbandonedWitchHousePiece;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public class StructurePieceTypes {
	protected static final Logger LOGGER = LogManager.getLogger();

	public static final IStructurePieceType NETHER_SHRINE = NetherShrinePiece.Piece::new;

	public static final IStructurePieceType BALLON = BallonPiece.Piece::new;

	public static final IStructurePieceType BEES = BeesPiece.Piece::new;

	public static final IStructurePieceType HIGH_TEMPEL = HighTempelPiece.Piece::new;

	public static final IStructurePieceType NETHER_PYRAMID = NetherPyramidPiece.Piece::new;

	public static final IStructurePieceType SMALL_TEMPEL = SmallTempelPiece.Piece::new;

	public static final IStructurePieceType WATER_SHRINE = WaterShrinePiece.Piece::new;

	public static final IStructurePieceType PLAYER_HOUSE = PlayerhousePiece.Piece::new;

	public static final IStructurePieceType MINERAL_TEMPLE = MineralTemplePiece.Piece::new;
	
	public static final IStructurePieceType FLOODED_TEMPLE = FloodedTemplePiece.Piece::new;
	
	public static final IStructurePieceType HARBOUR_HOUSE = HarbourBuildingPiece::new;
	public static final IStructurePieceType BETTER_HARBOUR = BetterHarbourPieces.HarbourBuildingPiece::new;
	
	public static final IStructurePieceType HARBOUR_GROUND = HarbourPieces.HarbourPiece::new;
	
	public static final IStructurePieceType HARBOUR_VILLAGER = HarbourPieces.VillagerPiece::new;
	
	public static final IStructurePieceType INFESTED_PRISON = InfestedPrisonPiece.Piece::new;
	
	public static final IStructurePieceType JUNGLE_TOWER = JungleTowerPiece.Piece::new;
	
	public static final IStructurePieceType WITCH_HOUSE = AbandonedWitchHousePiece.Piece::new;
	
	public static final IStructurePieceType END_TEMPLE = EndTemplePiece.Piece::new;
	
	public static final IStructurePieceType GUARDIAN_MEETING = GuardianMeetingPiece.Piece::new;

	public static final IStructurePieceType CUSTOM = CustomPiece.Piece::new;


	public static void regsiter() {
		LOGGER.debug("Shrines: registering structure piece types");
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "ballon"), BALLON);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "bees"), BEES);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "nether_shrine"),
				NETHER_SHRINE);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "high_tempel"), HIGH_TEMPEL);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "nether_pyramid"),
				NETHER_PYRAMID);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "small_tempel"), SMALL_TEMPEL);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "water_shrine"), WATER_SHRINE);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "player_house"), PLAYER_HOUSE);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "mineral_temple"),
				MINERAL_TEMPLE);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "flooded_temple"),
				FLOODED_TEMPLE);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "harbour_house"),
				HARBOUR_HOUSE);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "harbour_ground"),
				HARBOUR_GROUND);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "harbour_villager"),
				HARBOUR_VILLAGER);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "infested_prison"),
				INFESTED_PRISON);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "witch_house"), WITCH_HOUSE);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "jungle_tower"), JUNGLE_TOWER);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "end_temple"), END_TEMPLE);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "guardian_meeting"), GUARDIAN_MEETING);
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "better_harbour"), BETTER_HARBOUR);
		
		Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(Shrines.MODID, "custom"), CUSTOM);
	}
}