/*
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
package com.silverminer.shrines.structures.load.legacy;

import com.google.common.collect.Lists;
import com.silverminer.shrines.config.DefaultStructureConfig;
import com.silverminer.shrines.structures.load.StructureData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class LegacyStructureData implements IStructureConfig {
    protected static final Logger LOGGER = LogManager.getLogger(LegacyStructureData.class);
    public final List<ConfigOption<?>> CONFIGS = Lists.newArrayList();
    public String name;
    public ConfigOption<Boolean> generate = add(new ConfigOption<Boolean>("generate", true, Boolean::valueOf));
    public ConfigOption<Double> spawn_chance = add(new ConfigOption<Double>("spawn_chance", 0.6D, Double::valueOf));
    public ConfigOption<Boolean> needs_ground = add(new ConfigOption<Boolean>("needs_ground", true, Boolean::valueOf));
    public ConfigOption<Boolean> use_random_varianting = add(new ConfigOption<Boolean>("use_random_varianting", true, Boolean::valueOf));
    public ConfigOption<Integer> distance = add(new ConfigOption<Integer>("distance", 50, Integer::valueOf));
    public ConfigOption<Integer> seed = add(new ConfigOption<Integer>("seed", 0, Integer::valueOf));
    public ConfigOption<Integer> seperation = add(new ConfigOption<Integer>("seperation", 8, Integer::valueOf));
    public ConfigOption<List<Biome.Category>> categories = add(new ConfigOption<List<Biome.Category>>("categories",
            Lists.newArrayList(Biome.Category.PLAINS, Biome.Category.TAIGA, Biome.Category.FOREST),
            LegacyStructureData::readCategories));
    public ConfigOption<List<String>> blacklist = add(
            new ConfigOption<List<String>>("blacklist", Lists.newArrayList(), LegacyStructureData::readBlackList));
    public ConfigOption<List<String>> dimensions = add(new ConfigOption<List<String>>("dimensions",
            Lists.newArrayList("overworld"), LegacyStructureData::readDimensions));
    public ConfigOption<List<PieceData>> pieces = add(new ConfigOption<List<PieceData>>("pieces",
            Lists.newArrayList(new PieceData("resource", BlockPos.ZERO)), LegacyStructureData::readPieces));
    public ConfigOption<Integer> base_height_offset = add(new ConfigOption<Integer>("base_height_offset", 0,
            Integer::valueOf));

    public LegacyStructureData(String name, Random rand) {
        this(name, rand.nextInt(Integer.MAX_VALUE));
    }

    public LegacyStructureData(String name, int seed) {
        this.name = name;
        this.seed.setValue(seed, this.getName());
    }

    public static List<Biome.Category> readCategories(String s) {
        if (s.startsWith("[") && s.endsWith("]")) {
            s = s.substring(1, s.length() - 1);
        }
        s = s.replaceAll(" ", "").replaceAll("\n", "");
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
                    categories.add(c);
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
        }
        s = s.replaceAll(" ", "").replaceAll("\n", "");
        List<String> list = Lists.newArrayList(s.split(","));
        List<String> newList = Lists.newArrayList();
        for (String s1 : list) {
            if (validateBiome(s1))
                newList.add(s1);
        }
        return newList;
    }

    public static boolean validateBiome(String s) {
        for (ResourceLocation b : ForgeRegistries.BIOMES.getKeys()) {
            if (b.toString().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static List<PieceData> readPieces(String s) {
        if (s.startsWith("[") && s.endsWith("]")) {
            s = s.substring(1, s.length() - 1);
        }
        s = s.replaceAll(" ", "").replaceAll("\n", "");
        List<String> cats = Lists.newArrayList();
        String[] parts = s.split(",");
        if (!(parts.length % 4 == 0)) {
            LOGGER.info("Something went wrong reading pieces: Comma count didn't match");
            return null;
        }
        for (int i = 0; i < parts.length / 4; i++) {
            int idx = i * 4;
            cats.add(parts[idx] + "," + parts[idx + 1] + "," + parts[idx + 2] + "," + parts[idx + 3]);
        }
        List<PieceData> categories = Lists.newArrayList();
        for (String cat : cats) {
            PieceData c = PieceData.fromString(cat);
            categories.add(c);
        }
        return categories;
    }

    public static List<String> readDimensions(String s) {
        if (s.startsWith("[") && s.endsWith("]")) {
            s = s.substring(1, s.length() - 1);
        }
        s = s.replaceAll(" ", "").replaceAll("\n", "");
        return Lists.newArrayList(s.split(","));
    }

    public <T> ConfigOption<T> add(ConfigOption<T> option) {
        if (!CONFIGS.contains(option)) {
            CONFIGS.add(option);
        }
        return option;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        StringBuilder config = new StringBuilder();
        for (ConfigOption<?> co : CONFIGS) {
            config.append(co.toString()).append(";");
        }
        return config.toString();
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

    @Override
    public boolean getGenerate() {
        return this.generate.getValue();
    }

    @Override
    public double getSpawnChance() {
        return this.spawn_chance.getValue();
    }

    @Override
    public boolean getNeedsGround() {
        return this.needs_ground.getValue();
    }

    @Override
    public int getDistance() {
        return this.distance.getValue();
    }

    @Override
    public int getSeparation() {
        return this.seperation.getValue();
    }

    @Override
    public int getSeed() {
        return this.seed.getValue();
    }

    @Override
    public List<? extends Category> getWhitelist() {
        return this.categories.getValue();
    }

    @Override
    public List<? extends String> getBlacklist() {
        return this.blacklist.getValue();
    }

    @Override
    public List<? extends String> getDimensions() {
        return this.dimensions.getValue();
    }

    @Override
    public boolean getUseRandomVarianting() {
        return this.use_random_varianting.getValue();
    }

    @Override
    public double getLootChance() {
        throw new RuntimeException("Tried to access loot chance of custom structure but there is no");
    }

    @Override
    public boolean getSpawnVillagers() {
        throw new RuntimeException("Tried to access spawn villagers of custom structure but there is no");
    }

    @Override
    public int compareTo(IStructureConfig o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public boolean isBuiltIn() {
        return false;
    }

    @Override
    public boolean getActive() {
        return this.getGenerate();
    }

    @Override
    public void setActive(boolean value) {
        this.generate.setValue(value, this.getName());
    }

    @Override
    public List<? extends IConfigOption<?>> getAllOptions() {
        return this.CONFIGS;
    }

    public StructureData toUpToDateData() {
        StructureData structure = new StructureData(DefaultStructureConfig.CUSTOM);
        structure.setName(this.getName());
        structure.setKey(this.getDataName());
        structure.setTransformLand(this.getNeedsGround());
        structure.setGenerate(this.getGenerate());
        structure.setSpawn_chance(this.getSpawnChance());
        structure.setUse_random_varianting(this.getUseRandomVarianting());
        structure.setDistance(this.getDistance());
        structure.setSeperation(this.getSeparation());
        structure.setSeed_modifier(this.getSeed());
        structure.setHeight_offset(this.base_height_offset.getValue());
        structure.setBiomeBlacklist(this.getBlacklist().stream().map(Objects::toString).collect(Collectors.toList()));
        structure.setBiomeCategoryWhitelist(this.getWhitelist().stream().map(Enum::toString).collect(Collectors.toList()));
        structure.setDimension_whitelist(this.getDimensions().stream().map(Objects::toString).collect(Collectors.toList()));
        return structure;
    }
}