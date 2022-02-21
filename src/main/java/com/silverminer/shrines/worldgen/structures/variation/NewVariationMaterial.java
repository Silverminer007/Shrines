/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.worldgen.structures.variation;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.ShrinesMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistryEntry;
import silverminer.dynamicregistries.registry.RegistryAccessExtension;

import java.util.List;

public class NewVariationMaterial extends ForgeRegistryEntry<NewVariationMaterial> {
    public static final Codec<NewVariationMaterial> CODEC =
            RecordCodecBuilder.create(newVariationMaterialInstance ->
                    newVariationMaterialInstance.group(
                                    Codec.list(NewVariationMaterialElement.CODEC).fieldOf("types").forGetter(NewVariationMaterial::types),
                                    Codec.STRING.fieldOf("material_id").forGetter(NewVariationMaterial::materialID))
                            .apply(newVariationMaterialInstance, NewVariationMaterial::new));
    public static final ResourceKey<Registry<NewVariationMaterial>> REGISTRY = RegistryAccessExtension.createRegistryKey(ShrinesMod.MODID, "worldgen/variation_material");
    private final List<NewVariationMaterialElement> types;
    private final String materialID;

    public NewVariationMaterial(List<NewVariationMaterialElement> types, String materialID) {
        this.types = ImmutableList.copyOf(types);
        this.materialID = materialID;
    }

    public NewVariationMaterialElement getElement(String typeID) {
        return this.types().stream().filter(type -> type.typeID().equals(typeID)).findFirst().orElse(null);
    }

    public NewVariationMaterialElement getElement(Block block) {
        return this.types().stream().filter(type -> type.blockID().equals(block.getRegistryName())).findFirst().orElse(null);
    }

    public NewVariationMaterial withMaterialID(String newMaterialID) {
        return new NewVariationMaterial(types(), newMaterialID);
    }

    public List<NewVariationMaterialElement> types() {
        return this.types;
    }

    public String materialID() {
        return this.materialID;
    }
}