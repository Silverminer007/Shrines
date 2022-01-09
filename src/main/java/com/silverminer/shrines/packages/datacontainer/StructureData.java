/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class StructureData implements Comparable<StructureData> {
   public static final Codec<StructureData> CODEC = RecordCodecBuilder.create((structureDataInstance) -> structureDataInstance.group(
               Codec.STRING.fieldOf(ConfigOptions.LATEST.name()).forGetter(StructureData::getName),
               ResourceLocation.CODEC.fieldOf(ConfigOptions.LATEST.key()).forGetter(StructureData::getKey),
               SpawnConfiguration.CODEC.fieldOf("spawn_configuration").forGetter(StructureData::getSpawnConfiguration),
               ResourceLocation.CODEC.optionalFieldOf(ConfigOptions.LATEST.iconPath(), null).forGetter(StructureData::getIconPath),
               ResourceLocation.CODEC.optionalFieldOf(ConfigOptions.LATEST.novel(), new ResourceLocation("")).forGetter(StructureData::getNovel),
               VariationConfiguration.CODEC.optionalFieldOf("variation_configuration", VariationConfiguration.ALL_DISABLED).forGetter(StructureData::getVariationConfiguration))
         .apply(structureDataInstance, StructureData::new));
   protected static final Logger LOGGER = LogManager.getLogger(StructureData.class);
   private final SpawnConfiguration spawnConfiguration;
   private final VariationConfiguration variationConfiguration;
   private String name;
   private ResourceLocation key;
   private ResourceLocation novel;
   private ResourceLocation iconPath;

   public StructureData(String name, ResourceLocation key, SpawnConfiguration spawnConfiguration, @Nullable ResourceLocation iconPath, @Nullable ResourceLocation novel) {
      this(name, key, spawnConfiguration, iconPath, novel, VariationConfiguration.ALL_DISABLED);
   }

   /**
    * @param name               the Displayname for the Structure
    * @param key                the Key of the structure. This identifies this structure
    * @param spawnConfiguration spawn configuration options of this structure
    * @param iconPath           the path to the icon of this structure or null to use key's value
    * @param novel              the path to the structure's Novels or null to use key's value
    */
   public StructureData(String name, ResourceLocation key, SpawnConfiguration spawnConfiguration, @Nullable ResourceLocation iconPath, @Nullable ResourceLocation novel, VariationConfiguration variationConfiguration) {
      this.name = name;
      this.key = key;
      this.novel = Objects.requireNonNullElse(novel, key);
      this.iconPath = Objects.requireNonNullElse(iconPath, key);
      this.spawnConfiguration = spawnConfiguration;
      this.variationConfiguration = variationConfiguration;
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

   public VariationConfiguration getVariationConfiguration() {
      return variationConfiguration;
   }

   public ResourceLocation getKey() {
      return key;
   }

   public void setKey(ResourceLocation key) {
      this.key = key;
   }

   public SpawnConfiguration getSpawnConfiguration() {
      return spawnConfiguration;
   }

   public ResourceLocation getNovel() {
      return novel;
   }

   public void setNovel(ResourceLocation novel) {
      this.novel = novel;
   }

   public ResourceLocation getIconPath() {
      return iconPath;
   }

   public void setIconPath(ResourceLocation iconPath) {
      this.iconPath = iconPath;
   }

   @Override
   public int compareTo(StructureData o) {
      return this.getName().compareTo(o.getName());
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}