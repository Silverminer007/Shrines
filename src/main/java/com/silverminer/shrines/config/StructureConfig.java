package com.silverminer.shrines.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.registries.ForgeRegistries;

public class StructureConfig {
	public final StructureGenConfig BALLON;
	public final StructureGenConfig BEES;
	public final StructureGenConfig HIGH_TEMPEL;
	public final StructureGenConfig SMALL_TEMPEL;
	public final LootableStructureGenConfig NETHER_SHRINE;
	public final LootableStructureGenConfig NETHER_PYRAMID;
	public final StructureGenConfig WATER_SHRINE;
	public final LootableStructureGenConfig PLAYER_HOUSE;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLISTED_BIOMES;

	public StructureConfig(final ForgeConfigSpec.Builder SERVER_BUILDER) {
		BALLON = new StructureGenConfig(SERVER_BUILDER, "Ballon", "ballon", 0.6D, 80, 15, 143665);
		BEES = new StructureGenConfig(SERVER_BUILDER, "Bees", "bees", 0.6D, 70, 12, 779806245);
		HIGH_TEMPEL = new StructureGenConfig(SERVER_BUILDER, "High Tempel", "high_tempel", 0.6D, 85, 18, 536987987);
		SMALL_TEMPEL = new StructureGenConfig(SERVER_BUILDER, "Small Tempel", "small_tempel", 0.6D, 75, 13, 4765321);
		NETHER_SHRINE = new LootableStructureGenConfig(SERVER_BUILDER, "Nether Shrine", "nether_shrine", 0.6D, 80, 15,
				653267, 1.0D);
		NETHER_PYRAMID = new LootableStructureGenConfig(SERVER_BUILDER, "Nether Pyramid", "nether_pyramid", 0.6D, 150,
				50, 7428394, 1.0D);
		WATER_SHRINE = new StructureGenConfig(SERVER_BUILDER, "Water Shrine", "water_shrine", 0.6D, 80, 15, 143665);
		PLAYER_HOUSE = new LootableStructureGenConfig(SERVER_BUILDER, "Player House", "player_house", 0.6D, 80, 15,
				751963298, 1.0D);
		BLACKLISTED_BIOMES = SERVER_BUILDER
				.comment("Structure Generation Config", "Take care what you change, this changes may cant be undone",
						"", "Biomes in which Structures cant generate in")
				.defineList(
						"structures.blacklisted_biomes", getAllBiomesForCategory(Biome.Category.RIVER,
								Biome.Category.OCEAN, Biome.Category.THEEND, Biome.Category.NETHER),
						StructureConfig::validateBiome);
	}

	public static class StructureGenConfig {
		public final ForgeConfigSpec.BooleanValue GENERATE;
		public final ForgeConfigSpec.DoubleValue SPAWN_CHANCE;
		public final ForgeConfigSpec.IntValue DISTANCE;
		public final ForgeConfigSpec.IntValue SEPARATION;
		public final ForgeConfigSpec.IntValue SEED;
		public final ForgeConfigSpec.ConfigValue<List<? extends Biome.Category>> BIOME_CATEGORIES;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> BIOME_BLACKLIST;

		public StructureGenConfig(final ForgeConfigSpec.Builder SERVER_BUILDER, String name, String dataName,
				int dSeed) {
			this(SERVER_BUILDER, name, dataName, 0.6D, 50, 10, dSeed);
		}

		public StructureGenConfig(final ForgeConfigSpec.Builder SERVER_BUILDER, String name, String dataName,
				double dSpawnChance, int dDistance, int dSeparation, int dSeed) {
			dataName = dataName.toLowerCase(Locale.ROOT);
			GENERATE = SERVER_BUILDER.comment("Generate " + name + "s?").define("structures.ballon.generate", true);
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
					"structures." + dataName + ".biome_categories",
					Arrays.asList(Biome.Category.PLAINS, Biome.Category.FOREST, Biome.Category.TAIGA),
					StructureConfig::validateBiomeCategory);
			BIOME_BLACKLIST = SERVER_BUILDER.comment("Biomes the " + name + " can NOT generate in").defineList(
					"structures." + dataName + ".biome_blacklist", Collections.emptyList(),
					StructureConfig::validateBiome);
		}
	}

	public static class LootableStructureGenConfig extends StructureGenConfig {
		public final ForgeConfigSpec.DoubleValue LOOT_CHANCE;

		public LootableStructureGenConfig(Builder SERVER_BUILDER, String name, String dataName, double dSpawnChance,
				int dDistance, int dSeparation, int dSeed, double dLootChance) {
			super(SERVER_BUILDER, name, dataName, dSpawnChance, dDistance, dSeparation, dSeed);
			LOOT_CHANCE = SERVER_BUILDER.comment(name + " Generate Loot Chance [default: " + dLootChance + "]")
					.defineInRange("structures." + dataName.toLowerCase(Locale.ROOT) + ".loot_chance", dLootChance, 0.0,
							1.0);
		}

		public LootableStructureGenConfig(final ForgeConfigSpec.Builder SERVER_BUILDER, String name, String dataName,
				int dSeed, double dLootChance) {
			this(SERVER_BUILDER, name, dataName, 0.6D, 50, 10, dSeed, dLootChance);
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