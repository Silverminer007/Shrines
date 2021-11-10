/**
 * Silverminer (and Team)
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * <p>
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.structures.load;

import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Silverminer
 *
 */
public class DefaultedStructureData {
    protected final String name;
    protected final String key;
    protected final int seed_modifier;
    protected boolean transformLand = true;
    protected boolean generate = true;
    protected double spawnChance = 0.6D;
    protected boolean useRandomVarianting = true;
    protected int distance = 60;
    protected int seperation = 12;
    protected int height_offset;
    protected ArrayList<Biome.Category> biome_category_whitelist = Lists.newArrayList(Biome.Category.PLAINS,
            Biome.Category.FOREST, Biome.Category.TAIGA, Biome.Category.SAVANNA, Biome.Category.JUNGLE,
            Biome.Category.MESA, Biome.Category.ICY, Biome.Category.DESERT, Biome.Category.SWAMP,
            Biome.Category.MUSHROOM);
    protected ArrayList<String> biome_blacklist = Lists.newArrayList();
    protected ArrayList<String> dimension_whitelist = Lists.newArrayList("minecraft:overworld");
    protected String start_pool;
    protected String novel = "";

    public DefaultedStructureData(String name, String key, int seed_modifier) {
        this.name = name;
        this.key = new ResourceLocation(ShrinesMod.MODID, key).toString();
        this.seed_modifier = seed_modifier;
    }

    public DefaultedStructureData addToBiomeCategoryWhitelist(Biome.Category... whitelist) {
        this.biome_category_whitelist.addAll(Arrays.asList(whitelist));
        return this;
    }

    public DefaultedStructureData removeFromBiomeCategoryWhitelist(Biome.Category... whitelist) {
        for (Biome.Category s : whitelist) {
            this.biome_category_whitelist.remove(s);
        }
        return this;
    }

    public DefaultedStructureData addDimensionToWhitelist(String dimension) {
        this.dimension_whitelist.add(dimension);
        return this;
    }

    public DefaultedStructureData setStartPool(String start_pool) {
        this.start_pool = start_pool;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public boolean getGenerate() {
        return this.generate;
    }

    public DefaultedStructureData setGenerate(boolean generate) {
        this.generate = generate;
        return this;
    }

    public double getSpawnChance() {
        return this.spawnChance;
    }

    public DefaultedStructureData setSpawnChance(double spawnChance) {
        this.spawnChance = spawnChance;
        return this;
    }

    public int getDistance() {
        return this.distance;
    }

    public DefaultedStructureData setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public int getSeperation() {
        return this.seperation;
    }

    public DefaultedStructureData setSeperation(int seperation) {
        this.seperation = seperation;
        return this;
    }

    public int getSeedModifier() {
        return this.seed_modifier;
    }

    public List<String> getBiomeBlacklist() {
        return this.biome_blacklist;
    }

    public DefaultedStructureData setBiomeBlacklist(String... blacklist) {
        this.biome_blacklist.clear();
        for (String s : blacklist)
            this.biome_blacklist.add(s);
        return this;
    }

    public List<String> getBiomeCategoryWhitelist() {
        return this.biome_category_whitelist.stream().map(cat -> cat.toString()).collect(Collectors.toList());
    }

    public DefaultedStructureData setBiomeCategoryWhitelist(Biome.Category... whitelist) {
        this.biome_category_whitelist.clear();
        for (Biome.Category s : whitelist) {
            this.biome_category_whitelist.add(s);
        }
        return this;
    }

    public List<String> getDimensionWhitelist() {
        return this.dimension_whitelist;
    }

    public DefaultedStructureData setDimensionWhitelist(ArrayList<String> dimension) {
        this.dimension_whitelist = dimension;
        return this;
    }

    public boolean getUseRandomVarianting() {
        return this.useRandomVarianting;
    }

    public DefaultedStructureData setUseRandomVarianting(boolean useRandomVarianting) {
        this.useRandomVarianting = useRandomVarianting;
        return this;
    }

    public boolean isBuiltIn() {
        return true;
    }

    public boolean getActive() {
        return this.generate;
    }

    public void setActive(boolean value) {
        this.setGenerate(value);
    }

    public boolean isTransformLand() {
        return transformLand;
    }

    public DefaultedStructureData setTransformLand(boolean transformLand) {
        this.transformLand = transformLand;
        return this;
    }

    public int getHeight_offset() {
        return height_offset;
    }

    public DefaultedStructureData setHeight_offset(int height_offset) {
        this.height_offset = height_offset;
        return this;
    }

    public String getKey() {
        return key;
    }

    public String getNovel() {
        return this.novel;
    }

    public DefaultedStructureData setNovel(String novel) {
        this.novel = novel;
        return this;
    }

    public String getStart_pool() {
        if (this.start_pool == null) {
            return new ResourceLocation(this.getKey() + "/start_pool").toString();
        } else {
            return this.start_pool;
        }
    }
}