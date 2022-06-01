package com.silverminer.shrines.structures.witch_house;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
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

public class AbandonedWitchHousePiece {
	private static final ArrayList<ResourceLocation> location = Lists
			.newArrayList(new ResourceLocation("shrines:witch_house/abandoned_witch_house"));

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new AbandonedWitchHousePiece.Piece(templateManager, location.get(random.nextInt(location.size())),
				pos, rotation, 0, true));
	}

	public static class Piece extends ColorStructurePiece {

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn, boolean defaultValue) {
			super(StructurePieceTypes.WITCH_HOUSE, templateManager, location, pos, rotation, componentTypeIn,
					defaultValue);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.WITCH_HOUSE, templateManager, cNBT);
		}

		public Block getDefaultPlank() {
			return Blocks.SPRUCE_PLANKS;
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.WITCH_HOUSE.USE_RANDOM_VARIANTING.get();
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			if (Config.STRUCTURES.WITCH_HOUSE.LOOT_CHANCE.get() > rand.nextDouble()) {
				if (function.equals("chest")) {
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down(3));
					if (tileentity instanceof LockableLootTileEntity) {
						((LockableLootTileEntity) tileentity).setLootTable(ShrinesLootTables.WITCH_HOUSE,
								rand.nextLong());
					}
				}
				if (function.equals("chest_cobweb")) {
					worldIn.setBlockState(pos, Blocks.COBWEB.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down());
					if (tileentity instanceof LockableLootTileEntity) {
						((LockableLootTileEntity) tileentity).setLootTable(ShrinesLootTables.WITCH_HOUSE,
								rand.nextLong());
					}
				}
				if (function.equals("chest_op")) {
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					TileEntity tileentity = worldIn.getTileEntity(pos.down());
					if (tileentity instanceof LockableLootTileEntity) {
						((LockableLootTileEntity) tileentity).setLootTable(ShrinesLootTables.WITCH_HOUSE,
								rand.nextLong());
					}
				}
			}
		}
	}
}