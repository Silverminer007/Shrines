/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.network;

import com.silverminer.shrines.Shrines;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.minecraftforge.network.NetworkRegistry.ABSENT;
import static net.minecraftforge.network.NetworkRegistry.ACCEPTVANILLA;

public class NetworkManager {
   private static final String PROTOCOL_VERSION = "1.0"; // MAJOR.MINOR -> MAJOR must match and client must have a newer or equal version compared to the server
   public static final SimpleChannel SIMPLE_CHANNEL = NetworkRegistry.newSimpleChannel(
         Shrines.location("main"),
         () -> PROTOCOL_VERSION,
         version -> checkVersionMatch(version, PROTOCOL_VERSION, true),
         version -> checkVersionMatch(version, PROTOCOL_VERSION, false)
   );

   public static boolean checkVersionMatch(@NotNull String remoteVersion, String protocol_version, boolean client) {
      if (ACCEPTVANILLA.equals(remoteVersion) || ABSENT.equals(remoteVersion)) {
         return true;
      }
      try {
         String[] remoteVersionParts = remoteVersion.split("\\.");
         if (remoteVersionParts.length != 2) {
            return false;
         }
         String[] versionParts = protocol_version.split("\\.");
         if (versionParts.length != 2) {
            return false;
         }
         int remoteMinor = Integer.parseInt(remoteVersionParts[1]);
         int protocolMinor = Integer.parseInt(versionParts[1]);
         return Objects.equals(Integer.parseInt(remoteVersionParts[0]), Integer.parseInt(versionParts[0])) &&
               (!client ? remoteMinor >= protocolMinor : remoteMinor <= protocolMinor);
      } catch (NumberFormatException e) {
         return false;
      }
   }

   public static void registerPackets() {
      int id = 0;
      SIMPLE_CHANNEL.registerMessage(id++, STCUnlockSnippetPacket.class, STCUnlockSnippetPacket::encoder, STCUnlockSnippetPacket::decoder, STCUnlockSnippetPacket::messageConsumer);
      SIMPLE_CHANNEL.registerMessage(id++, STCSendStoriesPacket.class, STCSendStoriesPacket::encoder, STCSendStoriesPacket::decoder, STCSendStoriesPacket::messageConsumer);
      SIMPLE_CHANNEL.registerMessage(id, CTSFetchStoriesPacket.class, CTSFetchStoriesPacket::encoder, CTSFetchStoriesPacket::decoder, CTSFetchStoriesPacket::messageConsumer);
   }
}