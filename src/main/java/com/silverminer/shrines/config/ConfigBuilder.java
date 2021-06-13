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
package com.silverminer.shrines.config;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * @author Silverminer
 *
 */
public class ConfigBuilder implements IStructureConfig {
	protected final Type type;
	protected final String name;
	protected boolean generate = true;
	protected double spawnChance = 0.6D;
	protected boolean needsGround = true;
	protected int distance = 60;
	protected int separation = 12;
	protected final int seed;
	protected ArrayList<Category> biomes = Lists.newArrayList(Biome.Category.PLAINS, Biome.Category.FOREST,
			Biome.Category.TAIGA, Biome.Category.SAVANNA, Biome.Category.JUNGLE, Biome.Category.MESA,
			Biome.Category.ICY, Biome.Category.DESERT, Biome.Category.SWAMP, Biome.Category.MUSHROOM);
	protected ArrayList<String> blacklist = Lists.newArrayList();
	protected ArrayList<String> dimensions = Lists.newArrayList("minecraft:overworld");
	protected boolean useRandomVarianting = true;
	protected double lootChance = 1.0D;
	protected boolean spawn_villagers = true;

	public ConfigBuilder(String name, int seed, Type typeIn) {
		this.name = name;
		this.seed = seed;
		this.type = typeIn;
	}

	public NewStructureConfig build(final ForgeConfigSpec.Builder SERVER_BUILDER) {
		switch (this.type) {
		case NORMAL:
			return new NewStructureConfig(SERVER_BUILDER, name, generate, spawnChance, needsGround, distance,
					separation, seed, biomes, blacklist, dimensions, useRandomVarianting, null, null);
		case LOOTABLE:
			return new NewStructureConfig(SERVER_BUILDER, name, generate, spawnChance, needsGround, distance,
					separation, seed, biomes, blacklist, dimensions, useRandomVarianting, lootChance, null);
		case HARBOUR:
			return new NewStructureConfig(SERVER_BUILDER, name, generate, spawnChance, needsGround, distance,
					separation, seed, biomes, blacklist, dimensions, useRandomVarianting, lootChance, spawn_villagers);
		}
		return null;
	}

	public ConfigBuilder setGenerate(boolean generate) {
		this.generate = generate;
		return this;
	}

	public ConfigBuilder setSpawnVillagers(boolean spawn_villagers) {
		this.spawn_villagers = spawn_villagers;
		return this;
	}

	public ConfigBuilder setLootChance(double lootChance) {
		this.lootChance = lootChance;
		return this;
	}

	public ConfigBuilder addToBlacklist(String... blacklist) {
		for (String s : blacklist)
			this.blacklist.add(s);
		return this;
	}

	public ConfigBuilder addDimension(String dimension) {
		this.dimensions.add(dimension);
		return this;
	}

	public ConfigBuilder setDimension(ArrayList<String> dimension) {
		this.dimensions = dimension;
		return this;
	}

	public ConfigBuilder setSpawnChance(double spawnChance) {
		this.spawnChance = spawnChance;
		return this;
	}

	public ConfigBuilder setDistance(int distance) {
		this.distance = distance;
		return this;
	}

	public ConfigBuilder setSeparation(int separation) {
		this.separation = separation;
		return this;
	}

	public ConfigBuilder setNeedsGround(boolean needsGround) {
		this.needsGround = needsGround;
		return this;
	}

	public ConfigBuilder setBiomes(Category... biomes) {
		this.biomes = Lists.newArrayList(biomes);
		return this;
	}

	public ConfigBuilder addBiome(Category biome) {
		this.biomes.add(biome);
		return this;
	}

	public ConfigBuilder removeBiome(Category biome) {
		this.biomes.add(biome);
		return this;
	}

	public ConfigBuilder setUseRandomVarianting(boolean useRandomVarianting) {
		this.useRandomVarianting = useRandomVarianting;
		return this;
	}

	@Override
	public int compareTo(IStructureConfig o) {
		return this.name.compareTo(o.getName());
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean getGenerate() {
		return this.generate;
	}

	@Override
	public double getSpawnChance() {
		return this.spawnChance;
	}

	@Override
	public boolean getNeedsGround() {
		return this.needsGround;
	}

	@Override
	public int getDistance() {
		return this.distance;
	}

	@Override
	public int getSeparation() {
		return this.separation;
	}

	@Override
	public int getSeed() {
		return this.seed;
	}

	@Override
	public List<? extends Category> getWhitelist() {
		return this.biomes;
	}

	@Override
	public List<? extends String> getBlacklist() {
		return this.blacklist;
	}

	@Override
	public List<? extends String> getDimensions() {
		return this.dimensions;
	}

	@Override
	public boolean getUseRandomVarianting() {
		return this.useRandomVarianting;
	}

	@Override
	public double getLootChance() {
		return this.lootChance;
	}

	@Override
	public boolean getSpawnVillagers() {
		return this.spawn_villagers;
	}

	public static enum Type {
		NORMAL, LOOTABLE, HARBOUR;
	}

	@Override
	public boolean isBuiltIn() {
		return true;
	}

	@Override
	public boolean getActive() {
		return this.generate;
	}

	@Override
	public void setActive(boolean value) {
		this.setGenerate(value);
	}

	@Override
	public List<? extends IConfigOption<?>> getAllOptions() {
		return Lists.newArrayList();
	}
}