package com.silverminer.shrines.new_custom_structures;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class StructuresPacket {
	protected String name;
	protected boolean isIncluded;

	protected List<StructureData> structures;

	public StructuresPacket(String name, ListNBT structures, boolean isIncluded) {
		this(name, structures.stream().map((inbt) -> {
			if (inbt.getId() == 10)
				return new StructureData((CompoundNBT) inbt);
			else
				return null;
		}).filter(nbt -> nbt != null).collect(Collectors.toList()), isIncluded);
	}

	public StructuresPacket(String name, List<StructureData> structures, boolean isIncluded) {
		this.name = name;
		this.structures = structures;
		this.isIncluded = isIncluded;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<StructureData> getStructures() {
		return structures;
	}

	public void setStructures(ArrayList<StructureData> structures) {
		this.structures = structures;
	}

	public boolean isIncluded() {
		return isIncluded;
	}
}