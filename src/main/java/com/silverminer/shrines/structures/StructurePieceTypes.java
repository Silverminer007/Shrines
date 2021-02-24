package com.silverminer.shrines.structures;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.structures.ballon.BallonPiece;
import com.silverminer.shrines.structures.bees.BeesPiece;
import com.silverminer.shrines.structures.flooded_temple.FloodedTemplePiece;
import com.silverminer.shrines.structures.harbour.HarbourPieces;
import com.silverminer.shrines.structures.high_tempel.HighTempelPiece;
import com.silverminer.shrines.structures.mineral_temple.MineralTemplePiece;
import com.silverminer.shrines.structures.nether_pyramid.NetherPyramidPiece;
import com.silverminer.shrines.structures.nether_shrine.NetherShrinePiece;
import com.silverminer.shrines.structures.player_house.PlayerhousePiece;
import com.silverminer.shrines.structures.small_tempel.SmallTempelPiece;
import com.silverminer.shrines.structures.water_shrine.WaterShrinePiece;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public class StructurePieceTypes {

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
	public static final IStructurePieceType HARBOUR_HOUSE = HarbourPieces.HarbourPiece::new;
	public static final IStructurePieceType HARBOUR_GROUND = HarbourPieces.GroundPiece::new;

	public static void regsiter() {
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
	}
}