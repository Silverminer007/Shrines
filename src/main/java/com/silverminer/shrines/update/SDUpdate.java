/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.update;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.config.ShrinesConfig;
import com.silverminer.shrines.generators.ShrinesBiomeTagsProvider;
import com.silverminer.shrines.random_variation.RandomVariationConfig;
import com.silverminer.shrines.registries.StructureRegistry;
import com.silverminer.shrines.structures.ShrinesConfiguration;
import com.silverminer.shrines.structures.placement_types.RelativePlacementCalculator;
import com.silverminer.shrines.structures.spawn_criteria.GroundLevelDeltaSpawnCriteria;
import com.silverminer.shrines.structures.spawn_criteria.HeightSpawnCriteria;
import com.silverminer.shrines.structures.spawn_criteria.RandomChanceSpawnCriteria;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SDUpdate {
   private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

   public static void updateStructureData(@NotNull Path oldNameSpacePath, @NotNull Path newNameSpacePath, String namespaceName) throws IOException {
      List<String> disabledStructures = new ArrayList<>(ShrinesConfig.disabledStructures.get());
      RegistryAccess registryAccess = RegistryAccess.BUILTIN.get();// TODO This only includes builtin (Code registered) elements. If we want to include also datapack loaded elements, we have to move the updater back to world load, what makes it even more complicated, because we have to reload twice ...
      Path structureDataPath = oldNameSpacePath.resolve("shrines_structures");
      Files.walk(structureDataPath, Integer.MAX_VALUE).map(structureDataItemPath -> {
         if (Files.isRegularFile(structureDataItemPath)) {
            try {
               String structureDataItemString = Files.readString(structureDataItemPath);
               return StructureData.loadFromJSON(JsonParser.parseString(structureDataItemString));
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         return null;
      }).filter(Objects::nonNull).map(structureData -> {
         SpawnConfiguration spawnConfiguration = structureData.getSpawnConfiguration();
         TagKey<Biome> biomeTagKey = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(namespaceName, "has_structure/" + structureData.getKey().getPath()));
         Holder<StructureTemplatePool> startPoolHolder = Holder.Reference.createStandAlone(BuiltinRegistries.TEMPLATE_POOL,
               ResourceKey.create(Registry.TEMPLATE_POOL_REGISTRY, new ResourceLocation(spawnConfiguration.start_pool)));
         ConfiguredStructureFeature<?, ?> configuredStructureFeature = StructureRegistry.SURFACE.get().configured(
               new ShrinesConfiguration(startPoolHolder, spawnConfiguration.jigsawMaxDepth(), List.of(
                     new HeightSpawnCriteria(64, Integer.MAX_VALUE, 32),
                     new RandomChanceSpawnCriteria(spawnConfiguration.spawn_chance()),
                     new GroundLevelDeltaSpawnCriteria(2.0D, 32)),
                     new RelativePlacementCalculator(spawnConfiguration.height_offset())), biomeTagKey,
               spawnConfiguration.transformLand());

         ResourceKey<ConfiguredStructureFeature<?, ?>> configuredStructureFeatureResourceKey =
               ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
                     new ResourceLocation(namespaceName, structureData.getKey().getPath()));
         StructureSet structureSet = new StructureSet(Holder.Reference.createStandAlone(
               BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, configuredStructureFeatureResourceKey),
               new RandomSpreadStructurePlacement(spawnConfiguration.distance(), spawnConfiguration.separation(),
                     RandomSpreadType.LINEAR, spawnConfiguration.seed_modifier()));

         List<Either<ResourceKey<Biome>, TagKey<Biome>>> biomes = new ArrayList<>();
         if (spawnConfiguration.biome_blacklist().isEmpty()) {
            for (String biomeCategory : spawnConfiguration.biome_category_whitelist()) {
               try {
                  switch (Biome.BiomeCategory.valueOf(biomeCategory)) {
                     case NONE -> biomes.add(Either.left(Biomes.THE_VOID));
                     case TAIGA -> biomes.add(Either.right(BiomeTags.IS_TAIGA));
                     case EXTREME_HILLS -> biomes.add(Either.right(BiomeTags.IS_HILL));
                     case JUNGLE -> biomes.add(Either.right(BiomeTags.IS_JUNGLE));
                     case MESA -> biomes.add(Either.right(ShrinesBiomeTagsProvider.IS_MESA));
                     case PLAINS -> biomes.add(Either.right(ShrinesBiomeTagsProvider.IS_PLAINS));
                     case SAVANNA -> biomes.add(Either.right(ShrinesBiomeTagsProvider.IS_SAVANNA));
                     case ICY -> biomes.add(Either.right(ShrinesBiomeTagsProvider.IS_ICY));
                     case THEEND -> biomes.add(Either.right(ShrinesBiomeTagsProvider.IS_THEEND));
                     case BEACH -> biomes.add(Either.right(BiomeTags.IS_BEACH));
                     case FOREST -> biomes.add(Either.right(BiomeTags.IS_FOREST));
                     case OCEAN -> biomes.add(Either.right(BiomeTags.IS_OCEAN));
                     case DESERT -> biomes.add(Either.right(ShrinesBiomeTagsProvider.IS_DESERT));
                     case RIVER -> biomes.add(Either.right(BiomeTags.IS_RIVER));
                     case SWAMP -> biomes.add(Either.right(ShrinesBiomeTagsProvider.IS_SWAMP));
                     case MUSHROOM -> biomes.add(Either.right(ShrinesBiomeTagsProvider.IS_MUSHROOM));
                     case NETHER -> biomes.add(Either.right(BiomeTags.IS_NETHER));
                     case UNDERGROUND -> {
                        biomes.add(Either.left(Biomes.LUSH_CAVES));
                        biomes.add(Either.left(Biomes.DRIPSTONE_CAVES));
                     }
                     case MOUNTAIN -> biomes.add(Either.right(BiomeTags.IS_MOUNTAIN));
                  }
               } catch (NullPointerException e) {
                  Shrines.LOGGER.error("Skipped invalid Biome Category [{}]", biomeCategory);
               }
            }
         } else {
            for (Map.Entry<ResourceKey<Biome>, Biome> biomeEntry : ForgeRegistries.BIOMES.getEntries()) {
               if (spawnConfiguration.biome_category_whitelist().contains(Biome.getBiomeCategory(Holder.direct(biomeEntry.getValue())).toString()) &&
                     !spawnConfiguration.biome_blacklist().contains(biomeEntry.getKey().location().toString())) {
                  biomes.add(Either.left(biomeEntry.getKey()));
               }
            }
         }

         if (!spawnConfiguration.generate() && !disabledStructures.contains(structureData.getKey().toString())) {
            disabledStructures.add(structureData.getKey().toString());
         }

         return new NewStructureData(structureData.getKey().getPath(), structureSet, configuredStructureFeature, biomes.stream().map(either -> {
            Either<String, String> stringEither = either.mapBoth(ResourceKey::location, TagKey::location).mapBoth(Object::toString, Objects::toString).mapRight(string -> "#" + string);
            return stringEither.left().orElseGet(() -> stringEither.right().orElse(ShrinesBiomeTagsProvider.EMPTY.location().toString()));
         }).toList(), new RandomVariationConfig(List.of()));
      }).forEach(structureContainer -> {
         Path structureSetPath = newNameSpacePath.resolve("worldgen").resolve("structure_set").resolve(structureContainer.key + ".json");
         RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, registryAccess);
         StructureSet.DIRECT_CODEC.encode(structureContainer.structureSet(), registryOps, new JsonObject()).resultOrPartial(Shrines.LOGGER::error).ifPresent(jsonElement ->
         {
            try {
               if (!Files.exists(structureSetPath.getParent())) {
                  Files.createDirectories(structureSetPath.getParent());
               }
               Files.writeString(structureSetPath, GSON.toJson(jsonElement));
            } catch (IOException e) {
               e.printStackTrace();
            }
         });

         Path configuredStructureFeaturePath = newNameSpacePath.resolve("worldgen").resolve("configured_structure_feature").resolve(structureContainer.key() + ".json");
         ConfiguredStructureFeature.DIRECT_CODEC.encode(structureContainer.configuredStructureFeature(), registryOps, new JsonObject()).resultOrPartial(Shrines.LOGGER::error).ifPresent(jsonElement ->
         {
            try {
               if (!Files.exists(configuredStructureFeaturePath.getParent())) {
                  Files.createDirectories(configuredStructureFeaturePath.getParent());
               }
               Files.writeString(configuredStructureFeaturePath, GSON.toJson(jsonElement));
            } catch (IOException e) {
               e.printStackTrace();
            }
         });

         Path randomVariationConfigPath = newNameSpacePath.resolve("shrines").resolve("random_variation").resolve("config").resolve(structureContainer.key() + ".json");
         RandomVariationConfig.DIRECT_CODEC.encode(structureContainer.randomVariationConfig(), registryOps, new JsonObject()).resultOrPartial(Shrines.LOGGER::error).ifPresent(jsonElement ->
         {
            try {
               if (!Files.exists(randomVariationConfigPath.getParent())) {
                  Files.createDirectories(randomVariationConfigPath.getParent());
               }
               Files.writeString(randomVariationConfigPath, GSON.toJson(jsonElement));
            } catch (IOException e) {
               e.printStackTrace();
            }
         });

         Path biomeTagPath = newNameSpacePath.resolve("tags").resolve("worldgen").resolve("biome").resolve("has_structure").resolve(structureContainer.key() + ".json");
         JsonObject tagFile = new JsonObject();
         tagFile.add("replace", new JsonPrimitive(false));
         JsonArray tagValues = new JsonArray();
         structureContainer.biomes().forEach(tagValues::add);
         tagFile.add("values", tagValues);
         try {
            if (!Files.exists(biomeTagPath.getParent())) {
               Files.createDirectories(biomeTagPath.getParent());
            }
            Files.writeString(biomeTagPath, GSON.toJson(tagFile));
         } catch (IOException e) {
            e.printStackTrace();
         }
      });
      ShrinesConfig.disabledStructures.set(disabledStructures);
   }

   private record NewStructureData(String key, StructureSet structureSet,
                                   ConfiguredStructureFeature<?, ?> configuredStructureFeature,
                                   List<String> biomes, RandomVariationConfig randomVariationConfig) {
   }

   public static class StructureData implements Comparable<StructureData> {
      public static final Codec<StructureData> CODEC = RecordCodecBuilder.create((structureDataInstance) -> structureDataInstance.group(
                  Codec.STRING.fieldOf("name").forGetter(StructureData::getName),
                  ResourceLocation.CODEC.fieldOf("key").forGetter(StructureData::getKey),
                  SpawnConfiguration.CODEC.fieldOf("spawn_configuration").forGetter(StructureData::getSpawnConfiguration),
                  ResourceLocation.CODEC.optionalFieldOf("icon_path", null).forGetter(StructureData::getIconPath),
                  ResourceLocation.CODEC.optionalFieldOf("novel", new ResourceLocation("")).forGetter(StructureData::getNovel),
                  Codec.either(VariationConfiguration.CODEC, NewVariationConfiguration.CODEC).optionalFieldOf("variation_configuration",
                        Either.right(new NewVariationConfiguration(false))).forGetter(structureData -> Either.right(structureData.variationConfiguration)))
            .apply(structureDataInstance, StructureData::new));
      protected static final Logger LOGGER = LogManager.getLogger(StructureData.class);
      private final SpawnConfiguration spawnConfiguration;
      private final NewVariationConfiguration variationConfiguration;
      private final String name;
      private final ResourceLocation key;
      private final ResourceLocation novel;
      private final ResourceLocation iconPath;

      public StructureData(String name, ResourceLocation key, SpawnConfiguration spawnConfiguration, @Nullable ResourceLocation iconPath, @Nullable ResourceLocation novel,
                           Either<VariationConfiguration, NewVariationConfiguration> variationConfiguration) {
         this.name = name;
         this.key = key;
         this.novel = Objects.requireNonNullElse(novel, key);
         this.iconPath = Objects.requireNonNullElse(iconPath, key);
         this.spawnConfiguration = spawnConfiguration;
         this.variationConfiguration =
               variationConfiguration.right().orElse(variationConfiguration.left().map(VariationConfiguration::toNewConfiguration).orElse(
                     new NewVariationConfiguration(false, new ArrayList<>(), new ArrayList<>())));
      }

      @Nullable
      public static StructureData loadFromJSON(JsonElement tag) {
         DataResult<Pair<StructureData, JsonElement>> dataResult = CODEC.decode(JsonOps.INSTANCE, tag);
         Optional<Pair<StructureData, JsonElement>> optional = dataResult.resultOrPartial(LOGGER::error);
         return optional.map(Pair::getFirst).orElse(null);
      }

      public NewVariationConfiguration getVariationConfiguration() {
         return variationConfiguration;
      }

      public ResourceLocation getKey() {
         return key;
      }

      public SpawnConfiguration getSpawnConfiguration() {
         return spawnConfiguration;
      }

      public ResourceLocation getNovel() {
         return novel;
      }

      public ResourceLocation getIconPath() {
         return iconPath;
      }

      @Override
      public int compareTo(StructureData o) {
         return this.getName().compareTo(o.getName());
      }

      public String getName() {
         return name;
      }
   }

   public record SpawnConfiguration(boolean transformLand, boolean generate, double spawn_chance, int distance,
                                    int separation, int seed_modifier, int height_offset,
                                    List<String> biome_blacklist,
                                    List<String> biome_category_whitelist,
                                    List<String> dimension_whitelist, String start_pool,
                                    int jigsawMaxDepth) {
      public static final Codec<SpawnConfiguration> CODEC = RecordCodecBuilder.create((structureDataInstance) -> structureDataInstance.group(
                  Codec.BOOL.optionalFieldOf("transform_land", true).forGetter(SpawnConfiguration::transformLand),
                  Codec.BOOL.optionalFieldOf("generate", true).forGetter(SpawnConfiguration::generate),
                  Codec.DOUBLE.fieldOf("spawn_chance").forGetter(SpawnConfiguration::spawn_chance),
                  Codec.intRange(1, Integer.MAX_VALUE).fieldOf("distance").forGetter(SpawnConfiguration::distance),
                  Codec.intRange(1, Integer.MAX_VALUE).fieldOf("separation").forGetter(SpawnConfiguration::separation),
                  Codec.INT.fieldOf("seed_modifier").forGetter(SpawnConfiguration::seed_modifier),
                  Codec.INT.optionalFieldOf("height_offset", 0).forGetter(SpawnConfiguration::height_offset),
                  Codec.list(Codec.STRING).fieldOf("biome_blacklist").forGetter(SpawnConfiguration::biome_blacklist),
                  Codec.list(Codec.STRING).fieldOf("biome_category_whitelist").forGetter(SpawnConfiguration::biome_category_whitelist),
                  Codec.list(Codec.STRING).fieldOf("dimension_whitelist").forGetter(SpawnConfiguration::dimension_whitelist),
                  Codec.STRING.fieldOf("start_pool").forGetter(SpawnConfiguration::start_pool),
                  Codec.INT.optionalFieldOf("jigsaw_max_depth", 7).forGetter(SpawnConfiguration::jigsawMaxDepth))
            .apply(structureDataInstance, SpawnConfiguration::new));
   }

   public static class NewVariationConfiguration {
      public static final Codec<NewVariationConfiguration> CODEC =
            RecordCodecBuilder.create(newVariationConfigurationInstance ->
                  newVariationConfigurationInstance.group(
                              Codec.BOOL.fieldOf("active").forGetter(NewVariationConfiguration::isEnabled),
                              Codec.list(Codec.STRING).fieldOf("disabled_materials").forGetter(NewVariationConfiguration::getDisabledMaterials),
                              Codec.list(Codec.STRING).fieldOf("disabled_types").forGetter(NewVariationConfiguration::getDisabledTypes))
                        .apply(newVariationConfigurationInstance, NewVariationConfiguration::new));

      private final boolean isEnabled;
      private final List<String> disabledMaterials;
      private final List<String> disabledTypes;

      public NewVariationConfiguration(boolean isEnabled) {
         this(isEnabled, new ArrayList<>(), new ArrayList<>());
      }

      public NewVariationConfiguration(boolean isEnabled, List<String> disabledMaterials, List<String> disabledTypes) {
         this.isEnabled = isEnabled;
         this.disabledMaterials = ImmutableList.copyOf(disabledMaterials);
         this.disabledTypes = ImmutableList.copyOf(disabledTypes);
      }

      public boolean isEnabled() {
         return this.isEnabled;
      }

      public List<String> getDisabledMaterials() {
         return disabledMaterials;
      }

      public List<String> getDisabledTypes() {
         return disabledTypes;
      }
   }

   public record VariationConfiguration(boolean isEnabled,
                                        SDUpdate.VariationConfiguration.SimpleVariationConfiguration simpleVariationConfiguration,
                                        SDUpdate.VariationConfiguration.NestedVariationConfiguration nestedVariationConfiguration) {
      public static final Codec<VariationConfiguration> CODEC = RecordCodecBuilder.create(variationConfigurationInstance -> variationConfigurationInstance.group(
            Codec.BOOL.fieldOf("is_enabled").forGetter(VariationConfiguration::isEnabled),
            SimpleVariationConfiguration.CODEC.optionalFieldOf("simple", SimpleVariationConfiguration.ALL_DISABLED).forGetter(VariationConfiguration::simpleVariationConfiguration),
            NestedVariationConfiguration.CODEC.optionalFieldOf("nested", NestedVariationConfiguration.ALL_DISABLED).forGetter(VariationConfiguration::nestedVariationConfiguration)
      ).apply(variationConfigurationInstance, VariationConfiguration::new));

      @Contract(" -> new")
      public @NotNull NewVariationConfiguration toNewConfiguration() {
         Pair<List<String>, List<String>> pairDisabledMaterialsTypes = this.simpleVariationConfiguration().getDisabledMaterials();
         List<String> disabledTypes = this.nestedVariationConfiguration().getDisabledTypes();
         disabledTypes.addAll(pairDisabledMaterialsTypes.getSecond());
         return new NewVariationConfiguration(this.isEnabled(), pairDisabledMaterialsTypes.getFirst(), disabledTypes);
      }

      public record SimpleVariationConfiguration(boolean isWoolEnabled, boolean isTerracottaEnabled,
                                                 boolean isGlazedTerracottaEnabled, boolean isConcreteEnabled,
                                                 boolean isConcretePowderEnabled, boolean arePlanksEnabled,
                                                 boolean areOresEnabled, boolean areStonesEnabled,
                                                 boolean areBeesEnabled) {
         public static final Codec<SimpleVariationConfiguration> CODEC = RecordCodecBuilder.create(variationConfigurationInstance -> variationConfigurationInstance.group(
               Codec.BOOL.fieldOf("is_wool_enabled").forGetter(SimpleVariationConfiguration::isWoolEnabled),
               Codec.BOOL.fieldOf("is_terracotta_enabled").forGetter(SimpleVariationConfiguration::isTerracottaEnabled),
               Codec.BOOL.fieldOf("is_glazed_terracotta").forGetter(SimpleVariationConfiguration::isGlazedTerracottaEnabled),
               Codec.BOOL.fieldOf("is_concrete_enabled").forGetter(SimpleVariationConfiguration::isConcreteEnabled),
               Codec.BOOL.fieldOf("is_concrete_powder_enabled").forGetter(SimpleVariationConfiguration::isConcretePowderEnabled),
               Codec.BOOL.fieldOf("are_planks_enabled").forGetter(SimpleVariationConfiguration::arePlanksEnabled),
               Codec.BOOL.fieldOf("are_ores_enabled").forGetter(SimpleVariationConfiguration::areOresEnabled),
               Codec.BOOL.fieldOf("are_stones_enabled").forGetter(SimpleVariationConfiguration::areStonesEnabled),
               Codec.BOOL.fieldOf("are_bees_enabled").forGetter(SimpleVariationConfiguration::areBeesEnabled)
         ).apply(variationConfigurationInstance, SimpleVariationConfiguration::new));
         public static final SimpleVariationConfiguration ALL_DISABLED = new SimpleVariationConfiguration(false, false, false, false, false, false, false, false, false);

         public @NotNull Pair<List<String>, List<String>> getDisabledMaterials() {
            List<String> disabledMaterials = new ArrayList<>();
            List<String> disabledTypes = new ArrayList<>();
            if (!this.isWoolEnabled()) {
               disabledTypes.add("wool");
            }
            if (!this.isTerracottaEnabled()) {
               disabledTypes.add("terracotta");
            }
            if (!this.isGlazedTerracottaEnabled()) {
               disabledTypes.add("glazed_terracotta");
            }
            if (!this.isConcreteEnabled()) {
               disabledTypes.add("concrete");
            }
            if (!this.isConcretePowderEnabled()) {
               disabledTypes.add("concrete_powder");
            }
            if (!this.arePlanksEnabled()) {
               disabledTypes.add("planks");
            }
            if (!this.areOresEnabled()) {
               disabledMaterials.add("ores");
            }
            if (!this.areStonesEnabled()) {
               disabledMaterials.add("stone");
            }
            if (!this.areBeesEnabled()) {
               disabledMaterials.add("bees");
            }
            return Pair.of(disabledMaterials, disabledTypes);
         }
      }

      public record NestedVariationConfiguration(boolean areSlabsEnabled, boolean isButtonEnabled,
                                                 boolean isFenceEnabled,
                                                 boolean areNormalLogsEnabled, boolean areStrippedLogsEnabled,
                                                 boolean areTrapdoorsEnabled, boolean areDoorsEnabled,
                                                 boolean isStairEnabled,
                                                 boolean isStandingSignEnabled, boolean isWallSignEnabled) {
         public static final Codec<NestedVariationConfiguration> CODEC = RecordCodecBuilder.create(variationConfigurationInstance -> variationConfigurationInstance.group(
               Codec.BOOL.fieldOf("are_slabs_enabled").forGetter(NestedVariationConfiguration::areSlabsEnabled),
               Codec.BOOL.fieldOf("is_button_enabled").forGetter(NestedVariationConfiguration::isButtonEnabled),
               Codec.BOOL.fieldOf("is_fence_enabled").forGetter(NestedVariationConfiguration::isFenceEnabled),
               Codec.BOOL.fieldOf("are_normal_logs_enabled").forGetter(NestedVariationConfiguration::areNormalLogsEnabled),
               Codec.BOOL.fieldOf("are_stripped_logs_enabled").forGetter(NestedVariationConfiguration::areStrippedLogsEnabled),
               Codec.BOOL.fieldOf("are_trapdoors_enabled").forGetter(NestedVariationConfiguration::areTrapdoorsEnabled),
               Codec.BOOL.fieldOf("are_doors_enabled").forGetter(NestedVariationConfiguration::areDoorsEnabled),
               Codec.BOOL.fieldOf("is_stair_enabled").forGetter(NestedVariationConfiguration::isStairEnabled),
               Codec.BOOL.fieldOf("is_standing_sign_enabled").forGetter(NestedVariationConfiguration::isStandingSignEnabled),
               Codec.BOOL.fieldOf("is_wall_sign_enabled").forGetter(NestedVariationConfiguration::isWallSignEnabled)
         ).apply(variationConfigurationInstance, NestedVariationConfiguration::new));
         public static final NestedVariationConfiguration ALL_DISABLED = new NestedVariationConfiguration(false, false, false, false, false, false, false, false, false, false);

         public @NotNull List<String> getDisabledTypes() {
            List<String> disabledTypes = new ArrayList<>();
            if (this.areSlabsEnabled()) {
               disabledTypes.add("slab");
            }
            if (this.isButtonEnabled()) {
               disabledTypes.add("button");
            }
            if (this.isFenceEnabled()) {
               disabledTypes.add("fence");
            }
            if (this.areNormalLogsEnabled()) {
               disabledTypes.add("log");
            }
            if (this.areStrippedLogsEnabled()) {
               disabledTypes.add("stripped_log");
            }
            if (this.areTrapdoorsEnabled()) {
               disabledTypes.add("trapdoor");
            }
            if (this.areDoorsEnabled()) {
               disabledTypes.add("door");
            }
            if (this.isStairEnabled()) {
               disabledTypes.add("stair");
            }
            if (this.isStandingSignEnabled()) {
               disabledTypes.add("standing_sign");
            }
            if (this.isWallSignEnabled()) {
               disabledTypes.add("wall_sign");
            }
            return disabledTypes;
         }
      }
   }
}