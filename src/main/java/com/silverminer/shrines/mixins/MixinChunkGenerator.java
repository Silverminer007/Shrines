/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.mixins;

import com.mojang.datafixers.util.Pair;
import com.silverminer.shrines.commands.LocateInBiomeChunkGenerator;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Mixin(ChunkGenerator.class)
public abstract class MixinChunkGenerator implements LocateInBiomeChunkGenerator {
   @Shadow
   @Final
   protected BiomeSource runtimeBiomeSource;

   @Shadow
   protected abstract List<StructurePlacement> getPlacementsForFeature(Holder<ConfiguredStructureFeature<?, ?>> p_208091_);

   @Shadow
   @Nullable
   protected abstract BlockPos getNearestGeneratedStructure(BlockPos p_204383_, ConcentricRingsStructurePlacement p_204384_);

   @SuppressWarnings("unchecked")
   @Override
   public Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> findNearestMapFeature(ServerLevel pLevel, HolderSet<ConfiguredStructureFeature<?, ?>> pStructureSet, BlockPos pPos, int pSearchRadius, boolean pSkipKnownStructures, Predicate<Holder<Biome>> biomeHolderPredicate) {
      Set<Holder<Biome>> set = pStructureSet.stream().flatMap((p_211699_) -> p_211699_.value().biomes().stream()).collect(Collectors.toSet());
      if (set.isEmpty()) {
         return null;
      } else {
         Set<Holder<Biome>> set1 = this.runtimeBiomeSource.possibleBiomes();
         if (Collections.disjoint(set1, set) || set.stream().noneMatch(biomeHolderPredicate)) {
            return null;
         } else {
            Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> pair = null;
            double d0 = Double.MAX_VALUE;
            Map<StructurePlacement, Set<Holder<ConfiguredStructureFeature<?, ?>>>> map = new Object2ObjectArrayMap<>();

            for (Holder<ConfiguredStructureFeature<?, ?>> holder : pStructureSet) {
               if (set1.stream().anyMatch(holder.value().biomes()::contains)) {
                  for (StructurePlacement structureplacement : this.getPlacementsForFeature(holder)) {
                     map.computeIfAbsent(structureplacement, (p_211663_) -> new ObjectArraySet()).add(holder);
                  }
               }
            }

            List<Map.Entry<StructurePlacement, Set<Holder<ConfiguredStructureFeature<?, ?>>>>> list = new ArrayList<>(map.size());

            for (Map.Entry<StructurePlacement, Set<Holder<ConfiguredStructureFeature<?, ?>>>> entry : map.entrySet()) {
               StructurePlacement structureplacement1 = entry.getKey();
               if (structureplacement1 instanceof ConcentricRingsStructurePlacement concentricringsstructureplacement) {
                  BlockPos blockpos = this.getNearestGeneratedStructure(pPos, concentricringsstructureplacement);
                  if (blockpos != null) {
                     double d1 = pPos.distSqr(blockpos);
                     if (d1 < d0 && biomeHolderPredicate.test(pLevel.getBiome(blockpos))) {
                        d0 = d1;
                        pair = Pair.of(blockpos, entry.getValue().iterator().next());
                     }
                  }
               } else if (structureplacement1 instanceof RandomSpreadStructurePlacement) {
                  list.add(entry);
               }
            }

            if (!list.isEmpty()) {
               int i = SectionPos.blockToSectionCoord(pPos.getX());
               int j = SectionPos.blockToSectionCoord(pPos.getZ());

               for (int k = 0; k <= pSearchRadius; ++k) {
                  boolean flag = false;

                  for (Map.Entry<StructurePlacement, Set<Holder<ConfiguredStructureFeature<?, ?>>>> entry1 : list) {
                     RandomSpreadStructurePlacement randomspreadstructureplacement = (RandomSpreadStructurePlacement) entry1.getKey();
                     Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> pair1 = getNearestGeneratedStructure(entry1.getValue(), pLevel, pLevel.structureFeatureManager(), i, j, k, pSkipKnownStructures, pLevel.getSeed(), randomspreadstructureplacement, biomeHolderPredicate);
                     if (pair1 != null) {
                        flag = true;
                        double d2 = pPos.distSqr(pair1.getFirst());
                        if (d2 < d0) {
                           d0 = d2;
                           pair = pair1;
                        }
                     }
                  }

                  if (flag) {
                     return pair;
                  }
               }
            }

            return pair;
         }
      }
   }

   @Nullable
   private static Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> getNearestGeneratedStructure(Set<Holder<ConfiguredStructureFeature<?, ?>>> p_208060_, LevelReader levelReader, StructureFeatureManager p_208062_, int p_208063_, int p_208064_, int p_208065_, boolean p_208066_, long p_208067_, @NotNull RandomSpreadStructurePlacement p_208068_, Predicate<Holder<Biome>> biomeHolderPredicate) {
      int i = p_208068_.spacing();

      for (int j = -p_208065_; j <= p_208065_; ++j) {
         boolean flag = j == -p_208065_ || j == p_208065_;

         for (int k = -p_208065_; k <= p_208065_; ++k) {
            boolean flag1 = k == -p_208065_ || k == p_208065_;
            if (flag || flag1) {
               int l = p_208063_ + i * j;
               int i1 = p_208064_ + i * k;
               ChunkPos chunkpos = p_208068_.getPotentialFeatureChunk(p_208067_, l, i1);
               if (biomeHolderPredicate.test(levelReader.getBiome(chunkpos.getMiddleBlockPosition(0)))) {

                  for (Holder<ConfiguredStructureFeature<?, ?>> holder : p_208060_) {
                     StructureCheckResult structurecheckresult = p_208062_.checkStructurePresence(chunkpos, holder.value(), p_208066_);
                     if (structurecheckresult != StructureCheckResult.START_NOT_PRESENT) {
                        if (!p_208066_ && structurecheckresult == StructureCheckResult.START_PRESENT) {
                           return Pair.of(StructureFeature.getLocatePos(p_208068_, chunkpos), holder);
                        }

                        ChunkAccess chunkaccess = levelReader.getChunk(chunkpos.x, chunkpos.z, ChunkStatus.STRUCTURE_STARTS);
                        StructureStart structurestart = p_208062_.getStartForFeature(SectionPos.bottomOf(chunkaccess), holder.value(), chunkaccess);
                        if (structurestart != null && structurestart.isValid()) {
                           if (p_208066_ && structurestart.canBeReferenced()) {
                              p_208062_.addReference(structurestart);
                              return Pair.of(StructureFeature.getLocatePos(p_208068_, structurestart.getChunkPos()), holder);
                           }

                           if (!p_208066_) {
                              return Pair.of(StructureFeature.getLocatePos(p_208068_, structurestart.getChunkPos()), holder);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return null;
   }
}
