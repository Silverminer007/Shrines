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
package com.silverminer.shrines.core.structures.jungle_tower;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
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
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class JungleTowerPiece {
	private static final ArrayList<ResourceLocation> location = Lists
			.newArrayList(new ResourceLocation("shrines:jungle_tower/jungle_tower"));

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random, ChunkGenerator chunkGenerator) {
		int height = StructureUtils.getAverageHeight(pos.offset(-16, 0, -16), chunkGenerator, 2);
		pieces.add(new JungleTowerPiece.Piece(templateManager, location.get(random.nextInt(location.size())), pos,
				rotation, 0, false, height));
	}

	public static class Piece extends ColorStructurePiece {

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn, boolean defaultValue, int height) {
			super(StructurePieceTypes.JUNGLE_TOWER, templateManager, location, pos, rotation, componentTypeIn,
					defaultValue, height);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.JUNGLE_TOWER, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.JUNGLE_TOWER.USE_RANDOM_VARIANTING.get();
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			boolean loot = Config.STRUCTURES.JUNGLE_TOWER.LOOT_CHANCE.get() > rand.nextDouble();
			if (function.equals("chest")) {
				worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(),
						loot ? ShrinesLootTables.JUNGLE_TOWER : ShrinesLootTables.EMPTY);
			}
		}
	}
}