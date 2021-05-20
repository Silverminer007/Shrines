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
package com.silverminer.shrines.forge.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.silverminer.shrines.core.utils.IFunctionProvider;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Silverminer
 *
 */
public class ForgeFunctionProvider implements IFunctionProvider {

	@Override
	public List<String> getBiomes() {
		return ForgeRegistries.BIOMES.getKeys().stream().map(b -> b.toString()).collect(Collectors.toList());
	}

	@Override
	public Block getBlockByID(String ID) {
		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ID));
	}

}