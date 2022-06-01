package com.silverminer.shrines.structures.nether_pyramid;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.ShrinesStructureStart;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class NetherPyramidStructure extends AbstractStructure<NoFeatureConfig> {

	public NetherPyramidStructure(Codec<NoFeatureConfig> codec) {
		super(codec, 3, "nether_pyramid");
	}

	@Override
	public GenerationStage.Decoration getDecorationStage() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
		return NetherPyramidStructure.Start::new;
	}

	@Override
	public int getDistance() {
		return 150;
	}

	@Override
	public int getSeparation() {
		return 50;
	}

	@Override
	public int getSeedModifier() {
		return 7428394;
	}

	@Override
	public double getSpawnChance() {
		return 0.1;
	}

	public static class Start extends ShrinesStructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingbox,
				int p_i225806_5_, long seed) {
			super(structure, chunkX, chunkZ, boundingbox, p_i225806_5_, seed);
		}

		@Override
		public void func_230364_a_(ChunkGenerator chunkGenerator, TemplateManager templateManager, int chunkX,
				int chunkZ, Biome biome, NoFeatureConfig config) {
			int i = chunkX * 16;
			int j = chunkZ * 16;
			BlockPos blockpos = new BlockPos(i, 90, j);
			Rotation rotation = Rotation.randomRotation(this.rand);
			NetherPyramidPiece.generate(templateManager, blockpos, rotation, this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}