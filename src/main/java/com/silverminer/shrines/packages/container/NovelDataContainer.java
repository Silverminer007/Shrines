/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.container;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.packages.datacontainer.NovelsData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public class NovelDataContainer implements DataContainer<NovelsData, String> {
   protected static final Logger LOGGER = LogManager.getLogger(NovelDataContainer.class);
   public static final Codec<NovelDataContainer> CODEC = RecordCodecBuilder.create(novelDataContainerInstance ->
         novelDataContainerInstance.group(
               Codec.list(NovelsData.CODEC).fieldOf("novels_data").forGetter(NovelDataContainer::getAsList))
               .apply(novelDataContainerInstance, NovelDataContainer::new));
   private final HashMap<String, NovelsData> novels = new HashMap<>();

   public NovelDataContainer(List<NovelsData> novelsDataList) {
      for (NovelsData novelsData : novelsDataList) {
         this.novels.put(novelsData.getStructure(), novelsData);
      }
   }

   @Override
   public Stream<NovelsData> getAsStream() {
      return this.novels.values().stream();
   }

   @Override
   public ArrayList<NovelsData> getAsList() {
      return new ArrayList<>(this.novels.values());
   }

   @Override
   public Set<NovelsData> getAsSet() {
      return new HashSet<>(this.novels.values());
   }

   @Override
   public Iterable<NovelsData> getAsIterable() {
      return this.novels.values();
   }

   @Override
   public Collection<NovelsData> getAsCollection() {
      return this.novels.values();
   }

   @Override
   public int getSize() {
      return this.novels.size();
   }

   @Override
   public NovelsData getByKey(String key) {
      return this.novels.get(key);
   }

   @Override
   public boolean add(NovelsData element) {
      return this.add(element.getStructure(), element);
   }

   protected boolean add(String key, NovelsData element) {
      if (this.novels.containsKey(key)) {
         return false;
      } else {
         this.novels.put(key, element);
         return true;
      }
   }

   @Override
   public boolean remove(String key) {
      if (!this.novels.containsKey(key)) {
         return false;
      } else {
         this.novels.remove(key);
         return true;
      }
   }

   @Override
   public boolean containsKey(String key) {
      return this.novels.containsKey(key);
   }

   public static CompoundTag save(NovelDataContainer novelDataContainer) {
      DataResult<Tag> dataResult = CODEC.encode(novelDataContainer, NbtOps.INSTANCE, new CompoundTag());
      Optional<Tag> optionalTag = dataResult.resultOrPartial(LOGGER::error);
      return optionalTag.map(compound -> (CompoundTag) compound).orElse(new CompoundTag());
   }

   public static NovelDataContainer load(@Nullable CompoundTag tag) {
      DataResult<Pair<NovelDataContainer, Tag>> dataResult = CODEC.decode(NbtOps.INSTANCE, tag);
      Optional<Pair<NovelDataContainer, Tag>> pairOptional = dataResult.resultOrPartial(LOGGER::error);
      return pairOptional.map(Pair::getFirst).orElse(null);
   }
}
