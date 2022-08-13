/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.registries.StructureTypeRegistry;
import com.silverminer.shrines.structures.spawn_criteria.SpawnCriteria;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ShrinesStructure extends Structure {
   public static final Codec<ShrinesStructure> CODEC = RecordCodecBuilder.<ShrinesStructure>create((shrinesStructureInstance) ->
         shrinesStructureInstance.group(
                     settingsCodec(shrinesStructureInstance),
                     StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(ShrinesStructure::getStartPool),
                     ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(ShrinesStructure::getStartJigsawName),
                     Codec.intRange(0, 7).fieldOf("size").forGetter(ShrinesStructure::getMaxDepth),
                     HeightProvider.CODEC.fieldOf("start_height").forGetter(ShrinesStructure::getStartHeight),
                     Codec.BOOL.fieldOf("use_expansion_hack").forGetter(ShrinesStructure::useExpansionHack),
                     Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(ShrinesStructure::getProjectStartToHeightmap),
                     Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(ShrinesStructure::getMaxDistanceFromCenter),
                     Codec.list(SpawnCriteria.CODEC).fieldOf("spawn_criteria").forGetter(ShrinesStructure::getSpawnCriteriaList))
               .apply(shrinesStructureInstance, ShrinesStructure::new)).flatXmap(verifyRange(), verifyRange());
   private final Holder<StructureTemplatePool> startPool;
   @Nullable
   private final ResourceLocation startJigsawName;
   private final int maxDepth;
   private final HeightProvider startHeight;
   private final boolean useExpansionHack;
   @Nullable
   private final Heightmap.Types projectStartToHeightmap;
   private final int maxDistanceFromCenter;
   private final List<SpawnCriteria> spawnCriteriaList;

   public ShrinesStructure(Structure.StructureSettings structureSettings, Holder<StructureTemplatePool> startPool, @Nullable ResourceLocation startJigsawName, int maxDepth, HeightProvider startHeight, boolean useExpansionHack, @Nullable Heightmap.Types projectStartToHeightmap, int maxDistanceFromCenter, List<SpawnCriteria> spawnCriteriaList) {
      super(structureSettings);
      this.startPool = startPool;
      this.startJigsawName = startJigsawName;
      this.maxDepth = maxDepth;
      this.startHeight = startHeight;
      this.useExpansionHack = useExpansionHack;
      this.projectStartToHeightmap = projectStartToHeightmap;
      this.maxDistanceFromCenter = maxDistanceFromCenter;
      this.spawnCriteriaList = spawnCriteriaList;
   }

   public ShrinesStructure(Structure.StructureSettings structureSettings, Holder<StructureTemplatePool> startPool, @NotNull Optional<ResourceLocation> startJigsawName, int maxDepth, HeightProvider startHeight, boolean useExpansionHack, @Nullable Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceFromCenter, List<SpawnCriteria> spawnCriteriaList) {
      this(structureSettings, startPool, startJigsawName.orElse(null), maxDepth, startHeight, useExpansionHack, projectStartToHeightmap.orElse(null), maxDistanceFromCenter, spawnCriteriaList);
   }

   @SuppressWarnings("deprecation")
   @Override
   public Optional<GenerationStub> findGenerationPoint(@NotNull GenerationContext generationContext) {
      if (this.getSpawnCriteriaList().stream()
            .anyMatch(spawnCriteria -> !spawnCriteria.test(generationContext))) {
         return Optional.empty();
      } else {
         Pools.forceBootstrap();
         ChunkPos chunkpos = generationContext.chunkPos();
         int i = this.startHeight.sample(generationContext.random(), new WorldGenerationContext(generationContext.chunkGenerator(), generationContext.heightAccessor()));
         BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), i, chunkpos.getMinBlockZ());

         return JigsawPlacement.addPieces(generationContext, this.getStartPool(), this.getStartJigsawName(), this.getMaxDepth(), blockpos,
               this.useExpansionHack(), this.getProjectStartToHeightmap(), this.getMaxDistanceFromCenter());
      }
   }

   @Override
   public @NotNull StructureType<?> type() {
      return StructureTypeRegistry.DEFAULT.get();
   }

   public Holder<StructureTemplatePool> getStartPool() {
      return startPool;
   }

   public Optional<ResourceLocation> getStartJigsawName() {
      return Optional.ofNullable(startJigsawName);
   }

   public int getMaxDepth() {
      return maxDepth;
   }

   public HeightProvider getStartHeight() {
      return startHeight;
   }

   public boolean useExpansionHack() {
      return useExpansionHack;
   }

   @Nullable
   public Optional<Heightmap.Types> getProjectStartToHeightmap() {
      return Optional.ofNullable(projectStartToHeightmap);
   }

   public int getMaxDistanceFromCenter() {
      return maxDistanceFromCenter;
   }

   public List<SpawnCriteria> getSpawnCriteriaList() {
      return spawnCriteriaList;
   }

   @Contract(pure = true)
   private static @NotNull Function<ShrinesStructure, DataResult<ShrinesStructure>> verifyRange() {
      return (shrinesStructure) -> shrinesStructure.maxDistanceFromCenter + switch (shrinesStructure.terrainAdaptation()) {
         case NONE -> 0;
         case BURY, BEARD_THIN, BEARD_BOX -> 12;
         default -> throw new IncompatibleClassChangeError();
      } > 128 ? DataResult.error("Structure size including terrain adaptation must not exceed 128") : DataResult.success(shrinesStructure);
   }
}