/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.container;

import com.silverminer.shrines.packages.datacontainer.StructureTemplate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructureTemplateContainer implements DataContainer<ResourceLocation, UUID> {
   private final HashMap<UUID, StructureTemplate> templates = new HashMap<>();

   public StructureTemplateContainer() {
   }

   @Override
   public Stream<ResourceLocation> getAsStream() {
      return templates.values().stream().map(StructureTemplate::getTemplateLocation);
   }

   @Override
   public ArrayList<ResourceLocation> getAsList() {
      return this.getAsStream().collect(Collectors.toCollection(ArrayList::new));
   }

   public ArrayList<StructureTemplate> getElementsAsList() {
      return new ArrayList<>(this.templates.values());
   }

   @Override
   public Set<ResourceLocation> getAsSet() {
      return this.getAsStream().collect(Collectors.toSet());
   }

   @Override
   public Iterable<ResourceLocation> getAsIterable() {
      return this.getAsList();
   }

   @Override
   public Collection<ResourceLocation> getAsCollection() {
      return this.getAsList();
   }

   public List<UUID> getKeysAsList(){
      return new ArrayList<>(this.templates.keySet());
   }
   @Override
   public int getSize() {
      return this.templates.size();
   }

   @Override
   public ResourceLocation getByKey(UUID key) {
      return this.templates.get(key).getTemplateLocation();
   }

   public StructureTemplate getElementByKey(UUID key) {
      return this.templates.get(key);
   }

   @Deprecated
   @Override
   public boolean add(ResourceLocation element) {
      return false;
      // Impossible without ID
   }

   public boolean add(UUID templateID, ResourceLocation templateLocation) {
      return this.add(new StructureTemplate(templateLocation, templateID));
   }

   public boolean add(StructureTemplate structureTemplate) {
      return this.add(structureTemplate.getTemplateID(), structureTemplate);
   }

   protected boolean add(UUID templateID, StructureTemplate structureTemplate) {
      if (!this.templates.containsKey(templateID)) {
         this.templates.put(templateID, structureTemplate);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean remove(UUID key) {
      if (this.templates.containsKey(key)) {
         this.templates.remove(key);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean containsKey(UUID key) {
      return this.templates.containsKey(key);
   }

   public static CompoundTag save(StructureTemplateContainer structureTemplateContainer) {
      CompoundTag tag = new CompoundTag();
      ListTag templates = new ListTag();
      templates.addAll(structureTemplateContainer.templates.values().stream().map(StructureTemplate::save).toList());
      tag.put("templates", templates);
      return tag;
   }

   public static StructureTemplateContainer load(CompoundTag tag) {
      ListTag templates = tag.getList("templates", 10);
      StructureTemplateContainer structureTemplateContainer = new StructureTemplateContainer();
      for (Tag templateTag : templates) {
         if (templateTag instanceof CompoundTag compoundTag) {
            structureTemplateContainer.add(StructureTemplate.load(compoundTag));
         }
      }
      return structureTemplateContainer;
   }
}
