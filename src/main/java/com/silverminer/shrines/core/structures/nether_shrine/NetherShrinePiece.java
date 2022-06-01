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
package com.silverminer.shrines.core.structures.nether_shrine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.silverminer.shrines.core.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.core.structures.ColorStructurePiece;
import com.silverminer.shrines.core.structures.StructurePieceTypes;
import com.silverminer.shrines.forge.config.Config;

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

public class NetherShrinePiece {
	private static final ArrayList<ResourceLocation> location = Lists.newArrayList(
			new ResourceLocation("shrines:nether_shrine/nether_shrine_001"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_002"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_003"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_004"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_005"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_006"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_007"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_008"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_009"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_010"),
			new ResourceLocation("shrines:nether_shrine/nether_shrine_011"));

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random) {
		pieces.add(new NetherShrinePiece.Piece(templateManager, location.get(random.nextInt(location.size())), pos,
				rotation, 0));
	}

	public static class Piece extends ColorStructurePiece {

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn) {
			super(StructurePieceTypes.NETHER_SHRINE, templateManager, location, pos, rotation, componentTypeIn, true);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.NETHER_SHRINE, templateManager, cNBT);
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			boolean loot = Config.STRUCTURES.NETHER_SHRINE.LOOT_CHANCE.get() > rand.nextDouble();
			if ("chest1".equals(function) || "chest2".equals(function)) {
				worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(),
						loot ? ShrinesLootTables.getRandomNetherLoot(rand) : ShrinesLootTables.EMPTY);
			}
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.NETHER_SHRINE.USE_RANDOM_VARIANTING.get();
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR;
		}

		public float getStoneChangeChance() {
			return 0.2F;
		}
	}
}