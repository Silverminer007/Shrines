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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.ShrinesMod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ShrinesPacketHandler {

	protected static final Logger LOGGER = LogManager.getLogger(ShrinesPacketHandler.class);

	public static final String PROTOCOL_VERSION = "2.0";

	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(ShrinesMod.MODID, "main_channel")).clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();

	public static void register() {
		int id = 0;
		CHANNEL.registerMessage(id++, CustomStructuresPacket.class, CustomStructuresPacket::encode,
				CustomStructuresPacket::decode, CustomStructuresPacket::handle);
	}

	public static void sendTo(Object message, PlayerEntity player) {
		CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
	}

	public static void sendToAll(Object message) {
		CHANNEL.send(PacketDistributor.ALL.noArg(), message);
	}

	public static void sendToServer(Object message) {
		CHANNEL.sendToServer(message);
	}
}