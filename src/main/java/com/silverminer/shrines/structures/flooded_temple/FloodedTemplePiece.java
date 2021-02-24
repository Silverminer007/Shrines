package com.silverminer.shrines.structures.flooded_temple;

import java.util.List;
import java.util.Random;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class FloodedTemplePiece {
	private static final ResourceLocation location = new ResourceLocation("shrines:flooded_temple/flooded_temple");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new FloodedTemplePiece.Piece(templateManager, location, pos, rotation, 0));
	}

	public static class Piece extends ColorStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(StructurePieceTypes.FLOODED_TEMPLE, templateManager, location, pos, rotation, componentTypeIn, true);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.FLOODED_TEMPLE, templateManager, cNBT);
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			super.handleDataMarker(function, pos, worldIn, rand, sbb);
			if (Config.STRUCTURES.FLOODED_TEMPLE.LOOT_CHANCE.get() > rand.nextDouble()) {
				if (function.equals("chest")) {
					if (worldIn.getBlockState(pos.up()).getBlock() == Blocks.AIR)
						worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					else
						worldIn.setBlockState(pos, Blocks.COBWEB.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down());
					if (tileentity instanceof ChestTileEntity) {
						((ChestTileEntity) tileentity).setLootTable(ShrinesLootTables.FLOODED_TEMPLE, rand.nextLong());
					}
				}
			}
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.FLOODED_TEMPLE.USE_RANDOM_VARIANTING.get();
		}

		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}

		public boolean validateBlock(BlockPos pos, BlockState newState, ISeedReader world) {
			return true;
		}

		public boolean overwriteWool() {
			return false;
		}

		public boolean overwriteTerracotta() {
			return false;
		}

		public boolean overwriteGlazedTerracotta() {
			return false;
		}
	}
}