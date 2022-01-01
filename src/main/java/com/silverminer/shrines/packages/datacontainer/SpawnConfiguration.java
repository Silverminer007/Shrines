package com.silverminer.shrines.packages.datacontainer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.packages.configuration.ConfigOptions;

import java.util.List;

public class SpawnConfiguration {
   public static final Codec<SpawnConfiguration> CODEC = RecordCodecBuilder.create((structureDataInstance) -> structureDataInstance.group(
               Codec.BOOL.optionalFieldOf(ConfigOptions.LATEST.transformLand(), true).forGetter(SpawnConfiguration::isTransformLand),
               Codec.BOOL.optionalFieldOf(ConfigOptions.LATEST.generate(), true).forGetter(SpawnConfiguration::isGenerate),
               Codec.DOUBLE.fieldOf(ConfigOptions.LATEST.spawnChance()).forGetter(SpawnConfiguration::getSpawn_chance),
               Codec.BOOL.optionalFieldOf(ConfigOptions.LATEST.useRandomVarianting(), false).forGetter(SpawnConfiguration::isUse_random_varianting),
               Codec.intRange(1, Integer.MAX_VALUE).fieldOf(ConfigOptions.LATEST.distance()).forGetter(SpawnConfiguration::getDistance),
               Codec.intRange(1, Integer.MAX_VALUE).fieldOf(ConfigOptions.LATEST.separation()).forGetter(SpawnConfiguration::getSeparation),
               Codec.INT.fieldOf(ConfigOptions.LATEST.seedModifier()).forGetter(SpawnConfiguration::getSeed_modifier),
               Codec.INT.optionalFieldOf(ConfigOptions.LATEST.heightOffset(), 0).forGetter(SpawnConfiguration::getHeight_offset),
               Codec.list(Codec.STRING).fieldOf(ConfigOptions.LATEST.biomeBlacklist()).forGetter(SpawnConfiguration::getBiome_blacklist),
               Codec.list(Codec.STRING).fieldOf(ConfigOptions.LATEST.biomeCategoryWhitelist()).forGetter(SpawnConfiguration::getBiome_category_whitelist),
               Codec.list(Codec.STRING).fieldOf(ConfigOptions.LATEST.dimensionWhitelist()).forGetter(SpawnConfiguration::getDimension_whitelist),
               Codec.STRING.fieldOf(ConfigOptions.LATEST.startPool()).forGetter(SpawnConfiguration::getStart_pool),
               Codec.INT.optionalFieldOf(ConfigOptions.LATEST.jigsawMaxDepth(), 7).forGetter(SpawnConfiguration::getJigsawMaxDepth))
         .apply(structureDataInstance, SpawnConfiguration::new));
   private boolean transformLand;
   private boolean generate;
   private double spawn_chance;
   private boolean use_random_varianting;
   private int distance;
   private int separation;
   private int seed_modifier;
   private int height_offset;

   private List<String> biome_blacklist;
   private List<String> biome_category_whitelist;
   private List<String> dimension_whitelist;

   private String start_pool;
   private int jigsawMaxDepth;

   public SpawnConfiguration(boolean transformLand, boolean generate, double spawn_chance, boolean use_random_varianting, int distance, int separation, int seed_modifier, int height_offset, List<String> biome_blacklist, List<String> biome_category_whitelist, List<String> dimension_whitelist, String start_pool, int jigsawMaxDepth) {
      this.transformLand = transformLand;
      this.generate = generate;
      this.spawn_chance = spawn_chance;
      this.use_random_varianting = use_random_varianting;
      this.distance = distance;
      this.separation = separation;
      this.seed_modifier = seed_modifier;
      this.height_offset = height_offset;
      this.biome_blacklist = biome_blacklist;
      this.biome_category_whitelist = biome_category_whitelist;
      this.dimension_whitelist = dimension_whitelist;
      this.start_pool = start_pool;
      this.jigsawMaxDepth = jigsawMaxDepth;
   }

   public boolean isTransformLand() {
      return transformLand;
   }

   public void setTransformLand(boolean transformLand) {
      this.transformLand = transformLand;
   }

   public boolean isGenerate() {
      return generate;
   }

   public void setGenerate(boolean generate) {
      this.generate = generate;
   }

   public double getSpawn_chance() {
      return spawn_chance;
   }

   public void setSpawn_chance(double spawn_chance) {
      this.spawn_chance = spawn_chance;
   }

   public boolean isUse_random_varianting() {
      return use_random_varianting;
   }

   public void setUse_random_varianting(boolean use_random_varianting) {
      this.use_random_varianting = use_random_varianting;
   }

   public int getDistance() {
      return distance;
   }

   public void setDistance(int distance) {
      this.distance = distance;
   }

   public int getSeparation() {
      return separation;
   }

   public void setSeparation(int separation) {
      this.separation = separation;
   }

   public int getSeed_modifier() {
      return seed_modifier;
   }

   public void setSeed_modifier(int seed_modifier) {
      this.seed_modifier = seed_modifier;
   }

   public int getHeight_offset() {
      return height_offset;
   }

   public void setHeight_offset(int height_offset) {
      this.height_offset = height_offset;
   }

   public List<String> getBiome_blacklist() {
      return biome_blacklist;
   }

   public void setBiome_blacklist(List<String> biome_blacklist) {
      this.biome_blacklist = biome_blacklist;
   }

   public List<String> getBiome_category_whitelist() {
      return biome_category_whitelist;
   }

   public void setBiome_category_whitelist(List<String> biome_category_whitelist) {
      this.biome_category_whitelist = biome_category_whitelist;
   }

   public List<String> getDimension_whitelist() {
      return dimension_whitelist;
   }

   public void setDimension_whitelist(List<String> dimension_whitelist) {
      this.dimension_whitelist = dimension_whitelist;
   }

   public String getStart_pool() {
      return start_pool;
   }

   public void setStart_pool(String start_pool) {
      this.start_pool = start_pool;
   }

   public int getJigsawMaxDepth() {
      return jigsawMaxDepth;
   }

   public void setJigsawMaxDepth(int jigsawMaxDepth) {
      this.jigsawMaxDepth = jigsawMaxDepth;
   }
}
