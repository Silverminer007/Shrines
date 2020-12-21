package com.silverminer.shrines.structures.nether_pyramid;

import java.util.List;
import java.util.Random;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.structures.AbstractStructurePiece;
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
import net.minecraft.world.gen.feature.template.TemplateManager;

public class NetherPyramidPiece {
	private static final ResourceLocation location = new ResourceLocation("shrines:nether_pyramid/nether_pyramid");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new NetherPyramidPiece.Piece(templateManager, location, pos.add(0, -1, 0), rotation, 0));
	}

	public static class Piece extends AbstractStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(StructurePieceTypes.NETHER_PYRAMID, templateManager, location, pos, rotation, componentTypeIn);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.NETHER_PYRAMID, templateManager, cNBT);
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			//if (Config.STRUCTURES.GENERATE_NETHER_PYRAMID_LOOT_CHANCE.get() > rand.nextDouble()) {
			if (Config.STRUCTURES.NETHER_PYRAMID.LOOT_CHANCE.get() > rand.nextDouble()) {
				if ("chest_left".equals(function) || "chest_right".equals(function) || "chest_d1".equals(function)
						|| "chest_d2".equals(function) || "chest_d3".equals(function) || "chest_d4".equals(function)) {
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down());
					if (tileentity instanceof ChestTileEntity) {
						((ChestTileEntity) tileentity).setLootTable(ShrinesLootTables.getRandomLoot(rand),
								rand.nextLong());
					}
				}
			}
		}
	}
}