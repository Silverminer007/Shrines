/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.generators;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.random_variation.RandomVariationMaterial;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import static com.silverminer.shrines.registries.RandomVariationMaterialRegistry.*;

public class RandomVariationMaterialTagProvider extends TagsProvider<RandomVariationMaterial> {
   public static final TagKey<RandomVariationMaterial> ANY = create("any");
   public static final TagKey<RandomVariationMaterial> BEES = create("bees");
   public static final TagKey<RandomVariationMaterial> COLOUR = create("colour");
   public static final TagKey<RandomVariationMaterial> LIGHT = create("light");
   public static final TagKey<RandomVariationMaterial> ORE = create("ore");
   public static final TagKey<RandomVariationMaterial> STONE = create("stone");
   public static final TagKey<RandomVariationMaterial> WOOD = create("wood");

   protected RandomVariationMaterialTagProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
      super(dataGenerator, RegistryAccess.BUILTIN.get().ownedRegistryOrThrow(RandomVariationMaterial.REGISTRY), Shrines.MODID, existingFileHelper);
   }

   private static TagKey<RandomVariationMaterial> create(String path) {
      return TagKey.create(RandomVariationMaterial.REGISTRY, Shrines.location(path));
   }

   @Override
   protected void addTags() {
      this.tag(ANY)
            .addTag(BEES)
            .addTag(COLOUR)
            .addTag(LIGHT)
            .addTag(ORE)
            .addTag(STONE)
            .addTag(WOOD);
      this.tag(BEES)
            .add(BEE_NEST.getKey())
            .add(BEEHIVE.getKey());
      this.tag(COLOUR)
            .add(BLACK.getKey())
            .add(BLUE.getKey())
            .add(BROWN.getKey())
            .add(CYAN.getKey())
            .add(GRAY.getKey())
            .add(GREEN.getKey())
            .add(LIGHT_BLUE.getKey())
            .add(LIGHT_GRAY.getKey())
            .add(LIME.getKey())
            .add(MAGENTA.getKey())
            .add(ORANGE.getKey())
            .add(PINK.getKey())
            .add(PURPLE.getKey())
            .add(RED.getKey())
            .add(WHITE.getKey())
            .add(YELLOW.getKey());
      this.tag(LIGHT)
            .add(NORMAL_LIGHT.getKey())
            .add(SOUL_LIGHT.getKey());
      this.tag(ORE)
            .add(COAL.getKey())
            .add(DIAMOND.getKey())
            .add(EMERALD.getKey())
            .add(GOLD.getKey())
            .add(IRON.getKey())
            .add(REDSTONE.getKey());
      this.tag(STONE)
            .add(ANDESITE.getKey())
            .add(COBBLED_DEEPSLATE.getKey())
            .add(COBBLESTONE.getKey())
            .add(DEEPSLATE_BRICKS.getKey())
            .add(DEEPSLATE_TILES.getKey())
            .add(DIORITE.getKey())
            .add(GRANITE.getKey())
            .add(MOSSY_COBBLESTONE.getKey())
            .add(MOSSY_STONE_BRICKS.getKey())
            .add(NETHER_BRICKS.getKey())
            .add(POLISHED_ANDESITE.getKey())
            .add(POLISHED_BLACKSTONE_BRICKS.getKey())
            .add(POLISHED_DEEPSLATE.getKey())
            .add(POLISHED_DIORITE.getKey())
            .add(POLISHED_GRANITE.getKey())
            .add(RED_NETHER_BRICKS.getKey())
            .add(RED_SANDSTONE.getKey())
            .add(SANDSTONE.getKey())
            .add(STONE_BRICKS.getKey());
      this.tag(WOOD)
            .add(ACACIA.getKey())
            .add(BIRCH.getKey())
            .add(CRIMSON.getKey())
            .add(DARK_OAK.getKey())
            .add(MANGROVE.getKey())
            .add(JUNGLE.getKey())
            .add(OAK.getKey())
            .add(SPRUCE.getKey())
            .add(WARPED.getKey());
   }
}