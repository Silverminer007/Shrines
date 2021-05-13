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
package com.silverminer.shrines.structures.ballon;

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
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class BallonPiece {
	private static final ArrayList<ResourceLocation> location = Lists.newArrayList(
			new ResourceLocation("shrines:ballon/ballon_1"), new ResourceLocation("shrines:ballon/ballon_2"),
			new ResourceLocation("shrines:ballon/ballon_3"), new ResourceLocation("shrines:ballon/ballon_4"),
			new ResourceLocation("shrines:ballon/ballon_5"), new ResourceLocation("shrines:ballon/ballon_6"),
			new ResourceLocation("shrines:ballon/ballon_7"), new ResourceLocation("shrines:ballon/ballon2_1"),
			new ResourceLocation("shrines:ballon/ballon2_2"), new ResourceLocation("shrines:ballon/ballon2_3"),
			new ResourceLocation("shrines:ballon/ballon2_4"));

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		boolean flag = true;
		if (flag)
			pieces.add(new BallonPiece.Piece(templateManager, location.get(random.nextInt(location.size())), pos,
					rotation, 0, random));
		else
			// Test function for single variant
			pieces.add(new BallonPiece.Piece(templateManager, location.get(0), pos, rotation, 0, random));
	}

	public static class Piece extends ColorStructurePiece {
		protected int heightOffset = 0;

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn, Random rand) {
			super(StructurePieceTypes.BALLON, templateManager, location, pos, rotation, componentTypeIn, true);
			this.heightOffset = 5 + rand.nextInt(25);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.BALLON, templateManager, cNBT);
			this.heightOffset = cNBT.getInt("height");
		}

		protected void addAdditionalSaveData(CompoundNBT tagCompound) {
			super.addAdditionalSaveData(tagCompound);
			tagCompound.putInt("height", this.heightOffset);
		}

		@Override
		protected int getHeight(ISeedReader world, BlockPos blockpos1) {
			return super.getHeight(world, blockpos1) + this.heightOffset - 1;
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR;
		}

		public boolean overwriteWool() {
			return true;
		}

		public boolean overwriteWood() {
			return true;
		}

		public boolean overwritePlanks() {
			return true;
		}

		public boolean overwriteSlabs() {
			return true;
		}

		public Block getDefaultPlank() {
			return Blocks.SPRUCE_PLANKS;
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.BALLON.USE_RANDOM_VARIANTING.get();
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			boolean loot = Config.STRUCTURES.BALLON.LOOT_CHANCE.get() > rand.nextDouble();
			if (function.equals("chest")) {
				worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				LockableLootTileEntity.setLootTable(worldIn, rand, pos.above(2),
						loot ? ShrinesLootTables.BALLON : ShrinesLootTables.EMPTY);
			}
		}
	}
}