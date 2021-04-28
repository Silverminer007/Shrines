package com.silverminer.shrines.structures.mineral_temple;

import java.util.List;
import java.util.Random;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class MineralTemplePiece {
	private static final ResourceLocation location = new ResourceLocation("shrines:mineral_temple/mineral_temple");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new MineralTemplePiece.Piece(templateManager, location, pos, rotation, 0));
	}

	public static class Piece extends ColorStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(StructurePieceTypes.MINERAL_TEMPLE, templateManager, location, pos, rotation, componentTypeIn, true);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.MINERAL_TEMPLE, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.MINERAL_TEMPLE.USE_RANDOM_VARIANTING.get();
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			if (Config.STRUCTURES.MINERAL_TEMPLE.LOOT_CHANCE.get() > rand.nextDouble()) {
				if ("chest".equals(function)) {
					worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
					TileEntity tileentity = worldIn.getBlockEntity(pos.below());
					if (tileentity instanceof ChestTileEntity) {
						((ChestTileEntity) tileentity).setLootTable(ShrinesLootTables.MINERAL_TEMPLE, rand.nextLong());
					}
				}
			}
		}
	}
}