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
package com.silverminer.shrines.utils.network;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.network.PacketBuffer;

/**
 * Packet to send from client to server
 * 
 * @author Silverminer
 *
 */
public class CCustomStructuresPacket implements IPacket {
	protected static final Logger LOGGER = LogManager.getLogger(CCustomStructuresPacket.class);

	public final ArrayList<CustomStructureData> datas;

	public CCustomStructuresPacket(ArrayList<CustomStructureData> datas) {
		this.datas = datas;
		LOGGER.info("Sending structures to server: {}\n{}", datas, Utils.getData("house1", false).PIECES_ON_FLY);
	}

	public static void encode(CCustomStructuresPacket pkt, PacketBuffer buf) {
		buf.writeInt(pkt.datas.size());
		for (CustomStructureData csd : pkt.datas) {
			buf.writeNbt(CustomStructureData.write(csd));
		}
	}

	public static CCustomStructuresPacket decode(PacketBuffer buf) {
		ArrayList<CustomStructureData> datas = Lists.newArrayList();
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			datas.add(CustomStructureData.read(buf.readNbt()));
		}
		return new CCustomStructuresPacket(datas);
	}
}