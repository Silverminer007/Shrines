/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.worldgen.structures.variation;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.silverminer.shrines.init.VariationMaterialsRegistry;
import com.silverminer.shrines.packages.datacontainer.NewVariationConfiguration;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomVariantsProcessor implements PostPlacementProcessor {
   protected final HashMap<NewVariationMaterial, NewVariationMaterial> MATERIAL_REMAPS = new HashMap<>();
   protected final HashMap<String, String> TYPE_REMAPS = new HashMap<>();
   private NewVariationConfiguration variationConfiguration;

   public RandomVariantsProcessor(NewVariationConfiguration variationConfiguration) {
      this.variationConfiguration = variationConfiguration;
   }

   @Override
   public void afterPlace(@NotNull WorldGenLevel worldGenLevel, @NotNull StructureFeatureManager structureFeatureManager, @NotNull ChunkGenerator chunkGenerator,
                          @NotNull Random random, @NotNull BoundingBox chunkBounds, @NotNull ChunkPos chunkPos, @NotNull PiecesContainer pieces) {
      if (this.variationConfiguration.isEnabled() || true) {
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
      if (this.MATERIAL_REMAPS.isEmpty() || this.TYPE_REMAPS.isEmpty()) {
         this.TYPE_REMAPS.clear();
         this.MATERIAL_REMAPS.clear();
         ListMultimap<String, NewVariationMaterial> materialChanges = LinkedListMultimap.create();
         ListMultimap<String, String> typeChanges = LinkedListMultimap.create();
         for (NewVariationMaterial variationMaterial : VariationMaterialsRegistry.VARIATION_MATERIALS_REGISTRY.get().getValues()) {
            if (variationMaterial.alignMaterial()) {
               materialChanges.put(variationMaterial.materialID(), variationMaterial);
            } else {
               this.MATERIAL_REMAPS.put(variationMaterial, variationMaterial);
               for (NewVariationMaterialElement element : variationMaterial.types()) {
                  typeChanges.put(variationMaterial.materialID(), element.typeID());
               }
            }
         }
         for (String key : materialChanges.keySet()) {
            List<NewVariationMaterial> materials = materialChanges.get(key);
            List<NewVariationMaterial> variations = new ArrayList<>(materials);
            for (NewVariationMaterial NewVariationMaterial : materials) {
               if (variations.size() > 0) {
                  NewVariationMaterial target = variations.get(rand.nextInt(variations.size()));
                  variations.remove(target);
                  this.MATERIAL_REMAPS.put(NewVariationMaterial, target);
               } else {
                  break;
               }
            }
         }
         for(String materialID : typeChanges.keySet()) {
            List<String> variations = new ArrayList<>(typeChanges.get(materialID));
            for (String newElement : typeChanges.get(materialID)) {
               if (variations.size() > 0) {
                  String target = variations.get(rand.nextInt(variations.size()));
                  variations.remove(target);
                  this.TYPE_REMAPS.putIfAbsent(newElement, target);
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
      for (NewVariationMaterial variation : MATERIAL_REMAPS.keySet()) {
         if (this.variationConfiguration.isMaterialEnabled(variation.materialID())) {
            NewVariationMaterialElement element = variation.getElement(oldBlock);
            if (element != null && this.variationConfiguration.isTypeEnabled(element.typeID())) {
               NewVariationMaterial remap = MATERIAL_REMAPS.getOrDefault(variation, variation);
               NewVariationMaterialElement remapElement = remap.getElement(this.TYPE_REMAPS.getOrDefault(element.typeID(), element.typeID()));
               if (remapElement != null) {
                  Block newBlock = ForgeRegistries.BLOCKS.getValue(remapElement.blockID());
                  if (newBlock != null) {
                     return newBlock.withPropertiesOf(oldBlockState);
                  }
               }
            }
         }
      }
      return oldBlockState;
   }

   public void resetRemaps() {
      // We're currently using a VERY hacky method to reset our remaps. We reset them on every world load, so we don't guarantee that maps are unique, but we can assume that.
      // Also, user are able to reset the list their self by reloading the world.
      // Another option is to use mixins to reset this map, but I don't think the effort is worth
      this.MATERIAL_REMAPS.clear();
      this.TYPE_REMAPS.clear();
   }

   public void setVariationConfiguration(NewVariationConfiguration variationConfiguration) {
      this.variationConfiguration = variationConfiguration;
   }

   public NewVariationConfiguration getVariationConfiguration() {
      return variationConfiguration;
   }
}
