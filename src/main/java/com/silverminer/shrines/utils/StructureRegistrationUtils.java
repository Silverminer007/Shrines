/*
  Silverminer (and Team)

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
  (Mozilla Public License 2.0) for more details.

  You should have received a copy of the MPL (Mozilla Public License 2.0)
  License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.silverminer.shrines.config.Config;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.init.StructureRegistryHolder;
import com.silverminer.shrines.worldgen.structures.ShrinesStructure;
import com.silverminer.shrines.packages.datacontainer.StructureData;

/**
 * @author Silverminer
 */
public class StructureRegistrationUtils {
   protected static final Logger LOGGER = LogManager.getLogger(StructureRegistrationUtils.class);

   public static void setupWorldGen() {
      registerConfiguredStructureFeatures();
      registerStructureSeparationSettings();
   }

   public static void registerStructureSeparationSettings() {
      for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
         ShrinesStructure structure = holder.getStructure();
         StructureFeature.STRUCTURES_REGISTRY.put(structure.getConfig().getKey().toString(),
               structure);

         if (structure.getConfig().isTransformLand()) {
            StructureFeature.NOISE_AFFECTING_FEATURES = ImmutableList.<StructureFeature<?>>builder()
                  .addAll(StructureFeature.NOISE_AFFECTING_FEATURES).add(structure).build();
         }

         StructureFeatureConfiguration structureSeparationSettings = new StructureFeatureConfiguration(
               structure.getDistance(), structure.getSeparation(), structure.getSeedModifier());

         StructureSettings.DEFAULTS = ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
               .putAll(StructureSettings.DEFAULTS).put(structure, structureSeparationSettings).build();

         BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings()
                  .structureConfig();
            if (structureMap instanceof ImmutableMap) {
               Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
               tempMap.put(structure, structureSeparationSettings);
               settings.getValue().structureSettings().structureConfig = tempMap;
            } else {
               structureMap.put(structure, structureSeparationSettings);
            }
         });
         LOGGER.debug("Registered Structure Separation Settings for {}",
               holder.getStructure().getConfig().getName());
      }
   }

   public static boolean verifyBiome(Biome biome, StructureRegistryHolder holder) {
      if (biome.getRegistryName() != null && !Config.SETTINGS.BLACKLISTED_BIOMES.get().contains(biome.getRegistryName().toString())) {
         return holder.getStructure().getConfig().isGenerate() && StructureRegistrationUtils.checkBiome(
               holder.getStructure().getConfig().getBiome_blacklist(),
               holder.getStructure().getConfig().getBiome_category_whitelist(), biome.getRegistryName(), biome.getBiomeCategory());
      }
      return false;
   }

   public static boolean checkBiome(List<? extends String> blacklistedBiomes,
                                    List<? extends String> whitelistedBiomeCategories, ResourceLocation name, Biome.BiomeCategory category) {
      if (!whitelistedBiomeCategories.isEmpty()) {
         if (blacklistedBiomes.isEmpty()) {
            return !blacklistedBiomes.contains(name.toString())
                  && whitelistedBiomeCategories.contains(category.toString());
         } else {
            return true;
         }
      }
      return false;
   }

   public static void registerConfiguredStructureFeatures() {
      for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
         ShrinesStructure structure = holder.getStructure();
         if (structure.getRegistryName() == null) {
            continue;
         }
         BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
               structure.getRegistryName().toString(), holder.getConfiguredStructure());
         LOGGER.debug("Registered configured structure feature of {}", holder.getStructure().getConfig().getName());
      }
   }

   private static Method GETCODEC_METHOD;

   public static void addDimensionalSpacing(ServerLevel world) {

      /*
       * Skip Terraforged's chunk generator as they are a special case of a mod
       * locking down their chunkgenerator. They will handle your structure spacing
       * for your if you add to WorldGenRegistries.NOISE_GENERATOR_SETTINGS in your
       * structure's registration.
       */
      try {
         if (GETCODEC_METHOD == null)
            GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "codec");
         @SuppressWarnings("unchecked")
         // cgRL = chunk generator Resource Location
         ResourceLocation cgRL = Registry.CHUNK_GENERATOR
               .getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(world.getChunkSource().getGenerator()));
         if (cgRL != null && cgRL.getNamespace().equals("terraforged"))
            return;
      } catch (Exception e) {
         LOGGER.error("Was unable to check if " + world.dimension().location()
               + " is using Terraforged's ChunkGenerator.");
      }

      Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(
            world.getChunkSource().getGenerator().getSettings().structureConfig());
      if (world.getChunkSource().getGenerator() instanceof FlatLevelSource
            && world.dimension().equals(Level.OVERWORLD)) {
         return;
      }
      for (StructureRegistryHolder holder : NewStructureInit.STRUCTURES) {
         if (isAllowedForWorld(world, holder.getStructure().getConfig())) {
            tempMap.put(holder.getStructure(),
                  StructureSettings.DEFAULTS.get(holder.getStructure()));
         } else {
            tempMap.remove(holder.getStructure());
         }
      }
      world.getChunkSource().getGenerator().getSettings().structureConfig = tempMap;
   }

   public static boolean isAllowedForWorld(ServerLevel currentWorld, StructureData config) {
      String worldID = currentWorld.getLevel().dimension().location().toString();
      return config.getDimension_whitelist().contains(worldID);
   }
}