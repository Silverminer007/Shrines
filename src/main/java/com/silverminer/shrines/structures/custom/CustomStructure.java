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
import com.silverminer.shrines.structures.StructurePools;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.VillageConfig;

public class CustomStructure extends AbstractStructure {
	protected static final Logger LOG = LogManager.getLogger(CustomStructure.class);
	private CustomStructureData csd;

	public CustomStructure(Codec<VillageConfig> codec, String name, CustomStructureData csd) {
		super(codec, name, csd);
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
	public JigsawPattern getPools() {
		return StructurePools.BALLOON;
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
}