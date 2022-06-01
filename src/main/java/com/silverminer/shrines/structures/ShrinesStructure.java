package com.silverminer.shrines.structures;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.Shrines;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

public abstract class ShrinesStructure<C extends IFeatureConfig> extends Structure<C> {
	protected static final Logger LOGGER = LogManager.getLogger(ShrinesStructure.class);
	public final int size;

	public final String name;

	public ShrinesStructure(Codec<C> codec, int sizeIn, String nameIn) {
		super(codec);
		this.size = sizeIn;
		this.name = nameIn;
	}

	@Override
	public String getStructureName() {
		return new ResourceLocation(Shrines.MODID, this.name).toString();
	}

	@Override
	public GenerationStage.Decoration func_236396_f_() {
		return this.getDecorationStage();
	}

	public abstract GenerationStage.Decoration getDecorationStage();

	protected boolean isSurfaceFlat(@Nonnull ChunkGenerator generator, int chunkX, int chunkZ) {
		// Size of the area to check.
		int offset = this.getSize() * 16;

		int xStart = (chunkX << 4);
		int zStart = (chunkZ << 4);

		int i1 = generator.func_222531_c(xStart, zStart, Heightmap.Type.WORLD_SURFACE_WG);
		int j1 = generator.func_222531_c(xStart, zStart + offset, Heightmap.Type.WORLD_SURFACE_WG);
		int k1 = generator.func_222531_c(xStart + offset, zStart, Heightmap.Type.WORLD_SURFACE_WG);
		int l1 = generator.func_222531_c(xStart + offset, zStart + offset, Heightmap.Type.WORLD_SURFACE_WG);
		int minHeight = Math.min(Math.min(i1, j1), Math.min(k1, l1));
		int maxHeight = Math.max(Math.max(i1, j1), Math.max(k1, l1));

		return Math.abs(maxHeight - minHeight) <= 4;
	}

	public int getSize() {
		return this.size;
	}

	public abstract int getDistance();

	public abstract int getSeparation();

	public abstract int getSeedModifier();

	public abstract double getSpawnChance();

	@Override
	protected boolean func_230363_a_(ChunkGenerator generator, BiomeProvider provider, long seed, SharedSeedRandom rand,
			int chunkX, int chunkZ, Biome biome, ChunkPos pos, IFeatureConfig config) {
		if (isSurfaceFlat(generator, chunkX, chunkZ)) {

			// Check the entire size of the structure to see if it's all a viable biome:
			for (Biome biome1 : provider.getBiomes(chunkX * 16 + 9, generator.func_230356_f_(), chunkZ * 16 + 9,
					getSize() * 16)) {
				if (!biome1.func_242440_e().func_242493_a(this)) {
					return false;
				}
				if (biome1.getCategory() == Category.NETHER || biome1.getCategory() == Category.THEEND
						|| biome1.getCategory() == Category.OCEAN || biome1.getCategory() == Category.RIVER) {
					return false;
				}
			}

			int i = chunkX >> 4;
			int j = chunkZ >> 4;
			rand.setSeed((long) (i ^ j << 4) ^ seed);
			rand.nextInt();
			return rand.nextDouble() < getSpawnChance();
		}

		return false;
	}

	@Override
	public ChunkPos func_236392_a_(StructureSeparationSettings settings, long seed, SharedSeedRandom sharedSeedRand,
			int x, int z) {
		int spacing = this.getDistance();
		int separation = this.getSeparation();

		int k = Math.floorDiv(x, spacing);
		int l = Math.floorDiv(z, spacing);

		sharedSeedRand.setLargeFeatureSeedWithSalt(seed, k, l, getSeedModifier());

		int i1;
		int j1;
		if (this.func_230365_b_()) {
			i1 = sharedSeedRand.nextInt(spacing - separation);
			j1 = sharedSeedRand.nextInt(spacing - separation);
		} else {
			i1 = (sharedSeedRand.nextInt(spacing - separation) + sharedSeedRand.nextInt(spacing - separation)) / 2;
			j1 = (sharedSeedRand.nextInt(spacing - separation) + sharedSeedRand.nextInt(spacing - separation)) / 2;
		}

		return new ChunkPos(k * spacing + i1, l * spacing + j1);
	}
}