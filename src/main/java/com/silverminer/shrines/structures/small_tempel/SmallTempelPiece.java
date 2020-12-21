package com.silverminer.shrines.structures.small_tempel;

import java.util.List;
import java.util.Random;

import com.silverminer.shrines.structures.AbstractStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class SmallTempelPiece {
	private static final ResourceLocation location = new ResourceLocation("shrines:small_tempel/small_tempel");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new SmallTempelPiece.Piece(templateManager, location, pos, rotation, 0));
	}

	public static class Piece extends AbstractStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos,
				Rotation rotation, int componentTypeIn) {
			super(StructurePieceTypes.SMALL_TEMPEL, templateManager, location, pos, rotation, componentTypeIn);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.SMALL_TEMPEL, templateManager, cNBT);
		}
	}
}