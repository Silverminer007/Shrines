package com.silverminer.shrines.new_custom_structures.novels;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;

import net.minecraft.nbt.CompoundNBT;

public class NovelsDataRegistry {
	private static final ArrayList<NovelsData> NOVELS = Lists.newArrayList();
	public static NovelDataSaver novelsDataSaver;
	private static final double NEEDED_NOVELS = 5.0D;// TODO Move to config option

	public static double getNovelAmount(String structure) {
		for (NovelsData novel : NOVELS) {
			if (novel.getStructure() == structure) {
				return novel.getFoundStructuresCount() / NEEDED_NOVELS;
			}
		}
		return 0.0D;
	}

	public static boolean hasNovelOf(String structure) {
		for (NovelsData novel : NOVELS) {
			if (novel.getStructure() == structure) {
				return true;
			}
		}
		return false;
	}

	public static NovelsData getNovelOf(String structure) {
		for (NovelsData novel : NOVELS) {
			if (novel.getStructure() == structure) {
				return novel;
			}
		}
		return null;
	}

	public static boolean setNovelOf(String structure, NovelsData newNovel) {
		if (NOVELS.contains(newNovel)) {
			return false;
		}
		NovelsData oldNovel = getNovelOf(structure);
		if (oldNovel == null) {
			NOVELS.add(newNovel);
			novelsDataSaver.setDirty();
			return true;
		}
		NOVELS.set(NOVELS.indexOf(oldNovel), newNovel);
		novelsDataSaver.setDirty();
		return true;
	}

	public static CompoundNBT write() {
		CompoundNBT tag = new CompoundNBT();
		int i = 0;
		for (NovelsData novel : NOVELS) {
			tag.put(String.valueOf(i++), novel.save());
		}
		tag.putInt("Novels", i);
		ShrinesMod.LOGGER.info("{}", i);
		return tag;
	}

	public static void read(CompoundNBT tag) {
		NOVELS.clear();
		int novels = tag.getInt("Novels");
		for (int i = 0; i < novels; i++) {
			try {
				NOVELS.add(NovelsData.read(tag.getCompound(String.valueOf(i))));
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
}