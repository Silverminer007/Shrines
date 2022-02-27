/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.container;

import com.silverminer.shrines.packages.datacontainer.TemplatePool;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public class TemplatePoolContainer implements DataContainer<TemplatePool, ResourceLocation> {
   private final HashMap<ResourceLocation, TemplatePool> templatePools;

   public TemplatePoolContainer() {
      this(new HashMap<>());
   }

   public TemplatePoolContainer(List<TemplatePool> structureTemplatePools) {
      this();
      for (TemplatePool pool : structureTemplatePools) {
         if (!templatePools.containsKey(pool.getSaveName()))
            templatePools.put(pool.getSaveName(), pool);
         else
            throw new IllegalArgumentException("template pools must have unique file names. Got duplicated key: " + pool.getSaveName());
      }
   }

   public TemplatePoolContainer(HashMap<ResourceLocation, TemplatePool> templatePools) {
      this.templatePools = templatePools;
   }

   public Stream<TemplatePool> getAsStream() {
      return this.templatePools.values().stream();
   }

   public ArrayList<TemplatePool> getAsList() {
      return new ArrayList<>(this.templatePools.values());
   }

   public Set<TemplatePool> getAsSet() {
      return new HashSet<>(this.templatePools.values());
   }

   public Iterable<TemplatePool> getAsIterable() {
      return this.templatePools.values();
   }

   public Collection<TemplatePool> getAsCollection() {
      return this.templatePools.values();
   }

   @Override
   public int getSize() {
      return this.templatePools.size();
   }

   @Nullable
   public TemplatePool getByKey(ResourceLocation poolSaveName) {
      return this.templatePools.get(poolSaveName);
   }

   public boolean add(TemplatePool templatePool) {
      return this.add(templatePool.getSaveName(), templatePool);
   }

   public boolean add(ResourceLocation poolSaveName, TemplatePool templatePool) {
      if (!this.templatePools.containsKey(poolSaveName) || this.templatePools.get(poolSaveName) == null) {
         this.templatePools.put(poolSaveName, templatePool);
         return true;
      }
      return false;
   }

   public boolean remove(ResourceLocation poolSaveName) {
      if (this.templatePools.containsKey(poolSaveName)) {
         this.templatePools.remove(poolSaveName);
         return true;
      }
      return false;
   }

   @Override
   public boolean containsKey(ResourceLocation key) {
      return this.templatePools.containsKey(key);
   }
}
