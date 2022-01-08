/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.structures.load;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.structures.load.options.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Random;

public class StructureData implements Comparable<StructureData> {
    public boolean successful = true;
    public boolean registered = false;
    public StringConfigOption name;
    public StringConfigOption key;

    public BooleanConfigOption transformLand;
    public BooleanConfigOption generate;
    public DoubleConfigOption spawn_chance;
    public BooleanConfigOption use_random_varianting;
    public IntegerConfigOption distance;
    public IntegerConfigOption seperation;
    public IntegerConfigOption seed_modifier;
    public IntegerConfigOption height_offset;

    public StringListConfigOption biome_blacklist;
    public StringListConfigOption biome_category_whitelist;
    public StringListConfigOption dimension_whitelist;

    public StringConfigOption start_pool;

    public StringConfigOption novel;

    public StructureData(CompoundNBT data) {
        this.name = new StringConfigOption(data.getCompound(ConfigOptions.name));
        this.key = new StringConfigOption(data.getCompound(ConfigOptions.key));
        this.transformLand = new BooleanConfigOption(data.getCompound(ConfigOptions.transform_land));
        this.generate = new BooleanConfigOption(data.getCompound(ConfigOptions.generate));
        this.spawn_chance = new DoubleConfigOption(data.getCompound(ConfigOptions.spawn_chance));
        this.use_random_varianting = new BooleanConfigOption(data.getCompound(ConfigOptions.use_random_varianting));
        this.distance = new IntegerConfigOption(data.getCompound(ConfigOptions.distance));
        this.seperation = new IntegerConfigOption(data.getCompound(ConfigOptions.seperation));
        this.seed_modifier = new IntegerConfigOption(data.getCompound(ConfigOptions.seed_modifier));
        this.height_offset = new IntegerConfigOption(data.getCompound(ConfigOptions.height_offset));
        this.biome_blacklist = new StringListConfigOption(data.getCompound(ConfigOptions.biome_blacklist));
        this.biome_category_whitelist = new StringListConfigOption(
                data.getCompound(ConfigOptions.biome_category_whitelist));
        this.dimension_whitelist = new StringListConfigOption(data.getCompound(ConfigOptions.dimension_whitelist));
        this.start_pool = new StringConfigOption(data.getCompound(ConfigOptions.start_pool));
        this.novel = new StringConfigOption(data.getCompound(ConfigOptions.novel));
        this.successful = data.getBoolean("Successful");
        this.registered = data.getBoolean("Registered");
    }

    public StructureData(DefaultedStructureData data) {
        this.name = new StringConfigOption(ConfigOptions.name, data.getName(), ConfigOptions.Comments.name);
        this.key = new StringConfigOption(ConfigOptions.key,
                new ResourceLocation(data.getKey()).toString().replace("minecraft:", ShrinesMod.MODID + ":"),
                ConfigOptions.Comments.key);
        this.transformLand = new BooleanConfigOption(ConfigOptions.transform_land, data.isTransformLand(),
                ConfigOptions.Comments.transform_land);
        this.generate = new BooleanConfigOption(ConfigOptions.generate, data.getGenerate(),
                ConfigOptions.Comments.generate);
        this.spawn_chance = new DoubleConfigOption(ConfigOptions.spawn_chance, data.getSpawnChance(),
                ConfigOptions.Comments.spawn_chance);
        this.use_random_varianting = new BooleanConfigOption(ConfigOptions.use_random_varianting,
                data.getUseRandomVarianting(), ConfigOptions.Comments.use_random_varianting);
        this.distance = new IntegerConfigOption(ConfigOptions.distance, data.getDistance(),
                ConfigOptions.Comments.distance);
        this.seperation = new IntegerConfigOption(ConfigOptions.seperation, data.getSeperation(),
                ConfigOptions.Comments.separation);
        this.seed_modifier = new IntegerConfigOption(ConfigOptions.seed_modifier, data.getSeedModifier(),
                ConfigOptions.Comments.seed_modifier);
        this.height_offset = new IntegerConfigOption(ConfigOptions.height_offset, data.getHeight_offset(),
                ConfigOptions.Comments.height_offset);
        this.biome_blacklist = new StringListConfigOption(ConfigOptions.biome_blacklist, data.getBiomeBlacklist(),
                ConfigOptions.Comments.biome_blacklist);
        this.biome_category_whitelist = new StringListConfigOption(ConfigOptions.biome_category_whitelist,
                data.getBiomeCategoryWhitelist(), ConfigOptions.Comments.biome_category_whitelist);
        this.dimension_whitelist = new StringListConfigOption(ConfigOptions.dimension_whitelist,
                data.getDimensionWhitelist(), ConfigOptions.Comments.dimension_whitelist);
        this.start_pool = new StringConfigOption(ConfigOptions.start_pool, data.getStart_pool(), ConfigOptions.Comments.start_pool);
        this.novel = new StringConfigOption(ConfigOptions.novel, data.getNovel(), ConfigOptions.Comments.novel);
    }

    public CompoundNBT write(CompoundNBT tag) {
        tag.put(ConfigOptions.name, this.name.write());
        tag.put(ConfigOptions.key, this.key.write());
        tag.put(ConfigOptions.transform_land, this.transformLand.write());
        tag.put(ConfigOptions.generate, this.generate.write());
        tag.put(ConfigOptions.spawn_chance, this.spawn_chance.write());
        tag.put(ConfigOptions.use_random_varianting, this.use_random_varianting.write());
        tag.put(ConfigOptions.distance, this.distance.write());
        tag.put(ConfigOptions.seperation, this.seperation.write());
        tag.put(ConfigOptions.seed_modifier, this.seed_modifier.write());
        tag.put(ConfigOptions.height_offset, this.height_offset.write());
        tag.put(ConfigOptions.biome_blacklist, this.biome_blacklist.write());
        tag.put(ConfigOptions.biome_category_whitelist, this.biome_category_whitelist.write());
        tag.put(ConfigOptions.dimension_whitelist, this.dimension_whitelist.write());
        tag.put(ConfigOptions.start_pool, this.start_pool.write());
        tag.put(ConfigOptions.novel, this.novel.write());
        tag.putBoolean("Successful", this.successful);
        tag.putBoolean("Registered", this.registered);
        return tag;
    }

    public Boolean getGenerate() {
        return generate.getValue();
    }

    public void setGenerate(Boolean generate) {
        this.generate.setValue(generate);
        ;
    }

    public Double getSpawn_chance() {
        return spawn_chance.getValue();
    }

    public void setSpawn_chance(Double spawn_chance) {
        this.spawn_chance.setValue(spawn_chance);
    }

    public Boolean getUse_random_varianting() {
        return use_random_varianting.getValue();
    }

    public void setUse_random_varianting(Boolean use_random_varianting) {
        this.use_random_varianting.setValue(use_random_varianting);
    }

    public Integer getDistance() {
        return distance.getValue();
    }

    public void setDistance(Integer distance) {
        distance = Math.abs(distance);
        this.distance.setValue(distance);
    }

    public Integer getSeperation() {
        return seperation.getValue();
    }

    public void setSeperation(Integer seperation) {
        seperation = Math.abs(seperation);
        this.seperation.setValue(seperation);
    }

    public Integer getSeed_modifier() {
        return seed_modifier.getValue();
    }

    public void setSeed_modifier(Integer seed_modifier) {
        if (seed_modifier == 0) {
            seed_modifier = new Random().nextInt(Integer.MAX_VALUE);
        }
        this.seed_modifier.setValue(seed_modifier);
    }

    public Integer getHeight_offset() {
        return height_offset.getValue();
    }

    public void setHeight_offset(Integer height_offset) {
        this.height_offset.setValue(height_offset);
    }

    public List<String> getBiomeBlacklist() {
        return biome_blacklist.getValue();
    }

    public void setBiomeBlacklist(List<String> biome_blacklist) {
        this.biome_blacklist.setValue(biome_blacklist);
    }

    public List<String> getBiomeCategoryWhitelist() {
        return biome_category_whitelist.getValue();
    }

    public void setBiomeCategoryWhitelist(List<String> biome_category_whitelist) {
        this.biome_category_whitelist.setValue(biome_category_whitelist);
    }

    public List<String> getDimension_whitelist() {
        return dimension_whitelist.getValue();
    }

    public void setDimension_whitelist(List<String> dimension_whitelist) {
        this.dimension_whitelist.setValue(dimension_whitelist);
    }

    public boolean getTransformLand() {
        return transformLand.getValue();
    }

    public void setTransformLand(boolean transformLand) {
        this.transformLand.setValue(transformLand);
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public String getKey() {
        return key.getValue();
    }

    public void setKey(String key) {
        this.key.setValue(new ResourceLocation(key).toString().replace("minecraft:", ShrinesMod.MODID + ":"));
    }

    public String getNovel() {
        return novel.getValue();
    }

    public void setNovel(String novel) {
        this.novel.setValue(novel);
    }

    public String getStart_pool() {
        return start_pool.getValue();
    }

    public void setStart_pool(String start_pool) {
        this.start_pool.setValue(start_pool);
    }

    @Override
    public int compareTo(StructureData o) {
        return this.getName().compareTo(o.getName());
    }
}