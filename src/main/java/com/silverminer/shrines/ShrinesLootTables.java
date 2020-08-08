package com.silverminer.shrines;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.util.ResourceLocation;

public class ShrinesLootTables {
	private static final Set<ResourceLocation> LOOT_TABLES = Sets.newHashSet();

	private static final Set<ResourceLocation> NETHER_LOOT_TABLES = Sets.newHashSet();

	/**
	 * From Jungle Temples
	 */
	public static final ResourceLocation NETHER_SHRINE_CHEST = register("shrines:chests/nether_shrine_chest", true);

	/**
	 * From "Village Toolsmith"
	 */
	public static final ResourceLocation NETHER_SHRINE_CHEST1 = register("shrines:chests/nether_shrine_chest1", true);

	/**
	 * From Nether Bridges
	 */
	public static final ResourceLocation NETHER_SHRINE_CHEST2 = register("shrines:chests/nether_shrine_chest2", true);

	private static ResourceLocation register(String id, boolean isNetherChest) {
		return register(new ResourceLocation(id), isNetherChest);
	}

	private static ResourceLocation register(ResourceLocation id, boolean isNetherChest) {
		if (!LOOT_TABLES.add(id)) {
			throw new IllegalArgumentException(id + " is already a registered built-in loot table");
		}
		if (isNetherChest) {
			NETHER_LOOT_TABLES.add(id);
		}
		return id;
	}

	public static ResourceLocation getRandomLoot(Random rand) {
		return (ResourceLocation) LOOT_TABLES.toArray()[rand.nextInt(LOOT_TABLES.size())];
	}

	public static ResourceLocation getRandomNetherLoot(Random rand) {
		return (ResourceLocation) NETHER_LOOT_TABLES.toArray()[rand.nextInt(LOOT_TABLES.size())];
	}
}