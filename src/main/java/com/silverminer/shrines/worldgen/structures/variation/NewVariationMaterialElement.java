/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.worldgen.structures.variation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record NewVariationMaterialElement(String typeID, ResourceLocation blockID) {
    public static final Codec<NewVariationMaterialElement> CODEC =
            RecordCodecBuilder.create(newVariationMaterialElementInstance ->
                    newVariationMaterialElementInstance.group(
                                    Codec.STRING.fieldOf("type_id").forGetter(NewVariationMaterialElement::typeID),
                                    ResourceLocation.CODEC.fieldOf("block_id").forGetter(NewVariationMaterialElement::blockID))
                            .apply(newVariationMaterialElementInstance, NewVariationMaterialElement::new));
}