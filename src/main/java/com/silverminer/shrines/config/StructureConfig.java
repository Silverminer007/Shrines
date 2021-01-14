package com.silverminer.shrines.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.registries.ForgeRegistries;

public class StructureConfig {
	public final StructureGenConfig BALLON;
	public final StructureGenConfig BEES;
	public final LootableStructureGenConfig HIGH_TEMPEL;
	public final StructureGenConfig SMALL_TEMPEL;
	public final LootableStructureGenConfig NETHER_SHRINE;
	public final LootableStructureGenConfig NETHER_PYRAMID;
	public final StructureGenConfig WATER_SHRINE;
	public final LootableStructureGenConfig PLAYER_HOUSE;
	public final LootableStructureGenConfig MINERAL_TEMPLE;
	public final LootableStructureGenConfig FLOODED_TEMPLE;
	public final StructureGenConfig HARBOUR;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLISTED_BIOMES;

	public StructureConfig(final ForgeConfigSpec.Builder SERVER_BUILDER) {
		BALLON = new StructureGenConfig.ConfigBuilder("Ballon", "ballon", 143665).setDistance(50).setSeparation(8)
				.setNeedsGround(false).build(SERVER_BUILDER);
		BEES = new StructureGenConfig.ConfigBuilder("Bees", "bees", 779806245).setDistance(70).setSeparation(12)
				.setUseRandomVarianting(false).build(SERVER_BUILDER);
		HIGH_TEMPEL = new LootableStructureGenConfig.LootableConfigBuilder("High Tempel", "high_tempel", 536987987)
				.setDistance(85).setSeparation(18).build(SERVER_BUILDER);
		SMALL_TEMPEL = new StructureGenConfig.ConfigBuilder("Small Tempel", "small_tempel", 4765321).setDistance(75)
				.setSeparation(13).build(SERVER_BUILDER);
		NETHER_SHRINE = new LootableStructureGenConfig.LootableConfigBuilder("Nether Shrine", "nether_shrine", 653267)
				.setDistance(80).setSeparation(15).build(SERVER_BUILDER);
		NETHER_PYRAMID = new LootableStructureGenConfig.LootableConfigBuilder("Nether Pyramid", "nether_pyramid",
				7428394).setDistance(150).setSeparation(50).build(SERVER_BUILDER);
		WATER_SHRINE = new StructureGenConfig.ConfigBuilder("Water Shrine", "water_shrine", 643168754).setDistance(80)
				.setSeparation(15).build(SERVER_BUILDER);
		PLAYER_HOUSE = new LootableStructureGenConfig.LootableConfigBuilder("Player House", "player_house", 751963298)
				.setDistance(80).setSeparation(15).build(SERVER_BUILDER);
		MINERAL_TEMPLE = new LootableStructureGenConfig.LootableConfigBuilder("Mineral Temple", "mineral_temple",
				576143753).setDistance(50).setSeparation(10).setUseRandomVarianting(false).build(SERVER_BUILDER);
		FLOODED_TEMPLE = new LootableStructureGenConfig.LootableConfigBuilder("Flooded Temple", "flooded_temple",
				54315143).setDistance(50).setSeparation(10).setUseRandomVarianting(false).build(SERVER_BUILDER);
		HARBOUR = new StructureGenConfig.ConfigBuilder("Harbour", "harbour", 789034134).setDistance(50).setSeparation(8)
				.build(SERVER_BUILDER);
		BLACKLISTED_BIOMES = SERVER_BUILDER
				.comment("Structure Generation Config", "Take care what you change, this changes may cant be undone",
						"", "Biomes in which Structures cant generate in")
				.defineList("structures.blacklisted_biomes",
						getAllBiomesForCategory(Biome.Category.THEEND, Biome.Category.NETHER),
						StructureConfig::validateBiome);
	}

	public static class StructureGenConfig {
		public final ForgeConfigSpec.BooleanValue GENERATE;
		public final ForgeConfigSpec.DoubleValue SPAWN_CHANCE;
		public final ForgeConfigSpec.BooleanValue NEEDS_GROUND;
		public final ForgeConfigSpec.IntValue DISTANCE;
		public final ForgeConfigSpec.IntValue SEPARATION;
		public final ForgeConfigSpec.IntValue SEED;
		public final ForgeConfigSpec.ConfigValue<List<? extends Biome.Category>> BIOME_CATEGORIES;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> BIOME_BLACKLIST;
		public final ForgeConfigSpec.BooleanValue USE_RANDOM_VARIANTING;

		public StructureGenConfig(final ForgeConfigSpec.Builder SERVER_BUILDER, String name, String dataName,
				double dSpawnChance, int dDistance, int dSeparation, int dSeed, boolean needsGround,
				boolean useRandomVarianting, Category... biomeCategories) {
			biomeCategories = biomeCategories == null
					? new Category[] { Biome.Category.PLAINS, Biome.Category.FOREST, Biome.Category.TAIGA }
					: biomeCategories;
			dataName = dataName.toLowerCase(Locale.ROOT);
			GENERATE = SERVER_BUILDER.comment("Generate " + name + "s?").define("structures." + dataName + ".generate",
					true);
			NEEDS_GROUND = SERVER_BUILDER.comment("Needs " + name + " Ground? [default: " + needsGround + "]")
					.define("structures." + dataName + ".needs_ground", needsGround);
			SPAWN_CHANCE = SERVER_BUILDER.comment(name + " Spawn Chance [default: " + dSpawnChance + "]")
					.defineInRange("structures." + dataName + ".spawn_chance", dSpawnChance, 0.0, 1.0);
			DISTANCE = SERVER_BUILDER.comment(name + " Distance (in chunks) [default: " + dDistance + "]")
					.defineInRange("structures." + dataName + ".distance", dDistance, 1, 500);
			SEPARATION = SERVER_BUILDER.comment(name + " Minimum Separation (in chunks) [default: " + dSeparation + "]")
					.defineInRange("structures." + dataName + ".separation", dSeparation, 1, 500);
			SEED = SERVER_BUILDER
					.comment(name + " Seed (Only Change if you know what you are doing)[default: " + dSeed + "]")
					.defineInRange("structures." + dataName + ".seed", dSeed, Integer.MIN_VALUE, Integer.MAX_VALUE);
			BIOME_CATEGORIES = SERVER_BUILDER.comment("Biome Types the " + name + " can generate in").defineList(
					"structures." + dataName + ".biome_categories", Arrays.asList(biomeCategories),
					StructureConfig::validateBiomeCategory);
			BIOME_BLACKLIST = SERVER_BUILDER.comment("Biomes the " + name + " can NOT generate in").defineList(
					"structures." + dataName + ".biome_blacklist", Collections.emptyList(),
					StructureConfig::validateBiome);
			USE_RANDOM_VARIANTING = SERVER_BUILDER
					.comment("Use Random Varianting for " + name + "? [default: " + useRandomVarianting + "]")
					.define("structures." + dataName + ".varianting", useRandomVarianting);
		}

		public static class ConfigBuilder {
			protected final String name;
			protected final String dataName;
			protected final int seed;
			protected double spawnChance = 0.6D;
			protected int distance = 60;
			protected int separation = 12;
			protected boolean needsGround = true;
			protected boolean useRandomVarianting = true;
			protected Category[] biomes = new Category[] { Biome.Category.PLAINS, Biome.Category.FOREST,
					Biome.Category.TAIGA, Biome.Category.SAVANNA, Biome.Category.JUNGLE, Biome.Category.MESA,
					Biome.Category.ICY, Biome.Category.DESERT, Biome.Category.SWAMP, Biome.Category.MUSHROOM };

			public ConfigBuilder(String name, String dataName, int seed) {
				this.name = name;
				this.dataName = dataName;
				this.seed = seed;
			}

			public StructureGenConfig build(final ForgeConfigSpec.Builder SERVER_BUILDER) {
				return new StructureGenConfig(SERVER_BUILDER, name, dataName, spawnChance, distance, separation, seed,
						needsGround, useRandomVarianting, biomes);
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
				this.biomes = biomes;
				return this;
			}

			public ConfigBuilder setUseRandomVarianting(boolean useRandomVarianting) {
				this.useRandomVarianting = useRandomVarianting;
				return this;
			}
		}
	}

	public static class LootableStructureGenConfig extends StructureGenConfig {
		public final ForgeConfigSpec.DoubleValue LOOT_CHANCE;

		public LootableStructureGenConfig(Builder SERVER_BUILDER, String name, String dataName, double dSpawnChance,
				int dDistance, int dSeparation, int dSeed, double dLootChance, boolean needsGround,
				boolean useRandomVarianting, Category... biomeCategories) {
			super(SERVER_BUILDER, name, dataName, dSpawnChance, dDistance, dSeparation, dSeed, needsGround,
					useRandomVarianting, biomeCategories);
			LOOT_CHANCE = SERVER_BUILDER.comment(name + " Generate Loot Chance [default: " + dLootChance + "]")
					.defineInRange("structures." + dataName.toLowerCase(Locale.ROOT) + ".loot_chance", dLootChance, 0.0,
							1.0);
		}

		public static class LootableConfigBuilder extends ConfigBuilder {
			protected double lootChance = 1.0D;

			public LootableConfigBuilder(String name, String dataName, int seed) {
				super(name, dataName, seed);
			}

			public LootableStructureGenConfig build(final ForgeConfigSpec.Builder SERVER_BUILDER) {
				return new LootableStructureGenConfig(SERVER_BUILDER, name, dataName, spawnChance, distance, separation,
						seed, lootChance, needsGround, useRandomVarianting, biomes);
			}

			public LootableConfigBuilder setLootChance(double lootChance) {
				this.lootChance = lootChance;
				return this;
			}

			public LootableConfigBuilder setSpawnChance(double spawnChance) {
				this.spawnChance = spawnChance;
				return this;
			}

			public LootableConfigBuilder setDistance(int distance) {
				this.distance = distance;
				return this;
			}

			public LootableConfigBuilder setSeparation(int separation) {
				this.separation = separation;
				return this;
			}

			public LootableConfigBuilder setNeedsGround(boolean needsGround) {
				this.needsGround = needsGround;
				return this;
			}

			public LootableConfigBuilder setBiomes(Category... biomes) {
				this.biomes = biomes;
				return this;
			}

			public LootableConfigBuilder setUseRandomVarianting(boolean useRandomVarianting) {
				this.useRandomVarianting = useRandomVarianting;
				return this;
			}
		}
	}

	private static boolean validateBiome(Object o) {
		return o == null || ForgeRegistries.BIOMES.containsKey(new ResourceLocation((String) o));
	}

	private static boolean validateBiomeCategory(Object o) {
		for (Biome.Category category : Biome.Category.values()) {
			if (category == Biome.Category.valueOf((String) o)) {
				return true;
			}
		}
		return false;
	}

	public static List<String> getAllBiomesForCategory(Biome.Category... categories) {
		List<String> biomes = new ArrayList<>();

		for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
			for (Biome.Category category : categories) {
				if (biome.getCategory() == category) {
					biomes.add(Objects.requireNonNull(biome.getRegistryName()).toString());
				}
			}
		}

		return biomes;
	}
}