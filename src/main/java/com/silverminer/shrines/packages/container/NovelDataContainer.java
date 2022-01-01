package com.silverminer.shrines.packages.container;

import com.google.common.collect.Lists;
import com.silverminer.shrines.packages.datacontainer.NovelsData;
import net.minecraft.nbt.CompoundTag;

import java.util.*;
import java.util.stream.Stream;

public class NovelDataContainer implements DataContainer<NovelsData, String> {
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
      CompoundTag tag = new CompoundTag();
      int i = 0;
      for (NovelsData novel : novelDataContainer.getAsIterable()) {
         tag.put(String.valueOf(i++), novel.save());
      }
      tag.putInt("Novels", i);
      return tag;
   }

   public static NovelDataContainer load(CompoundTag tag) {
      ArrayList<NovelsData> novelsData = Lists.newArrayList();
      int novels = tag.getInt("Novels");
      for (int i = 0; i < novels; i++) {
         try {
            novelsData.add(NovelsData.read(tag.getCompound(String.valueOf(i))));
         } catch (Throwable t) {
            t.printStackTrace();
         }
      }
      return new NovelDataContainer(novelsData);
   }
}
