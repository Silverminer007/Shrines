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
package com.silverminer.shrines.structures.ballon;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.ConfigBuilder;
import com.silverminer.shrines.config.ConfigBuilder.Type;
import com.silverminer.shrines.structures.AbstractStructure;

import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class BallonStructure extends AbstractStructure {
	protected static final ConfigBuilder BALLON_CONFIG = new ConfigBuilder("Ballon", 143665, Type.LOOTABLE)
			.setLootChance(0.25D).setDistance(50).setSeparation(8).setNeedsGround(false);

	public BallonStructure(Codec<VillageConfig> codec) {
		super(codec, 1, 10, "ballon", BALLON_CONFIG);
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	public static class Start extends StructureStart<VillageConfig> {

		public Start(Structure<VillageConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingbox,
				int p_i225806_5_, long seed) {
			super(structure, chunkX, chunkZ, boundingbox, p_i225806_5_, seed);
		}

		@Override
		public void generatePieces(DynamicRegistries p_230364_1_, ChunkGenerator p_230364_2_,
				TemplateManager p_230364_3_, int p_230364_4_, int p_230364_5_, Biome p_230364_6_,
				VillageConfig p_230364_7_) {
		}
	}
}