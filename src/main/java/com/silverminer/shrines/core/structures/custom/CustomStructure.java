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
package com.silverminer.shrines.core.structures.custom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.core.structures.AbstractStructure;
import com.silverminer.shrines.core.structures.AbstractStructureStart;
import com.silverminer.shrines.core.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.core.utils.StructureUtils;
import com.silverminer.shrines.forge.config.Config;
import com.silverminer.shrines.forge.config.StructureConfig.StructureGenConfig;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class CustomStructure extends AbstractStructure<NoFeatureConfig> {
	protected static final Logger LOG = LogManager.getLogger(CustomStructure.class);
	private CustomStructureData csd;

	public CustomStructure(Codec<NoFeatureConfig> codec, String name, CustomStructureData csd) {
		super(codec, 3, name);
		this.csd = csd;
	}

	public boolean isEndStructure() {
		return csd.categories.getValue().contains(Biome.Category.THEEND);
	}

	public boolean isNetherStructure() {
		return csd.categories.getValue().contains(Biome.Category.NETHER);
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
		return CustomStructure.Start::new;
	}

	@Override
	public StructureGenConfig getConfig() {
		return Config.STRUCTURES.CUSTOM;
	}

	public int getDistance() {
		return (int) (csd.distance.getValue() * Config.STRUCTURES.DISTANCE_FACTOR.get());
	}

	public int getSeparation() {
		return (int) (csd.seperation.getValue() * Config.STRUCTURES.SEPERATION_FACTOR.get());
	}

	public int getSeedModifier() {
		return csd.seed.getValue();
	}

	public double getSpawnChance() {
		return csd.spawn_chance.getValue();
	}

	public boolean needsGround() {
		return csd.needs_ground.getValue();
	}

	/**
	 * 
	 * @param name
	 * @param category
	 * @return
	 */
	public boolean validateSpawn(ResourceLocation name, Category category) {
		if (!csd.generate.getValue()) {
			return false;
		}
		boolean flag = csd.categories.getValue().contains(category);

		if (!csd.blacklist.getValue().isEmpty() && flag) {
			flag = !csd.blacklist.getValue().contains(name.toString());
		}

		return flag;
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
			BlockPos blockpos = new BlockPos(i, 1, j);
			Rotation rotation = Rotation.getRandom(this.random);
			if (!(this.getFeature() instanceof CustomStructure))
				return;
			CustomStructure cS = (CustomStructure) this.getFeature();
			int height = StructureUtils.getAverageHeight(new BlockPos(i, 0, j).offset(-24, 0, -24), chunkGenerator, 3) + cS.csd.base_height_offset.getValue();
			CustomPiece.generate(templateManager, blockpos, rotation, this.pieces, this.random, cS.csd.use_random_varianting.getValue(),
					cS.csd.pieces.getValue(), cS.name, cS.csd.ignore_air.getValue(), height, chunkGenerator);
			this.calculateBoundingBox();
		}
	}
}