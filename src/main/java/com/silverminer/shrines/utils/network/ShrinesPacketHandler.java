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

import com.silverminer.shrines.ShrinesMod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ShrinesPacketHandler {

	protected static final Logger LOGGER = LogManager.getLogger(ShrinesPacketHandler.class);

	public static final String PROTOCOL_VERSION = "4.0";

	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(ShrinesMod.MODID, "main_channel"))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();

	public static void register() {
		int id = 0;
		CHANNEL.registerMessage(id++, CCustomStructuresPacket.class, CCustomStructuresPacket::encode,
				CCustomStructuresPacket::decode, Handles.HandleCustomStructures::handleCustomStructuresServer);
		CHANNEL.registerMessage(id++, SCustomStructuresPacket.class, SCustomStructuresPacket::encode,
				SCustomStructuresPacket::decode, Handles.HandleCustomStructures::handleCustomStructuresClient);
		CHANNEL.registerMessage(id++, CSaveCustomStructuresPacket.class, CSaveCustomStructuresPacket::encode,
				CSaveCustomStructuresPacket::decode, Handles.HandleCustomStructures::handleSaveCS);
		CHANNEL.registerMessage(id++, SSaveCSDonePacket.class, SSaveCSDonePacket::encode,
				SSaveCSDonePacket::decode, Handles.HandleCustomStructures::handleSaveCSDone);
	}

	public static void sendTo(IPacket message, PlayerEntity player) {
		CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
	}

	public static void sendToAll(IPacket message) {
		CHANNEL.send(PacketDistributor.ALL.noArg(), message);
	}

	public static void sendToServer(IPacket message) {
		if (Minecraft.getInstance().getConnection() != null) {
			CHANNEL.sendToServer(message);
		} else {
			LOGGER.error("Failed to send custom structure data to server because connections are death");
		}
	}
}