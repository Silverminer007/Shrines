/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public class StructureIconContainer implements DataContainer<byte[], ResourceLocation> {
   private final HashMap<ResourceLocation, byte[]> icons;

   public StructureIconContainer(HashMap<ResourceLocation, byte[]> icons) {
      this.icons = icons;
   }

   public StructureIconContainer() {
      this(new HashMap<>());
   }

   public Iterable<ResourceLocation> getKeys() {
      return this.icons.keySet();
   }

   @Override
   public Stream<byte[]> getAsStream() {
      return this.icons.values().stream();
   }

   @Override
   public ArrayList<byte[]> getAsList() {
      return new ArrayList<>(this.icons.values());
   }

   @Override
   public Set<byte[]> getAsSet() {
      return new HashSet<>(this.icons.values());
   }

   @Override
   public Iterable<byte[]> getAsIterable() {
      return this.icons.values();
   }

   @Override
   public Collection<byte[]> getAsCollection() {
      return this.icons.values();
   }

   @Override
   public int getSize() {
      return this.icons.size();
   }

   @Override
   public byte[] getByKey(ResourceLocation key) {
      return this.icons.get(key);
   }

   /**
    * DO NOT USE. Use {@link #add(ResourceLocation, byte[])} instead
    * @param element the element to add
    * @return false, because this method is unable to add an element without key
    */
   @Deprecated
   @Override
   public boolean add(byte[] element) {
      // This is an edge case. We don't store a certain object, so we can't get the key from the object we can't add it to the map
      return false;
   }

   public boolean add(ResourceLocation key, byte[] element) {
      if (this.icons.containsKey(key)) {
         return false;
      } else {
         this.icons.put(key, element);
         return true;
      }
   }

   @Override
   public boolean remove(ResourceLocation key) {
      if (!this.icons.containsKey(key)) {
         return false;
      } else {
         this.icons.remove(key);
         return true;
      }
   }

   @Override
   public boolean containsKey(ResourceLocation key) {
      return this.icons.containsKey(key);
   }

   public static StructureIconContainer load(@Nullable CompoundTag tag) {
      if (tag == null) {
         return null;
      }
      HashMap<ResourceLocation, byte[]> structureIcons = new HashMap<>();
      for (int i = 0; i < tag.getInt("size"); i++) {
         CompoundTag element = tag.getCompound(String.valueOf(i));
         ResourceLocation key = new ResourceLocation(element.getString("location"));
         byte[] file = element.getByteArray("file");
         structureIcons.put(key, file);
      }
      return new StructureIconContainer(structureIcons);
   }

   public static CompoundTag save(StructureIconContainer structureIconContainer) {
      CompoundTag tag = new CompoundTag();
      tag.putInt("size", structureIconContainer.getSize());
      int i = 0;
      for (ResourceLocation key : structureIconContainer.getKeys()) {
         CompoundTag element = new CompoundTag();
         element.putString("location", key.toString());
         element.putByteArray("file", structureIconContainer.getByKey(key));
         tag.put(String.valueOf(i++), element);
      }
      return tag;
   }
}
