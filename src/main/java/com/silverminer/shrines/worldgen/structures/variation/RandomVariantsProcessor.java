/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.worldgen.structures.variation;

import com.google.common.collect.ImmutableList;
import com.silverminer.shrines.packages.datacontainer.VariationConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomVariantsProcessor implements PostPlacementProcessor {
   protected static final List<List<? extends VariationMaterial>> VARIATION_TYPES = ImmutableList.of(VariationMaterialPool.WOOD, VariationMaterialPool.WOOL,
         VariationMaterialPool.TERRACOTTA, VariationMaterialPool.GLAZED_TERRACOTTA, VariationMaterialPool.CONCRETE, VariationMaterialPool.CONCRETE_POWDER,
         VariationMaterialPool.ORE, VariationMaterialPool.BEES, VariationMaterialPool.STONE);
   protected final HashMap<VariationMaterial, VariationMaterial> BLOCK_REMAPS = new HashMap<>();
   private final VariationConfiguration variationConfiguration;

   public RandomVariantsProcessor(VariationConfiguration variationConfiguration) {
      this.variationConfiguration = variationConfiguration;
   }

   @Override
   public void afterPlace(@NotNull WorldGenLevel worldGenLevel, @NotNull StructureFeatureManager structureFeatureManager, @NotNull ChunkGenerator chunkGenerator,
                          @NotNull Random random, @NotNull BoundingBox chunkBounds, @NotNull ChunkPos chunkPos, @NotNull PiecesContainer pieces) {
      if (this.variationConfiguration.isEnabled()) {
         BoundingBox structureBounds = pieces.calculateBoundingBox();
         createBlockRemaps(random);
         for (int x = chunkBounds.minX(); x <= chunkBounds.maxX(); x++) {
            for (int y = chunkBounds.minY(); y <= chunkBounds.maxY(); y++) {
               for (int z = chunkBounds.minZ(); z <= chunkBounds.maxZ(); z++) {
                  BlockPos position = new BlockPos(x, y, z);
                  if (worldGenLevel.isEmptyBlock(position) || !structureBounds.isInside(position) || !pieces.isInsidePiece(position)) {
                     continue;
                  }
                  BlockState blockStateAtPos = worldGenLevel.getBlockState(position);
                  if (blockStateAtPos.isAir()) {
                     continue;
                  }
                  BlockState newBlockState = this.calculateNewBlock(blockStateAtPos);
                  worldGenLevel.setBlock(position, newBlockState, 3);
               }
            }
         }
      }
   }

   private void createBlockRemaps(Random rand) {
      if (this.BLOCK_REMAPS.isEmpty()) {
         for (List<? extends VariationMaterial> variationMaterialType : VARIATION_TYPES) {
            List<VariationMaterial> variations = new ArrayList<>(variationMaterialType);
            for (VariationMaterial variationMaterial : variationMaterialType) {
               if (variations.size() > 0) {
                  VariationMaterial target = variations.get(rand.nextInt(variations.size()));
                  variations.remove(target);
                  this.BLOCK_REMAPS.put(variationMaterial, target);
               } else {
                  break;
               }
            }
         }
      }
   }

   private BlockState calculateNewBlock(BlockState oldBlockState) {
      Block oldBlock = oldBlockState.getBlock();
      if (oldBlockState.isAir()) {
         return oldBlockState;
      }
      for (VariationMaterial variation : BLOCK_REMAPS.keySet()) {
         VariationMaterialElement variationMaterialElement = variation.getElement(oldBlock);
         if (variationMaterialElement != null && variationMaterialElement.isEnabled(this.variationConfiguration)) {
            VariationMaterial newVariationMaterial = BLOCK_REMAPS.get(variation);
            Block newBlock = variation.getNewBlock(newVariationMaterial, oldBlock);
            return newVariationMaterial.applyProperties(oldBlockState, newBlock.defaultBlockState());
         }
      }
      return oldBlockState;
   }

   public void resetRemaps() {
      // We're currently using a VERY hacky method to reset our remaps. We reset them on every world load, so we don't guarantee that maps are unique, but we can assume that.
      // Also, user are able to reset the list their self by reloading the world.
      // Another option is to use mixins to reset this map, but I don't think the effort is worth
      this.BLOCK_REMAPS.clear();
   }
}
