package com.silverminer.shrines.structures.novels;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.silverminer.shrines.config.Config;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class NovelsDataRegistry extends SavedData {
	public static NovelsDataRegistry INSTANCE = new NovelsDataRegistry(Lists.newArrayList());
	private static final String DATA_NAME = "shrines_novels";
	private final ArrayList<NovelsData> novelsData;

	public NovelsDataRegistry(ArrayList<NovelsData> novelsData){
		this.novelsData = novelsData;
	}

	public double getNovelAmount(String structure) {
		if(Config.SETTINGS.NEEDED_NOVELS.get() == 0) {
			return 0.0D;
		}
		for (NovelsData novel : novelsData) {
			if (novel.getStructure().equals(structure)) {
				double amount = novel.getFoundStructuresCount();
				return amount / ((double) Config.SETTINGS.NEEDED_NOVELS.get());
			}
		}
		return 0.0D;
	}

	public boolean hasNovelOf(String structure) {
		for (NovelsData novel : novelsData) {
			if (novel.getStructure().equals(structure)) {
				return true;
			}
		}
		return false;
	}

	public NovelsData getNovelOf(String structure) {
		for (NovelsData novel : novelsData) {
			if (novel.getStructure().equals(structure)) {
				return novel;
			}
		}
		return null;
	}

	public boolean setNovelOf(String structure, NovelsData newNovel) {
		if (novelsData.contains(newNovel)) {
			return false;
		}
		NovelsData oldNovel = getNovelOf(structure);
		if (oldNovel == null) {
			novelsData.add(newNovel);
			this.setDirty();
			return true;
		}
		novelsData.set(novelsData.indexOf(oldNovel), newNovel);
		this.setDirty();
		return true;
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		int i = 0;
		for (NovelsData novel : novelsData) {
			tag.put(String.valueOf(i++), novel.save());
		}
		tag.putInt("Novels", i);
		return tag;
	}

	public static NovelsDataRegistry load(CompoundTag tag) {
		ArrayList<NovelsData> novelsData = Lists.newArrayList();
		int novels = tag.getInt("Novels");
		for (int i = 0; i < novels; i++) {
			try {
				novelsData.add(NovelsData.read(tag.getCompound(String.valueOf(i))));
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		return new NovelsDataRegistry(novelsData);
	}

	public static void loadData(ServerLevel world) {
		if (world == null)
			return;
		DimensionDataStorage storage = world.getDataStorage();

		NovelsDataRegistry.INSTANCE = storage.computeIfAbsent(NovelsDataRegistry::load, () -> NovelsDataRegistry.INSTANCE, DATA_NAME);
	}
}