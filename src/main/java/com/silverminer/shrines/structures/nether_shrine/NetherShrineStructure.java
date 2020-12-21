package com.silverminer.shrines.structures.nether_shrine;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.AbstractStructureStart;

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

public class NetherShrineStructure extends AbstractStructure<NoFeatureConfig> {

	public NetherShrineStructure(Codec<NoFeatureConfig> codec) {
		super(codec, 3, "nether_shrine");
	}

	@Override
	public GenerationStage.Decoration getDecorationStage() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
		return NetherShrineStructure.Start::new;
	}

	/*@Override
	public int getDistance() {
		return Config.STRUCTURES.NETHER_SHRINE_DISTANCE.get();
	}

	@Override
	public int getSeparation() {
		return Config.STRUCTURES.NETHER_SHRINE_SEPARATION.get();
	}

	@Override
	public int getSeedModifier() {
		return Config.STRUCTURES.NETHER_SHRINE_SEED.get();
	}

	@Override
	public double getSpawnChance() {
		return Config.STRUCTURES.NETHER_SHRINE_SPAWN_CHANCE.get();
	}*/
	@Override
	public int getDistance() {
		return Config.STRUCTURES.NETHER_SHRINE.DISTANCE.get();
	}

	@Override
	public int getSeparation() {
		return Config.STRUCTURES.NETHER_SHRINE.SEPARATION.get();
	}

	@Override
	public int getSeedModifier() {
		return Config.STRUCTURES.NETHER_SHRINE.SEED.get();
	}

	@Override
	public double getSpawnChance() {
		return Config.STRUCTURES.NETHER_SHRINE.SPAWN_CHANCE.get();
	}

	public static class Start extends AbstractStructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingbox,
				int p_i225806_5_, long seed) {
			super(structure, chunkX, chunkZ, boundingbox, p_i225806_5_, seed);
		}

		@Override
		public void func_230364_a_(DynamicRegistries p_230364_1_, ChunkGenerator chunkGenerator,
				TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
			int i = chunkX * 16;
			int j = chunkZ * 16;
			BlockPos blockpos = new BlockPos(i, 0, j);
			Rotation rotation = Rotation.randomRotation(this.rand);
			NetherShrinePiece.generate(templateManager, blockpos, rotation, this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}