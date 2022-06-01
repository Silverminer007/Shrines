/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.mixin;

import com.ygdevs.shrines_arch.random_variation.RandomVariationConfig;
import com.ygdevs.shrines_arch.registries.RandomVariationConfigRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(StructureStart.class)
public class MixinStructureStart {
   @Final
   @Shadow
   private ConfiguredStructureFeature<?, ?> feature;

   @Final
   @Shadow
   private PiecesContainer pieceContainer;

   @Inject(method = "placeInChunk", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/world/level/levelgen/structure/PostPlacementProcessor;afterPlace(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/StructureFeatureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;)V"))
   private void shrines_onPlaceInChunk(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random,
                                       BoundingBox boundingBox, ChunkPos chunkPos, CallbackInfo ci) {
      List<StructurePiece> list = this.pieceContainer.pieces();
      if (list.isEmpty()) {
         return;
      }

      MinecraftServer server = worldGenLevel.getServer();
      if(server == null) {
         return;
      }
      RegistryAccess registryAccess = server.registryAccess();
      Registry<ConfiguredStructureFeature<?, ?>> configuredStructureFeatureRegistry = registryAccess.registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
      ResourceLocation featureKey = configuredStructureFeatureRegistry.getKey(this.feature);
      RandomVariationConfig randomVariationConfig = RandomVariationConfigRegistry.REGISTRY.get(featureKey);
      if(randomVariationConfig == null || randomVariationConfig.remaps().isEmpty()) {
         return;
      }

      BoundingBox box = this.pieceContainer.calculateBoundingBox();
      WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
      worldgenrandom.setLargeFeatureSeed(worldGenLevel.getSeed(), box.minX(), box.minZ());

      randomVariationConfig.process(worldGenLevel, worldgenrandom, boundingBox, box, this.pieceContainer::isInsidePiece);
   }
}