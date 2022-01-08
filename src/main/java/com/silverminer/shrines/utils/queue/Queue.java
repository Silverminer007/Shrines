/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

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
