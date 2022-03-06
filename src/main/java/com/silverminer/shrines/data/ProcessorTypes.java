/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.data;

import com.mojang.serialization.Codec;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.worldgen.processors.RemoveBlocksProcessor;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ProcessorTypes {

    public static StructureProcessorType<RemoveBlocksProcessor> REMOVE_BLOCKS_PROCESSOR = register("remove_block_processor", RemoveBlocksProcessor.CODEC);

    private static <T extends StructureProcessor> StructureProcessorType<T> register(String name, Codec<T> codec) {
        return Registry.register(Registry.STRUCTURE_PROCESSOR, ShrinesMod.location(name), () -> codec);
    }

    public static void bootstrap() {
    }
}