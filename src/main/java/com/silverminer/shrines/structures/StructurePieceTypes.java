package com.silverminer.shrines.structures;

import com.silverminer.shrines.structures.ballon.BallonPiece;
import com.silverminer.shrines.structures.bees.BeesPiece;
import com.silverminer.shrines.structures.high_tempel.HighTempelPiece;
import com.silverminer.shrines.structures.nether_pyramid.NetherPyramidPiece;
import com.silverminer.shrines.structures.nether_shrine.NetherShrinePiece;
import com.silverminer.shrines.structures.small_tempel.SmallTempelPiece;
import com.silverminer.shrines.structures.water_shrine.WaterShrinePiece;

import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public class StructurePieceTypes {

	public static final IStructurePieceType NETHER_SHRINE = IStructurePieceType.register(NetherShrinePiece.Piece::new,
			"nether_shrine");

	public static final IStructurePieceType BALLON = IStructurePieceType.register(BallonPiece.Piece::new, "ballon");

	public static final IStructurePieceType BEES = IStructurePieceType.register(BeesPiece.Piece::new, "bees");

	public static final IStructurePieceType HIGH_TEMPEL = IStructurePieceType.register(HighTempelPiece.Piece::new,
			"high_tempel");

	public static final IStructurePieceType NETHER_PYRAMID = IStructurePieceType.register(NetherPyramidPiece.Piece::new,
			"nether_pyramid");

	public static final IStructurePieceType SMALL_TEMPEL = IStructurePieceType.register(SmallTempelPiece.Piece::new,
			"small_tempel");

	public static final IStructurePieceType WATER_SHRINE = IStructurePieceType.register(WaterShrinePiece.Piece::new,
			"water_shrine");
}