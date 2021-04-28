package com.silverminer.shrines.structures.harbour;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.config.StructureConfig.StructureGenConfig;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.AbstractStructureStart;

import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class HarbourStructure extends AbstractStructure<NoFeatureConfig> {

	public HarbourStructure(Codec<NoFeatureConfig> codec) {
		super(codec, 3, "harbour");
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
		return HarbourStructure.Start::new;
	}

	@Override
	public StructureGenConfig getConfig() {
		return Config.STRUCTURES.HARBOUR;
	}

	protected boolean isSurfaceFlatExtended(@Nonnull ChunkGenerator generator, int chunkX, int chunkZ) {
		int xStart = (chunkX << 4);
		int zStart = (chunkZ << 4);
		MutableBoundingBox mbb = MutableBoundingBox.createProper(xStart, 0, zStart, xStart + 100, 0,
				zStart + 100);
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
		if(super.validateGeneration(generator, provider, seed, rand, chunkX, chunkZ, biome, pos, config, exeptStructure))
			return this.isSurfaceFlatExtended(generator, chunkX, chunkZ);

		return false;
	}

	public static class Start extends AbstractStructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingbox,
				int p_i225806_5_, long seed) {
			super(structure, chunkX, chunkZ, boundingbox, p_i225806_5_, seed);
		}

		@Override
		public void generatePieces(DynamicRegistries registries, ChunkGenerator chunkGenerator,
				TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
			int i = chunkX * 16;
			int j = chunkZ * 16;
			BlockPos blockpos = new BlockPos(i, 0, j);
			Rotation rotation = Rotation.getRandom(this.random);
			HarbourPieces.generate(templateManager, blockpos, rotation, this.pieces, this.random, chunkGenerator);
			//BetterHarbourPieces.generate(templateManager, blockpos, rotation, this.components, this.rand, chunkGenerator);
			//HarbourBallonPiece.generate(templateManager, blockpos, rotation, this.components, this.rand);
			this.calculateBoundingBox();
		}
	}
}