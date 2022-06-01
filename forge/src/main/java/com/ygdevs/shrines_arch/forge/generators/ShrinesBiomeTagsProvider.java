/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.forge.generators;

import com.ygdevs.shrines_arch.Shrines;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.ygdevs.shrines_arch.data.ShrinesBiomeTags.*;

public class ShrinesBiomeTagsProvider extends TagsProvider<Biome> {

   @SuppressWarnings("depreciation")
   protected ShrinesBiomeTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
      super(dataGenerator, BuiltinRegistries.BIOME, Shrines.MODID, existingFileHelper);
   }

   @Override
   protected void addTags() {
      this.tag(EMPTY);
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
      this.tag(ORIENTAL_HUT).addTag(IS_PLAINS).addTag(IS_SAVANNA).addTag(IS_ICY).addTag(BiomeTags.IS_MOUNTAIN);
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
