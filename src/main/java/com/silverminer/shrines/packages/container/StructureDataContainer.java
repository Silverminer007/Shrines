/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.container;

import com.silverminer.shrines.packages.datacontainer.StructureData;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public class StructureDataContainer implements DataContainer<StructureData, ResourceLocation> {
   private final HashMap<ResourceLocation, StructureData> structureData;

   public StructureDataContainer() {
      this(new HashMap<>());
   }

   public StructureDataContainer(List<StructureData> structureData) {
      this(new HashMap<>());
      for (StructureData structureData1 : structureData) {
         if (!this.structureData.containsKey(structureData1.getKey()))
            this.structureData.put(structureData1.getKey(), structureData1);
         else
            throw new IllegalArgumentException("structures must have unique keys. Got duplicated key: " + structureData1.getKey());
      }
   }

   public StructureDataContainer(HashMap<ResourceLocation, StructureData> StructureData) {
      this.structureData = StructureData;
   }

   public Stream<StructureData> getAsStream() {
      return this.structureData.values().stream();
   }

   public ArrayList<StructureData> getAsList() {
      return new ArrayList<>(this.structureData.values());
   }

   public Set<StructureData> getAsSet() {
      return new HashSet<>(this.structureData.values());
   }

   public Iterable<StructureData> getAsIterable() {
      return this.structureData.values();
   }

   public Collection<StructureData> getAsCollection() {
      return this.structureData.values();
   }

   public int getSize() {
      return this.structureData.size();
   }

   @Nullable
   public StructureData getByKey(ResourceLocation structureKey) {
      return this.structureData.get(structureKey);
   }

   public boolean add(StructureData StructureData) {
      return this.add(StructureData.getKey(), StructureData);
   }

   public boolean add(ResourceLocation structureKey, StructureData StructureData) {
      if (!this.structureData.containsKey(structureKey) || this.structureData.get(structureKey) == null) {
         this.structureData.put(structureKey, StructureData);
         return true;
      }
      return false;
   }

   @Override
   public boolean remove(ResourceLocation key) {
      if (this.structureData.containsKey(key)) {
         this.structureData.remove(key);
         return true;
      }
      return false;
   }

   @Override
   public boolean containsKey(ResourceLocation key) {
      return this.structureData.containsKey(key);
   }
}
