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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

/**
 * Packet to send from client to server
 * 
 * @author Silverminer
 *
 */
public class CSaveCustomStructuresPacket implements IPacket {
	protected static final Logger LOGGER = LogManager.getLogger(CSaveCustomStructuresPacket.class);

	public final String structure;
	public final String author;
	public final boolean entities;
	public final ResourceLocation dimension;

	public CSaveCustomStructuresPacket(String structure, String author, boolean entities, ResourceLocation dimension) {
		this.structure = structure;
		this.author = author;
		this.entities = entities;
		this.dimension = dimension;
	}

	public static void encode(CSaveCustomStructuresPacket pkt, PacketBuffer buf) {
		buf.writeUtf(pkt.structure);
		buf.writeUtf(pkt.author);
		buf.writeBoolean(pkt.entities);
		buf.writeResourceLocation(pkt.dimension);
	}

	public static CSaveCustomStructuresPacket decode(PacketBuffer buf) {
		return new CSaveCustomStructuresPacket(buf.readUtf(), buf.readUtf(), buf.readBoolean(), buf.readResourceLocation());
	}
}