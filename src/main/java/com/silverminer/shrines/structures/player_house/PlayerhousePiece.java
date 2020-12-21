package com.silverminer.shrines.structures.player_house;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
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
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class PlayerhousePiece {
	private static final ArrayList<ResourceLocation> location = Lists.newArrayList(
			new ResourceLocation("shrines:player_house/player_house_1"),
			new ResourceLocation("shrines:player_house/player_house_2"),
			new ResourceLocation("shrines:player_house/player_house_3"),
			new ResourceLocation("shrines:player_house/player_house_spruce_table_1"),
			new ResourceLocation("shrines:player_house/player_house_spruce_table_2"),
			new ResourceLocation("shrines:player_house/player_house_table"));

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new PlayerhousePiece.Piece(templateManager, location.get(random.nextInt(location.size())), pos,
				rotation, 0));
	}

	public static class Piece extends AbstractStructurePiece {

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(StructurePieceTypes.PLAYER_HOUSE, templateManager, location, pos, rotation, componentTypeIn);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.PLAYER_HOUSE, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			//if (Config.STRUCTURES.GENERATE_PLAYER_HOUSE_LOOT_CHANCE.get() > rand.nextDouble()) {
			if (Config.STRUCTURES.PLAYER_HOUSE.LOOT_CHANCE.get() > rand.nextDouble()) {
				if ("chest".equals(function)) {
					if (rand.nextInt(6) == 0) {
						if (rand.nextInt(2) == 0) {
							worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
							TileEntity tileentity = worldIn.getTileEntity(pos.down());
							if (tileentity instanceof ChestTileEntity) {
								((ChestTileEntity) tileentity).setLootTable(ShrinesLootTables.HOUSE_OP,
										rand.nextLong());
							}
						} else {
							worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
							TileEntity tileentity = worldIn.getTileEntity(pos.down());
							if (tileentity instanceof ChestTileEntity) {
								((ChestTileEntity) tileentity).setLootTable(ShrinesLootTables.HOUSE_OP_2,
										rand.nextLong());
							}
						}
					} else {
						worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
						TileEntity tileentity = worldIn.getTileEntity(pos.down());
						if (tileentity instanceof ChestTileEntity) {
							((ChestTileEntity) tileentity).setLootTable(ShrinesLootTables.getRandomVillageLoot(rand),
									rand.nextLong());
						}
					}
				}
				if ("chest_furnace".equals(function)) {
					if (rand.nextInt(2) == 0) {
						worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
						TileEntity tileentity = worldIn.getTileEntity(pos.down());
						if (tileentity instanceof ChestTileEntity) {
							((ChestTileEntity) tileentity).setLootTable(ShrinesLootTables.FURNACE, rand.nextLong());
						}
					} else {
						worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
						TileEntity tileentity = worldIn.getTileEntity(pos.down());
						if (tileentity instanceof ChestTileEntity) {
							((ChestTileEntity) tileentity).setLootTable(ShrinesLootTables.FURNACE_2, rand.nextLong());
						}
					}
				}
			}
		}
	}
}