/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.update;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.random_variation.RandomVariationMaterial;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class RVMUpdate {
   public static JsonElement update(JsonElement jsonElement) {
      return OldVariationMaterial.CODEC.decode(JsonOps.INSTANCE, jsonElement)
            .map(Pair::getFirst).map(OldVariationMaterial::update)
            .map(randomVariationMaterial ->
                  RandomVariationMaterial.DIRECT_CODEC.encode(randomVariationMaterial, JsonOps.INSTANCE, new JsonObject()).resultOrPartial(Shrines.LOGGER::error).orElse(null))
            .resultOrPartial(Shrines.LOGGER::error).orElse(null);
   }

   record OldVariationMaterial(
         List<OldVariationMaterialElement> types,
         String materialID) {
      static final Codec<OldVariationMaterial> CODEC =
            RecordCodecBuilder.create(newVariationMaterialInstance ->
                  newVariationMaterialInstance.group(
                              Codec.list(OldVariationMaterialElement.CODEC).fieldOf("types").forGetter(OldVariationMaterial::types),
                              Codec.STRING.fieldOf("material_id").forGetter(OldVariationMaterial::materialID))
                        .apply(newVariationMaterialInstance, OldVariationMaterial::new));

      OldVariationMaterial(List<OldVariationMaterialElement> types, String materialID) {
         this.types = ImmutableList.copyOf(types);
         this.materialID = materialID;
      }

      RandomVariationMaterial update() {
         return new RandomVariationMaterial(this.types().stream().map(OldVariationMaterialElement::update).toList());
      }
   }

   record OldVariationMaterialElement(String typeID, ResourceLocation blockID) {
      static final Codec<OldVariationMaterialElement> CODEC =
            RecordCodecBuilder.create(newVariationMaterialElementInstance ->
                  newVariationMaterialElementInstance.group(
                              Codec.STRING.fieldOf("type_id").forGetter(OldVariationMaterialElement::typeID),
                              ResourceLocation.CODEC.fieldOf("block_id").forGetter(OldVariationMaterialElement::blockID))
                        .apply(newVariationMaterialElementInstance, OldVariationMaterialElement::new));
      Pair<Block, ResourceLocation> update() {
         return Pair.of(ForgeRegistries.BLOCKS.getValue(this.blockID()), Shrines.location(this.typeID()));
      }
   }
}