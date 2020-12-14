package com.silverminer.shrines.loot_tables;

import java.util.ArrayList;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;

public class ShrinesLootTables {
	private static final ArrayList<ResourceLocation> LOOT_TABLES = Lists.newArrayList();

	private static final ArrayList<ResourceLocation> NETHER_LOOT_TABLES = Lists.newArrayList();

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

	/**
	 * From Nether Bastions
	 */
	public static final ResourceLocation NETHER_PYRAMID_CHEST1 = register("shrines:chests/nether_pyramid_chest1", false);

	/**
	 * From Ruined Portal
	 */
	public static final ResourceLocation NETHER_PYRAMID_CHEST2 = register("shrines:chests/nether_pyramid_chest2", false);

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
		return (ResourceLocation) LOOT_TABLES.get(rand.nextInt(LOOT_TABLES.size()));
	}

	public static ResourceLocation getRandomNetherLoot(Random rand) {
		return (ResourceLocation) NETHER_LOOT_TABLES.get(rand.nextInt(LOOT_TABLES.size()));
	}
}
