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
package com.silverminer.shrines.config;

import java.util.stream.Collectors;

import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.structures.AbstractStructure;
import com.silverminer.shrines.structures.custom.CustomStructure;

import net.minecraftforge.common.ForgeConfigSpec;

public class StructureConfig {

	public StructureConfig(final ForgeConfigSpec.Builder SERVER_BUILDER) {
		for (AbstractStructure structure : NewStructureInit.STRUCTURES.values().stream()
				.filter(struct -> !(struct instanceof CustomStructure)).collect(Collectors.toList())) {
			structure.buildConfig(SERVER_BUILDER);
		}
	}
}