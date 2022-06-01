package com.silverminer.shrines.structures.small_tempel;

import java.util.List;
import java.util.Random;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;

import net.minecraft.block.Block;
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

public class SmallTempelPiece {
	private static final ResourceLocation location = new ResourceLocation("shrines:small_tempel/small_tempel");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new SmallTempelPiece.Piece(templateManager, location, pos, rotation, 0));
	}

	public static class Piece extends ColorStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(StructurePieceTypes.SMALL_TEMPEL, templateManager, location, pos, rotation, componentTypeIn, true);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.SMALL_TEMPEL, templateManager, cNBT);
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.SMALL_TEMPEL.USE_RANDOM_VARIANTING.get();
		}

		public Block getDefaultPlank() {
			return Blocks.SPRUCE_PLANKS;
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			if (Config.STRUCTURES.SMALL_TEMPEL.LOOT_CHANCE.get() > rand.nextDouble()) {
				if (function.equals("chest")) {
					worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
					TileEntity tileentity = worldIn.getBlockEntity(pos.below());
					if (tileentity instanceof ChestTileEntity) {
						((ChestTileEntity) tileentity).setLootTable(ShrinesLootTables.SMALL_TEMPEL, rand.nextLong());
					}
				}
			}
		}
	}
}