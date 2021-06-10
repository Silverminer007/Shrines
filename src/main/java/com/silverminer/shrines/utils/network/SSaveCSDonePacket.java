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

/**
 * Packet to send from client to server
 * 
 * @author Silverminer
 *
 */
public class SSaveCSDonePacket implements IPacket {
	protected static final Logger LOGGER = LogManager.getLogger(SSaveCSDonePacket.class);

	public SSaveCSDonePacket() {
	}

	public static void encode(SSaveCSDonePacket pkt, PacketBuffer buf) {
	}

	public static SSaveCSDonePacket decode(PacketBuffer buf) {
		return new SSaveCSDonePacket();
	}
}