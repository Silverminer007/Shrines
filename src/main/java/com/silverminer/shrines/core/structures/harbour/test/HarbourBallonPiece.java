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

import com.google.common.collect.Lists;
import com.silverminer.shrines.core.loot_tables.ShrinesLootTables;
import com.silverminer.shrines.core.structures.ColorStructurePiece;
import com.silverminer.shrines.core.structures.StructurePieceTypes;
import com.silverminer.shrines.core.utils.StructureUtils;
import com.silverminer.shrines.forge.config.Config;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class HarbourBallonPiece {
	private static final ArrayList<ResourceLocation> location = Lists.newArrayList(
			new ResourceLocation("shrines:ballon/ballon_1"), new ResourceLocation("shrines:ballon/ballon_2"),
			new ResourceLocation("shrines:ballon/ballon_3"), new ResourceLocation("shrines:ballon/ballon_4"),
			new ResourceLocation("shrines:ballon/ballon_5"), new ResourceLocation("shrines:ballon/ballon_6"),
			new ResourceLocation("shrines:ballon/ballon_7"), new ResourceLocation("shrines:ballon/ballon2_1"),
			new ResourceLocation("shrines:ballon/ballon2_2"), new ResourceLocation("shrines:ballon/ballon2_3"),
			new ResourceLocation("shrines:ballon/ballon2_4"));

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random, ChunkGenerator chunkGenerator) {
		int height = StructureUtils.getAverageHeight(pos.offset(-24, 0, -24), chunkGenerator, 3);
		boolean flag = false;
		if (flag)
			pieces.add(new HarbourBallonPiece.Piece(templateManager, location.get(random.nextInt(location.size())), pos,
					rotation, 0, random, height));
		else
			// Test function for single variant
			pieces.add(
					new HarbourBallonPiece.Piece(templateManager, location.get(0), pos, rotation, 0, random, height));
	}

	public static class Piece extends ColorStructurePiece {

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn, Random rand, int height) {
			super(StructurePieceTypes.BALLON, templateManager, location, pos, rotation, componentTypeIn, true, height);
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.BALLON, templateManager, cNBT);
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
			if (Config.STRUCTURES.BALLON.LOOT_CHANCE.get() > rand.nextDouble()) {
				if (function.equals("chest")) {
					worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
					TileEntity tileentity = worldIn.getBlockEntity(pos.above(2));
					if (tileentity instanceof BarrelTileEntity) {
						((BarrelTileEntity) tileentity).setLootTable(ShrinesLootTables.BALLON, rand.nextLong());
					}
				}
			}
		}

		@Override
		public boolean postProcess(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGen,
				Random rand, MutableBoundingBox mbb, ChunkPos chunkPos, BlockPos pos) {
			BlockPos ecke = new BlockPos(0, 0, 0);
			for (int i = 1; i < 100; i = (int) Math.ceil(i * 1.25)) {
				for (int s = 1; s >= -1; s = s - 2) {
					ecke = pos.offset(i * s, 0, i * s);
					for (int x = 0; x <= (Math.abs(i * s * 2)); x = x + 1) {
						BlockPos position = new BlockPos(ecke.getX() + (x * -s), 120, ecke.getZ());
						if (s == 1)
							world.setBlock(position, Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
						else
							world.setBlock(position, Blocks.GOLD_BLOCK.defaultBlockState(), 3);
					}
					for (int z = 0; z <= (Math.abs(i * s * 2)); z = z + 1) {
						BlockPos position = new BlockPos(ecke.getX(), 120, ecke.getZ() + (z * -s));
						if (s == 1)
							world.setBlock(position, Blocks.NETHERITE_BLOCK.defaultBlockState(), 3);
						else
							world.setBlock(position, Blocks.IRON_BLOCK.defaultBlockState(), 3);
					}
				}
			}
			return true;
		}
	}
}