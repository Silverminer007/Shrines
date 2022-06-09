/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.structures;

import com.silverminer.shrines.config.ShrinesConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;

public class StructureUtils {
   public static boolean invalidateStructure(@NotNull RegistryAccess registryAccess, Structure structure) {
      Registry<Structure> registry = registryAccess.registryOrThrow(Registry.STRUCTURE_REGISTRY);
      ResourceLocation id = registry.getKey(structure);
      if (id != null) {
         ResourceKey<Structure> resourceKey = ResourceKey.create(Registry.STRUCTURE_REGISTRY, id);
         Holder<Structure> holder = Holder.Reference.createStandAlone(registry, resourceKey);
         for (String structureID : ShrinesConfig.disabledStructures.get()) {
            if (structureID.startsWith("#")) {
               TagKey<Structure> tagKey = TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(structureID.substring(1)));
               if (registry.getTag(tagKey).map(tag -> tag.contains(holder)).orElse(false)) {
                  return true;
               }
            } else {
               if (id.toString().equals(structureID)) {
                  return true;
               }
            }
         }

      }
      return false;
   }
}
