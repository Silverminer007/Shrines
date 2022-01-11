/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.queue;

import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCUpdateQueuePosition;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerQueue implements Queue<UUID> {
   private final ArrayList<UUID> queue = new ArrayList<>();

   public PlayerQueue() {
   }

   public ArrayList<UUID> getQueue() {
      return this.queue;
   }

   public void join(UUID newUUID) {
      if (!this.queue.contains(newUUID)) {
         this.queue.add(newUUID);
      }
      this.update(this.queue.size() - 1);
   }

   public void leave(UUID uuid) {
      this.queue.remove(uuid);
      this.updateAll();
   }

   public void update(int positionChanged) {
      ShrinesPacketHandler.sendTo(new STCUpdateQueuePosition(positionChanged), this.getQueue().get(positionChanged));
   }
}
