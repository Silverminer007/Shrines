package com.silverminer.shrines.utils.queue;

import java.util.ArrayList;

public interface Queue<T> {
   ArrayList<T> getQueue();

   void join(T element);

   void leave(T element);

   void update(int changedPosition);

   default void updateAll() {
      for (int i = 0; i < this.getQueue().size(); i++) {
         this.update(i);
      }
   }
}
