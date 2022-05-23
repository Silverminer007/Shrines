package com.silverminer.shrines.generators;

import com.silverminer.shrines.Shrines;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.silverminer.shrines.registries.ConfiguredStructureFeatureRegistry.*;

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