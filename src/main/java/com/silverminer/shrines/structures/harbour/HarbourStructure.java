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
package com.silverminer.shrines.structures.harbour;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.ConfigBuilder;
import com.silverminer.shrines.config.ConfigBuilder.Type;
import com.silverminer.shrines.structures.AbstractStructure;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.SnowyVillagePools;
import net.minecraft.world.gen.feature.structure.VillageConfig;

public class HarbourStructure extends AbstractStructure {
	public static final String NAME = "harbour";
	protected static final ConfigBuilder HARBOUR_CONFIG = new ConfigBuilder("Harbour", 651398043, Type.HARBOUR)
			.setDistance(50).setSeparation(8)
			.setBiomes(Biome.Category.PLAINS, Biome.Category.FOREST, Biome.Category.TAIGA, Biome.Category.SAVANNA,
					Biome.Category.JUNGLE, Biome.Category.MESA, Biome.Category.ICY, Biome.Category.SWAMP,
					Biome.Category.MUSHROOM)
			.setNeedsGround(false);

	public HarbourStructure(Codec<VillageConfig> codec) {
		super(codec, NAME, HARBOUR_CONFIG);
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}


	@Override
	public JigsawPattern getPools() {
		return SnowyVillagePools.START;
	}
}