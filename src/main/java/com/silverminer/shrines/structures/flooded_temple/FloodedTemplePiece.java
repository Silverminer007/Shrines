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
package com.silverminer.shrines.structures.flooded_temple;

import java.util.List;
import java.util.Random;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;

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
			boolean loot = Config.STRUCTURES.FLOODED_TEMPLE.LOOT_CHANCE.get() > rand.nextDouble();
			if (function.equals("chest")) {
				if (worldIn.getBlockState(pos.below()).getBlock() == Blocks.AIR)
					worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				else
					worldIn.setBlock(pos, Blocks.COBWEB.defaultBlockState(), 3);
				LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(),
						loot ? ShrinesLootTables.FLOODED_TEMPLE : ShrinesLootTables.EMPTY);
			}
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.FLOODED_TEMPLE.USE_RANDOM_VARIANTING.get();
		}

		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
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