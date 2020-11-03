package com.silverminer.shrines.structures.water_shrine;

import java.util.List;
import java.util.Random;

import com.silverminer.shrines.structures.ShrinesStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class WaterShrinePiece {
	private static final ResourceLocation location = new ResourceLocation("shrines:water_shrine/water_shrine");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new WaterShrinePiece.Piece(templateManager, location, pos, rotation, 0));
	}

	public static class Piece extends ShrinesStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos,
				Rotation rotation, int componentTypeIn) {
			super(StructurePieceTypes.WATER_SHRINE, templateManager, location, pos, rotation, componentTypeIn);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.WATER_SHRINE, templateManager, cNBT);
		}
	}
}