package com.silverminer.shrines.structures.custom.helper;

import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.silverminer.shrines.utils.OptionParsingResult;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class CustomStructureData {
	protected static final Logger LOGGER = LogManager.getLogger(CustomStructureData.class);
	public static final List<String> OPTIONS = Lists.newArrayList();

	public final String name;

	public final List<ConfigOption<?>> CONFIGS = Lists.newArrayList();
	public ConfigOption<Boolean> generate = add(new ConfigOption<Boolean>("generate", true, Boolean::valueOf,
			BoolArgumentType.bool(), BoolArgumentType::getBool));
	public ConfigOption<Double> spawn_chance = add(new ConfigOption<Double>("spawn_chance", 0.6D, Double::valueOf,
			DoubleArgumentType.doubleArg(0.0, 1.0), DoubleArgumentType::getDouble));
	public ConfigOption<Boolean> needs_ground = add(new ConfigOption<Boolean>("needs_ground", true, Boolean::valueOf,
			BoolArgumentType.bool(), BoolArgumentType::getBool));
	public ConfigOption<Boolean> use_random_varianting = add(new ConfigOption<Boolean>("use_random_varianting", true,
			Boolean::valueOf, BoolArgumentType.bool(), BoolArgumentType::getBool));
	public ConfigOption<Integer> distance = add(new ConfigOption<Integer>("distance", 50, Integer::valueOf,
			IntegerArgumentType.integer(), IntegerArgumentType::getInteger));
	public ConfigOption<Integer> seed;
	public ConfigOption<Integer> seperation = add(new ConfigOption<Integer>("seperation", 8, Integer::valueOf,
			IntegerArgumentType.integer(), IntegerArgumentType::getInteger));
	public ConfigOption<List<Biome.Category>> categories = add(new ConfigOption<List<Biome.Category>>("categories",
			Lists.newArrayList(Biome.Category.PLAINS, Biome.Category.TAIGA, Biome.Category.FOREST),
			CustomStructureData::readCategories, StringArgumentType.greedyString(), StringArgumentType::getString));
	public ConfigOption<List<String>> blacklist = add(new ConfigOption<List<String>>("blacklist", Lists.newArrayList(),
			CustomStructureData::readBlackList, StringArgumentType.greedyString(), StringArgumentType::getString));
	public ConfigOption<List<PieceData>> pieces = add(
			new ConfigOption<List<PieceData>>("pieces", Lists.newArrayList(new PieceData("resource", BlockPos.ZERO)),
					CustomStructureData::readPieces, StringArgumentType.greedyString(), StringArgumentType::getString));
	public ConfigOption<Boolean> ignore_air = add(new ConfigOption<Boolean>("ignore_air", true, Boolean::valueOf,
			BoolArgumentType.bool(), BoolArgumentType::getBool));

	public CustomStructureData(String name, Random rand) {
		this(name, rand.nextInt(Integer.MAX_VALUE));
	}

	public CustomStructureData(String name, int seed) {
		this.name = name;
		this.seed = add(new ConfigOption<Integer>("seed", seed, Integer::valueOf, IntegerArgumentType.integer(0),
				IntegerArgumentType::getInteger));
	}

	public <T> ConfigOption<T> add(ConfigOption<T> option) {
		if (!OPTIONS.contains(option.getName())) {
			OPTIONS.add(option.getName());
		}
		if (!CONFIGS.contains(option)) {
			CONFIGS.add(option);
		}
		return option;
	}

	public String getName() {
		return this.name;
	}

	public String toString() {
		String config = "";
		for (ConfigOption<?> co : CONFIGS) {
			config += co.toString() + ";";
		}
		return config;
	}

	public String toStringReadAble() {
		return this.toString().replaceAll(";", ";\n");
	}

	public void fromString(String config) {
		config = config.replaceAll(" ", "").replaceAll("\n", "");
		while (true) {
			int idx = config.indexOf(";");
			if (idx == -1) {
				break;
			}
			String sub = config.substring(0, idx);
			int idx2 = sub.indexOf(":");
			if (idx2 == -1) {
				break;
			}
			this.fromString(sub.substring(0, idx2), sub.substring(idx2 + 1));
			if (config.length() <= idx + 1) {
				break;
			}
			config = config.substring(idx + 1);
		}
	}

	public OptionParsingResult fromString(String option, String value) {
		for (ConfigOption<?> co : CONFIGS) {
			if (co.getName().equals(option)) {
				OptionParsingResult res = co.fromString(value, this);
				return res;
			}
		}
		return new OptionParsingResult(false, null);
	}

	public static List<Biome.Category> readCategories(String s) {
		if (s.startsWith("[") && s.endsWith("]")) {
			s = s.substring(1, s.length() - 1);
		}
		try {
			List<String> cats = Lists.newArrayList();
			while ((s.contains(","))) {
				int idx = s.lastIndexOf(",");
				cats.add(s.substring(idx + 1));
				s = s.substring(0, idx);
			}
			cats.add(s);
			List<Biome.Category> categories = Lists.newArrayList();
			for (String cat : cats) {
				if (cat.equals("DEFAULT")) {
					return Lists.newArrayList(Biome.Category.PLAINS, Biome.Category.TAIGA, Biome.Category.FOREST);
				} else if (cat.equals("ALL")) {
					return Lists.newArrayList(Biome.Category.values());
				} else {
					Biome.Category c = Biome.Category.valueOf(cat);
					if (c != null) {
						categories.add(c);
					}
				}
			}
			return categories;
		} catch (Throwable t) {
			LOGGER.warn("Failed to parse [{}] to Categories", s);
			return null;
		}
	}

	public static List<String> readBlackList(String s) {
		if (s.startsWith("[") && s.endsWith("]")) {
			s = s.substring(1, s.length() - 1);
			List<String> list = Lists.newArrayList();
			while ((s.contains(","))) {
				int idx = s.lastIndexOf(",");
				list.add(s.substring(idx + 1));
				s = s.substring(0, idx);
			}
			list.add(s);
			return list;
		} else
			return Lists.newArrayList();
	}

	public static List<PieceData> readPieces(String s) {
		if (s.startsWith("[") && s.endsWith("]")) {
			s = s.substring(1, s.length() - 1);
			List<String> cats = Lists.newArrayList();
			while ((s.contains("+"))) {
				int idx = s.lastIndexOf("+");
				cats.add(s.substring(idx + 1));
				s = s.substring(0, idx);
			}
			cats.add(s);
			List<PieceData> categories = Lists.newArrayList();
			for (String cat : cats) {
				PieceData c = PieceData.fromString(cat);
				if (c != null) {
					categories.add(c);
				}
			}
			return categories;
		} else
			return Lists.newArrayList();
	}
}