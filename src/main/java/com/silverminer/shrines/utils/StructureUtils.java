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
package com.silverminer.shrines.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.silverminer.shrines.config.IStructureConfig;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;

/**
 * @author Silverminer
 *
 */
public class StructureUtils {
	protected static final Logger LOGGER = LogManager.getLogger(StructureUtils.class);

	/**
	 * 
	 * @param position the position to start. It's the middle point
	 * @param cG       An chunkGenerator instance to read world height
	 * @param size     the size to check in chunks
	 * @return
	 */
	private static int getAverageHeight(BlockPos position, ChunkGenerator cG, int size) {
		int xStart = position.getX() - size * 8;
		int zStart = position.getZ() - size * 8;
		int xEnd = xStart + size * 16;
		int zEnd = zStart + size * 16;
		ArrayList<Integer> heigths = new ArrayList<Integer>();
		for (int x = xStart; x < xEnd; x++) {
			for (int z = zStart; z < zEnd; z++) {
				int surface = cG.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
				boolean water = cG.getBaseColumn(x / 16, z / 16).getBlockState(new BlockPos(x, surface, z))
						.getBlock() == Blocks.WATER;
				heigths.add(water ? cG.getSeaLevel() : surface - 1);
			}
		}
		int max = 0;
		int min = Integer.MAX_VALUE;
		for (int i : heigths) {
			if (i > max) {
				max = i;
			}
			if (i < min) {
				min = i;
			}
		}
		LOGGER.info("Min {}, Max {}, Count {}!", min, max, heigths.size());
		return Math.min(getAverage(heigths) + 1, getMinHeight(position, cG, size) + 2);
	}

	private static int getAverage(ArrayList<Integer> list) {
		double summe = 0.0;

		for (int index = 0; index < list.size(); index++) {
			summe = summe + list.get(index);
		}

		if (list.size() > 0)
			return (int) (summe / list.size());
		else
			return 0;

	}

	/**
	 * 
	 * @param position the position to start. It's the middle point
	 * @param cG       An chunkGenerator instance to read world height
	 * @param size     the size to check in chunks
	 * @return
	 */
	public static int getMinHeight(BlockPos position, ChunkGenerator cG, int size) {
		int xStart = position.getX() - size * 8;
		int zStart = position.getZ() - size * 8;
		int xEnd = xStart + size * 16;
		int zEnd = zStart + size * 16;
		int min = 256;
		for (int x = xStart; x < xEnd; x++) {
			for (int z = zStart; z < zEnd; z++) {
				int surface = cG.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
				boolean water = cG.getBaseColumn(x / 16, z / 16).getBlockState(new BlockPos(x, surface, z))
						.getBlock() == Blocks.WATER;
				int nHeight = water ? cG.getSeaLevel() : surface - 1;
				if (nHeight < min) {
					min = nHeight;
				}
			}
		}
		return min;
	}

	public static int getHeight(ChunkGenerator cG, BlockPos pos, MutableBoundingBox mbb, Random rand) {
		Category biome = cG.getBiomeSource().getNoiseBiome(pos.getX(), pos.getY(), pos.getZ()).getBiomeCategory();
		if (biome == Category.NETHER) {
			int i = randomIntInclusive(rand, 32, 100);
			List<BlockPos> list1 = ImmutableList.of(new BlockPos(mbb.x0, 0, mbb.z0), new BlockPos(mbb.x1, 0, mbb.z0),
					new BlockPos(mbb.x0, 0, mbb.z1), new BlockPos(mbb.x1, 0, mbb.z1));
			List<IBlockReader> list = list1.stream().map((pos1) -> {
				return cG.getBaseColumn(pos1.getX(), pos1.getZ());
			}).collect(Collectors.toList());
			Heightmap.Type heightmap$type = Heightmap.Type.WORLD_SURFACE_WG;
			BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

			int k;
			for (k = i; k > 15; --k) {
				int l = 0;
				blockpos$mutable.set(0, k, 0);

				for (IBlockReader iblockreader : list) {
					BlockState blockstate = iblockreader.getBlockState(blockpos$mutable);
					if (blockstate != null && heightmap$type.isOpaque().test(blockstate)) {
						++l;
						if (l == 3) {
							return k;
						}
					}
				}
			}
			return k;
		} else if (biome == Category.THEEND) {
			int i1 = cG.getFirstOccupiedHeight(mbb.x0, mbb.z0, Heightmap.Type.WORLD_SURFACE_WG);
			int j1 = cG.getFirstOccupiedHeight(mbb.x0, mbb.z1, Heightmap.Type.WORLD_SURFACE_WG);
			int k1 = cG.getFirstOccupiedHeight(mbb.x1, mbb.z0, Heightmap.Type.WORLD_SURFACE_WG);
			int l1 = cG.getFirstOccupiedHeight(mbb.x1, mbb.z1, Heightmap.Type.WORLD_SURFACE_WG);
			return Math.min(Math.min(i1, j1), Math.min(k1, l1));
		} else {
			return getAverageHeight(pos, cG, Math.max(mbb.getXSpan(), Math.max(mbb.getYSpan(), mbb.getZSpan())) / 16);
		}
	}

	private static int randomIntInclusive(Random p_236335_0_, int p_236335_1_, int p_236335_2_) {
		return p_236335_0_.nextInt(p_236335_2_ - p_236335_1_ + 1) + p_236335_1_;
	}

	public static IStructureConfig getConfigOf(String structure, boolean onServer) {
		for(AbstractStructure st : NewStructureInit.STRUCTURES.values()) {
			if(st.getConfig().getName().equals(structure)) {
				return st.getConfig();
			}
		}
		return Utils.getData(structure, onServer);
	}
}