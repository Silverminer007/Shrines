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
package com.silverminer.shrines.structures.nether_pyramid;

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
import net.minecraft.world.gen.feature.template.TemplateManager;

public class NetherPyramidPiece {
	private static final ResourceLocation location = new ResourceLocation("shrines:nether_pyramid/nether_pyramid");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new NetherPyramidPiece.Piece(templateManager, location, pos.offset(0, -1, 0), rotation, 0));
	}

	public static class Piece extends ColorStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(StructurePieceTypes.NETHER_PYRAMID, templateManager, location, pos, rotation, componentTypeIn, false);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.NETHER_PYRAMID, templateManager, cNBT);
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			boolean loot = Config.STRUCTURES.NETHER_PYRAMID.LOOT_CHANCE.get() > rand.nextDouble();
				if ("chest_left".equals(function) || "chest_right".equals(function) || "chest_d1".equals(function)
						|| "chest_d2".equals(function) || "chest_d3".equals(function) || "chest_d4".equals(function)) {
					worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
					LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(), loot ? ShrinesLootTables.getRandomNetherLoot(rand) : ShrinesLootTables.EMPTY);
				}
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.NETHER_PYRAMID.USE_RANDOM_VARIANTING.get();
		}

		public boolean overwriteStone() {
			return true;
		}

		public boolean overwriteStairs() {
			return true;
		}

		public boolean overwriteSlabs() {
			return true;
		}

		public float getStoneChangeChance() {
			return 0.15F;
		}
	}
}