/**
 * Silverminer (and Team)
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * <p>
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.utils.network;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.utils.network.cts.*;
import com.silverminer.shrines.utils.network.stc.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class ShrinesPacketHandler {

    public static final String PROTOCOL_VERSION = "5.2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(ShrinesMod.MODID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();
    protected static final Logger LOGGER = LogManager.getLogger(ShrinesPacketHandler.class);

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, CTSFetchStructuresPacket.class, CTSFetchStructuresPacket::encode,
                CTSFetchStructuresPacket::decode, CTSFetchStructuresPacket::handle);
        CHANNEL.registerMessage(id++, STCFetchStructuresPacket.class, STCFetchStructuresPacket::encode,
                STCFetchStructuresPacket::decode, STCFetchStructuresPacket::handle);
        CHANNEL.registerMessage(id++, CTSAddedStructurePacketPacket.class, CTSAddedStructurePacketPacket::encode,
                CTSAddedStructurePacketPacket::decode, CTSAddedStructurePacketPacket::handle);
        CHANNEL.registerMessage(id++, CTSDeletedStructurePacketPacket.class, CTSDeletedStructurePacketPacket::encode,
                CTSDeletedStructurePacketPacket::decode, CTSDeletedStructurePacketPacket::handle);
        CHANNEL.registerMessage(id++, CTSEditedStructurePacketPacket.class, CTSEditedStructurePacketPacket::encode,
                CTSEditedStructurePacketPacket::decode, CTSEditedStructurePacketPacket::handle);
        CHANNEL.registerMessage(id++, CTSFetchNovelAmountPacket.class, CTSFetchNovelAmountPacket::encode,
                CTSFetchNovelAmountPacket::decode, CTSFetchNovelAmountPacket::handle);
        CHANNEL.registerMessage(id++, STCFetchNovelsAmountPacket.class, STCFetchNovelsAmountPacket::encode,
                STCFetchNovelsAmountPacket::decode, STCFetchNovelsAmountPacket::handle);
        CHANNEL.registerMessage(id++, CTSPlayerJoinedQueuePacket.class, CTSPlayerJoinedQueuePacket::encode,
                CTSPlayerJoinedQueuePacket::decode, CTSPlayerJoinedQueuePacket::handle);
        CHANNEL.registerMessage(id++, CTSPlayerLeftQueuePacket.class, CTSPlayerLeftQueuePacket::encode,
                CTSPlayerLeftQueuePacket::decode, CTSPlayerLeftQueuePacket::handle);
        CHANNEL.registerMessage(id++, STCUpdateQueueScreenPacket.class, STCUpdateQueueScreenPacket::encode,
                STCUpdateQueueScreenPacket::decode, STCUpdateQueueScreenPacket::handle);
        CHANNEL.registerMessage(id++, STCOpenStructuresPacketEditPacket.class,
                STCOpenStructuresPacketEditPacket::encode, STCOpenStructuresPacketEditPacket::decode,
                STCOpenStructuresPacketEditPacket::handle);
        CHANNEL.registerMessage(id++, STCEditStructuresPacketPacket.class,
                STCEditStructuresPacketPacket::encode, STCEditStructuresPacketPacket::decode,
                STCEditStructuresPacketPacket::handle);
        CHANNEL.registerMessage(id++, CTSAddTemplatesPacket.class,
                CTSAddTemplatesPacket::encode, CTSAddTemplatesPacket::decode,
                CTSAddTemplatesPacket::handle);
        CHANNEL.registerMessage(id++, CTSDeleteTemplatesPacket.class,
                CTSDeleteTemplatesPacket::encode, CTSDeleteTemplatesPacket::decode,
                CTSDeleteTemplatesPacket::handle);
        CHANNEL.registerMessage(id++, CTSRenameTemplatesPacket.class,
                CTSRenameTemplatesPacket::encode, CTSRenameTemplatesPacket::decode,
                CTSRenameTemplatesPacket::handle);
        CHANNEL.registerMessage(id++, CTSExportStructuresPacketPacket.class,
                CTSExportStructuresPacketPacket::encode, CTSExportStructuresPacketPacket::decode,
                CTSExportStructuresPacketPacket::handle);
        CHANNEL.registerMessage(id++, STCExportStructuresPacketPacket.class,
                STCExportStructuresPacketPacket::encode, STCExportStructuresPacketPacket::decode,
                STCExportStructuresPacketPacket::handle);
        CHANNEL.registerMessage(id++, CTSImportStructuresPacketPacket.class,
                CTSImportStructuresPacketPacket::encode, CTSImportStructuresPacketPacket::decode,
                CTSImportStructuresPacketPacket::handle);
        CHANNEL.registerMessage(id++, STCErrorPacket.class, STCErrorPacket::encode, STCErrorPacket::decode, STCErrorPacket::handle);
        LOGGER.info("Initializing networking on version [{}]. This should match between client and server",
                PROTOCOL_VERSION);
    }

    public static void sendTo(IPacket message, PlayerEntity player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
    }

    public static void sendTo(IPacket message, UUID uuid) {
        MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> server.getPlayerList().getPlayer(uuid)), message);
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