package com.silverminer.shrines.utils.queue;

import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.STCUpdateQueuePosition;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerQueue implements Queue<UUID> {
   private final ArrayList<UUID> queue = Lists.newArrayList();

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
      if (this.queue.contains(uuid)) {
         this.queue.remove(uuid);
      }
      this.updateAll();
   }

   public void update(int positionChanged) {
      ShrinesPacketHandler.sendTo(new STCUpdateQueuePosition(positionChanged), this.getQueue().get(positionChanged));
   }
}
