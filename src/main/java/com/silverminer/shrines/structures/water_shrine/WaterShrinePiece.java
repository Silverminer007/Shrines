package com.silverminer.shrines.structures.water_shrine;

import java.util.List;
import java.util.Random;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class WaterShrinePiece {
	private static final ResourceLocation location = new ResourceLocation("shrines:water_shrine/water_shrine");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new WaterShrinePiece.Piece(templateManager, location, pos, rotation, 0));
	}

	public static class Piece extends ColorStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(StructurePieceTypes.WATER_SHRINE, templateManager, location, pos, rotation, componentTypeIn, true);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.WATER_SHRINE, templateManager, cNBT);
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.WATER_SHRINE.USE_RANDOM_VARIANTING.get();
		}

		public boolean validateBlock(BlockPos pos, BlockState newState, ISeedReader world) {
			return true;
		}
	}
}