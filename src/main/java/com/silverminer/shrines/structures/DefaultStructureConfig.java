package com.silverminer.shrines.structures;

import com.google.common.collect.Lists;
import com.silverminer.shrines.new_custom_structures.DefaultedStructureData;

public class DefaultStructureConfig {
	public static final DefaultedStructureData ABANDONEDWITCHHOUSE_CONFIG = new DefaultedStructureData(
			"Abandoned Witch House", "abandoned_witch_house", 1721882513)// .setBiomes(Category.SWAMP, Category.FOREST)
					.addToBiomeBlacklist("minecraft:flower_forest", "minecraft:tall_birch_forest", "minecraft:forest",
							"minecraft:birch_forest", "minecraft:birch_forest_hills")
					.setDistance(60).setSeperation(11);

	public static final DefaultedStructureData BALLON_CONFIG = new DefaultedStructureData("Ballon", "balloon", 143665)
			.setDistance(50).setSeperation(8);

	public static final DefaultedStructureData BEES_CONFIG = new DefaultedStructureData("Bees", "bees", 779806245)
			.setDistance(70).setSeperation(12).setUseRandomVarianting(false);

	public static final DefaultedStructureData ENDTEMPLE_CONFIG = new DefaultedStructureData("End Temple", "end_temple",
			32 ^ 478392).setDistance(60).setSeperation(11)// .setBiomes(Category.THEEND)
					.addToBiomeBlacklist("minecraft:the_end", "minecraft:the_void", "minecraft:small_end_islands")
					.setDimensionWhitelist(Lists.newArrayList("minecraft:the_end"));

	public static final DefaultedStructureData FLOODEDTEMPLE_CONFIG = new DefaultedStructureData("Flooded Temple",
			"flooded_temple", 54315143).setDistance(50).setSeperation(10)
					.setUseRandomVarianting(false)/*
													 * .removeBiome(Category.DESERT) .removeBiome(Category.MESA)
													 */;

	public static final DefaultedStructureData GUARDIANMEETING_CONFIG = new DefaultedStructureData("Guardian Meeting",
			"guardian_meeting", 1498473232).setDistance(70).setSeperation(17).setUseRandomVarianting(false);

	public static final DefaultedStructureData HARBOUR_CONFIG = new DefaultedStructureData("Harbour", "harbour",
			651398043).setDistance(50).setSeperation(8);// .setBiomes(Biome.Category.OCEAN);

	public static final DefaultedStructureData HIGHTEMPLE_CONFIG = new DefaultedStructureData("High Tempel",
			"high_temple", 536987987).setDistance(85).setSeperation(18);

	public static final DefaultedStructureData JUNGLETOWER_CONFIG = new DefaultedStructureData("Jungle Tower",
			"jungle_tower", 987531843).setDistance(60).setSeperation(11);// .setBiomes(Category.JUNGLE);

	public static final DefaultedStructureData MINERALTEMPLE_CONFIG = new DefaultedStructureData("Mineral Temple",
			"mineral_temple", 576143753).setDistance(50).setSeperation(10).setUseRandomVarianting(false);

	public static final DefaultedStructureData NETHERPYRAMID_CONFIG = new DefaultedStructureData("Nether Pyramid",
			"nether_pyramid", 7428394).setDistance(150).setSeperation(50)
					.addDimensionToWhitelist("minecraft:the_nether");// .addBiome(Category.NETHER);

	public static final DefaultedStructureData NETHERSHRINE_CONFIG = new DefaultedStructureData("Nether Shrine",
			"nether_shrine", 653267).setDistance(80).setSeperation(15).addDimensionToWhitelist("minecraft:the_nether");// .addBiome(Category.NETHER);

	public static final DefaultedStructureData ORIENTALSANCTUARY_CONFIG = new DefaultedStructureData(
			"Oriental Sanctuary", "oriental_sanctuary", 143665).setDistance(50).setSeperation(14);

	public static final DefaultedStructureData PLAYERHOUSE_CONFIG = new DefaultedStructureData("Player House",
			"player_house", 751963298).setDistance(80).setSeperation(15);

	public static final DefaultedStructureData INFESTEDPRISON_CONFIG = new DefaultedStructureData("Infested Prison",
			"infested_prison", 567483014).setDistance(60).setSeperation(11);

	public static final DefaultedStructureData SHRINEOFSAVANNA_CONFIG = new DefaultedStructureData("Shrine of savanna",
			"shrine_of_savanna", 432333099).setDistance(67).setSeperation(11);

	public static final DefaultedStructureData SMALLTEMPLE_CONFIG = new DefaultedStructureData("Small Tempel",
			"small_temple", 4765321).setDistance(75).setSeperation(13);

	public static final DefaultedStructureData TRADER_HOUSE_CONFIG = new DefaultedStructureData("Trader House",
			"trader_house", 760055678).setDistance(60).setSeperation(11);

	public static final DefaultedStructureData WATCHTOWER_CONFIG = new DefaultedStructureData("Watchtower",
			"watchtower", 432189012).setDistance(77).setSeperation(16);

	public static final DefaultedStructureData WATERSHRINE_CONFIG = new DefaultedStructureData("Water Shrine",
			"water_shrine", 643168754).setDistance(80).setSeperation(15).setNovel("Last April, John took a trip to Las Vegas, Nevada. Las Vegas is a popular destination in the western portion of the United States. The town is most popular for its casinos, hotels, and exciting nightlife.\n"
					+ "In downtown Las Vegas, John spent a lot of time on The Strip, which is a 2.5 mile stretch of shopping, entertainment venues, luxury hotels, and fine dining experiences. This is probably the most commonly visited tourist area in the city. The Strip at night looks especially beautiful. All of the buildings light up with bright, neon, eye-catching signs to attract visitor attention.\n"
					+ "A stay in Las Vegas can feel similar to a visit to many popular cities worldwide. Many of the hotels have miniature versions of important international sites and monuments. These famous landmarks include the Eiffel Tower, Venice, and even ancient Rome.\n"
					+ "One day, John took a side trip outside of the city to visit the Grand Canyon, one of the Seven Wonders of the Natural World. The canyon offers a breathtaking view of Nevada’s ridges and natural landscape. John especially liked the canyon because it was removed from all of the noise and movement in downtown Las Vegas.\n"
					+ "John had a great time during his trip to Las Vegas. He did not win a lot of money in the casinos. However, he managed to see a lot of amazing sites during his visit to this city that never sleeps.  \n"
					+ "");
}