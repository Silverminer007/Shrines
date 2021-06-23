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
package com.silverminer.shrines.structures.high_temple;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.ConfigBuilder;
import com.silverminer.shrines.config.ConfigBuilder.Type;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.StructurePools;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.VillageConfig;

public class HighTempleStructure extends AbstractStructure {
	public static final String NAME ="high_temple";
	protected static final ConfigBuilder HIGHTEMPLE_CONFIG = new ConfigBuilder("High Tempel", 536987987, Type.LOOTABLE)
			.setDistance(85).setSeparation(18);

	public HighTempleStructure(Codec<VillageConfig> codec) {
		super(codec,NAME, HIGHTEMPLE_CONFIG);
	}


	@Override
	public JigsawPattern getPools() {
		return StructurePools.HIGH_TEMPLE;
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}
}