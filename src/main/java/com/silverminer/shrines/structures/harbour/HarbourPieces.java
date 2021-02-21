package com.silverminer.shrines.structures.harbour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.structures.ColorStructurePiece;
import com.silverminer.shrines.structures.StructurePieceTypes;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class HarbourPieces {
	protected static final Logger LOGGER = LogManager.getLogger(HarbourPieces.class);

	protected static final ArrayList<ResourceLocation> PIECES = Lists.newArrayList(
			new ResourceLocation("shrines:harbour/complete/harbour_p1"),
			new ResourceLocation("shrines:harbour/complete/harbour_p2"),
			new ResourceLocation("shrines:harbour/complete/harbour_p3"),
			new ResourceLocation("shrines:harbour/complete/harbour_p4"),
			new ResourceLocation("shrines:harbour/complete/harbour_p5"),
			new ResourceLocation("shrines:harbour/complete/harbour_p6"),
			new ResourceLocation("shrines:harbour/complete/harbour_p7"),
			new ResourceLocation("shrines:harbour/complete/harbour_p8"),
			new ResourceLocation("shrines:harbour/complete/harbour_p9"));

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random, ChunkGenerator chunkGenerator) {
		LOGGER.info("Generating Harbour on: {}", pos);
		boolean flag = true;
		if (flag) {
			rotation = Rotation.NONE;
			int height = getStartHeigth(pos, chunkGenerator) - 6;
			height = 56;
			pos = new BlockPos(pos.getX(), height, pos.getZ());
			LOGGER.info("Generating Harbourpieces on: {}", pos);
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES.get(0),
					pos.add(new BlockPos(0, 0, 0).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES.get(1),
					pos.add(new BlockPos(47, 0, 0).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES.get(2),
					pos.add(new BlockPos(94, 0, 0).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES.get(3),
					pos.add(new BlockPos(0, 0, 47).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES.get(4),
					pos.add(new BlockPos(47, 0, 47).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES.get(5),
					pos.add(new BlockPos(94, 0, 47).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES.get(6),
					pos.add(new BlockPos(0, 0, 94).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES.get(7),
					pos.add(new BlockPos(47, 0, 94).rotate(rotation)), rotation, 0, random, height));
			pieces.add(new HarbourPieces.HarbourPiece(templateManager, PIECES.get(8),
					pos.add(new BlockPos(94, 0, 94).rotate(rotation)), rotation, 0, random, height));
		} else {
			pos = pos.up(getStartHeigth(pos, chunkGenerator));
			MutableBoundingBox mbb = MutableBoundingBox.createProper(pos.getX(), pos.getY(), pos.getZ(), pos.getX(),
					pos.getY() + 1, pos.getZ());
			ArrayList<MutableBoundingBox> parts = new ArrayList<MutableBoundingBox>();
			int i = 0;
			int maxStructures = 20 + random.nextInt(10);
			int tries = 0;
			while (i < maxStructures && tries++ < 500) {
				BlockPos nextPos = pos.add(random.nextInt(50) * (random.nextBoolean() ? -1 : 1), 0,
						random.nextInt(50) * (random.nextBoolean() ? -1 : 1));
				for (MutableBoundingBox bb : parts) {
					if (bb.isVecInside(nextPos)) {
						continue;
					}
				}
				Rotation newRot = rotation.add(Rotation.randomRotation(random));
				HarbourPieceType type = HarbourPieceType.TYPES.get(random.nextInt(HarbourPieceType.TYPES.size()));
				if (type.structureIn >= type.getMaxStructures())
					continue;
				MutableBoundingBox newMBB = type.getBoundingBox(nextPos, newRot);
				if (!checkFlatness(newMBB, chunkGenerator))
					continue;
				for (MutableBoundingBox bb : parts) {
					if (areBoundingBoxesIntersecting(newMBB, bb)) {
						continue;
					}
				}
				pieces.add(new HarbourPieces.Piece(templateManager,
						type.getPieces()[random.nextInt(type.getPieces().length)], nextPos, newRot, 0, random, newMBB));
				parts.add(newMBB);
				mbb.expandTo(newMBB);
				i++;
				type.structureIn++;
				LOGGER.info("Generating Harbourpiece [{}] on: {}; Had found {} pieces, Pieces {}", type.getName(),
						nextPos, i, parts);
			}
			LOGGER.info("Adding Ground Piece: {}", mbb);
			pieces.add(new HarbourPieces.GroundPiece(5, mbb, parts));
		}
	}

	/**
	 * Only checks x and z pos to prevent generating into each other because of
	 * Terraforming
	 * 
	 * @param mmb1        First BoundingBox
	 * @param structurebb Second BoundingBox
	 * @return true if the BoundingBoxes are intersecting
	 */
	protected static boolean areBoundingBoxesIntersecting(MutableBoundingBox mmb1, MutableBoundingBox structurebb) {
		return mmb1.maxX >= structurebb.minX && mmb1.minX <= structurebb.maxX && mmb1.maxZ >= structurebb.minZ
				&& mmb1.minZ <= structurebb.maxZ;
	}

	protected static boolean checkFlatness(MutableBoundingBox mbb, ChunkGenerator chunkGenerator) {
		int minheight = 256;
		int maxheight = 0;
		for (int x = mbb.minX; x < mbb.maxX; x++) {
			for (int z = mbb.minZ; z < mbb.maxZ; z++) {
				int height = chunkGenerator.getHeight(x / 16, z / 16, Heightmap.Type.WORLD_SURFACE_WG);
				minheight = Math.min(minheight, height);
				maxheight = Math.max(maxheight, height);
			}
		}
		return Math.abs(maxheight - minheight) <= 4;
	}

	protected static int getStartHeigth(BlockPos pos, ChunkGenerator chunkGenerator) {
		MutableBoundingBox mbb = MutableBoundingBox.createProper(pos.getX() - 50, 0, pos.getZ() - 50, pos.getX() + 50,
				0, pos.getZ() + 50);
		int heigth = 0;
		for (int x = mbb.minX; x < mbb.maxX; x++) {
			for (int z = mbb.minZ; z < mbb.maxZ; z++) {
				heigth += chunkGenerator.getHeight(x / 16, z / 16, Heightmap.Type.WORLD_SURFACE_WG);
			}
		}
		return heigth / (mbb.maxX - mbb.minX * mbb.maxZ - mbb.minZ);
	}

	public static class Piece extends ColorStructurePiece {
		protected BlockPos offsetPos = BlockPos.ZERO;

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn, Random rand, MutableBoundingBox mbb) {
			super(StructurePieceTypes.HARBOUR_HOUSE, templateManager, location, pos, rotation, componentTypeIn, true);
			this.boundingBox = mbb;
		}

		public Piece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.HARBOUR_HOUSE, templateManager, cNBT);
		}

		@Override
		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.STRUCTURE_BLOCK;
		}

		public BlockPos getOffsetPos(Random rand) {
			return this.offsetPos;
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.HARBOUR.USE_RANDOM_VARIANTING.get();
		}

		public boolean overwriteWool() {
			return false;
		}
	}

	public static class HarbourPiece extends ColorStructurePiece {
		protected BlockPos offsetPos = BlockPos.ZERO;
		protected int height;

		public HarbourPiece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation,
				int componentTypeIn, Random rand, int height) {
			super(StructurePieceTypes.HARBOUR_HOUSE, templateManager, location, pos, rotation, componentTypeIn, true);
			this.height = height;
		}

		public HarbourPiece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.HARBOUR_HOUSE, templateManager, cNBT);
		}

		public StructureProcessor getProcessor() {
			return BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK;
		}

		@Override
		protected boolean useRandomVarianting() {
			return Config.STRUCTURES.HARBOUR.USE_RANDOM_VARIANTING.get();
		}

		public boolean overwriteWool() {
			return false;
		}

		protected int getHeight(ISeedReader world, BlockPos pos) {
			return this.height;
		}

		@Override
		public boolean validateBlock(BlockPos pos, BlockState newState, ISeedReader world) {
			LOGGER.info("Validated Block on {} against height: {}", pos.getY(), this.getHeight(world, pos));
			return pos.getY() - 6 > this.getHeight(world, pos);
		}
	}

	public static class GroundPiece extends StructurePiece {
		protected ArrayList<MutableBoundingBox> parts = new ArrayList<MutableBoundingBox>();

		public GroundPiece(int componentTypeIn, MutableBoundingBox mbb, ArrayList<MutableBoundingBox> partsIn) {
			super(StructurePieceTypes.HARBOUR_GROUND, componentTypeIn);
			this.boundingBox = mbb;
			this.parts = partsIn;
		}

		public GroundPiece(TemplateManager templateManager, CompoundNBT cNBT) {
			super(StructurePieceTypes.HARBOUR_GROUND, cNBT);
		}

		@Override
		protected void readAdditional(CompoundNBT tagCompound) {
		}

		@Override
		public boolean func_230383_a_(ISeedReader world, StructureManager structureManager,
				ChunkGenerator chunkgenerator, Random rand, MutableBoundingBox mbb, ChunkPos chunkPos,
				BlockPos blockPos) {
			for (int x = boundingBox.minX; x < boundingBox.maxX; x++) {
				for (int z = boundingBox.minZ; z < boundingBox.maxZ; z++) {
					BlockPos pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, new BlockPos(x, 0, z)).down();
					for (MutableBoundingBox bb : this.parts) {
						if (bb.isVecInside(pos))
							continue;
					}
					if (!world.getBlockState(pos).getMaterial().isLiquid()) {
						BlockState state = Blocks.STONE_BRICKS.getDefaultState();
						if (rand.nextInt(8) == 0)
							state = Blocks.MOSSY_STONE_BRICKS.getDefaultState();
						else if (rand.nextInt(16) == 0)
							state = Blocks.CRACKED_STONE_BRICKS.getDefaultState();
						else if (rand.nextInt(32) == 0)
							state = Blocks.POLISHED_BLACKSTONE_BRICKS.getDefaultState();
						else if (rand.nextInt(512) == 0)
							state = Blocks.WATER.getDefaultState();
						else if (rand.nextInt(256) == 0)
							state = Blocks.STONE_BRICK_STAIRS.getDefaultState().with(StairsBlock.WATERLOGGED, true)
									.with(StairsBlock.FACING, Direction.Plane.HORIZONTAL.random(rand));
						world.setBlockState(pos, state, 2);
					}
				}
			}
			return true;
		}
	}
}