/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.structures.placement_types.PlacementCalculator;
import com.silverminer.shrines.structures.spawn_criteria.SpawnCriteria;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.List;

public class ShrinesConfiguration extends JigsawConfiguration {
   public static final Codec<ShrinesConfiguration> CODEC = RecordCodecBuilder.create((shrinesConfigurationInstance) ->
         shrinesConfigurationInstance.group(
                     StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(ShrinesConfiguration::startPool),
                     Codec.intRange(0, 7).fieldOf("size").forGetter(ShrinesConfiguration::maxDepth),
                     Codec.list(SpawnCriteria.CODEC).fieldOf("spawn_criteria").forGetter(ShrinesConfiguration::getSpawnCriteria),
                     PlacementCalculator.CODEC.fieldOf("placement_calculator").forGetter(ShrinesConfiguration::getPlacementCalculator))
               .apply(shrinesConfigurationInstance, ShrinesConfiguration::new));
   private final List<SpawnCriteria> spawnCriteriaList;
   private final PlacementCalculator placementCalculator;

   public ShrinesConfiguration(Holder<StructureTemplatePool> startPool, int size, List<SpawnCriteria> spawnCriteriaList, PlacementCalculator placementCalculator) {
      super(startPool, size);
      this.spawnCriteriaList = spawnCriteriaList;
      this.placementCalculator = placementCalculator;
   }

   public List<SpawnCriteria> getSpawnCriteria() {
      return this.spawnCriteriaList;
   }

   public PlacementCalculator getPlacementCalculator() {
      return placementCalculator;
   }
}