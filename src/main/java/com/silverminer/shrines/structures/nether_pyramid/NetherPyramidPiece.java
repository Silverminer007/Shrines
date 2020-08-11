package com.silverminer.shrines.structures.nether_pyramid;

import java.util.List;
import java.util.Random;

import com.silverminer.shrines.structures.ShrinesStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class NetherPyramidPiece {
	private static final ResourceLocation location = new ResourceLocation("shrines:nether_pyramid/nether_pyramid");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new NetherPyramidPiece.Piece(templateManager, location, pos.add(0, -1, 0), rotation, 0));
	}

	public static class Piece extends ShrinesStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos,
				Rotation rotation, int componentTypeIn) {
			super(StructurePieceTypes.NETHER_PYRAMID, templateManager, location, pos, rotation, componentTypeIn);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.NETHER_PYRAMID, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}
	}
}