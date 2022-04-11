/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.generators;

import com.silverminer.shrines.ShrinesMod;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShrinesBiomeTagsProvider extends TagsProvider<Biome> {
   public static final TagKey<Biome> IS_PLAINS = createMinecraft("is_plains");
   public static final TagKey<Biome> IS_MESA = createMinecraft("is_mesa");
   public static final TagKey<Biome> IS_SAVANNA = createMinecraft("is_savanna");
   public static final TagKey<Biome> IS_ICY = createMinecraft("is_icy");
   public static final TagKey<Biome> IS_THEEND = createMinecraft("is_theend");
   public static final TagKey<Biome> IS_DESERT = createMinecraft("is_desert");
   public static final TagKey<Biome> IS_SWAMP = createMinecraft("is_swamp");
   public static final TagKey<Biome> IS_MUSHROOM = createMinecraft("is_mushroom");
   public static final TagKey<Biome> IS_NETHER = createMinecraft("is_nether");
   public static final TagKey<Biome> ABANDONED_VILLA = createShrines("abandoned_villa");
   public static final TagKey<Biome> ABANDONED_WITCH_HOUSE = createShrines("abandoned_witch_house");
   public static final TagKey<Biome> AZALEA_PAVILION = createShrines("azalea_pavilion");
   public static final TagKey<Biome> BALLOON = createShrines("balloon");
   public static final TagKey<Biome> BEES = createShrines("bees");
   public static final TagKey<Biome> END_TEMPLE = createShrines("end_temple");
   public static final TagKey<Biome> FLOODED_TEMPLE = createShrines("flooded_temple");
   public static final TagKey<Biome> GUARDIANS_MEETING = createShrines("guardians_meeting");
   public static final TagKey<Biome> HARBOUR = createShrines("harbour");
   public static final TagKey<Biome> HIGH_TEMPLE = createShrines("high_temple");
   public static final TagKey<Biome> JUNGLE_TOWER = createShrines("jungle_tower");
   public static final TagKey<Biome> LUXURY_VILLA = createShrines("luxury_villa");
   public static final TagKey<Biome> MINERAL_TEMPLE = createShrines("mineral_temple");
   public static final TagKey<Biome> MODERN_VILLA = createShrines("modern_villa");
   public static final TagKey<Biome> NETHER_PYRAMID_OVERWORLD = createShrines("nether_pyramid_overworld");
   public static final TagKey<Biome> NETHER_SHRINE_OVERWORLD = createShrines("nether_shrine_overworld");
   public static final TagKey<Biome> NETHER_PYRAMID_NETHER = createShrines("nether_pyramid_nether");
   public static final TagKey<Biome> NETHER_SHRINE_NETHER = createShrines("nether_shrine_nether");
   public static final TagKey<Biome> OASIS_SHRINE = createShrines("oasis_shrine");
   public static final TagKey<Biome> ORIENTAL_SANCTUARY = createShrines("oriental_sanctuary");
   public static final TagKey<Biome> PLAYER_HOUSE = createShrines("player_house");
   public static final TagKey<Biome> INFESTED_PRISON = createShrines("infested_prison");
   public static final TagKey<Biome> SHRINE_OF_SAVANNA = createShrines("shrine_of_savanna");
   public static final TagKey<Biome> SMALL_TEMPLE = createShrines("small_temple");
   public static final TagKey<Biome> TRADER_HOUSE = createShrines("trader_house");
   public static final TagKey<Biome> WATCH_TOWER = createShrines("watch_tower");
   public static final TagKey<Biome> WATER_SHRINE = createShrines("water_shrine");
   public static final TagKey<Biome> WORLD_TREE_MANOR = createShrines("world_tree_manor");

   @SuppressWarnings("depreciation")
   protected ShrinesBiomeTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
      super(dataGenerator, BuiltinRegistries.BIOME, ShrinesMod.MODID, existingFileHelper);
   }

   private static TagKey<Biome> createShrines(String name) {
      return TagKey.create(Registry.BIOME_REGISTRY, ShrinesMod.location("has_structure/" + name));
   }

   private static TagKey<Biome> createMinecraft(String name) {
      return TagKey.create(Registry.BIOME_REGISTRY, ShrinesMod.location(name));
   }

   @Override
   protected void addTags() {
      this.tag(IS_PLAINS).add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS);
      this.tag(IS_MESA).add(Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS);
      this.tag(IS_SAVANNA).add(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA);
      this.tag(IS_ICY).add(Biomes.SNOWY_PLAINS, Biomes.ICE_SPIKES);
      this.tag(IS_THEEND).add(Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, Biomes.SMALL_END_ISLANDS, Biomes.END_BARRENS);// NOT Biomes.THE_END. That only void. We don't want structures to spawn in void
      this.tag(IS_DESERT).add(Biomes.DESERT);
      this.tag(IS_SWAMP).add(Biomes.SWAMP);
      this.tag(IS_MUSHROOM).add(Biomes.MUSHROOM_FIELDS);
      this.tag(IS_NETHER).add(Biomes.NETHER_WASTES, Biomes.WARPED_FOREST, Biomes.CRIMSON_FOREST, Biomes.SOUL_SAND_VALLEY, Biomes.BASALT_DELTAS);
      this.tag(ABANDONED_VILLA).addTag(IS_SWAMP).addTag(BiomeTags.IS_JUNGLE);
      this.tag(ABANDONED_WITCH_HOUSE).addTag(IS_SWAMP).addTag(BiomeTags.IS_FOREST);
      this.tag(AZALEA_PAVILION).addTag(IS_SAVANNA).add(Biomes.BIRCH_FOREST);
      this.tag(BALLOON).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
            .addTag(IS_MESA).addTag(IS_ICY).addTag(IS_DESERT).addTag(IS_SWAMP).addTag(IS_MUSHROOM);
      this.tag(BEES).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA);
      this.tag(END_TEMPLE).addTag(IS_THEEND);
      this.tag(FLOODED_TEMPLE).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
            .addTag(IS_ICY).addTag(IS_SWAMP).addTag(IS_MUSHROOM);
      this.tag(GUARDIANS_MEETING).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
            .addTag(IS_MESA).addTag(IS_ICY).addTag(IS_DESERT).addTag(IS_SWAMP).addTag(IS_MUSHROOM);
      this.tag(HARBOUR).addTag(BiomeTags.IS_OCEAN).addTag(BiomeTags.IS_DEEP_OCEAN);
      this.tag(HIGH_TEMPLE).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
            .addTag(IS_MESA).addTag(IS_ICY).addTag(IS_DESERT).addTag(IS_SWAMP).addTag(IS_MUSHROOM);
      this.tag(JUNGLE_TOWER).addTag(BiomeTags.IS_JUNGLE);
      this.tag(LUXURY_VILLA).addTag(IS_PLAINS).addTag(IS_SAVANNA).addTag(IS_ICY);
      this.tag(MINERAL_TEMPLE).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
            .addTag(IS_MESA).addTag(IS_ICY).addTag(IS_DESERT).addTag(IS_SWAMP).addTag(IS_MUSHROOM);
      this.tag(MODERN_VILLA).addTag(IS_SWAMP).addTag(BiomeTags.IS_JUNGLE).addTag(IS_MUSHROOM).addTag(IS_DESERT).addTag(IS_MESA);
      this.tag(NETHER_PYRAMID_OVERWORLD).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
            .addTag(IS_MESA).addTag(IS_ICY).addTag(IS_DESERT).addTag(IS_SWAMP).addTag(IS_MUSHROOM);
      this.tag(NETHER_SHRINE_OVERWORLD).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
            .addTag(IS_MESA).addTag(IS_ICY).addTag(IS_DESERT).addTag(IS_SWAMP).addTag(IS_MUSHROOM);
      this.tag(NETHER_PYRAMID_NETHER).addTag(IS_NETHER);
      this.tag(NETHER_SHRINE_NETHER).addTag(IS_NETHER);
      this.tag(OASIS_SHRINE).addTag(IS_DESERT).addTag(IS_MESA);
      this.tag(ORIENTAL_SANCTUARY).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
            .addTag(IS_MESA).addTag(IS_ICY).addTag(IS_DESERT).addTag(IS_SWAMP).addTag(IS_MUSHROOM);
      this.tag(PLAYER_HOUSE).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
            .addTag(IS_MESA).addTag(IS_ICY).addTag(IS_DESERT).addTag(IS_SWAMP).addTag(IS_MUSHROOM);
      this.tag(INFESTED_PRISON).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
            .addTag(IS_MESA).addTag(IS_ICY).addTag(IS_DESERT).addTag(IS_SWAMP).addTag(IS_MUSHROOM);
      this.tag(SHRINE_OF_SAVANNA).addTag(IS_SAVANNA);
      this.tag(SMALL_TEMPLE).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
            .addTag(IS_MESA).addTag(IS_ICY).addTag(IS_DESERT).addTag(IS_SWAMP).addTag(IS_MUSHROOM);
      this.tag(TRADER_HOUSE).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
            .addTag(IS_MESA).addTag(IS_ICY).addTag(IS_DESERT).addTag(IS_SWAMP).addTag(IS_MUSHROOM);
      this.tag(WATCH_TOWER).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
            .addTag(IS_MESA).addTag(IS_ICY).addTag(IS_DESERT).addTag(IS_SWAMP).addTag(IS_MUSHROOM);
      this.tag(WATER_SHRINE).addTag(IS_PLAINS).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_TAIGA).addTag(IS_SAVANNA).addTag(BiomeTags.IS_JUNGLE)
            .addTag(IS_MESA).addTag(IS_ICY).addTag(IS_DESERT).addTag(IS_SWAMP).addTag(IS_MUSHROOM);
      this.tag(WORLD_TREE_MANOR).addTag(IS_SWAMP).addTag(BiomeTags.IS_TAIGA).addTag(BiomeTags.IS_FOREST);
   }

   @Override
   public @NotNull String getName() {
      return "Shrines Biome Tags";
   }
}
