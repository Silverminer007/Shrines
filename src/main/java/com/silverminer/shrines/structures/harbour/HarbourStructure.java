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
package com.silverminer.shrines.structures.harbour;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.ConfigBuilder;
import com.silverminer.shrines.config.ConfigBuilder.Type;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.StructurePools;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;

public class HarbourStructure extends AbstractStructure {
	public static final String NAME = "harbour";
	protected static final ConfigBuilder HARBOUR_CONFIG = new ConfigBuilder("Harbour", 651398043, Type.HARBOUR)
			.setDistance(50).setSeparation(8)
			.setBiomes(Biome.Category.PLAINS, Biome.Category.FOREST, Biome.Category.TAIGA, Biome.Category.SAVANNA,
					Biome.Category.JUNGLE, Biome.Category.MESA, Biome.Category.ICY, Biome.Category.SWAMP,
					Biome.Category.MUSHROOM)
			.setNeedsGround(false);

	public HarbourStructure(Codec<VillageConfig> codec) {
		super(codec, NAME, HARBOUR_CONFIG);
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}


	@Override
	public JigsawPattern getPools() {
		return StructurePools.BALLOON;
	}

	protected boolean isSurfaceFlatExtended(@Nonnull ChunkGenerator generator, int chunkX, int chunkZ) {
		int xStart = (chunkX << 4);
		int zStart = (chunkZ << 4);
		MutableBoundingBox mbb = MutableBoundingBox.createProper(xStart, 0, zStart, xStart + 100, 0, zStart + 100);
		int minheight = 256;
		int maxheight = 0;
		for (int x = mbb.x0; x < mbb.x1; x++) {
			for (int z = mbb.z0; z < mbb.z1; z++) {
				int height = generator.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
				minheight = Math.min(minheight, height);
				maxheight = Math.max(maxheight, height);
			}
		}
		return (maxheight - minheight) <= 8;
	}

	public boolean validateGeneration(ChunkGenerator generator, BiomeProvider provider, long seed,
			SharedSeedRandom rand, int chunkX, int chunkZ, Biome biome, ChunkPos pos, IFeatureConfig config,
			@Nullable Structure<?>... exeptStructure) {
		if (super.validateGeneration(generator, provider, seed, rand, chunkX, chunkZ, biome, pos, config,
				exeptStructure))
			return this.isSurfaceFlatExtended(generator, chunkX, chunkZ);

		return false;
	}
}