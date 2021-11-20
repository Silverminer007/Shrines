package com.silverminer.shrines.structures.load.options;

public class ConfigOptions {
	public static final String name = "name";// String
	public static final String key = "key"; // String
	public static final String transform_land = "transform_land"; // Boolean
	public static final String generate = "generate"; // Boolean
	public static final String spawn_chance = "spawn_chance"; // Double
	public static final String use_random_varianting = "use_random_varianting"; // Boolean
	public static final String distance = "distance"; // Integer
	public static final String seperation = "separation"; // Integer
	public static final String seed_modifier = "seed_modifier"; // Integer
	public static final String height_offset = "height_offset"; // Integer
	public static final String biome_blacklist = "biome_blacklist"; // List of Strings
	public static final String biome_category_whitelist = "biome_category_whitelist"; // List of Strings
	public static final String dimension_whitelist = "dimension_whitelist"; // List of Strings
	public static final String start_pool = "start_pool";
	public static final String novel = "novel"; // String

	public static class Comments {// TRANSLATION
		public static final String general_prefix = "options.shrines.";
		public static final String general_suffix = ".comment";
		public static final String name = general_prefix + ConfigOptions.name + general_suffix;// String
		public static final String key = general_prefix + ConfigOptions.key + general_suffix; // String
		public static final String transform_land = general_prefix + ConfigOptions.transform_land + general_suffix; // Boolean
		public static final String generate = general_prefix + ConfigOptions.generate + general_suffix; // Boolean
		public static final String spawn_chance = general_prefix + ConfigOptions.spawn_chance + general_suffix; // Double
		public static final String use_random_varianting = general_prefix + ConfigOptions.use_random_varianting + general_suffix; // Boolean
		public static final String distance = general_prefix + ConfigOptions.distance + general_suffix; // Integer
		public static final String separation = general_prefix + ConfigOptions.seperation + general_suffix; // Integer
		public static final String seed_modifier = general_prefix + ConfigOptions.seed_modifier + general_suffix; // Integer
		public static final String height_offset = general_prefix + ConfigOptions.height_offset + general_suffix; // Integer
		public static final String biome_blacklist = general_prefix + ConfigOptions.biome_blacklist + general_suffix; // List of Strings
		public static final String biome_category_whitelist = general_prefix + ConfigOptions.biome_category_whitelist + general_suffix; // List of Strings
		public static final String dimension_whitelist = general_prefix + ConfigOptions.dimension_whitelist + general_suffix; // List of Strings
		public static final String start_pool = general_prefix + ConfigOptions.start_pool + general_suffix;
		public static final String novel = general_prefix + ConfigOptions.novel + general_suffix; // String
	}
}