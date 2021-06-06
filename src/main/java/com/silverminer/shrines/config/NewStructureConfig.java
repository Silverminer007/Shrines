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

import java.util.List;
import java.util.Locale;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Silverminer
 *
 */
public class NewStructureConfig implements IStructureConfig {
	protected final String name;
	protected final ForgeConfigSpec.ConfigValue<Boolean> GENERATE;
	protected final ForgeConfigSpec.ConfigValue<Double> SPAWN_CHANCE;
	protected final ForgeConfigSpec.ConfigValue<Boolean> NEEDS_GROUND;
	protected final ForgeConfigSpec.ConfigValue<Integer> DISTANCE;
	protected final ForgeConfigSpec.ConfigValue<Integer> SEPARATION;
	protected final ForgeConfigSpec.ConfigValue<Integer> SEED;
	protected final ForgeConfigSpec.ConfigValue<List<? extends Biome.Category>> BIOME_CATEGORIES;
	protected final ForgeConfigSpec.ConfigValue<List<? extends String>> BIOME_BLACKLIST;
	protected final ForgeConfigSpec.ConfigValue<List<? extends String>> DIMENSIONS;
	protected final ForgeConfigSpec.ConfigValue<Boolean> USE_RANDOM_VARIANTING;
	protected final ForgeConfigSpec.ConfigValue<Double> LOOT_CHANCE;
	protected final ForgeConfigSpec.ConfigValue<Boolean> SPAWN_VILLAGERS;
	protected final List<ShrinesConfigOption<?>> OPTIONS = Lists.newArrayList();

	protected static final RuntimeException INVALID_ACCESS = new RuntimeException(
			"Tried to access an non initialised config value");

	public NewStructureConfig(final ForgeConfigSpec.Builder SERVER_BUILDER, String name, Boolean generate,
			Double spawn_chance, Boolean needs_ground, Integer distance, Integer seperation, Integer seed,
			List<Biome.Category> categories, List<String> blacklist, List<String> dimensions,
			Boolean useRandomVarianting, Double loot_chance, Boolean spawn_villagers) {
		this.name = name;
		String dataName = name.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
		if (generate != null) {
			GENERATE = SERVER_BUILDER.comment("Generate " + name + "s?").worldRestart()
					.define("structures." + dataName + ".generate", generate);
			OPTIONS.add(new ShrinesConfigOption<Boolean>(GENERATE, generate));
		} else {
			GENERATE = null;
		}
		if (spawn_chance != null) {
			SPAWN_CHANCE = SERVER_BUILDER.comment(name + " Spawn Chance [default: " + spawn_chance + "]").worldRestart()
					.defineInRange("structures." + dataName + ".spawn_chance", spawn_chance, 0.0, 1.0);
			OPTIONS.add(new ShrinesConfigOption<Double>(SPAWN_CHANCE, spawn_chance));
		} else {
			SPAWN_CHANCE = null;
		}
		if (needs_ground != null) {
			NEEDS_GROUND = SERVER_BUILDER.comment("Needs " + name + " Ground? [default: " + needs_ground + "]")
					.worldRestart().define("structures." + dataName + ".needs_ground", needs_ground);
			OPTIONS.add(new ShrinesConfigOption<Boolean>(NEEDS_GROUND, needs_ground));
		} else {
			NEEDS_GROUND = null;
		}
		if (distance != null) {
			DISTANCE = SERVER_BUILDER.comment(name + " Distance (in chunks) [default: " + distance + "]").worldRestart()
					.defineInRange("structures." + dataName + ".distance", distance, 1, 500);
			OPTIONS.add(new ShrinesConfigOption<Integer>(DISTANCE, distance));
		} else {
			DISTANCE = null;
		}
		if (seperation != null) {
			SEPARATION = SERVER_BUILDER.comment(name + " Minimum Separation (in chunks) [default: " + seperation + "]")
					.worldRestart().defineInRange("structures." + dataName + ".seperation", seperation, 1, 500);
			OPTIONS.add(new ShrinesConfigOption<Integer>(SEPARATION, seperation));
		} else {
			SEPARATION = null;
		}
		if (seed != null) {
			SEED = SERVER_BUILDER
					.comment(name + " Seed (Only Change if you know what you are doing)[default: " + seed + "]")
					.worldRestart()
					.defineInRange("structures." + dataName + ".seed", seed, Integer.MIN_VALUE, Integer.MAX_VALUE);
			OPTIONS.add(new ShrinesConfigOption<Integer>(SEED, seed));
		} else {
			SEED = null;
		}
		if (categories != null) {
			BIOME_CATEGORIES = SERVER_BUILDER.comment("Biome Types the " + name + " can generate in").worldRestart()
					.defineList("structures." + dataName + ".categories", categories,
							NewStructureConfig::validateBiomeCategory);
			OPTIONS.add(new ShrinesConfigOption<List<? extends Category>>(BIOME_CATEGORIES, categories));
		} else {
			BIOME_CATEGORIES = null;
		}
		if (blacklist != null) {
			BIOME_BLACKLIST = SERVER_BUILDER.comment("Biomes the " + name + " can NOT generate in").worldRestart()
					.defineList("structures." + dataName + ".blacklist", blacklist, NewStructureConfig::validateBiome);
			OPTIONS.add(new ShrinesConfigOption<List<? extends String>>(BIOME_BLACKLIST, blacklist));
		} else {
			BIOME_BLACKLIST = null;
		}
		if (dimensions != null) {
			DIMENSIONS = SERVER_BUILDER.comment("Here's a list of all dimensions where " + name + " can spawn in")
					.worldRestart().defineList("structures." + dataName + ".dimensions", dimensions, o -> o != null);
			OPTIONS.add(new ShrinesConfigOption<List<? extends String>>(DIMENSIONS, dimensions));
		} else {
			DIMENSIONS = null;
		}
		if (useRandomVarianting != null) {
			USE_RANDOM_VARIANTING = SERVER_BUILDER
					.comment("Use Random Varianting for " + name + "? [default: " + useRandomVarianting + "]")
					.worldRestart().define("structures." + dataName + ".use_random_varianting", useRandomVarianting);
			OPTIONS.add(new ShrinesConfigOption<Boolean>(USE_RANDOM_VARIANTING, useRandomVarianting));
		} else {
			USE_RANDOM_VARIANTING = null;
		}
		if (loot_chance != null) {
			LOOT_CHANCE = SERVER_BUILDER.comment(name + " Generate Loot Chance [default: " + loot_chance + "]")
					.worldRestart().defineInRange("structures." + dataName.toLowerCase(Locale.ROOT) + ".loot_chance",
							loot_chance, 0.0, 1.0);
			OPTIONS.add(new ShrinesConfigOption<Double>(LOOT_CHANCE, loot_chance));
		} else {
			LOOT_CHANCE = null;
		}
		if (spawn_villagers != null) {
			SPAWN_VILLAGERS = SERVER_BUILDER.comment(name + " Spawn Villagers [default: " + spawn_villagers + "]")
					.worldRestart()
					.define("structures." + dataName.toLowerCase(Locale.ROOT) + ".spawn_villagers", spawn_villagers);
			OPTIONS.add(new ShrinesConfigOption<Boolean>(SPAWN_VILLAGERS, spawn_villagers));
		} else {
			SPAWN_VILLAGERS = null;
		}
	}

	private static boolean validateBiome(Object o) {
		return o == null || ForgeRegistries.BIOMES.containsKey(new ResourceLocation((String) o));
	}

	private static boolean validateBiomeCategory(Object o) {
		for (Biome.Category category : Biome.Category.values()) {
			try {
				if (category == Biome.Category.valueOf((String) o)) {
					return true;
				}
			} catch (ClassCastException e) {
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean getGenerate() {
		if (GENERATE == null) {
			throw INVALID_ACCESS;
		} else {
			return GENERATE.get();
		}
	}

	@Override
	public double getSpawnChance() {
		if (SPAWN_CHANCE == null) {
			throw INVALID_ACCESS;
		} else {
			return SPAWN_CHANCE.get();
		}
	}

	@Override
	public boolean getNeedsGround() {
		if (NEEDS_GROUND == null) {
			throw INVALID_ACCESS;
		} else {
			return NEEDS_GROUND.get();
		}
	}

	@Override
	public int getDistance() {
		if (DISTANCE == null) {
			throw INVALID_ACCESS;
		} else {
			return DISTANCE.get();
		}
	}

	@Override
	public int getSeparation() {
		if (SEPARATION == null) {
			throw INVALID_ACCESS;
		} else {
			return SEPARATION.get();
		}
	}

	@Override
	public int getSeed() {
		if (SEED == null) {
			throw INVALID_ACCESS;
		} else {
			return SEED.get();
		}
	}

	@Override
	public List<? extends Category> getWhitelist() {
		if (BIOME_CATEGORIES == null) {
			throw INVALID_ACCESS;
		} else {
			return BIOME_CATEGORIES.get();
		}
	}

	@Override
	public List<? extends String> getBlacklist() {
		if (BIOME_BLACKLIST == null) {
			throw INVALID_ACCESS;
		} else {
			return BIOME_BLACKLIST.get();
		}
	}

	@Override
	public List<? extends String> getDimensions() {
		if (DIMENSIONS == null) {
			throw INVALID_ACCESS;
		} else {
			return DIMENSIONS.get();
		}
	}

	@Override
	public boolean getUseRandomVarianting() {
		if (USE_RANDOM_VARIANTING == null) {
			throw INVALID_ACCESS;
		} else {
			return USE_RANDOM_VARIANTING.get();
		}
	}

	@Override
	public double getLootChance() {
		if (LOOT_CHANCE == null) {
			throw INVALID_ACCESS;
		} else {
			return LOOT_CHANCE.get();
		}
	}

	@Override
	public boolean getSpawnVillagers() {
		if (SPAWN_VILLAGERS == null) {
			throw INVALID_ACCESS;
		} else {
			return SPAWN_VILLAGERS.get();
		}
	}

	@Override
	public int compareTo(IStructureConfig o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public boolean isBuiltIn() {
		return true;
	}

	@Override
	public boolean getActive() {
		return this.getGenerate();
	}

	@Override
	public void setActive(boolean value) {
		this.GENERATE.set(value);
		this.GENERATE.save();
	}

	@Override
	public List<? extends IConfigOption<?>> getAllOptions() {
		return this.OPTIONS;
	}
}