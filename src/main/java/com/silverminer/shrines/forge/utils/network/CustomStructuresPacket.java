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
package com.silverminer.shrines.forge.utils.network;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.silverminer.shrines.core.structures.custom.helper.CustomStructureData;

import net.minecraft.network.PacketBuffer;

public class CustomStructuresPacket {
	protected static final Logger LOGGER = LogManager.getLogger(CustomStructuresPacket.class);

	public final ArrayList<CustomStructureData> datas;
	public CustomStructuresPacket(ArrayList<CustomStructureData> datas) {
		this.datas = datas;
	}

	public static void encode(CustomStructuresPacket pkt, PacketBuffer buf) {
		buf.writeInt(pkt.datas.size());
		for(CustomStructureData csd : pkt.datas) {
			buf.writeNbt(CustomStructureData.write(csd));
		}
	}

	public static CustomStructuresPacket decode(PacketBuffer buf) {
		ArrayList<CustomStructureData> datas = Lists.newArrayList();
		int size = buf.readInt();
		for(int i = 0; i < size; i++) {
			datas.add(CustomStructureData.read(buf.readNbt()));
		}
		return new CustomStructuresPacket(datas);
	}
}