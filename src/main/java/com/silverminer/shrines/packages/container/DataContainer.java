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
