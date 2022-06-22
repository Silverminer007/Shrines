/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.mixins;

import com.silverminer.shrines.random_variation.RandomVariationConfig;
import com.silverminer.shrines.structures.PrideMonthProcessor;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;

@Mixin(StructureStart.class)
public class MixinStructureStart {
   @Final
   @Shadow
   private Structure structure;

   @Final
   @Shadow
   private PiecesContainer pieceContainer;

   @Inject(method = "placeInChunk", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/world/level/levelgen/structure/Structure;afterPlace(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;)V"))
   private void shrines_onPlaceInChunk(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, CallbackInfo ci) {
      List<StructurePiece> list = this.pieceContainer.pieces();
      if (list.isEmpty()) {
         return;
      }

      BoundingBox box = this.pieceContainer.calculateBoundingBox();
      MinecraftServer server = worldGenLevel.getServer();
      if (server == null) {
         return;
      }
      RegistryAccess registryAccess = server.registryAccess();

      Registry<Structure> configuredStructureFeatureRegistry = registryAccess.registryOrThrow(Registry.STRUCTURE_REGISTRY);
      ResourceLocation featureKey = configuredStructureFeatureRegistry.getKey(this.structure);
      RandomVariationConfig randomVariationConfig = registryAccess.registryOrThrow(RandomVariationConfig.REGISTRY).get(featureKey);
      if (randomVariationConfig == null || randomVariationConfig.remaps().isEmpty()) {
         return;
      }

      RandomSource newRandomSource =  randomSource.fork();
      newRandomSource.setSeed((long) box.minX() * box.minY() * box.minZ());
      randomVariationConfig.process(worldGenLevel, newRandomSource, boundingBox, box, this.pieceContainer::isInsidePiece);

      if (LocalDate.now().get(ChronoField.MONTH_OF_YEAR) == 6) {
         PrideMonthProcessor.process(worldGenLevel, boundingBox, box, this.pieceContainer::isInsidePiece);
      }
   }
}