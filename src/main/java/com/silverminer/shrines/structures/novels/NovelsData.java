package com.silverminer.shrines.structures.novels;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NumericTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class NovelsData {
	protected static final Logger LOGGER = LogManager.getLogger(NovelsData.class);
	private final String structure;
	private final ArrayList<BlockPos> found_structures = Lists.newArrayList();
	private int found_structures_count = 0;

	public NovelsData(String structure) {
		this.structure = structure;
	}

	public NovelsData(String structure, ArrayList<BlockPos> found_structures, int found_structures_count) {
		this(structure);
		this.found_structures.clear();
		this.found_structures.addAll(found_structures);
		this.setFoundStructuresCount(found_structures_count);
	}

	public ArrayList<BlockPos> getFoundStructures() {
		return found_structures;
	}

	public int getFoundStructuresCount() {
		return found_structures_count;
	}

	public void setFoundStructuresCount(int found_structures_count) {
		this.found_structures_count = found_structures_count;
	}

	public String getStructure() {
		return structure;
	}

	public void addFoundStructure(BlockPos pos) {
		this.found_structures.add(pos);
		this.found_structures_count++;
	}

	public CompoundTag save() {
		CompoundTag tag = new CompoundTag();
		tag.putString("Structure", structure);
		tag.putInt("count", found_structures_count);
		ListTag list = new ListTag();
		list.addAll(this.found_structures.stream().map(pos -> LongTag.valueOf(pos.asLong())).collect(Collectors.toList()));
		tag.put("Places", list);
		return tag;
	}

	public static NovelsData read(CompoundTag tag) {
		String structure = tag.getString("Structure");
		int count = tag.getInt("count");
		ListTag list = (ListTag) tag.get("Places");
		ArrayList<BlockPos> places = Lists.newArrayList();
		for(int i = 0; i < list.size(); i++) {
			places.add(BlockPos.of(((NumericTag)list.get(i)).getAsLong()));
		}
		return new NovelsData(structure, places, count);
	}
}