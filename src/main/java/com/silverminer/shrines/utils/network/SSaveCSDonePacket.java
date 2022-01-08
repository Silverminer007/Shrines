/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.utils.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.network.PacketBuffer;

/**
 * Packet to send from client to server
 *
 * @author Silverminer
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