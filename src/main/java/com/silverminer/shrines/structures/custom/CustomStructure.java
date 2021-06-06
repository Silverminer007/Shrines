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
package com.silverminer.shrines.structures.custom;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.AbstractStructureStart;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.structures.custom.helper.PieceData;
import com.silverminer.shrines.utils.StructureUtils;

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
		super(codec, 3, name, csd);
		this.csd = csd;
	}

	public List<? extends String> getDimensions() {
		return csd.dimensions.getValue();
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
		return CustomStructure.Start::new;
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
			BlockPos blockpos = new BlockPos(i, -1, j);
			Rotation rotation = Rotation.getRandom(this.random);
			if (!(this.getFeature() instanceof CustomStructure))
				return;
			CustomStructure cS = (CustomStructure) this.getFeature();
			List<PieceData> pieces = cS.csd.pieces.getValue();
			BlockPos lastOffset = pieces.get(pieces.size() - 1).offset;
			int size = 16 + Math.max(lastOffset.getX(), Math.max(lastOffset.getY(), lastOffset.getZ()));
			MutableBoundingBox mbb = MutableBoundingBox.createProper(-size, 0, -size, size, 0, size);
			mbb.move(new BlockPos(i, 0, j));
			int height = StructureUtils.getHeight(chunkGenerator, new BlockPos(mbb.x0, mbb.y0, mbb.z0), mbb, random)
					+ cS.csd.base_height_offset.getValue();
			CustomPiece.generate(templateManager, blockpos, rotation, this.pieces, this.random,
					cS.csd.use_random_varianting.getValue(), pieces, cS.name, cS.csd.ignore_air.getValue(), height,
					chunkGenerator);
			this.calculateBoundingBox();
		}
	}
}