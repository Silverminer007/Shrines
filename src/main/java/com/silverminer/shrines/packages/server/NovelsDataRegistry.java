package com.silverminer.shrines.packages.server;

import com.google.common.collect.Lists;
import com.silverminer.shrines.packages.container.NovelDataContainer;
import com.silverminer.shrines.packages.datacontainer.NovelsData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class NovelsDataRegistry extends SavedData {
   public static NovelsDataRegistry INSTANCE = new NovelsDataRegistry(Lists.newArrayList());
   private static final String DATA_NAME = "shrines_novels";

   private NovelDataContainer novelsData;

   public NovelsDataRegistry(ArrayList<NovelsData> novelsData) {
      this.novelsData = new NovelDataContainer(novelsData);
   }

   public NovelsDataRegistry(NovelDataContainer novelDataContainer) {
      this.novelsData = novelDataContainer;
   }

   public boolean hasNovelOf(String structure) {
      return this.novelsData.containsKey(structure);
   }

   public NovelsData getNovelOf(String structure) {
      return this.novelsData.getByKey(structure);
   }

   public boolean setNovelOf(String structure, NovelsData newNovel) {
      this.novelsData.remove(structure);
      this.novelsData.add(newNovel);
      this.setDirty();
      return true;
   }

   public NovelDataContainer getNovelsData() {
      return novelsData;
   }

   public void setNovelsData(@Nonnull NovelDataContainer novelsData) {
      this.novelsData = novelsData;
      this.setDirty();
   }

   @Override
   public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
      return NovelDataContainer.save(this.novelsData);
   }

   public static NovelsDataRegistry load(CompoundTag tag) {
      return new NovelsDataRegistry(NovelDataContainer.load(tag));
   }

   public static void loadData(ServerLevel world) {
      if (world == null)
         return;
      DimensionDataStorage storage = world.getDataStorage();

      NovelsDataRegistry.INSTANCE = storage.computeIfAbsent(NovelsDataRegistry::load, () -> NovelsDataRegistry.INSTANCE, DATA_NAME);
   }
}