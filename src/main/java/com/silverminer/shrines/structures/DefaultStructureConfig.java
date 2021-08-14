package com.silverminer.shrines.structures;

import com.google.common.collect.Lists;
import com.silverminer.shrines.config.ConfigBuilder;
import com.silverminer.shrines.config.ConfigBuilder.Type;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;

public class DefaultStructureConfig {
	public static final ConfigBuilder ABANDONEDWITCHHOUSE_CONFIG = new ConfigBuilder("Abandoned Witch House",
			1721882513, Type.LOOTABLE).setBiomes(Category.SWAMP, Category.FOREST)
					.addToBlacklist("minecraft:flower_forest", "minecraft:tall_birch_forest", "minecraft:forest",
							"minecraft:birch_forest", "minecraft:birch_forest_hills")
					.setDistance(60).setSeparation(11);

	public static final ConfigBuilder BALLON_CONFIG = new ConfigBuilder("Ballon", 143665, Type.LOOTABLE)
			.setLootChance(0.25D).setDistance(50).setSeparation(8);

	public static final ConfigBuilder BEES_CONFIG = new ConfigBuilder("Bees", 779806245, Type.LOOTABLE)
			.setDistance(70).setSeparation(12).setUseRandomVarianting(false);

	public static final ConfigBuilder ENDTEMPLE_CONFIG = new ConfigBuilder("End Temple", 32 ^ 478392, Type.LOOTABLE)
			.setDistance(60).setSeparation(11).setBiomes(Category.THEEND)
			.addToBlacklist("minecraft:the_end", "minecraft:the_void", "minecraft:small_end_islands")
			.setDimension(Lists.newArrayList("minecraft:the_end"));

	public static final ConfigBuilder FLOODEDTEMPLE_CONFIG = new ConfigBuilder("Flooded Temple", 54315143,
			Type.LOOTABLE).setDistance(50).setSeparation(10).setUseRandomVarianting(false).removeBiome(Category.DESERT)
			.removeBiome(Category.MESA);

	public static final ConfigBuilder GUARDIANMEETING_CONFIG = new ConfigBuilder("Guardian Meeting", 1498473232,
			Type.LOOTABLE).setLootChance(1.0D).setDistance(70).setSeparation(17).setUseRandomVarianting(false);

	public static final ConfigBuilder HARBOUR_CONFIG = new ConfigBuilder("Harbour", 651398043, Type.LOOTABLE)
			.setDistance(50).setSeparation(8).setBiomes(Biome.Category.OCEAN);

	public static final ConfigBuilder HIGHTEMPLE_CONFIG = new ConfigBuilder("High Tempel", 536987987, Type.LOOTABLE)
			.setDistance(85).setSeparation(18);

	public static final ConfigBuilder JUNGLETOWER_CONFIG = new ConfigBuilder("Jungle Tower", 987531843, Type.LOOTABLE).setDistance(60).setSeparation(11).setBiomes(Category.JUNGLE);

	public static final ConfigBuilder MINERALTEMPLE_CONFIG = new ConfigBuilder("Mineral Temple", 576143753,
			Type.LOOTABLE).setDistance(50).setSeparation(10).setUseRandomVarianting(false);

	public static final ConfigBuilder NETHERPYRAMID_CONFIG = new ConfigBuilder("Nether Pyramid", 7428394,
			Type.LOOTABLE).setDistance(150).setSeparation(50).addDimension("minecraft:the_nether").addBiome(Category.NETHER);

	public static final ConfigBuilder NETHERSHRINE_CONFIG = new ConfigBuilder("Nether Shrine", 653267, Type.LOOTABLE)
			.setDistance(80).setSeparation(15).addDimension("minecraft:the_nether").addBiome(Category.NETHER);

	public static final ConfigBuilder ORIENTALSANCTUARY_CONFIG = new ConfigBuilder("Oriental Sanctuary", 143665,
			Type.NORMAL).setDistance(50).setSeparation(14);

	public static final ConfigBuilder PLAYERHOUSE_CONFIG = new ConfigBuilder("Player House", 751963298, Type.LOOTABLE).setDistance(80).setSeparation(15);

	public static final ConfigBuilder INFESTEDPRISON_CONFIG = new ConfigBuilder("Infested Prison", 567483014,
			Type.LOOTABLE).setDistance(60).setSeparation(11);

	public static final ConfigBuilder SHRINEOFSAVANNA_CONFIG = new ConfigBuilder("Shrine of savanna", 432333099, Type.LOOTABLE).setDistance(67).setSeparation(11);

	public static final ConfigBuilder SMALLTEMPLE_CONFIG = new ConfigBuilder("Small Tempel", 4765321, Type.LOOTABLE).setDistance(75).setSeparation(13);

	public static final ConfigBuilder TRADER_HOUSE_CONFIG = new ConfigBuilder("Trader House",
			760055678, Type.LOOTABLE)
					.setDistance(60).setSeparation(11);

	public static final ConfigBuilder WATCHTOWER_CONFIG = new ConfigBuilder("Watchtower", 432189012,
			Type.LOOTABLE).setDistance(77).setSeparation(16);

	public static final ConfigBuilder WATERSHRINE_CONFIG = new ConfigBuilder("Water Shrine", 643168754,
			Type.LOOTABLE).setDistance(80).setSeparation(15);
}