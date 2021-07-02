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

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

public class ShrinesSettingsConfig {

	public final ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLISTED_BIOMES;

	public final ForgeConfigSpec.DoubleValue DISTANCE_FACTOR;
	public final ForgeConfigSpec.DoubleValue SEPERATION_FACTOR;
	public final ForgeConfigSpec.IntValue STRUCTURE_MIN_DISTANCE;
	public final ForgeConfigSpec.BooleanValue ADVANCED_LOGGING;

	public ShrinesSettingsConfig(final ForgeConfigSpec.Builder SERVER_BUILDER) {
		BLACKLISTED_BIOMES = SERVER_BUILDER
				.comment("Biomes where NO Structure (of this mod) can generate in. Custom structures too")
				.translation("config.shrines.blacklist").worldRestart().defineList("structures.blacklisted_biomes",
						Lists.newArrayList(), ShrinesSettingsConfig::validateBiome);
		DISTANCE_FACTOR = SERVER_BUILDER.comment(
				"Distance Factor (Default 1.0) Is multiplied on the structures distance. Allows changing distance of every structure at once")
				.translation("config.shrines.distance_factor").worldRestart()
				.defineInRange("structures.distance_factor", 1.0, 0.0, 100.0);
		SEPERATION_FACTOR = SERVER_BUILDER.comment(
				"Seperation Factor (Default 1.0) Is multiplied on the structures seperation. Allows changing seperation of every structure at once")
				.translation("config.shrines.seperation_factor").worldRestart()
				.defineInRange("structures.seperation_factor", 1.0, 0.0, 100.0);
		STRUCTURE_MIN_DISTANCE = SERVER_BUILDER.comment(
				"The structures min. distance is the smallest possible distance between two structures (of this mod)")
				.translation("config.shrines.structure_min_distance")
				.defineInRange("structures.structures_min_distance", 5, 1, 100);
		ADVANCED_LOGGING = SERVER_BUILDER.comment(
				"Use advanced logging. Gives more help by finding issues. Please enable this before reporting a bug")
				.translation("config.shrines.advanced_logging").define("structures.advanced_logging", true);
	}

	private static boolean validateBiome(Object o) {
		return o == null || ForgeRegistries.BIOMES.containsKey(new ResourceLocation((String) o));
	}
}