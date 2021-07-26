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

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.init.StructureRegistryHolder;

import net.minecraftforge.common.ForgeConfigSpec;

public class StructureConfig {

	public StructureConfig(final ForgeConfigSpec.Builder SERVER_BUILDER) {
		NewStructureInit.initStructures();
		ShrinesMod.LOGGER.error("Structures count at config: {}", NewStructureInit.STRUCTURES.size());
		for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
			holder.buildConfig(SERVER_BUILDER);
		}
	}
}