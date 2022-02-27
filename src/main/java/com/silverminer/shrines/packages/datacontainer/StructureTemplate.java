/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.datacontainer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class StructureTemplate {
   private final UUID templateID;
   private ResourceLocation templateLocation;

   public StructureTemplate(ResourceLocation templateLocation) {
      this(templateLocation, UUID.randomUUID());
   }

   public StructureTemplate(ResourceLocation templateLocation, UUID templateID) {
      this.templateID = templateID;
      this.templateLocation = templateLocation;
   }

   public UUID getTemplateID() {
      return templateID;
   }

   public ResourceLocation getTemplateLocation() {
      return templateLocation;
   }

   public void setTemplateLocation(ResourceLocation templateLocation) {
      this.templateLocation = templateLocation;
   }

   public CompoundTag save() {
      CompoundTag tag = new CompoundTag();
      tag.putUUID("templateID", this.getTemplateID());
      tag.putString("templateLocation", this.getTemplateLocation().toString());
      return tag;
   }

   public static StructureTemplate load(CompoundTag tag) {
      UUID templateID = tag.getUUID("templateID");
      ResourceLocation templateLocation = new ResourceLocation(tag.getString("templateLocation"));
      if (tag.contains("templateData", 10)) {
         CompoundTag templateData = tag.getCompound("templateData");
         return new FilledStructureTemplate(templateLocation, templateData, templateID);
      } else {
         return new StructureTemplate(templateLocation, templateID);
      }
   }
}
