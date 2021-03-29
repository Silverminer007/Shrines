package com.silverminer.shrines.structures;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.config.StructureConfig.StructureGenConfig;
import com.silverminer.shrines.init.StructureInit;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

public abstract class AbstractStructure<C extends IFeatureConfig> extends Structure<C> {
	protected static final Logger LOGGER = LogManager.getLogger(AbstractStructure.class);
	public final int size;

	public final String name;

	public AbstractStructure(Codec<C> codec, int sizeIn, String nameIn) {
		super(codec);
		this.size = sizeIn;
		this.name = nameIn;
	}

	@Override
	public String getStructureName() {
		return new ResourceLocation(Shrines.MODID, this.name).toString();
	}

	protected boolean isSurfaceFlat(@Nonnull ChunkGenerator generator, int chunkX, int chunkZ) {
		// Size of the area to check.
		int offset = this.getSize() * 16;

		int xStart = (chunkX << 4);
		int zStart = (chunkZ << 4);

		int i1 = generator.getHeight(xStart, zStart, Heightmap.Type.WORLD_SURFACE_WG);
		int j1 = generator.getHeight(xStart, zStart + offset, Heightmap.Type.WORLD_SURFACE_WG);
		int k1 = generator.getHeight(xStart + offset, zStart, Heightmap.Type.WORLD_SURFACE_WG);
		int l1 = generator.getHeight(xStart + offset, zStart + offset, Heightmap.Type.WORLD_SURFACE_WG);
		int minHeight = Math.min(Math.min(i1, j1), Math.min(k1, l1));
		int maxHeight = Math.max(Math.max(i1, j1), Math.max(k1, l1));

		return Math.abs(maxHeight - minHeight) <= 4;
	}

	public int getSize() {
		return this.size;
	}

	public int getDistance() {
		return this.getConfig().DISTANCE.get();
	}

	public int getSeparation() {
		return this.getConfig().SEPARATION.get();
	}

	public int getSeedModifier() {
		return this.getConfig().SEED.get();
	}

	public double getSpawnChance() {
		return this.getConfig().SPAWN_CHANCE.get();
	}

	public boolean needsGround() {
		return this.getConfig().NEEDS_GROUND.get();
	}

	public abstract StructureGenConfig getConfig();

	@Override
	protected boolean func_230363_a_(ChunkGenerator generator, BiomeProvider provider, long seed, SharedSeedRandom rand,
			int chunkX, int chunkZ, Biome biome, ChunkPos pos, IFeatureConfig config) {
		return this.validateGeneration(generator, provider, seed, rand, chunkX, chunkZ, biome, pos, config);
	}

	public boolean validateGeneration(ChunkGenerator generator, BiomeProvider provider, long seed,
			SharedSeedRandom rand, int chunkX, int chunkZ, Biome biome, ChunkPos pos, IFeatureConfig config,
			@Nullable Structure<?>... exeptStructure) {
		if (isSurfaceFlat(generator, chunkX, chunkZ)) {

			// Check the entire size of the structure to see if it's all a viable biome:
			for (Biome biome1 : provider.getBiomes(chunkX * 16 + 9, generator.getGroundHeight(), chunkZ * 16 + 9,
					getSize() * 16)) {
				if (!biome1.getGenerationSettings().hasStructure(this)) {
					return false;
				}
			}

			int i = chunkX >> 4;
			int j = chunkZ >> 4;
			rand.setSeed((long) (i ^ j << 4) ^ seed);
			return rand.nextDouble() < getSpawnChance();
//					&& checkForOtherStructures(generator, provider, seed, rand,chunkX, chunkZ, biome, pos, config, exeptStructure);
		}

		return false;
	}

	protected boolean checkForOtherStructures(ChunkGenerator generator, BiomeProvider provider, long seed,
			SharedSeedRandom rand, int chunkX, int chunkZ, Biome biome, ChunkPos pos, IFeatureConfig config,
			@Nullable Structure<?>... exeptStructure) {
		for (AbstractStructure<?> s : StructureInit.STRUCTURES_LIST) {
			if (exeptStructure != null)
				for (Structure<?> es : exeptStructure) {
					if (es.equals(s))
						continue;
				}
			if (new ChunkPos(chunkX, chunkZ).equals(s.getChunkPosForStructure(
					new StructureSeparationSettings(s.getDistance(), s.getSeparation(), s.getSeedModifier()), seed,
					rand, chunkX, chunkZ))
					&& s.validateGeneration(generator, provider, seed, rand, chunkX, chunkZ, biome, pos, config,
							Lists.asList(s, exeptStructure).toArray(new Structure<?>[exeptStructure.length]))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ChunkPos getChunkPosForStructure(StructureSeparationSettings settings, long seed,
			SharedSeedRandom sharedSeedRand, int x, int z) {
		int spacing = this.getDistance();
		int separation = this.getSeparation();

		int k = Math.floorDiv(x, spacing);
		int l = Math.floorDiv(z, spacing);

		sharedSeedRand.setLargeFeatureSeedWithSalt(seed, k, l, this.getSeedModifier());

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

	public boolean isEndStructure() {
		return false;
	}

	public boolean isNetherStructure() {
		return false;
	}
}