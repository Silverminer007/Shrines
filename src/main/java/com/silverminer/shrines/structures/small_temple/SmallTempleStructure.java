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
package com.silverminer.shrines.structures.small_temple;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.ConfigBuilder;
import com.silverminer.shrines.config.ConfigBuilder.Type;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.StructurePools;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.VillageConfig;

public class SmallTempleStructure extends AbstractStructure {
	public static final String NAME = "small_temple";
	protected static final ConfigBuilder SMALLTEMPLE_CONFIG = new ConfigBuilder("Small Tempel", 4765321, Type.LOOTABLE).setDistance(75).setSeparation(13);

	public SmallTempleStructure(Codec<VillageConfig> codec) {
		super(codec, NAME, SMALLTEMPLE_CONFIG);
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}


	@Override
	public JigsawPattern getPools() {
		return StructurePools.SMALL_TEMPLE;
	}
}