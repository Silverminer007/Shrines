package com.silverminer.shrines.structures.end_temple;

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

public class EndTempleStructure extends AbstractStructure<NoFeatureConfig> {

	public EndTempleStructure(Codec<NoFeatureConfig> codec) {
		super(codec, 3, "end_temple");
	}

	@Override
	public GenerationStage.Decoration getDecorationStage() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
		return EndTempleStructure.Start::new;
	}

	@Override
	public StructureGenConfig getConfig() {
		return Config.STRUCTURES.END_TEMPLE;
	}

	public boolean validateGeneration(ChunkGenerator generator, BiomeProvider provider, long seed,
			SharedSeedRandom rand, int chunkX, int chunkZ, Biome biome, ChunkPos pos, IFeatureConfig config,
			@Nullable Structure<?>... exeptStructure) {
		boolean flag = super.validateGeneration(generator, provider, seed, rand, chunkX, chunkZ, biome, pos, config, exeptStructure);
		int offset = this.getSize() * 16;

		int xStart = (chunkX << 4);
		int zStart = (chunkZ << 4);

		int i1 = generator.getHeight(xStart, zStart, Heightmap.Type.WORLD_SURFACE_WG);
		int j1 = generator.getHeight(xStart, zStart + offset, Heightmap.Type.WORLD_SURFACE_WG);
		int k1 = generator.getHeight(xStart + offset, zStart, Heightmap.Type.WORLD_SURFACE_WG);
		int l1 = generator.getHeight(xStart + offset, zStart + offset, Heightmap.Type.WORLD_SURFACE_WG);
		int minHeight = Math.min(Math.min(i1, j1), Math.min(k1, l1));
		return flag && minHeight > 60;
	}

	public boolean isEndStructure() {
		return true;
	}

	public static class Start extends AbstractStructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingbox,
				int p_i225806_5_, long seed) {
			super(structure, chunkX, chunkZ, boundingbox, p_i225806_5_, seed);
		}

		@Override
		public void func_230364_a_(DynamicRegistries registries, ChunkGenerator chunkGenerator,
				TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
			int i = chunkX * 16;
			int j = chunkZ * 16;
			BlockPos blockpos = new BlockPos(i, 0, j);
			Rotation rotation = Rotation.randomRotation(this.rand);
			EndTemplePiece.generate(templateManager, blockpos, rotation, this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}