/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

/*
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
package com.silverminer.shrines.packages.configuration;

import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.packages.datacontainer.SpawnConfiguration;
import com.silverminer.shrines.packages.datacontainer.StructureData;
import com.silverminer.shrines.packages.datacontainer.VariationConfiguration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Silverminer
 */
public class DefaultedStructureData {
   protected final String name;
   protected final ResourceLocation key;
   protected final int seed_modifier;
   protected boolean transformLand = true;
   protected boolean generate = true;
   protected double spawnChance = 0.6D;
   protected boolean useRandomVarianting = true;
   protected int distance = 60;
   protected int seperation = 12;
   protected int height_offset;
   protected ArrayList<Biome.BiomeCategory> biome_category_whitelist = Lists.newArrayList(Biome.BiomeCategory.PLAINS,
         Biome.BiomeCategory.FOREST, Biome.BiomeCategory.TAIGA, Biome.BiomeCategory.SAVANNA, Biome.BiomeCategory.JUNGLE,
         Biome.BiomeCategory.MESA, Biome.BiomeCategory.ICY, Biome.BiomeCategory.DESERT, Biome.BiomeCategory.SWAMP,
         Biome.BiomeCategory.MUSHROOM);
   protected ArrayList<String> biome_blacklist = Lists.newArrayList();
   protected ArrayList<String> dimension_whitelist = Lists.newArrayList("minecraft:overworld");
   protected String start_pool;
   protected ResourceLocation novel;

   public DefaultedStructureData(String name, String key, int seed_modifier) {
      this.name = name;
      this.key = new ResourceLocation(ShrinesMod.MODID, key);
      this.seed_modifier = seed_modifier;
   }

   public DefaultedStructureData addToBiomeCategoryWhitelist(Biome.BiomeCategory... whitelist) {
      this.biome_category_whitelist.addAll(Arrays.asList(whitelist));
      return this;
   }

   public DefaultedStructureData removeFromBiomeCategoryWhitelist(Biome.BiomeCategory... whitelist) {
      for (Biome.BiomeCategory s : whitelist) {
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

   @SuppressWarnings("unused")
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
      this.biome_blacklist.addAll(Arrays.asList(blacklist));
      return this;
   }

   public List<String> getBiomeCategoryWhitelist() {
      return this.biome_category_whitelist.stream().map(Enum::toString).collect(Collectors.toList());
   }

   public DefaultedStructureData setBiomeCategoryWhitelist(Biome.BiomeCategory... whitelist) {
      this.biome_category_whitelist.clear();
      this.biome_category_whitelist.addAll(Arrays.asList(whitelist));
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

   @SuppressWarnings("unused")
   public DefaultedStructureData setHeight_offset(int height_offset) {
      this.height_offset = height_offset;
      return this;
   }

   public ResourceLocation getKey() {
      return key;
   }

   public ResourceLocation getNovel() {
      return this.novel;
   }

   /**
    * Only required to set if the key is different to the structure's key
    *
    * @param novel the path to the structure's novel
    * @return the builder
    */
   public DefaultedStructureData setNovel(ResourceLocation novel) {
      this.novel = novel;
      return this;
   }

   public String getStart_pool() {
      return Objects.requireNonNullElseGet(this.start_pool, () -> new ResourceLocation(this.getKey() + "/start_pool").toString());
   }

   public StructureData toStructureData() {
      return new StructureData(
            this.getName(),
            this.getKey(),
            new SpawnConfiguration(
                  this.isTransformLand(),
                  this.getGenerate(),
                  this.getSpawnChance(),
                  this.getDistance(),
                  this.getSeperation(),
                  this.getSeedModifier(),
                  this.getHeight_offset(),
                  this.getBiomeBlacklist(),
                  this.getBiomeCategoryWhitelist(),
                  this.getDimensionWhitelist(),
                  this.getStart_pool(),
                  7),
            this.getKey(),
            this.getNovel(),
            this.getUseRandomVarianting() ? VariationConfiguration.ALL_ENABLED : VariationConfiguration.ALL_DISABLED);
   }
}