/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.datacontainer;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.ShrinesMod;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class StructureNovel extends ForgeRegistryEntry<StructureNovel> {
    public static final ResourceKey<Registry<StructureNovel>> REGISTRY = ResourceKey.createRegistryKey(ShrinesMod.location(ShrinesMod.MODID + "/structure_novels"));
    //RegistryAccessExtension.createRegistryKey(ShrinesMod.MODID, "structure_novels");
    public static final Codec<StructureNovel> CODEC = RecordCodecBuilder.create((structureNovelInstance ->
            structureNovelInstance.group(Codec.list(Codec.STRING).fieldOf("parts").forGetter(StructureNovel::getParts))
                    .apply(structureNovelInstance, StructureNovel::new)
    ));
    protected static final Logger LOGGER = LogManager.getLogger(StructureNovel.class);
    private final List<String> parts;

    public StructureNovel(String... parts) {
        this(Arrays.asList(parts));
    }

    public StructureNovel(List<String> parts) {
        this.parts = Objects.requireNonNullElse(parts, new ArrayList<>());
    }

    @Nullable
    public static Tag serialize(StructureNovel structureNovel) {
        DataResult<Tag> dataResult = CODEC.encode(structureNovel, NbtOps.INSTANCE, new CompoundTag());
        Optional<Tag> tagOptional = dataResult.resultOrPartial(LOGGER::error);
        return tagOptional.orElse(null);
    }

    @Nullable
    public static StructureNovel deserialize(Tag tag) {
        DataResult<Pair<StructureNovel, Tag>> dataResult = CODEC.decode(NbtOps.INSTANCE, tag);
        Optional<Pair<StructureNovel, Tag>> optionalStructureNovelTagPair = dataResult.resultOrPartial(LOGGER::error);
        return optionalStructureNovelTagPair.map(Pair::getFirst).orElse(null);
    }

    @Nonnull
    public List<String> getParts() {
        return parts;
    }
}