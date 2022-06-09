/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.generators;

import com.silverminer.shrines.Shrines;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.silverminer.shrines.registries.StructureRegistry.*;

public class StructureTagProvider extends TagsProvider<Structure> {
   private static final TagKey<Structure> ANY = create("any");
   private static final TagKey<Structure> HOUSE = create("house");
   private static final TagKey<Structure> TEMPLE = create("temple");
   private static final TagKey<Structure> SHRINE = create("shrine");
   private static final TagKey<Structure> VILLA = create("villa");
   private static final TagKey<Structure> LADY_JESSA = create("lady_jessa");
   private static final TagKey<Structure> SAM_HIT_APPLE = create("sam_hit_apple");
   private static final TagKey<Structure> CHPTR1 = create("chptr1");
   private static final TagKey<Structure> S1FY = create("s1fy");
   private static final TagKey<Structure> FORSCHER09 = create("forscher09");
   private static final TagKey<Structure> TIKOFAN = create("tikofan");
   private static final TagKey<Structure> AURELJ = create("aurelj");
   private static final TagKey<Structure> MEME_MAN_77 = create("meme_man_77");
   private static final TagKey<Structure> SILVERMINER = create("silverminer");

   protected StructureTagProvider(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
      super(pGenerator, BuiltinRegistries.STRUCTURES, Shrines.MODID, existingFileHelper);
   }

   private static TagKey<Structure> create(String path) {
      return TagKey.create(Registry.STRUCTURE_REGISTRY, Shrines.location(path));
   }

   @Override
   protected void addTags() {
      this.tag(ANY)
            .add(ABANDONED_VILLA.getKey())
            .add(ABANDONED_WITCH_HOUSE.getKey())
            .add(AZALEA_PAVILION.getKey())
            .add(BALLOON.getKey())
            .add(BEES.getKey())
            .add(END_TEMPLE.getKey())
            .add(FLOODED_TEMPLE.getKey())
            .add(GUARDIAN_MEETING.getKey())
            .add(HARBOUR.getKey())
            .add(HIGH_TEMPLE.getKey())
            .add(JUNGLE_TOWER.getKey())
            .add(LUXURY_VILLA.getKey())
            .add(MAYAN_TEMPLE.getKey())
            .add(MINERAL_TEMPLE.getKey())
            .add(MODERN_VILLA.getKey())
            .add(NETHER_PYRAMID_NETHER.getKey())
            .add(NETHER_PYRAMID_OVERWORLD.getKey())
            .add(NETHER_SHRINE_NETHER.getKey())
            .add(NETHER_SHRINE_OVERWORLD.getKey())
            .add(OASIS_SHRINE.getKey())
            .add(ORIENTAL_HUT.getKey())
            .add(ORIENTAL_SANCTUARY.getKey())
            .add(INFESTED_PRISON.getKey())
            .add(SHRINE_OF_SAVANNA.getKey())
            .add(SMALL_PLAYER_HOUSE.getKey())
            .add(SMALL_TEMPLE.getKey())
            .add(TALL_PLAYER_HOUSE.getKey())
            .add(TRADER_HOUSE.getKey())
            .add(WATCH_TOWER.getKey())
            .add(WATER_SHRINE.getKey())
            .add(WORLD_TREE_MANOR.getKey());
      this.tag(HOUSE)
            .add(ABANDONED_VILLA.getKey())
            .add(ABANDONED_WITCH_HOUSE.getKey())
            .add(HARBOUR.getKey())
            .add(LUXURY_VILLA.getKey())
            .add(MODERN_VILLA.getKey())
            .add(ORIENTAL_HUT.getKey())
            .add(INFESTED_PRISON.getKey())
            .add(SMALL_PLAYER_HOUSE.getKey())
            .add(TALL_PLAYER_HOUSE.getKey())
            .add(TRADER_HOUSE.getKey())
            .add(WORLD_TREE_MANOR.getKey());
      this.tag(TEMPLE)
            .add(END_TEMPLE.getKey())
            .add(FLOODED_TEMPLE.getKey())
            .add(HIGH_TEMPLE.getKey())
            .add(MAYAN_TEMPLE.getKey())
            .add(MINERAL_TEMPLE.getKey())
            .add(SMALL_TEMPLE.getKey());
      this.tag(SHRINE)
            .add(NETHER_SHRINE_NETHER.getKey())
            .add(NETHER_SHRINE_OVERWORLD.getKey())
            .add(OASIS_SHRINE.getKey())
            .add(ORIENTAL_SANCTUARY.getKey())
            .add(SHRINE_OF_SAVANNA.getKey())
            .add(WATER_SHRINE.getKey());
      this.tag(VILLA)
            .add(ABANDONED_VILLA.getKey())
            .add(MODERN_VILLA.getKey())
            .add(LUXURY_VILLA.getKey());
      this.tag(LADY_JESSA)
            .add(ABANDONED_VILLA.getKey())
            .add(MODERN_VILLA.getKey())
            .add(OASIS_SHRINE.getKey())
            .add(ORIENTAL_HUT.getKey())
            .add(WORLD_TREE_MANOR.getKey());
      this.tag(SAM_HIT_APPLE)
            .add(ABANDONED_WITCH_HOUSE.getKey())
            .add(FLOODED_TEMPLE.getKey())
            .add(JUNGLE_TOWER.getKey())
            .add(MINERAL_TEMPLE.getKey())
            .add(INFESTED_PRISON.getKey());
      this.tag(CHPTR1)
            .add(BALLOON.getKey())
            .add(NETHER_SHRINE_OVERWORLD.getKey())
            .add(NETHER_SHRINE_NETHER.getKey());
      this.tag(S1FY)
            .add(AZALEA_PAVILION.getKey())
            .add(BEES.getKey())
            .add(GUARDIAN_MEETING.getKey())
            .add(HARBOUR.getKey())
            .add(HIGH_TEMPLE.getKey())
            .add(SHRINE_OF_SAVANNA.getKey())
            .add(SMALL_TEMPLE.getKey())
            .add(WATER_SHRINE.getKey());
      this.tag(FORSCHER09).add(BALLOON.getKey());
      this.tag(TIKOFAN).add(END_TEMPLE.getKey());
      this.tag(AURELJ).add(ORIENTAL_SANCTUARY.getKey());
      this.tag(MEME_MAN_77)
            .add(TRADER_HOUSE.getKey())
            .add(WATCH_TOWER.getKey());
      this.tag(SILVERMINER)
            .add(AZALEA_PAVILION.getKey())
            .add(BALLOON.getKey())
            .add(HARBOUR.getKey())
            .add(MAYAN_TEMPLE.getKey())
            .add(NETHER_PYRAMID_NETHER.getKey())
            .add(NETHER_PYRAMID_OVERWORLD.getKey())
            .add(NETHER_SHRINE_NETHER.getKey())
            .add(NETHER_SHRINE_OVERWORLD.getKey())
            .add(SMALL_PLAYER_HOUSE.getKey())
            .add(TALL_PLAYER_HOUSE.getKey());
   }

   @Override
   public @NotNull String getName() {
      return "Shrines Structure Tags";
   }
}