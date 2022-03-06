/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.data;

import com.google.common.collect.ImmutableList;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.worldgen.processors.RemoveBlocksProcessor;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class ProcessorLists {
    public static final Holder<StructureProcessorList> SHRINES = register("default_processors",
            ImmutableList.of(new RemoveBlocksProcessor()));

    private static Holder<StructureProcessorList> register(String id, ImmutableList<StructureProcessor> processors) {
        ResourceLocation resourcelocation = ShrinesMod.location(id);
        StructureProcessorList structureprocessorlist = new StructureProcessorList(processors);
        return BuiltinRegistries.register(BuiltinRegistries.PROCESSOR_LIST, resourcelocation, structureprocessorlist);
    }
}