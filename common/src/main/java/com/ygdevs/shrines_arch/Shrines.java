/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch;

import com.mojang.logging.LogUtils;
import com.ygdevs.shrines_arch.config.Config;
import com.ygdevs.shrines_arch.random_variation.RandomVariationConfig;
import com.ygdevs.shrines_arch.random_variation.RandomVariationMaterial;
import com.ygdevs.shrines_arch.registries.*;
import com.ygdevs.shrines_arch.structures.placement_types.PlacementCalculatorType;
import com.ygdevs.shrines_arch.structures.spawn_criteria.SpawnCriteriaType;
import com.ygdevs.shrines_arch.update.Updater;
import dev.architectury.platform.Platform;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

public class Shrines {// TODO Custom registries TODO Data gen
   public static final String MODID = "shrines_arch";
   public static final Logger LOGGER = LogUtils.getLogger();
   public static String VERSION = "N/A";

   public static void init() {
      Utils.createRegistry(PlacementCalculatorType.REGISTRY);
      Utils.createRegistry(RandomVariationConfig.REGISTRY);
      Utils.createRegistry(RandomVariationMaterial.REGISTRY);
      Utils.createRegistry(SpawnCriteriaType.REGISTRY);
      ConfiguredStructureFeatureRegistry.REGISTRY.register();
      PlacementCalculatorTypeRegistry.REGISTRY.register();
      RandomVariationConfigRegistry.REGISTRY.register();
      RandomVariationMaterialRegistry.REGISTRY.register();
      SpawnCriteriaTypeRegistry.REGISTRY.register();
      StructureRegistry.REGISTRY.register();
      StructureSetRegistry.REGISTRY.register();
      TemplatePoolRegistry.REGISTRY.register();
   }

   public static void runUpdater() {
      Path minecraftDir = Platform.getGameFolder();
      Path oldPackDir = minecraftDir.resolve("shrines-data").resolve("3.x.x").resolve("Packets");
      Path newPackDir = minecraftDir.resolve("datapacks");
      if (Config.runStructureUpdater() && Files.exists(oldPackDir)) {
         try {
            Updater.updateAll(oldPackDir, newPackDir);
            Config.setRunStructureUpdater(false);
         } catch (Exception e) {
            LOGGER.error("Failed to update old structures", e);
         }
      }
   }

   @NotNull
   public static ResourceLocation location(@NotNull String path) {
      return new ResourceLocation(MODID, path);
   }

   public static boolean invalidateStructure(@NotNull RegistryAccess registryAccess, ConfiguredStructureFeature<?, ?> configuredStructureFeature) {
      Registry<ConfiguredStructureFeature<?, ?>> registry = registryAccess.registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
      ResourceLocation id = registry.getKey(configuredStructureFeature);
      if (id != null) {
         ResourceKey<ConfiguredStructureFeature<?, ?>> resourceKey = ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, id);
         Holder<ConfiguredStructureFeature<?, ?>> holder = Holder.Reference.createStandAlone(registry, resourceKey);
         for (String structure : Config.disabledStructures()) {
            if (structure.startsWith("#")) {
               TagKey<ConfiguredStructureFeature<?, ?>> tagKey = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(structure.substring(1)));
               if (registry.getTag(tagKey).map(tag -> tag.contains(holder)).orElse(false)) {
                  return true;
               }
            } else {
               if (id.toString().equals(structure)) {
                  return true;
               }
            }
         }

      }
      return false;
   }
}