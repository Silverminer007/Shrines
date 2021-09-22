/**
 * Silverminer (and Team)
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * 
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.structures.load;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;

import net.minecraft.util.ResourceLocation;

/**
 * @author Silverminer
 *
 */
public class DefaultedStructureData {
	protected final String name;
	protected final String key;
	protected boolean transformLand = true;
	protected boolean generate = true;
	protected double spawnChance = 0.6D;
	protected boolean useRandomVarianting = true;
	protected int distance = 60;
	protected int seperation = 12;
	protected final int seed_modifier;
	protected int height_offset;
	protected ArrayList<String> biome_blacklist = Lists.newArrayList();
	protected ArrayList<String> dimension_whitelist = Lists.newArrayList("minecraft:overworld");
	protected String novel = "";

	public DefaultedStructureData(String name, String key, int seed_modifier) {
		this.name = name;
		this.key = new ResourceLocation(ShrinesMod.MODID, key).toString();
		this.seed_modifier = seed_modifier;
	}

	public DefaultedStructureData setNovel(String novel) {
		this.novel = novel;
		return this;
	}

	public DefaultedStructureData setGenerate(boolean generate) {
		this.generate = generate;
		return this;
	}

	public DefaultedStructureData addToBiomeBlacklist(String... blacklist) {
		for (String s : blacklist)
			this.biome_blacklist.add(s);
		return this;
	}

	public DefaultedStructureData addDimensionToWhitelist(String dimension) {
		this.dimension_whitelist.add(dimension);
		return this;
	}

	public DefaultedStructureData setDimensionWhitelist(ArrayList<String> dimension) {
		this.dimension_whitelist = dimension;
		return this;
	}

	public DefaultedStructureData setSpawnChance(double spawnChance) {
		this.spawnChance = spawnChance;
		return this;
	}

	public DefaultedStructureData setDistance(int distance) {
		this.distance = distance;
		return this;
	}

	public DefaultedStructureData setSeperation(int seperation) {
		this.seperation = seperation;
		return this;
	}

	public DefaultedStructureData setUseRandomVarianting(boolean useRandomVarianting) {
		this.useRandomVarianting = useRandomVarianting;
		return this;
	}

	public String getName() {
		return this.name;
	}

	public boolean getGenerate() {
		return this.generate;
	}

	public double getSpawnChance() {
		return this.spawnChance;
	}

	public int getDistance() {
		return this.distance;
	}

	public int getSeperation() {
		return this.seperation;
	}

	public int getSeedModifier() {
		return this.seed_modifier;
	}

	public List<String> getBiomeBlacklist() {
		return this.biome_blacklist;
	}

	public List<String> getDimensionWhitelist() {
		return this.dimension_whitelist;
	}

	public boolean getUseRandomVarianting() {
		return this.useRandomVarianting;
	}

	public boolean isBuiltIn() {
		return true;
	}

	public boolean getActive() {
		return this.generate;
	}

	public void setActive(boolean value) {
		this.setGenerate(value);
	}

	public boolean isTransformLand() {
		return transformLand;
	}

	public DefaultedStructureData setTransformLand(boolean transformLand) {
		this.transformLand = transformLand;
		return this;
	}

	public int getHeight_offset() {
		return height_offset;
	}

	public DefaultedStructureData setHeight_offset(int height_offset) {
		this.height_offset = height_offset;
		return this;
	}

	public String getKey() {
		return key;
	}

	public String getNovel() {
		return this.novel;
	}
}