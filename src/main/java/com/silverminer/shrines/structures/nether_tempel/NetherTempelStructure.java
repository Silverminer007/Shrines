package com.silverminer.shrines.structures.nether_tempel;

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

public class NetherTempelStructure extends AbstractStructure<NoFeatureConfig> {

	public NetherTempelStructure(Codec<NoFeatureConfig> codec) {
		super(codec, 3, "nether_tempel");
	}

	@Override
	public GenerationStage.Decoration getDecorationStage() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
		return NetherTempelStructure.Start::new;
	}

	@Override
	public int getDistance() {
		return 70;
	}

	@Override
	public int getSeparation() {
		return 20;
	}

	@Override
	public int getSeedModifier() {
		return 7428394;
	}

	@Override
	public double getSpawnChance() {
		return 0.3;
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
			NetherTempelPiece.generate(templateManager, blockpos, rotation, this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}