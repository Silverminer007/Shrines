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
package com.silverminer.shrines.structures.end_temple;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.ConfigBuilder;
import com.silverminer.shrines.config.ConfigBuilder.Type;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.StructurePools;

import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.VillageConfig;

public class EndTempleStructure extends AbstractStructure {
	public static final String NAME = "end_temple";
	protected static final ConfigBuilder ENDTEMPLE_CONFIG = new ConfigBuilder("End Temple", 32 ^ 478392, Type.LOOTABLE)
			.setDistance(60).setSeparation(11).setBiomes(Category.THEEND)
			.addToBlacklist("minecraft:the_end", "minecraft:the_void", "minecraft:small_end_islands")
			.setDimension(Lists.newArrayList("end"));

	public EndTempleStructure(Codec<VillageConfig> codec) {
		super(codec, NAME, ENDTEMPLE_CONFIG);
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public JigsawPattern getPools() {
		return StructurePools.END_TEMPLE;
	}

	public boolean isEndStructure() {
		return true;
	}
}