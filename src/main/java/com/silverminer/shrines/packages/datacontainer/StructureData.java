package com.silverminer.shrines.packages.datacontainer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.packages.configuration.ConfigOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class StructureData implements Comparable<StructureData> {
   protected static final Logger LOGGER = LogManager.getLogger(StructureData.class);
   public static final Codec<StructureData> CODEC = RecordCodecBuilder.create((structureDataInstance) -> structureDataInstance.group(
               Codec.STRING.fieldOf(ConfigOptions.LATEST.name()).forGetter(StructureData::getName),
               ResourceLocation.CODEC.fieldOf(ConfigOptions.LATEST.key()).forGetter(StructureData::getKey),
               Codec.STRING.optionalFieldOf(ConfigOptions.LATEST.novel(), "").forGetter(StructureData::getNovel),
               SpawnConfiguration.CODEC.fieldOf("spawn_configuration").forGetter(StructureData::getSpawnConfiguration),
               ResourceLocation.CODEC.optionalFieldOf(ConfigOptions.LATEST.iconPath(), null).forGetter(StructureData::getIconPath))
         .apply(structureDataInstance, StructureData::new));
   private String name;
   private ResourceLocation key;
   private String novel;
   private ResourceLocation iconPath;
   private final SpawnConfiguration spawnConfiguration;

   /**
    * @param name               the Displayname for the Structure
    * @param key                the Key of the structure. This identifies this structure
    * @param novel              the novel of this structure
    * @param spawnConfiguration spawn configuration options of this structure
    * @param iconPath           the path to the icon of this structure or null to use key's value
    */
   public StructureData(String name, ResourceLocation key, String novel, SpawnConfiguration spawnConfiguration, @Nullable ResourceLocation iconPath) {
      this.name = name;
      this.key = key;
      this.novel = novel;
      this.iconPath = Objects.requireNonNullElse(iconPath, key);
      this.spawnConfiguration = spawnConfiguration;
   }

   @Nonnull
   public static CompoundTag saveToNBT(StructureData structureData) {
      DataResult<Tag> dataResult = CODEC.encode(structureData, NbtOps.INSTANCE, new CompoundTag());
      Optional<Tag> optional = dataResult.resultOrPartial(LOGGER::error);
      return (CompoundTag) optional.filter(value -> value instanceof CompoundTag).orElse(new CompoundTag());
   }

   @Nonnull
   public static JsonElement saveToJSON(StructureData structureData) {
      DataResult<JsonElement> dataResult = CODEC.encode(structureData, JsonOps.INSTANCE, new JsonObject());
      Optional<JsonElement> optional = dataResult.resultOrPartial(LOGGER::error);
      return optional.orElse(new JsonObject());
   }

   @Nullable
   public static StructureData loadFromNBT(CompoundTag tag) {
      DataResult<Pair<StructureData, Tag>> dataResult = CODEC.decode(NbtOps.INSTANCE, tag);
      Optional<Pair<StructureData, Tag>> optional = dataResult.resultOrPartial(LOGGER::error);
      return optional.map(Pair::getFirst).orElse(null);
   }

   @Nullable
   public static StructureData loadFromJSON(JsonElement tag) {
      DataResult<Pair<StructureData, JsonElement>> dataResult = CODEC.decode(JsonOps.INSTANCE, tag);
      Optional<Pair<StructureData, JsonElement>> optional = dataResult.resultOrPartial(LOGGER::error);
      return optional.map(Pair::getFirst).orElse(null);
   }

   public String getName() {
      return name;
   }

   public ResourceLocation getKey() {
      return key;
   }

   public boolean isTransformLand() {
      return this.getSpawnConfiguration().isTransformLand();
   }

   public boolean isGenerate() {
      return this.getSpawnConfiguration().isGenerate();
   }

   public double getSpawn_chance() {
      return this.getSpawnConfiguration().getSpawn_chance();
   }

   public boolean isUse_random_varianting() {
      return this.getSpawnConfiguration().isUse_random_varianting();
   }

   public int getDistance() {
      return this.getSpawnConfiguration().getDistance();
   }

   public int getSeparation() {
      return this.getSpawnConfiguration().getSeparation();
   }

   public int getSeed_modifier() {
      return this.getSpawnConfiguration().getSeed_modifier();
   }

   public int getHeight_offset() {
      return this.getSpawnConfiguration().getHeight_offset();
   }

   public List<String> getBiome_blacklist() {
      return this.getSpawnConfiguration().getBiome_blacklist();
   }

   public List<String> getBiome_category_whitelist() {
      return this.getSpawnConfiguration().getBiome_category_whitelist();
   }

   public List<String> getDimension_whitelist() {
      return this.getSpawnConfiguration().getDimension_whitelist();
   }

   public String getStart_pool() {
      return this.getSpawnConfiguration().getStart_pool();
   }

   public String getNovel() {
      return novel;
   }

   public int getJigsawMaxDepth() {
      return this.getSpawnConfiguration().getJigsawMaxDepth();
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setKey(ResourceLocation key) {
      this.key = key;
   }

   public void setTransformLand(boolean transformLand) {
      this.getSpawnConfiguration().setTransformLand(transformLand);
   }

   public void setGenerate(boolean generate) {
      this.getSpawnConfiguration().setGenerate(generate);
   }

   public void setSpawn_chance(double spawn_chance) {
      this.getSpawnConfiguration().setSpawn_chance(spawn_chance);
   }

   public void setUse_random_varianting(boolean use_random_varianting) {
      this.getSpawnConfiguration().setUse_random_varianting(use_random_varianting);
   }

   public void setDistance(int distance) {
      this.getSpawnConfiguration().setDistance(distance);
   }

   public void setSeparation(int separation) {
      this.getSpawnConfiguration().setSeparation(separation);
   }

   public void setSeed_modifier(int seed_modifier) {
      if (seed_modifier == 0) {
         seed_modifier = new Random().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
      }
      this.getSpawnConfiguration().setSeed_modifier(seed_modifier);
   }

   public void setHeight_offset(int height_offset) {
      this.getSpawnConfiguration().setHeight_offset(height_offset);
   }

   public void setBiome_blacklist(List<String> biome_blacklist) {
      this.getSpawnConfiguration().setBiome_blacklist(biome_blacklist);
   }

   public void setBiome_category_whitelist(List<String> biome_category_whitelist) {
      this.getSpawnConfiguration().setBiome_category_whitelist(biome_category_whitelist);
   }

   public void setDimension_whitelist(List<String> dimension_whitelist) {
      this.getSpawnConfiguration().setDimension_whitelist(dimension_whitelist);
   }

   public void setStart_pool(String start_pool) {
      this.getSpawnConfiguration().setStart_pool(start_pool);
   }

   public void setNovel(String novel) {
      this.novel = novel;
   }

   public void setJigsawMaxDepth(int jigsawMaxDepth) {
      this.getSpawnConfiguration().setJigsawMaxDepth(jigsawMaxDepth);
   }

   public SpawnConfiguration getSpawnConfiguration() {
      return spawnConfiguration;
   }

   public ResourceLocation getIconPath() {
      return iconPath;
   }

   public void setIconPath(ResourceLocation iconPath) {
      this.iconPath = iconPath;
   }

   @Override
   public int compareTo(@NotNull StructureData o) {
      return this.getName().compareTo(o.getName());
   }
}