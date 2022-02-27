/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.datacontainer;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.silverminer.shrines.packages.container.StructureDataContainer;
import com.silverminer.shrines.packages.container.StructureTemplateContainer;
import com.silverminer.shrines.packages.container.TemplatePoolContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StructuresPackageWrapper implements Comparable<StructuresPackageWrapper> {
   protected static final Logger LOGGER = LogManager.getLogger(StructuresPackageWrapper.class);
   private final StructuresPackageInfo structuresPacketInfo;
   private StructureDataContainer structures;
   private StructureTemplateContainer templates;
   private TemplatePoolContainer pools;
   private final UUID packageID;

   public StructuresPackageWrapper(StructuresPackageInfo structuresPackageInfo) {
      this(structuresPackageInfo, new StructureDataContainer(), new StructureTemplateContainer(), new TemplatePoolContainer(), UUID.randomUUID());
   }

   public StructuresPackageWrapper(StructuresPackageInfo structuresPacketInfo, StructureDataContainer structures, StructureTemplateContainer templates, TemplatePoolContainer pools) {
      this(structuresPacketInfo, structures, templates, pools, UUID.randomUUID());
   }

   public StructuresPackageWrapper(StructuresPackageInfo structuresPacketInfo, List<StructureData> structures, StructureTemplateContainer templates, List<TemplatePool> pools) {
      this(structuresPacketInfo, structures, templates, pools, UUID.randomUUID());
   }

   public StructuresPackageWrapper(StructuresPackageInfo structuresPacketInfo, List<StructureData> structures, StructureTemplateContainer templates, List<TemplatePool> pools, UUID packageID) {
      this(structuresPacketInfo, new StructureDataContainer(structures), templates, new TemplatePoolContainer(pools), packageID);
   }

   public StructuresPackageWrapper(StructuresPackageInfo structuresPacketInfo, StructureDataContainer structures, StructureTemplateContainer templates, TemplatePoolContainer pools, UUID packageID) {
      this.structuresPacketInfo = structuresPacketInfo;
      this.structures = structures;
      this.templates = templates;
      this.pools = pools;
      this.packageID = packageID;
   }

   public StructuresPackageInfo getStructuresPacketInfo() {
      return structuresPacketInfo;
   }

   public StructureTemplateContainer getTemplates() {
      return templates;
   }

   public void setTemplates(StructureTemplateContainer templates) {
      this.templates = templates;
   }

   public TemplatePoolContainer getPools() {
      return pools;
   }

   public void setPools(TemplatePoolContainer pools) {
      this.pools = pools;
   }

   public StructureDataContainer getStructures() {
      return structures;
   }

   public void setStructures(StructureDataContainer structures) {
      this.structures = structures;
   }

   public UUID getPackageID() {
      return packageID;
   }

   public static CompoundTag save(StructuresPackageWrapper structuresPackageWrapper) {
      CompoundTag tag = new CompoundTag();
      DataResult<Tag> dataResult = StructuresPackageInfo.CODEC.encode(structuresPackageWrapper.getStructuresPacketInfo(), NbtOps.INSTANCE, new CompoundTag());
      Optional<Tag> optionalTag = dataResult.resultOrPartial(LOGGER::error);
      if (optionalTag.isPresent()) {
         tag.put("info", optionalTag.get());
         // templates
         tag.put("templates", StructureTemplateContainer.save(structuresPackageWrapper.getTemplates()));
         //pools
         ListTag pools = new ListTag();
         for (TemplatePool templatePool : structuresPackageWrapper.getPools().getAsIterable()) {
            CompoundTag poolTag = templatePool.write();
            if (poolTag != null) {
               pools.add(poolTag);
            }
         }
         tag.put("pools", pools);
         //structures
         ListTag structures = new ListTag();
         for (StructureData structureData : structuresPackageWrapper.getStructures().getAsIterable()) {
            CompoundTag structureTag = new CompoundTag();
            structureTag.put("structure config", StructureData.saveToNBT(structureData));
            structures.add(structureTag);
         }
         tag.put("structures", structures);
         tag.putUUID("packageID", structuresPackageWrapper.getPackageID());
         return tag;
      } else {
         return null;
      }
   }

   public static StructuresPackageWrapper load(CompoundTag tag) {
      Tag info = tag.get("info");
      DataResult<Pair<StructuresPackageInfo, Tag>> dataResult = StructuresPackageInfo.CODEC.decode(NbtOps.INSTANCE, info);
      Optional<Pair<StructuresPackageInfo, Tag>> optionalStructuresPacketInfoTagPair = dataResult.resultOrPartial(LOGGER::error);
      if (optionalStructuresPacketInfoTagPair.isPresent()) {
         StructuresPackageInfo structuresPacketInfo = optionalStructuresPacketInfoTagPair.get().getFirst();
         CompoundTag templatesTag = tag.getCompound("templates");
         StructureTemplateContainer templates = StructureTemplateContainer.load(templatesTag);
         ListTag poolsTag = tag.getList("pools", 10);// Type 10 is CompoundTag
         TemplatePoolContainer pools = new TemplatePoolContainer();
         for (int i = 0; i < poolsTag.size(); i++) {
            TemplatePool templatePool = TemplatePool.read(poolsTag.getCompound(i));
            pools.add(templatePool);
         }
         ListTag structuresTag = tag.getList("structures", 10);// Type 10 is CompoundTag
         StructureDataContainer structures = new StructureDataContainer();
         for (Tag value : structuresTag) {
            CompoundTag structureTag = (CompoundTag) value;
            StructureData structureData = StructureData.loadFromNBT(structureTag.getCompound("structure config"));
            if (structureData != null) {
               structures.add(structureData);
            }
         }
         UUID packageID = tag.getUUID("packageID");
         return new
               StructuresPackageWrapper(structuresPacketInfo, structures, templates, pools, packageID);
      }
      return null;
   }

   @Override
   public int compareTo(@NotNull StructuresPackageWrapper o) {
      return this.getStructuresPacketInfo().compareTo(o.getStructuresPacketInfo());
   }
}
