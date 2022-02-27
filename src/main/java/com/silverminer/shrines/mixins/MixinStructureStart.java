/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.mixins;

import com.silverminer.shrines.packages.datacontainer.NewVariationConfiguration;
import com.silverminer.shrines.worldgen.structures.variation.RandomVariantsProcessor;
import com.silverminer.shrines.worldgen.structures.variation.RandomVariationProcessable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(StructureStart.class)
public class MixinStructureStart {
    @Final
    @Shadow
    private StructureFeature<?> feature;

    @Final
    @Shadow
    private PiecesContainer pieceContainer;

    @Inject(method = "placeInChunk", at = @At(value = "RETURN"))
    private void shrines_onPlaceInChunk(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random,
                                        BoundingBox boundingBox, ChunkPos chunkPos, CallbackInfo ci) {
        if (!this.pieceContainer.pieces().isEmpty()) {
            RandomVariantsProcessor randomVariantsProcessor = ((RandomVariationProcessable) this.feature).getRandomVariationProcessor();
            NewVariationConfiguration variationConfiguration =
                    ServerLifecycleHooks.getCurrentServer().registryAccess()
                            .ownedRegistryOrThrow(NewVariationConfiguration.REGISTRY)
                            .get(new ResourceLocation(this.feature.getFeatureName()));
            variationConfiguration = variationConfiguration == null ? randomVariantsProcessor.getVariationConfiguration() : variationConfiguration;
            randomVariantsProcessor.setVariationConfiguration(variationConfiguration);
            randomVariantsProcessor.afterPlace(worldGenLevel, structureFeatureManager, chunkGenerator, random, boundingBox, chunkPos, this.pieceContainer);
        }
    }
}