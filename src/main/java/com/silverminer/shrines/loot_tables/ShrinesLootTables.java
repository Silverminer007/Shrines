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
package com.silverminer.shrines.loot_tables;

import java.util.ArrayList;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;

public class ShrinesLootTables {
	private static final ArrayList<ResourceLocation> LOOT_TABLES = Lists.newArrayList();

	private static final ArrayList<ResourceLocation> NETHER_LOOT_TABLES = Lists.newArrayList();

	private static final ArrayList<ResourceLocation> VILLAGE_LOOT_TABLES = Lists.newArrayList();

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
	public static final ResourceLocation NETHER_PYRAMID_CHEST1 = register("shrines:chests/nether_pyramid_chest1");

	/**
	 * From Ruined Portal
	 */
	public static final ResourceLocation NETHER_PYRAMID_CHEST2 = register("shrines:chests/nether_pyramid_chest2");

	public static final ResourceLocation HOUSE_1 = register("minecraft:chests/village/village_plains_house", false,
			true);

	public static final ResourceLocation HOUSE_2 = register("minecraft:chests/village/village_savanna_house", false,
			true);

	public static final ResourceLocation HOUSE_3 = register("minecraft:chests/village/village_taiga_house", false,
			true);

	public static final ResourceLocation HOUSE_4 = register("minecraft:chests/village/village_snowy_house", false,
			true);

	public static final ResourceLocation MINERAL_TEMPLE = new ResourceLocation("shrines:chests/mineral_temple_chest");

	public static final ResourceLocation HOUSE_OP = new ResourceLocation("minecraft:chests/village/village_toolsmith");
	public static final ResourceLocation HOUSE_OP_2 = new ResourceLocation(
			"minecraft:chests/village/village_weaponsmith");

	public static final ResourceLocation FURNACE = new ResourceLocation("minecraft:chests/village/village_mason");

	public static final ResourceLocation FURNACE_2 = new ResourceLocation("minecraft:chests/village/village_butcher");

	public static final ResourceLocation HIGH_TEMPEL = new ResourceLocation("shrines:chests/high_tempel_chest");

	public static final ResourceLocation FLOODED_TEMPLE = new ResourceLocation("shrines:chests/flooded_temple_chest");
	public static final ResourceLocation HARBOUR = new ResourceLocation("shrines:chests/harbour_chest");
	public static final ResourceLocation HARBOUR_TAVERN = new ResourceLocation("shrines:chests/harbour_tavern_chest");
	public static final ResourceLocation INFECTED_PRISON = new ResourceLocation("shrines:chests/prison_chest");
	public static final ResourceLocation END_TEMPLE = new ResourceLocation("shrines:chests/end_temple_chest");
	public static final ResourceLocation JUNGLE_TOWER = new ResourceLocation("shrines:chests/jungle_tower_chest");
	public static final ResourceLocation WITCH_HOUSE = new ResourceLocation("shrines:chests/witch_house");
	public static final ResourceLocation SMALL_TEMPEL = new ResourceLocation("shrines:chests/small_tempel");
	public static final ResourceLocation WATER_SHRINE = new ResourceLocation("shrines:chests/water_shrine");
	public static final ResourceLocation BEES = new ResourceLocation("shrines:chests/bees");
	public static final ResourceLocation BALLON = new ResourceLocation("shrines:chests/ballon");
	public static final ResourceLocation GUARDIAN_MEETING = new ResourceLocation("shrines:chests/guardian_meeting");

	public static final ResourceLocation EMPTY = new ResourceLocation("");

	
   

	private static ResourceLocation register(String id, boolean isNetherChest, boolean isVillageChest) {
		return register(new ResourceLocation(id), isNetherChest, isVillageChest);
	}

	private static ResourceLocation register(String id, boolean isNetherChest) {
		return register(new ResourceLocation(id), isNetherChest);
	}

	private static ResourceLocation register(String id) {
		return register(new ResourceLocation(id));
	}

	private static ResourceLocation register(ResourceLocation id, boolean isNetherChest, boolean isVillageChest) {
		if (isVillageChest) {
			if (!VILLAGE_LOOT_TABLES.add(id)) {
				throw new IllegalArgumentException(id + " is already a registered built-in loot table");
			}
			return id;
		}
		if (isNetherChest) {
			if (!NETHER_LOOT_TABLES.add(id)) {
				throw new IllegalArgumentException(id + " is already a registered built-in loot table");
			}
			return id;
		}
		if (!LOOT_TABLES.add(id)) {
			throw new IllegalArgumentException(id + " is already a registered built-in loot table");
		}
		return id;
	}

	private static ResourceLocation register(ResourceLocation id, boolean isNetherChest) {
		return register(id, isNetherChest, false);
	}

	private static ResourceLocation register(ResourceLocation id) {
		return register(id, false, false);
	}

	public static ResourceLocation getRandomLoot(Random rand) {
		return (ResourceLocation) LOOT_TABLES.get(rand.nextInt(LOOT_TABLES.size()));
	}

	public static ResourceLocation getRandomNetherLoot(Random rand) {
		return (ResourceLocation) NETHER_LOOT_TABLES.get(rand.nextInt(LOOT_TABLES.size()));
	}

	public static ResourceLocation getRandomVillageLoot(Random rand) {
		return (ResourceLocation) VILLAGE_LOOT_TABLES.get(rand.nextInt(LOOT_TABLES.size()));
	}
}
