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
package com.silverminer.shrines.core.structures.harbour.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.silverminer.shrines.core.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.core.structures.ColorStructurePiece;
import com.silverminer.shrines.core.structures.StructurePieceTypes;
import com.silverminer.shrines.forge.config.Config;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class BetterHarbourPieces {
	protected static final Logger LOG = LogManager.getLogger(BetterHarbourPieces.class);

	protected static List<StructurePiece> PIECES = null;
	protected static final ArrayList<MutableBoundingBox> mbbs = Lists.newArrayList();

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random, ChunkGenerator chunkGenerator) {
		PIECES = pieces;
		int height = HarbourHelper.getStartHeigth(pos, chunkGenerator) - 6;
		pos = new BlockPos(pos.getX(), height, pos.getZ());
		LOG.info("Generating Harbourpieces on: {}, with height: {}", pos, height);
		BlockPos ecke = new BlockPos(0, 0, 0);
		ArrayList<MutableBoundingBox> bounds = Lists.newArrayList();
		for (int i = 1; i < 70; i = (int) Math.ceil(i * 1.25)) {
			for (int s = 1; s >= -1; s = s - 2) {
				ecke = pos.offset(i * s, 0, i * s);
				for (int x = 0; x <= (Math.abs(i * s * 2)); x = x + 1) {
					BlockPos position = new BlockPos(ecke.getX() + (x * -s), 0, ecke.getZ());
					ResourceLocation r = checkPos(position, bounds, chunkGenerator, i);
					if (r == null)
						break;
					PIECES.add(new BetterHarbourPieces.HarbourBuildingPiece(templateManager, r, position, rotation, 0, random,
							height));

				}
				for (int z = 0; z <= (Math.abs(i * s * 2)); z = z + 1) {
					BlockPos position = new BlockPos(ecke.getX(), 0, ecke.getZ() + (z * -s));
					ResourceLocation r = checkPos(position, bounds, chunkGenerator, i);
					if (r == null)
						break;
					PIECES.add(new BetterHarbourPieces.HarbourBuildingPiece(templateManager, r, position, rotation, 0, random,
							height));

				}
			}
		}
		pieces = PIECES;
	}

	protected static ResourceLocation checkPos(BlockPos pos, ArrayList<MutableBoundingBox> bounds, ChunkGenerator cG,
			int distance) {
		ArrayList<ResourceLocation> pp = HarbourHelper.getPossiblePieces(distance);
		if (pp == null || pp.isEmpty()) {
			return null;
		}
		MutableBoundingBox mbb = MutableBoundingBox.createProper(0, 0, 0, 0, 0, 0);
		ResourceLocation piece = null;
		for (ResourceLocation r : pp) {
			mbb = HarbourHelper.getBoundByPieces(r);
			mbb.move(pos.getX(), pos.getY(), pos.getZ());
			if (validatePiecePos(pos, mbb, cG)) {
				piece = r;
				break;
			}
		}
		if (piece == null || mbb == null)
			return null;
		mbbs.add(mbb);
		return piece;
	}

	public static boolean validatePiecePos(BlockPos pos, MutableBoundingBox mbb, ChunkGenerator cG) {
		for (MutableBoundingBox m : mbbs) {
			if (HarbourHelper.areBoundingBoxesIntersecting(m, mbb)) {
				return false;
			}
		}
		for (int x = mbb.x0; x <= mbb.x1; x++) {
			for (int z = mbb.z0; z <= mbb.z1; z++) {
				int n = 0;
				BlockState state = cG.getBaseColumn(x, z).getBlockState(new BlockPos(x, pos.getY() - n, z));
				while (!state.canOcclude()) {
					n++;
					state = cG.getBaseColumn(x, z).getBlockState(new BlockPos(x, pos.getY() - n, z));
					LOG.info("Checking state {} for validation. It's block {}", state, state.getBlock());
					if (n > 3) {
						return false;
					} else if (state.getBlock() instanceof FlowingFluidBlock) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static class HarbourBuildingPiece extends ColorStructurePiece {
		protected int height;

		public HarbourBuildingPiece(TemplateManager templateManagerIn, ResourceLocation locationIn, BlockPos posIn,
				Rotation rotationIn, int componentTypeIn, Random rand, int height) {
			super(StructurePieceTypes.BETTER_HARBOUR, templateManagerIn, locationIn, posIn, rotationIn, componentTypeIn,
					true);
			this.height = height;
		}

		public HarbourBuildingPiece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.BETTER_HARBOUR, templateManager, cNBT);
			this.height = cNBT.getInt("height");
			this.diamonds = cNBT.getInt("diamonds");
		}

		/**
		 * (abstract) Helper method to read subclass data from NBT
		 */
		protected void addAdditionalSaveData(CompoundNBT tagCompound) {
			super.addAdditionalSaveData(tagCompound);
			tagCompound.putInt("height", this.height);
			tagCompound.putInt("diamonds", this.diamonds);
		}

		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}

		public boolean overwriteOres() {
			return true;
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			boolean loot = Config.STRUCTURES.HARBOUR.LOOT_CHANCE.get() > rand.nextDouble();
			if (Config.STRUCTURES.HARBOUR.LOOT_CHANCE.get() > rand.nextDouble()) {
				if (function.equals("warehouse1_1")) {
					worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
					LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(), loot ? ShrinesLootTables.HARBOUR : ShrinesLootTables.EMPTY);
				}
				if (function.equals("warehouse1_2") || function.equals("warehouse1_3")) {
					worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
					LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(1), loot ? ShrinesLootTables.HARBOUR : ShrinesLootTables.EMPTY);
					LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(2), loot ? ShrinesLootTables.HARBOUR : ShrinesLootTables.EMPTY);
					LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(3), loot ? ShrinesLootTables.HARBOUR : ShrinesLootTables.EMPTY);
				}
				if (function.equals("chest_tavern")) {
					worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
					TileEntity tileentity = worldIn.getBlockEntity(pos.below(2));
					if (tileentity instanceof LockableLootTileEntity) {
						LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(2), loot ? ShrinesLootTables.HARBOUR_TAVERN : ShrinesLootTables.EMPTY);
					} else {
						LockableLootTileEntity.setLootTable(worldIn, rand, pos.below(3), loot ? ShrinesLootTables.HARBOUR_TAVERN : ShrinesLootTables.EMPTY);
					}
				}
			}
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.HARBOUR.USE_RANDOM_VARIANTING.get();
		}

		public boolean overwriteWool() {
			return false;
		}

		protected int getHeight(ISeedReader world, BlockPos pos) {
			return this.height + 7;
		}

		protected int diamonds = 0;

		@Override
		public boolean validateBlock(BlockPos pos, BlockState newState, ISeedReader world, Random rand) {
			if (newState.getBlock() == Blocks.DIAMOND_ORE) {
				if (diamonds <= 16) {
					this.diamonds++;
					return true;
				} else {
					if (COLORS.get(newState.getBlock()) == null)
						COLORS.put(newState.getBlock(), ORES.get(rand.nextInt(ORES.size())));
					world.setBlock(pos, COLORS.get(newState.getBlock()).defaultBlockState(), 3);
					return false;
				}
			}
			return true;
		}
	}
}