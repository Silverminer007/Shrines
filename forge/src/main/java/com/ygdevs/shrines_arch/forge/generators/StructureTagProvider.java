/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.forge.generators;

import com.ygdevs.shrines_arch.Shrines;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.ygdevs.shrines_arch.registries.ConfiguredStructureFeatureRegistry.*;

public class StructureTagProvider extends TagsProvider<ConfiguredStructureFeature<?, ?>> {
   private static final TagKey<ConfiguredStructureFeature<?, ?>> ANY = create("any");
   private static final TagKey<ConfiguredStructureFeature<?, ?>> HOUSE = create("house");
   private static final TagKey<ConfiguredStructureFeature<?, ?>> TEMPLE = create("temple");
   private static final TagKey<ConfiguredStructureFeature<?, ?>> SHRINE = create("shrine");
   private static final TagKey<ConfiguredStructureFeature<?, ?>> VILLA = create("villa");
   private static final TagKey<ConfiguredStructureFeature<?, ?>> LADY_JESSA = create("lady_jessa");
   private static final TagKey<ConfiguredStructureFeature<?, ?>> SAM_HIT_APPLE = create("sam_hit_apple");
   private static final TagKey<ConfiguredStructureFeature<?, ?>> CHPTR1 = create("chptr1");
   private static final TagKey<ConfiguredStructureFeature<?, ?>> S1FY = create("s1fy");
   private static final TagKey<ConfiguredStructureFeature<?, ?>> FORSCHER09 = create("forscher09");
   private static final TagKey<ConfiguredStructureFeature<?, ?>> TIKOFAN = create("tikofan");
   private static final TagKey<ConfiguredStructureFeature<?, ?>> AURELJ = create("aurelj");
   private static final TagKey<ConfiguredStructureFeature<?, ?>> MEME_MAN_77 = create("meme_man_77");
   private static final TagKey<ConfiguredStructureFeature<?, ?>> SILVERMINER = create("silverminer");

   protected StructureTagProvider(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
      super(pGenerator, BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, Shrines.MODID, existingFileHelper);
   }

   private static TagKey<ConfiguredStructureFeature<?, ?>> create(String path) {
      return TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, Shrines.location(path));
   }

   @Override
   protected void addTags() {
      this.tag(ANY)
            .add(ABANDONED_VILLA.get())
            .add(ABANDONED_WITCH_HOUSE.get())
            .add(AZALEA_PAVILION.get())
            .add(BALLOON.get())
            .add(BEES.get())
            .add(END_TEMPLE.get())
            .add(FLOODED_TEMPLE.get())
            .add(GUARDIAN_MEETING.get())
            .add(HARBOUR.get())
            .add(HIGH_TEMPLE.get())
            .add(JUNGLE_TOWER.get())
            .add(LUXURY_VILLA.get())
            .add(MINERAL_TEMPLE.get())
            .add(MODERN_VILLA.get())
            .add(NETHER_PYRAMID_NETHER.get())
            .add(NETHER_PYRAMID_OVERWORLD.get())
            .add(NETHER_SHRINE_NETHER.get())
            .add(NETHER_SHRINE_OVERWORLD.get())
            .add(OASIS_SHRINE.get())
            .add(ORIENTAL_HUT.get())
            .add(ORIENTAL_SANCTUARY.get())
            .add(INFESTED_PRISON.get())
            .add(SHRINE_OF_SAVANNA.get())
            .add(SMALL_PLAYER_HOUSE.get())
            .add(SMALL_TEMPLE.get())
            .add(TALL_PLAYER_HOUSE.get())
            .add(TRADER_HOUSE.get())
            .add(WATCH_TOWER.get())
            .add(WATER_SHRINE.get())
            .add(WORLD_TREE_MANOR.get());
      this.tag(HOUSE)
            .add(ABANDONED_VILLA.get())
            .add(ABANDONED_WITCH_HOUSE.get())
            .add(HARBOUR.get())
            .add(LUXURY_VILLA.get())
            .add(MODERN_VILLA.get())
            .add(ORIENTAL_HUT.get())
            .add(INFESTED_PRISON.get())
            .add(SMALL_PLAYER_HOUSE.get())
            .add(TALL_PLAYER_HOUSE.get())
            .add(TRADER_HOUSE.get())
            .add(WORLD_TREE_MANOR.get());
      this.tag(TEMPLE)
            .add(END_TEMPLE.get())
            .add(FLOODED_TEMPLE.get())
            .add(HIGH_TEMPLE.get())
            .add(MINERAL_TEMPLE.get())
            .add(SMALL_TEMPLE.get());
      this.tag(SHRINE)
            .add(NETHER_SHRINE_NETHER.get())
            .add(NETHER_SHRINE_OVERWORLD.get())
            .add(OASIS_SHRINE.get())
            .add(ORIENTAL_SANCTUARY.get())
            .add(SHRINE_OF_SAVANNA.get())
            .add(WATER_SHRINE.get());
      this.tag(VILLA)
            .add(ABANDONED_VILLA.get())
            .add(MODERN_VILLA.get())
            .add(LUXURY_VILLA.get());
      this.tag(LADY_JESSA)
            .add(ABANDONED_VILLA.get())
            .add(MODERN_VILLA.get())
            .add(OASIS_SHRINE.get())
            .add(ORIENTAL_HUT.get())
            .add(WORLD_TREE_MANOR.get());
      this.tag(SAM_HIT_APPLE)
            .add(ABANDONED_WITCH_HOUSE.get())
            .add(FLOODED_TEMPLE.get())
            .add(JUNGLE_TOWER.get())
            .add(MINERAL_TEMPLE.get())
            .add(INFESTED_PRISON.get());
      this.tag(CHPTR1)
            .add(BALLOON.get())
            .add(NETHER_SHRINE_OVERWORLD.get())
            .add(NETHER_SHRINE_NETHER.get());
      this.tag(S1FY)
            .add(AZALEA_PAVILION.get())
            .add(BEES.get())
            .add(GUARDIAN_MEETING.get())
            .add(HARBOUR.get())
            .add(HIGH_TEMPLE.get())
            .add(SHRINE_OF_SAVANNA.get())
            .add(SMALL_TEMPLE.get())
            .add(WATER_SHRINE.get());
      this.tag(FORSCHER09).add(BALLOON.get());
      this.tag(TIKOFAN).add(END_TEMPLE.get());
      this.tag(AURELJ).add(ORIENTAL_SANCTUARY.get());
      this.tag(MEME_MAN_77)
            .add(TRADER_HOUSE.get())
            .add(WATCH_TOWER.get());
      this.tag(SILVERMINER)
            .add(AZALEA_PAVILION.get())
            .add(BALLOON.get())
            .add(HARBOUR.get())
            .add(NETHER_PYRAMID_NETHER.get())
            .add(NETHER_PYRAMID_OVERWORLD.get())
            .add(NETHER_SHRINE_NETHER.get())
            .add(NETHER_SHRINE_OVERWORLD.get())
            .add(SMALL_PLAYER_HOUSE.get())
            .add(TALL_PLAYER_HOUSE.get());
   }

   @Override
   public @NotNull String getName() {
      return "Shrines Structure Tags";
   }
}