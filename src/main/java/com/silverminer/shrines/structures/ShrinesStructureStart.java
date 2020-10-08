package com.silverminer.shrines.structures;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructureStart;

public abstract class ShrinesStructureStart<C extends IFeatureConfig> extends StructureStart<C> {
	protected static final Logger LOGGER = LogManager.getLogger(ShrinesStructureStart.class);

	public ShrinesStructureStart(Structure<C> structure, int chunkX, int chunkZ, MutableBoundingBox boundingbox,
			int p_i225874_5_, long seed) {
		super(structure, chunkX, chunkZ, boundingbox, p_i225874_5_, seed);
	}

	@Override
	public void func_230366_a_(final ISeedReader seedReader, final StructureManager manager,
			final ChunkGenerator generator, final Random random, final MutableBoundingBox boundingBox,
			final ChunkPos position) {
		super.func_230366_a_(seedReader, manager, generator, random, boundingBox, position);

		for (int x = this.bounds.minX - 1; this.bounds.maxX + 1 >= x; ++x) {
			for (int z = this.bounds.minZ - 1; this.bounds.maxZ + 1 >= z; ++z) {
				for (int y = this.bounds.minY - 1; y > 1; --y) {
					BlockPos pos = new BlockPos(x, y, z);
					if (shouldBlockBeReplaced(seedReader, pos)) {
						replaceBlock(seedReader, pos);
					} else {
						break;
					}
				}
			}
		}
	}

	private boolean shouldBlockBeReplaced(ISeedReader seedReader, BlockPos pos) {
		return seedReader.isAirBlock(pos) || seedReader.getBlockState(pos).getMaterial().isLiquid()
				|| seedReader.getBlockState(pos).getMaterial().isReplaceable();
	}

	protected BlockState getTopLayerBlock(ISeedReader seedReader, BlockPos pos) {
		return seedReader.getBiome(pos).func_242440_e().func_242500_d().get().config.getTop();
	}

	protected BlockState getLowerLayerBlock(ISeedReader seedReader, BlockPos pos) {
		return seedReader.getBiome(pos).func_242440_e().func_242500_d().get().config.getUnder();
	}

	private void replaceBlock(ISeedReader seedReader, BlockPos pos) {
		BlockState state = getTopLayerBlock(seedReader, pos);

		Block downBlock = seedReader.getBlockState(pos.down()).getBlock();
		// Wenn unter dem Block keine Erde und kein Grass ist und der Block darunter
		// Stein ist oder die Drei darüber keine Luft dann nutze das untere Material
		if ((downBlock != Blocks.DIRT && downBlock != Blocks.GRASS_BLOCK)
				&& (downBlock == Blocks.STONE || !seedReader.isAirBlock(pos.up(1)) && !seedReader.isAirBlock(pos.up(2))
						&& !seedReader.isAirBlock(pos.up(3)))) {
			state = getLowerLayerBlock(seedReader, pos);
		}

		// Wenn über dem jetzigem Block Grass ist dann nutze hier statt Grass, Erde
		Block upBlock = seedReader.getBlockState(pos.up()).getBlock();
		if ((upBlock == Blocks.GRASS_BLOCK || upBlock == Blocks.GRASS_PATH)
				&& (state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.GRASS_PATH)) {
			state = Blocks.DIRT.getDefaultState();
		}

		seedReader.setBlockState(pos, state, 2);
	}
}