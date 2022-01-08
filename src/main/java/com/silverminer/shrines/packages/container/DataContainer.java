/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

public interface DataContainer<T, K> {
   Stream<T> getAsStream();

   ArrayList<T> getAsList();

   Set<T> getAsSet();

   Iterable<T> getAsIterable();

   Collection<T> getAsCollection();

   int getSize();

   T getByKey(K key);

   boolean add(T element);

   boolean remove(K key);

   boolean containsKey(K key);
}
