package com.silverminer.shrines.structures;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;

public abstract class ShrinesStructureStart<C extends IFeatureConfig> extends StructureStart<C> {

	public ShrinesStructureStart(Structure<C> structure, int chunkX, int chunkZ,
			MutableBoundingBox boundingbox, int p_i225874_5_, long seed) {
		super(structure, chunkX, chunkZ, boundingbox, p_i225874_5_, seed);
	}

	@Override
	public void func_230366_a_(ISeedReader seedReader, StructureManager manager, ChunkGenerator generator,
			Random random, MutableBoundingBox boundingBox, ChunkPos pos) {
		super.func_230366_a_(seedReader, manager, generator, random, boundingBox, pos);
		int i = this.bounds.minY;

		for (int j = boundingBox.minX; j <= boundingBox.maxX; ++j) {
			for (int k = boundingBox.minZ; k <= boundingBox.maxZ; ++k) {
				BlockPos blockPos = new BlockPos(j, i, k);
				if (!seedReader.isAirBlock(blockPos) && this.bounds.isVecInside(blockPos)) {
					boolean flag = false;

					for (StructurePiece structurepiece : this.components) {
						if (structurepiece.getBoundingBox().isVecInside(blockPos)) {
							flag = true;
							break;
						}
					}

					if (flag) {
						for (int l = i - 1; l > 1; --l) {
							BlockPos blockPos1 = new BlockPos(j, l, k);
							if (shouldBlockBeReplaced(seedReader, blockPos1)) {
								replaceBlock(seedReader, blockPos1);
							}

							for (Direction direction : Direction.values()) {
								BlockPos blockPos2 = blockPos1.offset(direction);

								if (shouldBlockBeReplaced(seedReader, blockPos2)
										&& direction.getAxis() != Direction.Axis.Y) {
									replaceBlock(seedReader, blockPos2);
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean shouldBlockBeReplaced(ISeedReader seedReader, BlockPos pos) {
		return seedReader.isAirBlock(pos) || seedReader.getBlockState(pos).getMaterial().isLiquid()
				|| seedReader.getBlockState(pos).getMaterial().isReplaceable();
	}

	protected BlockState getTopLayerBlock() {
		return Blocks.DIRT.getDefaultState();
	}

	protected BlockState getLowerLayerBlock() {
		return Blocks.STONE.getDefaultState();
	}

	private void replaceBlock(ISeedReader seedReader, BlockPos pos) {
		BlockState state = getTopLayerBlock();

		if (getTopLayerBlock().getBlock() == Blocks.DIRT && seedReader.isAirBlock(pos.up())) {
			state = Blocks.GRASS_BLOCK.getDefaultState();
		}

		if ((seedReader.getBlockState(pos.down()).getBlock() != Blocks.DIRT
				&& seedReader.getBlockState(pos.down()).getBlock() != Blocks.GRASS_BLOCK)
				&& (seedReader.getBlockState(pos.up()).getBlock() == Blocks.STONE || !seedReader.isAirBlock(pos.up())
						&& !seedReader.isAirBlock(pos.up(2)) && !seedReader.isAirBlock(pos.up(3)))) {
			state = getLowerLayerBlock();
		}

		seedReader.setBlockState(pos, state, 2);
	}
}