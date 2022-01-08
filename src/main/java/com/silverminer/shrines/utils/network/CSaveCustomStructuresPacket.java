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
import net.minecraft.util.ResourceLocation;

/**
 * Packet to send from client to server
 *
 * @author Silverminer
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