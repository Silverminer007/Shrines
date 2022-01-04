package com.silverminer.shrines.packages.datacontainer;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class NovelsData {
	public static final Codec<NovelsData> CODEC = RecordCodecBuilder.create(novelsDataInstance ->
			novelsDataInstance.group(
					Codec.STRING.fieldOf("structure").forGetter(NovelsData::getStructure),
					Codec.list(BlockPos.CODEC).fieldOf("found_structures").forGetter(NovelsData::getFoundStructures),
					Codec.INT.fieldOf("found_structures_count").forGetter(NovelsData::getFoundStructuresCount)).apply(novelsDataInstance, NovelsData::new));
	protected static final Logger LOGGER = LogManager.getLogger(NovelsData.class);
	private final String structure;
	private final ArrayList<BlockPos> found_structures = Lists.newArrayList();
	private int found_structures_count = 0;

	public NovelsData(String structure) {
		this.structure = structure;
	}

	public NovelsData(String structure, List<BlockPos> found_structures, int found_structures_count) {
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
}