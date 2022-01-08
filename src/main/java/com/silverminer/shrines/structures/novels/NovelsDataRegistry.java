/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.structures.novels;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.config.Config;

import net.minecraft.nbt.CompoundNBT;

public class NovelsDataRegistry {
	private static final ArrayList<NovelsData> NOVELS = Lists.newArrayList();
	public static NovelDataSaver novelsDataSaver;

	public static double getNovelAmount(String structure) {
		if(Config.SETTINGS.NEEDED_NOVELS.get() == 0) {
			return 0.0D;
		}
		for (NovelsData novel : NOVELS) {
			if (novel.getStructure().equals(structure)) {
				double amount = novel.getFoundStructuresCount();
				return amount / ((double) Config.SETTINGS.NEEDED_NOVELS.get());
			}
		}
		return 0.0D;
	}

	public static boolean hasNovelOf(String structure) {
		for (NovelsData novel : NOVELS) {
			if (novel.getStructure().equals(structure)) {
				return true;
			}
		}
		return false;
	}

	public static NovelsData getNovelOf(String structure) {
		for (NovelsData novel : NOVELS) {
			if (novel.getStructure().equals(structure)) {
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