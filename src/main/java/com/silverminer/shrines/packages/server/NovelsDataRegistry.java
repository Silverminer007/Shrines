/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.server;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.packages.container.NovelDataContainer;
import com.silverminer.shrines.packages.datacontainer.NovelsData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NovelsDataRegistry extends SavedData {
   public static final Codec<NovelsDataRegistry> CODEC = RecordCodecBuilder.create(novelsDataRegistryInstance ->
         novelsDataRegistryInstance.group(
                     Codec.mapPair(
                                 Codec.STRING.xmap(UUID::fromString, UUID::toString).fieldOf("player_id"),
                                 NovelDataContainer.CODEC.fieldOf("novels_data"))
                           .codec().listOf().fieldOf("novels_per_player").forGetter(novelsDataRegistry -> novelsDataRegistry.novelsData))
               .apply(novelsDataRegistryInstance, NovelsDataRegistry::new));
   protected static final Logger LOGGER = LogManager.getLogger(NovelsDataRegistry.class);
   private static final String DATA_NAME = "shrines_novels";
   public static NovelsDataRegistry INSTANCE = new NovelsDataRegistry(Lists.newArrayList());
   private final List<Pair<UUID, NovelDataContainer>> novelsData;

   public NovelsDataRegistry(List<Pair<UUID, NovelDataContainer>> pairs) {
      this.novelsData = new ArrayList<>(pairs);
   }

   public static void loadData(ServerLevel world) {
      if (world == null) return;
      DimensionDataStorage storage = world.getDataStorage();

      NovelsDataRegistry.INSTANCE = storage.computeIfAbsent(NovelsDataRegistry::load, () -> NovelsDataRegistry.INSTANCE, DATA_NAME);
   }

   public static NovelsDataRegistry load(CompoundTag tag) {
      DataResult<Pair<NovelsDataRegistry, Tag>> dataResult = CODEC.decode(NbtOps.INSTANCE, tag);
      Optional<Pair<NovelsDataRegistry, Tag>> pairOptional = dataResult.resultOrPartial(LOGGER::error);
      return pairOptional.map(Pair::getFirst).orElse(null);
   }

   public boolean hasNovelOf(UUID playerID, String structure) {
      return this.getNovelsData(playerID).containsKey(structure);
   }

   public NovelDataContainer getNovelsData(UUID playerID) {
      List<Pair<UUID, NovelDataContainer>> novel_of_player = this.novelsData.stream().filter(pair -> pair.getFirst().equals(playerID)).toList();
      return novel_of_player.size() > 0 ? novel_of_player.get(0).getSecond() : new NovelDataContainer(new ArrayList<>());
   }

   public NovelsData getNovelOf(UUID playerID, String structure) {
      return this.getNovelsData(playerID).getByKey(structure);
   }

   public void setNovelOf(UUID playerID, String structure, NovelsData newNovel) {
      NovelDataContainer novelDataContainer = this.getNovelsData(playerID);
      novelDataContainer.remove(structure);
      novelDataContainer.add(newNovel);
      this.setNovelsData(playerID, novelDataContainer);
      this.setDirty();
   }

   public void setNovelsData(UUID playerID, @Nonnull NovelDataContainer novelsData) {
      this.novelsData.removeIf(pair -> pair.getFirst().equals(playerID));
      this.novelsData.add(Pair.of(playerID, novelsData));
      this.setDirty();
   }

   @Override
   public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
      DataResult<Tag> dataResult = CODEC.encode(this, NbtOps.INSTANCE, tag);
      Optional<Tag> optionalTag = dataResult.resultOrPartial(LOGGER::error);
      return optionalTag.map(compound -> (CompoundTag) compound).orElse(tag);
   }
}