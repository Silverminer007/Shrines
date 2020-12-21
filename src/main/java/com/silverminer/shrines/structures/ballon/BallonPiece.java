package com.silverminer.shrines.structures.ballon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.AbstractStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class BallonPiece {
	private static final ArrayList<ResourceLocation> location = Lists.newArrayList(
			new ResourceLocation("shrines:ballon/ballon1"),
			new ResourceLocation("shrines:ballon/ballon2"),
			new ResourceLocation("shrines:ballon/ballon3"),
			new ResourceLocation("shrines:ballon/ballon4"),
			new ResourceLocation("shrines:ballon/ballon5"));

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new BallonPiece.Piece(templateManager, location.get(random.nextInt(location.size())), pos,
				rotation, 0));
	}

	public static class Piece extends AbstractStructurePiece {

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos,
				Rotation rotation, int componentTypeIn) {
			super(StructurePieceTypes.BALLON, templateManager, location, pos, rotation, componentTypeIn);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.BALLON, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK;
		}
	}
}