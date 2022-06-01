/*
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class ShrinesPacketHandler {

   public static final String PROTOCOL_VERSION = "7.0";
   public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
         .named(new ResourceLocation(ShrinesMod.MODID, "main_channel"))
         .clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals)
         .networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();
   protected static final Logger LOGGER = LogManager.getLogger(ShrinesPacketHandler.class);

   public static void register() {
      int id = 0;
      CHANNEL.registerMessage(id++, STCUpdateQueuePosition.class, STCUpdateQueuePosition::toBytes, STCUpdateQueuePosition::new, STCUpdateQueuePosition::handle);
      CHANNEL.registerMessage(id++, CTSPlayerJoinQueue.class, CTSPlayerJoinQueue::toBytes, CTSPlayerJoinQueue::new, CTSPlayerJoinQueue::handle);
      CHANNEL.registerMessage(id++, CTSPlayerLeaveQueue.class, CTSPlayerLeaveQueue::toBytes, CTSPlayerLeaveQueue::new, CTSPlayerLeaveQueue::handle);
      CHANNEL.registerMessage(id++, CTSSyncPackagesRequest.class, CTSSyncPackagesRequest::toBytes, CTSSyncPackagesRequest::new, CTSSyncPackagesRequest::handle);
      CHANNEL.registerMessage(id++, STCSyncPackages.class, STCSyncPackages::toBytes, STCSyncPackages::new, STCSyncPackages::handle);
      CHANNEL.registerMessage(id++, CTSSavePackages.class, CTSSavePackages::toBytes, CTSSavePackages::new, CTSSavePackages::handle);
      CHANNEL.registerMessage(id++, STCError.class, STCError::toBytes, STCError::new, STCError::handle);
      CHANNEL.registerMessage(id++, STCSyncNovels.class, STCSyncNovels::toBytes, STCSyncNovels::new, STCSyncNovels::handle);
      CHANNEL.registerMessage(id++, CTSSyncNovelsRequest.class, CTSSyncNovelsRequest::toBytes, CTSSyncNovelsRequest::new, CTSSyncNovelsRequest::handle);
      CHANNEL.registerMessage(id++, CTSSyncStructureIconsRequest.class, CTSSyncStructureIconsRequest::toBytes, CTSSyncStructureIconsRequest::new, CTSSyncStructureIconsRequest::handle);
      CHANNEL.registerMessage(id++, STCSyncStructureIcons.class, STCSyncStructureIcons::toBytes, STCSyncStructureIcons::new, STCSyncStructureIcons::handle);
      CHANNEL.registerMessage(id++, STCSyncAvailableDimensions.class, STCSyncAvailableDimensions::toBytes, STCSyncAvailableDimensions::new, STCSyncAvailableDimensions::handle);
      CHANNEL.registerMessage(id++, CTSSyncAvailableDimensionsRequest.class, CTSSyncAvailableDimensionsRequest::toBytes, CTSSyncAvailableDimensionsRequest::new, CTSSyncAvailableDimensionsRequest::handle);
      CHANNEL.registerMessage(id++, STCSendSaveResult.class, STCSendSaveResult::toBytes, STCSendSaveResult::new, STCSendSaveResult::handle);
      CHANNEL.registerMessage(id++, CTSImportPackage.class, CTSImportPackage::toBytes, CTSImportPackage::new, CTSImportPackage::handle);
      CHANNEL.registerMessage(id++, STCSaveExportedPackage.class, STCSaveExportedPackage::toBytes, STCSaveExportedPackage::new, STCSaveExportedPackage::handle);
      CHANNEL.registerMessage(id++, CTSExportPackage.class, CTSExportPackage::toBytes, CTSExportPackage::new, CTSExportPackage::handle);
      LOGGER.info("Initializing networking on version [{}]. This should match between client and server. Registered {} packets",
            PROTOCOL_VERSION, id);
   }

   @SuppressWarnings("unused")
   public static void sendTo(IPacket message, Player player) {
      sendTo(message, player, true);
   }

   public static void sendTo(IPacket message, Player player, boolean requiresSuccess) {
      try {
         CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
      } catch (NullPointerException e) {
         if (requiresSuccess) {
            throw e;
         }
      }
   }

   public static void sendTo(IPacket message, UUID uuid) {
      sendTo(message, uuid, true);
   }

   public static void sendTo(IPacket message, UUID uuid, boolean requiresSuccess) {
      sendTo(message, ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid), requiresSuccess);
   }

   @SuppressWarnings("unused")
   public static void sendToAll(IPacket message) {
      sendToAll(message, true);
   }

   public static void sendToAll(IPacket message, boolean requiresSuccess) {
      try {
         CHANNEL.send(PacketDistributor.ALL.noArg(), message);
      } catch (NullPointerException e) {
         if (requiresSuccess) {
            throw e;
         }
      }
   }

   public static void sendToServer(IPacket message) {
      sendToServer(message, true);
   }

   public static void sendToServer(IPacket message, boolean requiresSuccess) {
      try {
         CHANNEL.sendToServer(message);
      } catch (NullPointerException e) {
         if (requiresSuccess) {
            throw e;
         }
      }
   }
}