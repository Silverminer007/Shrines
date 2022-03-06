/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.datacontainer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.data.ProcessorLists;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TemplatePool implements Comparable<TemplatePool> {
    public static final Codec<TemplatePool> CODEC_V1 = RecordCodecBuilder.create((templatePoolInstance -> templatePoolInstance.group(
                    ResourceLocation.CODEC.fieldOf("name").forGetter(TemplatePool::getName),
                    ResourceLocation.CODEC.fieldOf("fallback").forGetter(TemplatePool::getFallback),
                    Codec.mapPair(
                            Entry.CODEC_V1.fieldOf("element"),
                            Codec.intRange(1, 150).fieldOf("weight")).codec().listOf().fieldOf("elements").forGetter(TemplatePool::getRawEntries))
            .apply(templatePoolInstance, TemplatePool::new)));
    protected static final Logger LOGGER = LogManager.getLogger(TemplatePool.class);
    private final ResourceLocation name;
    private final ArrayList<Entry> entries;
    private ResourceLocation fallback;
    private ResourceLocation saveName;

    public TemplatePool(ResourceLocation name, ResourceLocation fallback, List<Pair<Entry, Integer>> entries) {
        this(name, fallback, entries.stream().map(entryIntegerPair -> {
            Entry e = entryIntegerPair.getFirst();
            e.setWeight(entryIntegerPair.getSecond());
            return e;
        }).collect(Collectors.toCollection(ArrayList::new)));
    }

    public TemplatePool(ResourceLocation name, ArrayList<Entry> entries) {
        this(name, new ResourceLocation("empty"), entries);
    }

    public TemplatePool(ResourceLocation name, ResourceLocation fallback, ArrayList<Entry> entries) {
        this.name = name;
        this.saveName = name;
        this.fallback = fallback;
        this.entries = entries;
    }

    public static TemplatePool read(CompoundTag nbt) {
        TemplatePool pool = new TemplatePool(new ResourceLocation(nbt.getString("Name")), nbt.getList("Entries", 10).stream().map(inbt -> Entry.read((CompoundTag) inbt)).collect(Collectors.toCollection(ArrayList::new)));
        pool.setSaveName(new ResourceLocation(nbt.getString("Save Name")));
        return pool;
    }

    public static TemplatePool fromString(String s) {
        JsonObject jo = JsonParser.parseString(s).getAsJsonObject();
        return fromJson(jo);
    }

    public static TemplatePool fromJson(JsonObject jo) {
        DataResult<Pair<TemplatePool, JsonElement>> dataResult = CODEC_V1.decode(JsonOps.INSTANCE, jo);
        Optional<Pair<TemplatePool, JsonElement>> optionalTemplatePoolJsonElementPair = dataResult.resultOrPartial(LOGGER::error);
        DataResult<Pair<StructureTemplatePool, JsonElement>> pairDataResult = StructureTemplatePool.DIRECT_CODEC.decode(JsonOps.INSTANCE, jo);
        pairDataResult.resultOrPartial(LOGGER::error);
        return optionalTemplatePoolJsonElementPair.map(Pair::getFirst).orElse(null);
    }

    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("Name", this.getName().toString());
        nbt.putString("Save Name", this.getSaveName().toString());
        ListTag entries = new ListTag();
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
        DataResult<JsonElement> dataResult = CODEC_V1.encode(this, JsonOps.INSTANCE, new JsonObject());
        Optional<JsonElement> optionalJsonElement = dataResult.resultOrPartial(LOGGER::error);
        if (optionalJsonElement.isPresent() && optionalJsonElement.get() instanceof JsonObject jsonObject) {
            return jsonObject;
        } else {
            return null;
        }
    }

    public ResourceLocation getName() {
        return name;
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    private List<Pair<Entry, Integer>> getRawEntries() {
        return this.getEntries().stream().map(entry -> Pair.of(entry, entry.getWeight())).collect(Collectors.toList());
    }

    public ResourceLocation getSaveName() {
        return saveName;
    }

    public void setSaveName(ResourceLocation saveName) {
        this.saveName = saveName;
    }

    public ResourceLocation getFallback() {
        return fallback;
    }

    public void setFallback(ResourceLocation fallback) {
        this.fallback = fallback;
    }

    @Override
    public int compareTo(@NotNull TemplatePool o) {
        return this.getName().compareTo(o.getName());
    }

    public static class Entry implements Comparable<Entry> {
        public static final Codec<Entry> CODEC_V1 = RecordCodecBuilder.create((entryInstance) ->
                entryInstance.group(templateCodec(), processorsCodec(), projectionCodec(), typeCodec())
                        .apply(entryInstance, Entry::new));
        protected final Holder<StructureProcessorList> processors;
        private final ResourceLocation element_type;
        private ResourceLocation template;
        private int weight;
        private boolean terrain_matching;

        private Entry(ResourceLocation template, Holder<StructureProcessorList> processorListSupplier, StructureTemplatePool.Projection projection, ResourceLocation type) {
            this(template, 1, projection == StructureTemplatePool.Projection.TERRAIN_MATCHING, type, processorListSupplier);
        }

        public Entry(ResourceLocation template) {
            this(template, 1, false);
        }

        public Entry(ResourceLocation template, int weight, boolean terrain_matching) {
            this(template, weight, terrain_matching, new ResourceLocation("single_pool_element"), ProcessorLists.SHRINES);
        }

        public Entry(ResourceLocation template, int weight, boolean terrain_matching, ResourceLocation element_type, Holder<StructureProcessorList> processorListSupplier) {
            this.template = template;
            this.weight = weight;
            this.terrain_matching = terrain_matching;
            this.element_type = element_type;
            this.processors = processorListSupplier;
        }

        private static <E extends Entry> RecordCodecBuilder<E, Holder<StructureProcessorList>> processorsCodec() {
            return StructureProcessorType.LIST_CODEC.fieldOf("processors").forGetter((entry) -> entry.processors);
        }

        private static <E extends Entry> RecordCodecBuilder<E, ResourceLocation> templateCodec() {
            return ResourceLocation.CODEC.fieldOf("location").forGetter(Entry::getTemplate);
        }

        private static <E extends Entry> RecordCodecBuilder<E, StructureTemplatePool.Projection> projectionCodec() {
            return StructureTemplatePool.Projection.CODEC.fieldOf("projection").forGetter(Entry::getProjection);
        }

        private static <E extends Entry> RecordCodecBuilder<E, ResourceLocation> typeCodec() {
            return ResourceLocation.CODEC.fieldOf("element_type").forGetter(Entry::getType);
        }

        public static Entry read(CompoundTag nbt) {
            DataResult<Pair<Entry, Tag>> dataResult = CODEC_V1.decode(NbtOps.INSTANCE, nbt);
            Optional<Pair<Entry, Tag>> optionalEntryTagPair = dataResult.resultOrPartial(LOGGER::error);
            return optionalEntryTagPair.map(Pair::getFirst).orElse(null);
        }

        public StructureTemplatePool.Projection getProjection() {
            return this.terrain_matching ? StructureTemplatePool.Projection.TERRAIN_MATCHING : StructureTemplatePool.Projection.RIGID;
        }

        public CompoundTag write() {
            DataResult<Tag> dataResult = CODEC_V1.encode(this, NbtOps.INSTANCE, new CompoundTag());
            Optional<Tag> optionalTag = dataResult.resultOrPartial(LOGGER::error);
            return optionalTag.filter(tag -> tag instanceof CompoundTag).map(tag -> (CompoundTag) tag).orElse(new CompoundTag());
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

        public ResourceLocation getType() {
            return this.element_type;
        }
    }
}