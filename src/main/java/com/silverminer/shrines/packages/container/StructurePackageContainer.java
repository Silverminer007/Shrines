/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.container;

import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

/**
 * Saves structure package wrappers. Ensures unique package save directories
 * Holds all structure packages. Allows different save formats and prevent the need to rewrite all access to the structure packages
 */
public class StructurePackageContainer implements DataContainer<StructuresPackageWrapper, UUID> {
   private final HashMap<UUID, StructuresPackageWrapper> packages;

   public StructurePackageContainer() {
      this(new HashMap<>());
   }

   public StructurePackageContainer(HashMap<UUID, StructuresPackageWrapper> packages) {
      this.packages = packages;
   }

   public Stream<StructuresPackageWrapper> getAsStream() {
      return this.packages.values().stream();
   }

   public ArrayList<StructuresPackageWrapper> getAsList() {
      return new ArrayList<>(this.packages.values());
   }

   public Set<StructuresPackageWrapper> getAsSet() {
      return new HashSet<>(this.packages.values());
   }

   public Iterable<StructuresPackageWrapper> getAsIterable() {
      return this.packages.values();
   }

   public Collection<StructuresPackageWrapper> getAsCollection() {
      return this.packages.values();
   }

   @Override
   public int getSize() {
      return this.packages.size();
   }

   @Nullable
   @Override
   public StructuresPackageWrapper getByKey(UUID packageSaveName) {
      return this.packages.get(packageSaveName);
   }

   @Override
   public boolean add(StructuresPackageWrapper newPackage) {
      return this.add(newPackage.getPackageID(), newPackage);
   }

   protected boolean add(UUID packageSaveName, StructuresPackageWrapper newPackage) {
      if (!this.packages.containsKey(packageSaveName) || this.packages.get(packageSaveName) == null) {
         this.packages.put(packageSaveName, newPackage);
         return true;
      }
      return false;
   }

   public boolean remove(UUID packageSaveName) {
      if (this.packages.containsKey(packageSaveName)) {
         this.packages.remove(packageSaveName);
         return true;
      }
      return false;
   }

   @Override
   public boolean containsKey(UUID key) {
      return this.packages.containsKey(key);
   }

   public static CompoundTag save(StructurePackageContainer packages) {
      CompoundTag tag = new CompoundTag();
      int elementIdx = 0;
      for (Map.Entry<UUID, StructuresPackageWrapper> entry : packages.packages.entrySet()) {
         CompoundTag entryTag = StructuresPackageWrapper.save(entry.getValue());
         if (entryTag != null) {
            tag.put(String.valueOf(elementIdx++), entryTag);
         }
      }
      tag.putInt("size", elementIdx);
      return tag;
   }

   public static StructurePackageContainer load(@Nullable CompoundTag tag) {
      if (tag == null) {
         return null;
      }
      int elementCount = tag.getInt("size");
      HashMap<UUID, StructuresPackageWrapper> packages = new HashMap<>();
      for (int i = 0; i < elementCount; i++) {
         CompoundTag entryTag = tag.getCompound(String.valueOf(i));
         StructuresPackageWrapper wrapper = StructuresPackageWrapper.load(entryTag);
         if (wrapper != null) {
            packages.put(wrapper.getPackageID(), wrapper);
         }
      }
      return new StructurePackageContainer(packages);
   }
}
