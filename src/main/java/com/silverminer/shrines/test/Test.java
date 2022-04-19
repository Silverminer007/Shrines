/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.test;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.registry.Utils;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

public record Test(int number, String string, Block block, Holder<StructureTemplatePool> structureTemplatePoolHolder, Optional<Test> test, Optional<List<Test>> listTest) {
   public static final ResourceKey<? extends Registry<Test>> REGISTRY = ResourceKey.createRegistryKey(new ResourceLocation(Shrines.MODID, "test"));
   public static final Codec<Test> CODEC = RecordCodecBuilder.create(testInstance ->
         testInstance.group(
               Codec.INT.fieldOf("number").forGetter(Test::number),
               Codec.STRING.fieldOf("string").forGetter(Test::string),
               ForgeRegistries.BLOCKS.getCodec().fieldOf("block").forGetter(Test::block),
               StructureTemplatePool.CODEC.fieldOf("pool").forGetter(Test::structureTemplatePoolHolder),
               Utils.getCodec(REGISTRY).optionalFieldOf("test").forGetter(Test::test),
               Codec.list(Utils.getCodec(REGISTRY)).optionalFieldOf("test_list").forGetter(Test::listTest)
         ).apply(testInstance, Test::new));
}