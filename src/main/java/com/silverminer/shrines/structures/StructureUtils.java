/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.structures;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.Structures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StructureUtils {
   public static boolean invalidateStructure(@NotNull RegistryAccess registryAccess, ResourceKey<Structure> structure, @NotNull List<String> disabledStructures) {
      Registry<Structure> structureRegistry  = registryAccess.ownedRegistryOrThrow(Registry.STRUCTURE_REGISTRY);
      Holder<Structure> holder = structureRegistry.getHolderOrThrow(structure);
      for (String structureID : disabledStructures) {
         if (structureID.startsWith("#")) {
            TagKey<Structure> tagKey = TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(structureID.substring(1)));
            if (structureRegistry.getTag(tagKey).map(tag -> tag.contains(holder)).orElse(false)) {
               return true;
            }
         } else {
            if (structure.location().toString().equals(structureID)) {
               return true;
            }
         }
      }
      return false;
   }

   public static ResourceKey<Structure> getStructureKey(Structure structure, RegistryAccess registryAccess) {
      return registryAccess.ownedRegistryOrThrow(Registry.STRUCTURE_REGISTRY).getResourceKey(structure).orElseThrow();
   }
}
