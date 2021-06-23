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
package com.silverminer.shrines.structures.balloon;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.config.ConfigBuilder;
import com.silverminer.shrines.config.ConfigBuilder.Type;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.StructurePools;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.VillageConfig;

public class BalloonStructure extends AbstractStructure {
	public static final String NAME = "balloon";
	protected static final ConfigBuilder BALLON_CONFIG = new ConfigBuilder("Ballon", 143665, Type.LOOTABLE)
			.setLootChance(0.25D).setDistance(50).setSeparation(8).setNeedsGround(false);

	public BalloonStructure(Codec<VillageConfig> codec) {
		super(codec, NAME, BALLON_CONFIG);
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public JigsawPattern getPools() {
		LocalDate localdate = LocalDate.now();
		int month = localdate.get(ChronoField.MONTH_OF_YEAR);
		if (month != 6) {
			return StructurePools.BALLOON;
		} else {
			return StructurePools.BALLOON_RAINBOW;
		}
	}
}