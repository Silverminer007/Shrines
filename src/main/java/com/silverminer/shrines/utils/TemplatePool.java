/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.utils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TemplatePool implements Comparable<TemplatePool> {
    private final ResourceLocation name;
    private final ArrayList<Entry> entries;
    private ResourceLocation saveName;

    public TemplatePool(ResourceLocation name, ArrayList<Entry> entries) {
        this.name = name;
        this.saveName = name;
        this.entries = entries;
    }

    public static TemplatePool read(CompoundNBT nbt) {
        TemplatePool pool = new TemplatePool(new ResourceLocation(nbt.getString("Name")), nbt.getList("Entries", 10).stream().map(inbt -> Entry.read((CompoundNBT) inbt)).collect(Collectors.toCollection(ArrayList::new)));
        pool.setSaveName(new ResourceLocation(nbt.getString("Save Name")));
        return pool;
    }

    public static TemplatePool fromString(String s) {
        JsonObject jo = new JsonParser().parse(s).getAsJsonObject();
        return fromJson(jo);
    }

    public static TemplatePool fromJson(JsonObject jo) {
        ResourceLocation name = new ResourceLocation(jo.get("name").getAsString());
        ArrayList<Entry> entries = Lists.newArrayList();
        JsonArray elements = jo.get("elements").getAsJsonArray();
        for (int i = 0; i < elements.size(); i++) {
            Entry entry = Entry.fromJsonObject(elements.get(i).getAsJsonObject());
            if (entry != null)
                entries.add(entry);
        }
        return new TemplatePool(name, entries);
    }

    public CompoundNBT write() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Name", this.getName().toString());
        nbt.putString("Save Name", this.getSaveName().toString());
        ListNBT entries = new ListNBT();
        for (Entry e : this.getEntries()) {
            entries.add(e.write());
        }
        nbt.put("Entries", entries);
        return nbt;
    }

    public String toString() {
        Gson g = new Gson();
        return g.toJson(this.toJsonObject());
    }

    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.addProperty("name", this.getName().toString());
        jo.addProperty("fallback", "minecraft:empty");
        JsonArray ja = new JsonArray();
        for (Entry e : this.getEntries()) {
            ja.add(e.toJsonObject());
        }
        jo.add("elements", ja);
        return jo;
    }

    public ResourceLocation getName() {
        return name;
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public ResourceLocation getSaveName() {
        return saveName;
    }

    public void setSaveName(ResourceLocation saveName) {
        this.saveName = saveName;
    }

    @Override
    public int compareTo(@NotNull TemplatePool o) {
        return this.getName().compareTo(o.getName());
    }

    public static class Entry implements Comparable<Entry> {
        private ResourceLocation template;
        private int weight;
        private boolean terrain_matching;

        public Entry(String template) {
            this(new ResourceLocation(template), 1, false);
        }

        public Entry(ResourceLocation template, int weight, boolean terrain_matching) {
            this.template = template;
            this.weight = weight;
            this.terrain_matching = terrain_matching;
        }

        public static Entry read(CompoundNBT nbt) {
            return new Entry(new ResourceLocation(nbt.getString("Template")), nbt.getInt("weight"), nbt.getBoolean("Terrain Matching"));
        }

        public static Entry fromJsonObject(JsonObject jsonObject) {
            try {
                int weight = jsonObject.get("weight").getAsInt();
                JsonObject element = jsonObject.get("element").getAsJsonObject();
                ResourceLocation template = new ResourceLocation(element.get("location").getAsString());
                boolean terrain_matching = element.get("projection").getAsString().equals("terrain_matching");
                return new Entry(template, weight, terrain_matching);
            } catch (NullPointerException e) {
                e.printStackTrace();
                return null;
            }
        }

        public CompoundNBT write() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("Template", this.template.toString());
            nbt.putInt("weight", this.weight);
            nbt.putBoolean("Terrain Matching", this.terrain_matching);
            return nbt;
        }

        public JsonObject toJsonObject() {
            JsonObject entry = new JsonObject();
            entry.addProperty("weight", this.getWeight());
            JsonObject element = new JsonObject();
            element.addProperty("location", this.getTemplate().toString());
            element.addProperty("processors", "shrines:default_processors");
            if (this.isTerrain_matching()) {
                element.addProperty("projection", "terrain_matching");
            } else {
                element.addProperty("projection", "rigid");
            }
            element.addProperty("element_type", "minecraft:single_pool_element");
            entry.add("element", element);
            return entry;
        }

        public ResourceLocation getTemplate() {
            return template;
        }

        public void setTemplate(ResourceLocation template) {
            this.template = template;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public boolean isTerrain_matching() {
            return terrain_matching;
        }

        public void setTerrainMatching(boolean terrain_matching) {
            this.terrain_matching = terrain_matching;
        }

        @Override
        public int compareTo(@NotNull TemplatePool.Entry o) {
            return this.getTemplate().compareTo(o.getTemplate());
        }
    }
}