/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.packages.io.legacy_181.configuration;

import com.google.common.collect.Lists;
import com.silverminer.shrines.packages.datacontainer.SpawnConfiguration;
import com.silverminer.shrines.packages.datacontainer.StructureData;
import com.silverminer.shrines.packages.datacontainer.VariationConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
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
   public ConfigOption<Boolean> generate = add(new ConfigOption<>("generate", true, Boolean::valueOf));
   public ConfigOption<Double> spawn_chance = add(new ConfigOption<>("spawn_chance", 0.6D, Double::valueOf));
   public ConfigOption<Boolean> needs_ground = add(new ConfigOption<>("needs_ground", true, Boolean::valueOf));
   public ConfigOption<Boolean> use_random_varianting = add(new ConfigOption<>("use_random_varianting", true, Boolean::valueOf));
   public ConfigOption<Integer> distance = add(new ConfigOption<>("distance", 50, Integer::valueOf));
   public ConfigOption<Integer> seed = add(new ConfigOption<>("seed", 0, Integer::valueOf));
   public ConfigOption<Integer> seperation = add(new ConfigOption<>("seperation", 8, Integer::valueOf));
   public ConfigOption<List<Biome.BiomeCategory>> categories = add(new ConfigOption<>("categories",
         Lists.newArrayList(Biome.BiomeCategory.PLAINS, Biome.BiomeCategory.TAIGA, Biome.BiomeCategory.FOREST),
         LegacyStructureData::readCategories));
   public ConfigOption<List<String>> blacklist = add(
         new ConfigOption<>("blacklist", Lists.newArrayList(), LegacyStructureData::readBlackList));
   public ConfigOption<List<String>> dimensions = add(new ConfigOption<>("dimensions",
         Lists.newArrayList("overworld"), LegacyStructureData::readDimensions));
   public ConfigOption<List<PieceData>> pieces = add(new ConfigOption<>("pieces",
         Lists.newArrayList(new PieceData("resource", BlockPos.ZERO)), LegacyStructureData::readPieces));
   public ConfigOption<Integer> base_height_offset = add(new ConfigOption<>("base_height_offset", 0,
         Integer::valueOf));

   public LegacyStructureData(String name, Random rand) {
      this(name, rand.nextInt(Integer.MAX_VALUE));
   }

   public LegacyStructureData(String name, int seed) {
      this.name = name;
      this.seed.setValue(seed, this.getName());
   }

   public String getName() {
      return this.name;
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
   public List<? extends Biome.BiomeCategory> getWhitelist() {
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

   public static List<Biome.BiomeCategory> readCategories(String s) {
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
         List<Biome.BiomeCategory> categories = Lists.newArrayList();
         for (String cat : cats) {
            if (cat.equals("DEFAULT")) {
               return Lists.newArrayList(Biome.BiomeCategory.PLAINS, Biome.BiomeCategory.TAIGA, Biome.BiomeCategory.FOREST);
            } else if (cat.equals("ALL")) {
               return Lists.newArrayList(Biome.BiomeCategory.values());
            } else {
               Biome.BiomeCategory c = Biome.BiomeCategory.valueOf(cat);
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
   public int compareTo(IStructureConfig o) {
      return this.getName().compareTo(o.getName());
   }

   public StructureData toUpToDateData() {
      VariationConfiguration variationConfiguration = this.getUseRandomVarianting() ? VariationConfiguration.ALL_ENABLED : VariationConfiguration.ALL_DISABLED;
      SpawnConfiguration spawnConfiguration = new SpawnConfiguration(this.getNeedsGround(), this.getGenerate(), this.getSpawnChance(), this.getDistance(), this.getSeparation(),
            this.getSeed(), this.base_height_offset.getValue(), this.getBlacklist().stream().map(Objects::toString).collect(Collectors.toList()),
            this.getWhitelist().stream().map(Enum::toString).collect(Collectors.toList()), this.getDimensions().stream().map(Objects::toString).collect(Collectors.toList()), "",
            7);
      return new StructureData(this.getName(), new ResourceLocation(this.getDataName()), spawnConfiguration, null, null, variationConfiguration);
   }
}