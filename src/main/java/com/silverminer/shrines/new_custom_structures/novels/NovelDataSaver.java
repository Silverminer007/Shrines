package com.silverminer.shrines.new_custom_structures.novels;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class NovelDataSaver extends WorldSavedData {
	protected static final Logger LOG = LogManager.getLogger(NovelDataSaver.class);
	private static final String DATA_NAME = "shrines_novels";

	public NovelDataSaver() {
		super(DATA_NAME);
	}

	@Override
	public void load(CompoundNBT tag) {
		NovelsDataRegistry.read(tag);
	}

	@Override
	public CompoundNBT save(CompoundNBT tag) {
		return NovelsDataRegistry.write();
	}

	public static NovelDataSaver get(ServerWorld world) {
		if (world == null)
			return null;
		DimensionSavedDataManager storage = world.getDataStorage();

		return storage.computeIfAbsent(NovelDataSaver::new, DATA_NAME);
	}

}