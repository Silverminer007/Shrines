package com.silverminer.shrines.structures.high_tempel;

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

public class HighTempelPiece {
	private static final ResourceLocation bottom = new ResourceLocation("shrines:high_tempel/high_tempel_bottom");
	private static final ResourceLocation top = new ResourceLocation("shrines:high_tempel/high_tempel_top");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new HighTempelPiece.Piece(templateManager, bottom, pos, rotation, 0));
		pieces.add(new HighTempelPiece.Piece(templateManager, top, pos.add(new BlockPos(0, 32, 0)), rotation, 0));
	}

	public static class Piece extends AbstractStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos,
				Rotation rotation, int componentTypeIn) {
			super(StructurePieceTypes.HIGH_TEMPEL, templateManager, location, pos, rotation, componentTypeIn);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.HIGH_TEMPEL, templateManager, cNBT);
		}
	}
}