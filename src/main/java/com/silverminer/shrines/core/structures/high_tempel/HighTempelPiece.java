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
package com.silverminer.shrines.core.structures.high_tempel;

import java.util.List;
import java.util.Random;

import com.silverminer.shrines.core.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.core.structures.ColorStructurePiece;
import com.silverminer.shrines.core.structures.StructurePieceTypes;
import com.silverminer.shrines.forge.config.Config;

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
import net.minecraft.world.gen.feature.template.TemplateManager;

public class HighTempelPiece {
	private static final ResourceLocation location = new ResourceLocation("shrines:high_tempel/high_tempel");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new HighTempelPiece.Piece(templateManager, location, pos, rotation, 0));
	}

	public static class Piece extends ColorStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(StructurePieceTypes.HIGH_TEMPEL, templateManager, location, pos, rotation, componentTypeIn, true);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.HIGH_TEMPEL, templateManager, cNBT);
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			super.handleDataMarker(function, pos, worldIn, rand, sbb);
			boolean loot = Config.STRUCTURES.HIGH_TEMPEL.LOOT_CHANCE.get() > rand.nextDouble();
			if (function.equals("chest")) {
				worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(),
						loot ? ShrinesLootTables.HIGH_TEMPEL : ShrinesLootTables.EMPTY);
			}
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.HIGH_TEMPEL.USE_RANDOM_VARIANTING.get();
		}

		public Block getDefaultPlank() {
			return Blocks.DARK_OAK_PLANKS;
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