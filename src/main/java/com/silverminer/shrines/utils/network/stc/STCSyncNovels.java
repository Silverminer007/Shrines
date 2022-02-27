/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils.network.stc;

import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.container.NovelDataContainer;
import com.silverminer.shrines.packages.datacontainer.StructureNovel;
import com.silverminer.shrines.utils.network.IPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class STCSyncNovels implements IPacket {
   private final NovelDataContainer novelDataContainer;
   private final Map<ResourceLocation, StructureNovel> novelsRegistryData;

   public STCSyncNovels(FriendlyByteBuf buf) {
      this.novelDataContainer = NovelDataContainer.load(buf.readNbt());
      CompoundTag tag = buf.readNbt();
      this.novelsRegistryData = deserializeNovelsRegistry(Objects.requireNonNull(tag));
   }

   private static Map<ResourceLocation, StructureNovel> deserializeNovelsRegistry(CompoundTag tag) {
      Map<ResourceLocation, StructureNovel> map = new HashMap<>();
      ListTag listTag = tag.getList("list", 10);
      for (Tag entryTag : listTag) {
         if (entryTag instanceof CompoundTag compoundTag) {
            ResourceLocation key = new ResourceLocation(compoundTag.getString("key"));
            StructureNovel structureNovel = StructureNovel.deserialize(compoundTag.getCompound("value"));
            map.put(key, structureNovel);
         }
      }
      return map;
   }

   public STCSyncNovels(NovelDataContainer novels, Map<ResourceLocation, StructureNovel> novelsRegistryData) {
      this.novelDataContainer = novels;
      this.novelsRegistryData = novelsRegistryData;
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.writeNbt(NovelDataContainer.save(this.novelDataContainer));
      buf.writeNbt(serializeNovelsRegistry(this.novelsRegistryData));
   }

   private static CompoundTag serializeNovelsRegistry(Map<ResourceLocation, StructureNovel> novelsRegistryData) {
      ListTag listTag = new ListTag();
      for (Map.Entry<ResourceLocation, StructureNovel> entry : novelsRegistryData.entrySet()) {
         CompoundTag entryTag = new CompoundTag();
         entryTag.putString("key", entry.getKey().toString());
         Tag valueTag = StructureNovel.serialize(entry.getValue());
         if (!(valueTag instanceof CompoundTag)) {
            continue;
         }
         entryTag.put("value", valueTag);
         listTag.add(entryTag);
      }
      CompoundTag tag = new CompoundTag();
      tag.put("list", listTag);
      return tag;
   }

   public void handle(Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         PackageManagerProvider.CLIENT.setNovels(this.novelDataContainer);
         PackageManagerProvider.CLIENT.setNovelsRegistryData(this.novelsRegistryData);
         PackageManagerProvider.CLIENT.openNovelsOverviewScreen();
      });
      ctx.get().setPacketHandled(true);
   }
}