/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.datacontainer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class FilledStructureTemplate extends StructureTemplate {
   private final CompoundTag templateData;

   public FilledStructureTemplate(ResourceLocation templateLocation, CompoundTag templateData) {
      this(templateLocation, templateData, UUID.randomUUID());
   }

   public FilledStructureTemplate(ResourceLocation templateLocation, CompoundTag templateData, UUID templateID) {
      super(templateLocation, templateID);
      this.templateData = templateData;
   }

   public CompoundTag getTemplateData() {
      return templateData;
   }

   public StructureTemplate withoutTemplateData() {
      return new StructureTemplate(this.getTemplateLocation(), this.getTemplateID());
   }

   @Override
   public CompoundTag save() {
      CompoundTag tag = super.save();
      tag.put("templateData", this.getTemplateData());
      return tag;
   }
}
