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
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
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
   protected abstract List<StructurePlacement> getPlacementsForStructure(Holder<Structure> p_223139_, RandomState p_223140_);

   @Shadow
   @Nullable
   public abstract List<ChunkPos> getRingPositionsFor(ConcentricRingsStructurePlacement p_223120_, RandomState p_223121_);

   @SuppressWarnings("unchecked")
   @Nullable
   public Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel serverLevel, HolderSet<Structure> structureHolderSet,
                                                                    BlockPos blockPos, int maxDistance, boolean includeExisting,
                                                                    Predicate<Holder<Biome>> biomeValidator) {
      Map<StructurePlacement, Set<Holder<Structure>>> map = new Object2ObjectArrayMap<>();

      for (Holder<Structure> holder : structureHolderSet) {
         for (StructurePlacement structureplacement : this.getPlacementsForStructure(holder, serverLevel.getChunkSource().randomState())) {
            map.computeIfAbsent(structureplacement, (p_223127_) -> new ObjectArraySet()).add(holder);
         }
      }

      if (map.isEmpty()) {
         return null;
      } else {
         Pair<BlockPos, Holder<Structure>> pair2 = null;
         double d2 = Double.MAX_VALUE;
         StructureManager structuremanager = serverLevel.structureManager();
         List<Map.Entry<StructurePlacement, Set<Holder<Structure>>>> list = new ArrayList<>(map.size());

         for (Map.Entry<StructurePlacement, Set<Holder<Structure>>> entry : map.entrySet()) {
            StructurePlacement structureplacement1 = entry.getKey();
            if (structureplacement1 instanceof ConcentricRingsStructurePlacement concentricringsstructureplacement) {
               Pair<BlockPos, Holder<Structure>> pair = this.getNearestGeneratedStructure(entry.getValue(), serverLevel, structuremanager, blockPos, includeExisting, concentricringsstructureplacement, biomeValidator);
               BlockPos blockpos = pair.getFirst();
               double d0 = blockPos.distSqr(blockpos);
               if (d0 < d2) {
                  d2 = d0;
                  pair2 = pair;
               }
            } else if (structureplacement1 instanceof RandomSpreadStructurePlacement) {
               list.add(entry);
            }
         }

         if (!list.isEmpty()) {
            int i = SectionPos.blockToSectionCoord(blockPos.getX());
            int j = SectionPos.blockToSectionCoord(blockPos.getZ());

            for (int k = 0; k <= maxDistance; ++k) {
               boolean flag = false;

               for (Map.Entry<StructurePlacement, Set<Holder<Structure>>> entry1 : list) {
                  RandomSpreadStructurePlacement randomspreadstructureplacement = (RandomSpreadStructurePlacement) entry1.getKey();
                  Pair<BlockPos, Holder<Structure>> pair1 = getNearestGeneratedStructure(entry1.getValue(), serverLevel, structuremanager, i, j, k, includeExisting, serverLevel.getSeed(), randomspreadstructureplacement, biomeValidator);
                  if (pair1 != null) {
                     flag = true;
                     double d1 = blockPos.distSqr(pair1.getFirst());
                     if (d1 < d2) {
                        d2 = d1;
                        pair2 = pair1;
                     }
                  }
               }

               if (flag) {
                  return pair2;
               }
            }
         }

         return pair2;
      }
   }

   @Nullable
   private Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructure(Set<Holder<Structure>> holderSet,
                                                                          ServerLevel serverLevel, StructureManager structureManager,
                                                                          BlockPos blockPos, boolean p_223186_,
                                                                          ConcentricRingsStructurePlacement p_223187_,
                                                                          Predicate<Holder<Biome>> biomeValidator) {
      List<ChunkPos> list = this.getRingPositionsFor(p_223187_, serverLevel.getChunkSource().randomState());
      if (list == null) {
         throw new IllegalStateException("Somehow tried to find structures for a placement that doesn't exist");
      } else {
         Pair<BlockPos, Holder<Structure>> pair = null;
         double d0 = Double.MAX_VALUE;
         BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

         for (ChunkPos chunkpos : list) {
            if (biomeValidator.test(serverLevel.getBiome(chunkpos.getMiddleBlockPosition(0)))) {
               blockpos$mutableblockpos.set(SectionPos.sectionToBlockCoord(chunkpos.x, 8), 32, SectionPos.sectionToBlockCoord(chunkpos.z, 8));
               double d1 = blockpos$mutableblockpos.distSqr(blockPos);
               boolean flag = pair == null || d1 < d0;
               if (flag) {
                  Pair<BlockPos, Holder<Structure>> pair1 = ChunkGenerator.getStructureGeneratingAt(holderSet, serverLevel, structureManager, p_223186_, p_223187_, chunkpos);
                  if (pair1 != null) {
                     pair = pair1;
                     d0 = d1;
                  }
               }
            }
         }

         return pair;
      }
   }

   @Nullable
   private static Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructure(Set<Holder<Structure>> structures, LevelReader levelReader,
                                                                                 StructureManager structureManager, int x, int y,
                                                                                 int z, boolean includeExisting, long seed,
                                                                                 RandomSpreadStructurePlacement randomSpreadStructurePlacement,
                                                                                 Predicate<Holder<Biome>> biomeValidator) {
      int i = randomSpreadStructurePlacement.spacing();

      for(int j = -z; j <= z; ++j) {
         boolean flag = j == -z || j == z;

         for(int k = -z; k <= z; ++k) {
            boolean flag1 = k == -z || k == z;
            if (flag || flag1) {
               int l = x + i * j;
               int i1 = y + i * k;
               ChunkPos chunkpos = randomSpreadStructurePlacement.getPotentialStructureChunk(seed, l, i1);
               if (!biomeValidator.test(levelReader.getBiome(chunkpos.getMiddleBlockPosition(0)))) {
                  continue;
               }
               Pair<BlockPos, Holder<Structure>> pair = ChunkGenerator.getStructureGeneratingAt(structures, levelReader, structureManager, includeExisting, randomSpreadStructurePlacement, chunkpos);
               if (pair != null) {
                  return pair;
               }
            }
         }
      }

      return null;
   }
}
