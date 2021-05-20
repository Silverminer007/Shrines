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
package com.silverminer.shrines.core.structures.bees;

import java.util.List;
import java.util.Random;

import com.silverminer.shrines.core.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.core.structures.ColorStructurePiece;
import com.silverminer.shrines.core.structures.StructurePieceTypes;
import com.silverminer.shrines.core.utils.StructureUtils;
import com.silverminer.shrines.forge.config.Config;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class BeesPiece {
	private static final ResourceLocation location = new ResourceLocation("shrines:bees/bees");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random, ChunkGenerator chunkGenerator) {
		int height = StructureUtils.getAverageHeight(pos.offset(-8, 0, -8), chunkGenerator, 1);
		pieces.add(new BeesPiece.Piece(templateManager, location, pos.offset(0, -1, 0), rotation, 0, height));
	}

	public static class Piece extends ColorStructurePiece {
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn, int height) {
			super(StructurePieceTypes.BEES, templateManager, location, pos, rotation, componentTypeIn, true, height);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.BEES, templateManager, cNBT);
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.BEES.USE_RANDOM_VARIANTING.get();
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			boolean loot = Config.STRUCTURES.BEES.LOOT_CHANCE.get() > rand.nextDouble();
			if (function.equals("chest")) {
				worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(),
						loot ? ShrinesLootTables.BEES : ShrinesLootTables.EMPTY);
			}
		}
	}
}