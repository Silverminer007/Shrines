/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.mixins;

import com.silverminer.shrines.init.StructureInit;
import com.silverminer.shrines.init.StructureRegistryHolder;
import com.silverminer.shrines.utils.StructureRegistrationUtils;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin(StructureFeatures.class)
public class MixinStructureFeatures {
    @Inject(method = "registerStructures", at = @At(value = "HEAD"))
    private static void onRegisterStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> registry, CallbackInfo callbackInfo) {
        for (StructureRegistryHolder holder : StructureInit.STRUCTURES) {
            for (Biome biome : ForgeRegistries.BIOMES) {
                if (biome.getRegistryName() != null && StructureRegistrationUtils.verifyBiome(biome, holder)) {
                    registry.accept(holder.getConfiguredStructure(), ResourceKey.create(Registry.BIOME_REGISTRY, biome.getRegistryName()));
                }
            }
        }
    }
}
