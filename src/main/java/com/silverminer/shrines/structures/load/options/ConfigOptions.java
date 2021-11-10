package com.silverminer.shrines.structures.load.options;

public class ConfigOptions {
	public static final String name = "name";// String
	public static final String key = "key"; // String
	public static final String transform_land = "transform land"; // Boolean
	public static final String generate = "generate"; // Boolean
	public static final String spawn_chance = "spawn chance"; // Double
	public static final String use_random_varianting = "use random varianting"; // Boolean
	public static final String distance = "distance"; // Integer
	public static final String seperation = "seperation"; // Integer
	public static final String seed_modifier = "seed modifier"; // Integer
	public static final String height_offset = "height offset"; // Integer
	public static final String biome_blacklist = "biome blacklist"; // List of Strings
	public static final String biome_category_whitelist = "biome category whitelist"; // List of Strings
	public static final String dimension_whitelist = "dimension whitelist"; // List of Strings
	public static final String start_pool = "start pool";
	public static final String novel = "novel"; // String

	public static class Comments {// TRANSLATION
		public static final String general = "shrines.structures.comments.";
		public static final String name = general + ConfigOptions.name;// String
		public static final String key = general + ConfigOptions.key; // String
		public static final String transform_land = general + ConfigOptions.transform_land; // Boolean
		public static final String generate = general + ConfigOptions.generate; // Boolean
		public static final String spawn_chance = general + ConfigOptions.spawn_chance; // Double
		public static final String use_random_varianting = general + ConfigOptions.use_random_varianting; // Boolean
		public static final String distance = general + ConfigOptions.distance; // Integer
		public static final String seperation = general + ConfigOptions.seperation; // Integer
		public static final String seed_modifier = general + ConfigOptions.seed_modifier; // Integer
		public static final String height_offset = general + ConfigOptions.height_offset; // Integer
		public static final String biome_blacklist = general + ConfigOptions.biome_blacklist; // List of Strings
		public static final String biome_category_whitelist = general + ConfigOptions.biome_category_whitelist; // List of Strings
		public static final String dimension_whitelist = general + ConfigOptions.dimension_whitelist; // List of Strings
		public static final String start_pool = general + ConfigOptions.start_pool;
		public static final String novel = general + ConfigOptions.novel; // String
	}
}