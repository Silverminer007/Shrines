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
package com.silverminer.shrines.core.utils.saves;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.core.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.core.structures.custom.helper.ResourceData;
import com.silverminer.shrines.core.utils.custom_structures.Utils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

/**
 * @author Silverminer
 *
 */
public class BoundSaveData extends WorldSavedData {
	protected static final Logger LOG = LogManager.getLogger(BoundSaveData.class);
	private static final String DATA_NAME = "shrines-bounds";

	/**
	 * 
	 */
	public BoundSaveData() {
		super(DATA_NAME);
	}

	@Override
	public void load(CompoundNBT nbt) {
		int count = nbt.getInt("structures");
		for (int i = 0; i < count; i++) {
			CompoundNBT tag = nbt.getCompound(String.valueOf(i));
			CustomStructureData csd = Utils.getData(tag.getString("structure"), true);
			int idx = Utils.customsStructs.indexOf(csd);
			if (csd == null) {
				LOG.info("Failed to load structure {}", tag.getString("structure"));
				continue;
			}
			csd.PIECES_ON_FLY.clear();
			int amount = tag.getInt("bounds");
			for (int j = 0; j < amount; j++) {
				CompoundNBT resource = tag.getCompound(String.valueOf(j));
				csd.PIECES_ON_FLY.add(ResourceData.load(resource));
			}
			Utils.customsStructs.set(idx, csd);
		}
		CustomStructureData.sendToClients();
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		int i = 0;
		for (CustomStructureData csd : Utils.customsStructs) {
			CompoundNBT tag = new CompoundNBT();
			int j = 0;
			for (ResourceData rd : csd.PIECES_ON_FLY) {
				tag.put(String.valueOf(j++), rd.save());
			}
			tag.putString("structure", csd.getName());
			tag.putInt("bounds", j);
			nbt.put(String.valueOf(i++), tag);
		}
		nbt.putInt("structures", i);
		CustomStructureData.sendToClients();
		return nbt;
	}

	public static BoundSaveData get(ServerWorld world) {
		if (world == null)
			return null;
		LOG.debug("BoundSaveData read");
		DimensionSavedDataManager storage = world.getDataStorage();

		return storage.computeIfAbsent(BoundSaveData::new, DATA_NAME);
	}
}