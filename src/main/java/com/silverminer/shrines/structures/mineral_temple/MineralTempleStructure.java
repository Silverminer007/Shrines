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
package com.silverminer.shrines.structures.mineral_temple;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.ConfigBuilder;
import com.silverminer.shrines.config.ConfigBuilder.Type;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.StructurePools;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.VillageConfig;

public class MineralTempleStructure extends AbstractStructure {
	public static final String NAME = "mineral_temple";
	protected static final ConfigBuilder MINERALTEMPLE_CONFIG = new ConfigBuilder("Mineral Temple", 576143753,
			Type.LOOTABLE).setDistance(50).setSeparation(10).setUseRandomVarianting(false);

	public MineralTempleStructure(Codec<VillageConfig> codec) {
		super(codec, NAME, MINERALTEMPLE_CONFIG);
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}


	@Override
	public JigsawPattern getPools() {
		return StructurePools.MINERAL_TEMPLE;
	}
}