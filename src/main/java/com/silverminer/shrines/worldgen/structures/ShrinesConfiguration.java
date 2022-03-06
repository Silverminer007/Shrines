/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.worldgen.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class ShrinesConfiguration extends JigsawConfiguration {
    public static final Codec<ShrinesConfiguration> CODEC = RecordCodecBuilder.create((shrinesConfigurationInstance) ->
            shrinesConfigurationInstance.group(
                            StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(ShrinesConfiguration::startPool),
                            Codec.intRange(0, 7).fieldOf("size").forGetter(ShrinesConfiguration::maxDepth),
                            Codec.doubleRange(Double.MIN_VALUE, 1.0).fieldOf("spawn_chance").forGetter(ShrinesConfiguration::getSpawnChance),
                            Codec.BOOL.fieldOf("generate").forGetter(ShrinesConfiguration::isGenerate))
                    .apply(shrinesConfigurationInstance, ShrinesConfiguration::new));
    private final double spawnChance;
    private final boolean generate;

    public ShrinesConfiguration(Holder<StructureTemplatePool> startPool, int size, double spawnChance, boolean generate) {
        super(startPool, size);
        this.spawnChance = spawnChance;
        this.generate = generate;
    }

    public ShrinesConfiguration(Holder<StructureTemplatePool> startPool) {
        super(startPool, 7);
        this.spawnChance = 0.6;
        this.generate = true;
    }

    public double getSpawnChance() {
        return spawnChance;
    }

    public boolean isGenerate() {
        return generate;
    }
}