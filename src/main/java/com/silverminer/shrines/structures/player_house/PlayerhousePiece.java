/**
 * Silverminer (and Team)
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * 
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.structures.player_house;

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
	private static final ArrayList<ResourceLocation> v2_location = Lists
			.newArrayList(new ResourceLocation("shrines:player_house/player_house_v2_1"));

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		boolean flag = true;
		if (flag)
			if (random.nextInt(2) == 0)
				pieces.add(new PlayerhousePiece.Piece(templateManager, location.get(random.nextInt(location.size())),
						pos, rotation, 0, true, 1));
			else
				pieces.add(new PlayerhousePiece.Piece(templateManager,
						v2_location.get(random.nextInt(v2_location.size())), pos, rotation, 0, true, 2));
		else
			pieces.add(new PlayerhousePiece.Piece(templateManager, location.get(location.size() - 1), pos, rotation, 0,
					true, 0));
	}

	public static class Piece extends ColorStructurePiece {
		public int v = 0;

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn, boolean defaultValue, int vIn) {
			super(StructurePieceTypes.PLAYER_HOUSE, templateManager, location, pos, rotation, componentTypeIn,
					defaultValue);
			this.v = vIn;
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.PLAYER_HOUSE, templateManager, cNBT);
			this.v = cNBT.getInt("version");
		}

		protected void addAdditionalSaveData(CompoundNBT tagCompound) {
			super.addAdditionalSaveData(tagCompound);
			tagCompound.putInt("version", this.v);
		}

		public Block getDefaultPlank() {
			if (v == 2)
				return Blocks.SPRUCE_PLANKS;
			else
				return Blocks.OAK_PLANKS;
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.PLAYER_HOUSE.USE_RANDOM_VARIANTING.get();
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			boolean loot = Config.STRUCTURES.PLAYER_HOUSE.LOOT_CHANCE.get() > rand.nextDouble();
			boolean chest2 = "chest_2".equals(function);
			if ("chest".equals(function) || chest2) {
				worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				BlockPos position;
				if (chest2) {
					position = pos.below(5);
				} else {
					position = pos.below();
				}
				if (rand.nextInt(6) == 0) {
					if (rand.nextInt(2) == 0) {
						LockableLootTileEntity.setLootTable(worldIn, rand, position, loot ? ShrinesLootTables.HOUSE_OP : ShrinesLootTables.EMPTY);
					} else {
						LockableLootTileEntity.setLootTable(worldIn, rand, position, loot ? ShrinesLootTables.HOUSE_OP_2 : ShrinesLootTables.EMPTY);
					}
				} else {
					LockableLootTileEntity.setLootTable(worldIn, rand, position, loot ? ShrinesLootTables.getRandomVillageLoot(rand) : ShrinesLootTables.EMPTY);
				}
			}
			if ("chest_furnace".equals(function)) {
				worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				if (rand.nextInt(2) == 0) {
					LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(),
							loot ? ShrinesLootTables.FURNACE : ShrinesLootTables.EMPTY);
				} else {
					LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(),
							loot ? ShrinesLootTables.FURNACE_2 : ShrinesLootTables.EMPTY);
				}
			}
		}
	}
}