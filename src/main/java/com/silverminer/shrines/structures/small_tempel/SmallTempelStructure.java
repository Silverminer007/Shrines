package com.silverminer.shrines.structures.small_tempel;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.structures.ShrinesStructure;
import com.silverminer.shrines.structures.ShrinesStructureStart;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class SmallTempelStructure extends ShrinesStructure<NoFeatureConfig> {

	public SmallTempelStructure(Codec<NoFeatureConfig> codec) {
		super(codec, 3, "small_tempel");
	}

	@Override
	public GenerationStage.Decoration getDecorationStage() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
		return SmallTempelStructure.Start::new;
	}

	@Override
	public int getDistance() {
		return 75;
	}

	@Override
	public int getSeparation() {
		return 13;
	}

	@Override
	public int getSeedModifier() {
		return 4765321;
	}

	@Override
	public double getSpawnChance() {
		return 0.6;
	}

	public static class Start extends ShrinesStructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingbox,
				int p_i225806_5_, long seed) {
			super(structure, chunkX, chunkZ, boundingbox, p_i225806_5_, seed);
		}

		@Override
		public void func_230364_a_(DynamicRegistries p_230364_1_, ChunkGenerator chunkGenerator,
				TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
			int i = chunkX * 16;
			int j = chunkZ * 16;
			BlockPos blockpos = new BlockPos(i, 90, j);
			Rotation rotation = Rotation.randomRotation(this.rand);
			SmallTempelPiece.generate(templateManager, blockpos, rotation, this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}