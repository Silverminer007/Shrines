package com.silverminer.shrines.structures;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.SectionPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

public abstract class ShrinesStructure<C extends IFeatureConfig> extends Structure<C> {
	public final int size;

	public final String name;

	public ShrinesStructure(Codec<C> codec, int sizeIn, String nameIn) {
		super(codec);
		this.size = sizeIn;
		this.name = nameIn;
		Structure.field_236365_a_.put(this.getStructureName(), this);
	}

	@Override
	public String getStructureName() {
		return this.name;
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
			}

			int i = chunkX >> 4;
			int j = chunkZ >> 4;
			rand.setSeed((long) (i ^ j << 4) ^ seed);
			rand.nextInt();
			return rand.nextDouble() < getSpawnChance();
		}

		return false;
	}

	public static BlockPos getSurfaceStructurePosition(@Nonnull ChunkGenerator generator, int size, Rotation rotation,
			int chunkX, int chunkZ) {
		int xOffset = size * 16;
		int zOffset = size * 16;

		int x = (chunkX << 4);
		int z = (chunkZ << 4);

		return new BlockPos(x + (xOffset / 2), 0, z + (zOffset / 2));
	}

	public ChunkPos seperate(StructureSeparationSettings settings, long seed, SharedSeedRandom sharedSeedRand, int x,
			int z) {
		int spacing = this.getDistance();
		int gridX = ((x / spacing) * spacing);
		int gridZ = ((z / spacing) * spacing);

		int offset = this.getSeparation() + 1;
		sharedSeedRand.setLargeFeatureSeedWithSalt(seed, gridX, gridZ, this.getSeedModifier());
		int offsetX = sharedSeedRand.nextInt(offset);
		int offsetZ = sharedSeedRand.nextInt(offset);

		int gridOffsetX = gridX + offsetX;
		int gridOffsetZ = gridZ + offsetZ;

		return new ChunkPos(gridOffsetX, gridOffsetZ);
	}

	@Nullable
	@Override
	public BlockPos func_236388_a_(IWorldReader world, StructureManager structureManager, BlockPos startPos,
			int searchRadius, boolean skipExistingChunks, long seed, StructureSeparationSettings settings) {
		settings = new StructureSeparationSettings(this.getDistance(), this.getSeparation(), this.getSeedModifier());
		int i = settings.func_236668_a_();
		int j = startPos.getX() >> 4;
		int k = startPos.getZ() >> 4;
		int l = 0;

		for (SharedSeedRandom sharedseedrandom = new SharedSeedRandom(); l <= searchRadius; ++l) {
			for (int i1 = -l; i1 <= l; ++i1) {
				boolean flag = i1 == -l || i1 == l;

				for (int j1 = -l; j1 <= l; ++j1) {
					boolean flag1 = j1 == -l || j1 == l;
					if (flag || flag1) {
						int k1 = j + i * i1;
						int l1 = k + i * j1;
						ChunkPos chunkpos = this.seperate(settings, seed, sharedseedrandom, k1, l1);
						IChunk ichunk = world.getChunk(chunkpos.x, chunkpos.z, ChunkStatus.STRUCTURE_STARTS);
						StructureStart<?> structurestart = structureManager
								.func_235013_a_(SectionPos.from(ichunk.getPos(), 0), this, ichunk);
						if (structurestart != null && structurestart.isValid()) {
							if (skipExistingChunks && structurestart.isRefCountBelowMax()) {
								structurestart.incrementRefCount();
								return structurestart.getPos();
							}

							if (!skipExistingChunks) {
								return structurestart.getPos();
							}
						}

						if (l == 0) {
							break;
						}
					}
				}

				if (l == 0) {
					break;
				}
			}
		}

		return null;
	}

	public StructureStart<?> func_236389_a_(DynamicRegistries registries, ChunkGenerator chunkGenerator,
			BiomeProvider biomeProvider, TemplateManager templateManager, long seed, ChunkPos chunkPos, Biome biome,
			int p_236389_8_, SharedSeedRandom random, StructureSeparationSettings settings, C config) {

		ChunkPos chunkpos = this.seperate(
				new StructureSeparationSettings(this.getDistance(), this.getSeparation(), this.getSeedModifier()), seed,
				random, chunkPos.x, chunkPos.z);

		if (chunkPos.x == chunkpos.x && chunkPos.z == chunkpos.z && this.func_230363_a_(chunkGenerator, biomeProvider,
				seed, random, chunkPos.x, chunkPos.z, biome, chunkpos, config)) {

			StructureStart<C> structurestart = this.func_236387_a_(chunkPos.x, chunkPos.z,
					MutableBoundingBox.getNewBoundingBox(), p_236389_8_, seed);

			structurestart.func_230364_a_(registries, chunkGenerator, templateManager, chunkPos.x, chunkPos.z, biome, config);

			if (structurestart.isValid()) {
				return structurestart;
			}
		}

		return StructureStart.DUMMY;
	}

	private StructureStart<C> func_236387_a_(int p_236387_1_, int p_236387_2_, MutableBoundingBox boundingbox,
			int p_236387_4_, long p_236387_5_) {
		return this.getStartFactory().create(this, p_236387_1_, p_236387_2_, boundingbox, p_236387_4_, p_236387_5_);
	}
}