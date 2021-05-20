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
package com.silverminer.shrines.core.utils;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
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
	public static int getAverageHeight(BlockPos position, ChunkGenerator cG, int size) {
		int xStart = position.getX() - size * 8;
		int zStart = position.getZ() - size * 8;
		int xEnd = xStart + size * 16;
		int zEnd = zStart + size * 16;
		ArrayList<Integer> heigths = new ArrayList<Integer>();
		for (int x = xStart; x < xEnd; x++) {
			for (int z = zStart; z < zEnd; z++) {
				int surface = cG.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
				boolean water = cG.getBaseColumn(x / 16, z / 16)
						.getBlockState(new BlockPos(x, surface, z)).getBlock() == Blocks.WATER;
				if (water) {
					LOGGER.info("Water!");
				}
				heigths.add(water ? cG.getSeaLevel() : surface - 1);
			}
		}
		int max = 0;
		int min = Integer.MAX_VALUE;
		for(int i : heigths) {
			if(i > max) {
				max = i;
			}
			if(i < min) {
				min = i;
			}
		}
		LOGGER.info("Min {}, Max {}, Count {}!", min, max, heigths.size());
		return getAverage(heigths) + 1;
	}

	public static int getAverage(ArrayList<Integer> list) {
		double summe = 0.0;

		for (int index = 0; index < list.size(); index++) {
			summe = summe + list.get(index);
		}

		if (list.size() > 0)
			return (int) (summe / list.size());
		else
			return 0;

	}
}