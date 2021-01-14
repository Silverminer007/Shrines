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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class HarbourPieces {
	protected static final Logger LOGGER = LogManager.getLogger(HarbourPieces.class);
	private static final ArrayList<ResourceLocation> warehouse_locations = Lists.newArrayList(
			new ResourceLocation("shrines:harbour/warehouse1"), new ResourceLocation("shrines:harbour/warehouse2"),
			new ResourceLocation("shrines:harbour/warehouse3"));

	private static final ArrayList<ResourceLocation> house_locations = Lists.newArrayList(
			new ResourceLocation("shrines:harbour/harbourhouse1"),
			new ResourceLocation("shrines:harbour/harbourhouse2"));

	private static final ArrayList<ResourceLocation> ware_locations = Lists
			.newArrayList(new ResourceLocation("shrines:harbour/ware1"));

	private static final ResourceLocation crane_location = new ResourceLocation("shrines:harbour/crane");

	public static void generate(TemplateManager templateManager, BlockPos pos, Rotation rotation,
			List<StructurePiece> pieces, Random random, ChunkGenerator chunkGenerator) {
		LOGGER.info("Generating Harbour on: {}", pos);
		pos = pos.up(chunkGenerator.getHeight(pos.getX() / 16, pos.getZ() / 16, Heightmap.Type.WORLD_SURFACE_WG));
		MutableBoundingBox mbb = MutableBoundingBox.createProper(pos.getX(), pos.getY(), pos.getZ(), pos.getX(),
				pos.getY() + 1, pos.getZ());
		ArrayList<MutableBoundingBox> parts = new ArrayList<MutableBoundingBox>();
		BlockPos nextWater = chunkGenerator.getBiomeProvider().findBiomePosition(pos.getX(), pos.getY(), pos.getZ(),
				100, 3, biomeTest -> {
					return biomeTest.getCategory() == Biome.Category.OCEAN
							|| biomeTest.getCategory() == Biome.Category.RIVER;
				}, random, true);
		if (nextWater == null)
			nextWater = pos;
		int i = 0;
		int maxStructures = 20 + random.nextInt(10);
		int tries = 0;
		int houses = 0;
		int wares = 0;
		int warehouses = 0;
		int cranes = 0;
		LOGGER.info("Generating Harbour on Water pos: {}", nextWater);
		while (i < maxStructures && tries++ < 500) {
			BlockPos nextPos = pos.add(random.nextInt(50) * (random.nextBoolean() ? -1 : 1), 0,
					random.nextInt(50) * (random.nextBoolean() ? -1 : 1));
			for (MutableBoundingBox bb : parts) {
				if (bb.isVecInside(nextPos)) {
					continue;
				}
			}
			Rotation newRot = rotation.add(Rotation.randomRotation(random));
			BlockPos houseSize = new BlockPos(20, 15, 15).rotate(newRot);
			BlockPos warehouseSize = new BlockPos(20, 15, 20).rotate(newRot);
			BlockPos craneSize = new BlockPos(15, 10, 15).rotate(newRot);
			BlockPos wareSize = new BlockPos(7, 5, 7).rotate(newRot);
			double distance = Math.pow(nextWater.distanceSq(nextPos), 0.5);
			LOGGER.info("Generating Harbour on: {}; Had found {} pieces; Distance {}", nextPos, i, distance);
			if (random.nextInt(300) < distance && houses < 4) {
				MutableBoundingBox mbb2 = MutableBoundingBox.createProper(nextPos.getX(), nextPos.getY(),
						nextPos.getZ(), nextPos.getX() + houseSize.getX(), nextPos.getY() + houseSize.getY(),
						nextPos.getZ() + houseSize.getZ());
				for (MutableBoundingBox bb : parts) {
					if (mbb2.intersectsWith(bb))
						continue;
				}
				pieces.add(new HarbourPieces.Piece(templateManager,
						house_locations.get(random.nextInt(house_locations.size())), nextPos, newRot, 0, random, mbb2));
				parts.add(mbb2);
				mbb.expandTo(mbb2);
				i++;
				houses++;
			} else if (random.nextInt(200) < distance && warehouses < 5) {
				MutableBoundingBox mbb2 = MutableBoundingBox.createProper(nextPos.getX(), nextPos.getY(),
						nextPos.getZ(), nextPos.getX() + warehouseSize.getX(), nextPos.getY() + warehouseSize.getY(),
						nextPos.getZ() + warehouseSize.getZ());
				for (MutableBoundingBox bb : parts) {
					if (mbb2.intersectsWith(bb))
						continue;
				}
				pieces.add(new HarbourPieces.Piece(templateManager,
						warehouse_locations.get(random.nextInt(warehouse_locations.size())), nextPos, newRot, 0, random,
						mbb2));
				parts.add(mbb2);
				mbb.expandTo(mbb2);
				i++;
				warehouses++;
			} else if (random.nextInt(100) < distance && cranes < 2) {
				MutableBoundingBox mbb2 = MutableBoundingBox.createProper(nextPos.getX(), nextPos.getY(),
						nextPos.getZ(), nextPos.getX() + craneSize.getX(), nextPos.getY() + craneSize.getY(),
						nextPos.getZ() + craneSize.getZ());
				for (MutableBoundingBox bb : parts) {
					if (mbb2.intersectsWith(bb))
						continue;
				}
				pieces.add(new HarbourPieces.Piece(templateManager, crane_location, nextPos, newRot, 0, random, mbb2));
				parts.add(mbb2);
				mbb.expandTo(mbb2);
				i++;
				cranes++;
			} else if (random.nextInt(200) < distance && wares < 4) {
				MutableBoundingBox mbb2 = MutableBoundingBox.createProper(nextPos.getX(), nextPos.getY(),
						nextPos.getZ(), nextPos.getX() + wareSize.getX(), nextPos.getY() + wareSize.getY(),
						nextPos.getZ() + wareSize.getZ());
				for (MutableBoundingBox bb : parts) {
					if (mbb2.intersectsWith(bb))
						continue;
				}
				pieces.add(new HarbourPieces.Piece(templateManager,
						ware_locations.get(random.nextInt(ware_locations.size())), nextPos, newRot, 0, random, mbb2));
				parts.add(mbb2);
				mbb.expandTo(mbb2);
				i++;
				wares++;
			}
		}
		LOGGER.info("Adding Ground Piece: {}", mbb);
		pieces.add(new HarbourPieces.GroundPiece(5, mbb, parts));
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
						world.setBlockState(pos, state, 2);
					}
				}
			}
			return true;
		}
	}
}