/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.registries;

import com.mojang.datafixers.util.Pair;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.random_variation.RandomVariationMaterial;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class RandomVariationMaterialRegistry {
   public static final DeferredRegister<RandomVariationMaterial> REGISTRY = DeferredRegister.create(RandomVariationMaterial.REGISTRY, Shrines.MODID);
   public static final Supplier<IForgeRegistry<RandomVariationMaterial>> FORGE_REGISTRY_SUPPLIER =
         REGISTRY.makeRegistry(() -> new RegistryBuilder<RandomVariationMaterial>().dataPackRegistry(RandomVariationMaterial.DIRECT_CODEC));

   public static final RegistryObject<RandomVariationMaterial> ACACIA = REGISTRY.register("acacia", () -> wood(Blocks.ACACIA_PLANKS, Blocks.ACACIA_SLAB, Blocks.ACACIA_BUTTON, Blocks.ACACIA_STAIRS, Blocks.ACACIA_FENCE, Blocks.ACACIA_FENCE_GATE, Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_TRAPDOOR, Blocks.ACACIA_DOOR, Blocks.ACACIA_SIGN, Blocks.ACACIA_WALL_SIGN, Blocks.ACACIA_LEAVES, Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD));
   public static final RegistryObject<RandomVariationMaterial> BIRCH = REGISTRY.register("birch", () -> wood(Blocks.BIRCH_PLANKS, Blocks.BIRCH_SLAB, Blocks.BIRCH_BUTTON, Blocks.BIRCH_STAIRS, Blocks.BIRCH_FENCE, Blocks.BIRCH_FENCE_GATE, Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_TRAPDOOR, Blocks.BIRCH_DOOR, Blocks.BIRCH_SIGN, Blocks.BIRCH_WALL_SIGN, Blocks.BIRCH_LEAVES, Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD));
   public static final RegistryObject<RandomVariationMaterial> CRIMSON = REGISTRY.register("crimson", () -> wood(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_BUTTON, Blocks.CRIMSON_STAIRS, Blocks.CRIMSON_FENCE, Blocks.CRIMSON_FENCE_GATE, Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM, Blocks.CRIMSON_TRAPDOOR, Blocks.CRIMSON_DOOR, Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN, Blocks.CRIMSON_HYPHAE, Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE));
   public static final RegistryObject<RandomVariationMaterial> DARK_OAK = REGISTRY.register("dark_oak", () -> wood(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_BUTTON, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_FENCE_GATE, Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_TRAPDOOR, Blocks.DARK_OAK_DOOR, Blocks.DARK_OAK_SIGN, Blocks.DARK_OAK_WALL_SIGN, Blocks.DARK_OAK_LEAVES, Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD));
   public static final RegistryObject<RandomVariationMaterial> JUNGLE = REGISTRY.register("jungle", () -> wood(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_BUTTON, Blocks.JUNGLE_STAIRS, Blocks.JUNGLE_FENCE, Blocks.JUNGLE_FENCE_GATE, Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_TRAPDOOR, Blocks.JUNGLE_DOOR, Blocks.JUNGLE_SIGN, Blocks.JUNGLE_WALL_SIGN, Blocks.JUNGLE_LEAVES, Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD));
   public static final RegistryObject<RandomVariationMaterial> MANGROVE = REGISTRY.register("mangrove", () -> wood(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_SLAB, Blocks.MANGROVE_BUTTON, Blocks.MANGROVE_STAIRS, Blocks.MANGROVE_FENCE, Blocks.MANGROVE_FENCE_GATE, Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG, Blocks.MANGROVE_TRAPDOOR, Blocks.MANGROVE_DOOR, Blocks.MANGROVE_SIGN, Blocks.MANGROVE_WALL_SIGN, Blocks.MANGROVE_LEAVES, Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD));
   public static final RegistryObject<RandomVariationMaterial> OAK = REGISTRY.register("oak", () -> wood(Blocks.OAK_PLANKS, Blocks.OAK_SLAB, Blocks.OAK_BUTTON, Blocks.OAK_STAIRS, Blocks.OAK_FENCE, Blocks.OAK_FENCE_GATE, Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG, Blocks.OAK_TRAPDOOR, Blocks.OAK_DOOR, Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN, Blocks.OAK_LEAVES, Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD));
   public static final RegistryObject<RandomVariationMaterial> SPRUCE = REGISTRY.register("spruce", () -> wood(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_BUTTON, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_FENCE, Blocks.SPRUCE_FENCE_GATE, Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_TRAPDOOR, Blocks.SPRUCE_DOOR, Blocks.SPRUCE_SIGN, Blocks.SPRUCE_WALL_SIGN, Blocks.SPRUCE_LEAVES, Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD));
   public static final RegistryObject<RandomVariationMaterial> WARPED = REGISTRY.register("warped", () -> wood(Blocks.WARPED_PLANKS, Blocks.WARPED_SLAB, Blocks.WARPED_BUTTON, Blocks.WARPED_STAIRS, Blocks.WARPED_FENCE, Blocks.WARPED_FENCE_GATE, Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM, Blocks.WARPED_TRAPDOOR, Blocks.WARPED_DOOR, Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN, Blocks.WARPED_HYPHAE, Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE));

   public static final RegistryObject<RandomVariationMaterial> BEE_NEST = REGISTRY.register("bee_nest", () -> bee(Blocks.BEE_NEST));
   public static final RegistryObject<RandomVariationMaterial> BEEHIVE = REGISTRY.register("beehive", () -> bee(Blocks.BEEHIVE));

   public static final RegistryObject<RandomVariationMaterial> BLACK = REGISTRY.register("black", () -> colour(Blocks.BLACK_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA, Blocks.BLACK_WOOL, Blocks.BLACK_CONCRETE, Blocks.BLACK_CONCRETE_POWDER, Blocks.BLACK_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS_PANE, Blocks.BLACK_BANNER, Blocks.BLACK_WALL_BANNER, Blocks.BLACK_CANDLE, Blocks.BLACK_CANDLE_CAKE, Blocks.BLACK_BED, Blocks.BLACK_SHULKER_BOX, Blocks.BLACK_CARPET));
   public static final RegistryObject<RandomVariationMaterial> BLUE = REGISTRY.register("blue", () -> colour(Blocks.BLUE_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA, Blocks.BLUE_WOOL, Blocks.BLUE_CONCRETE, Blocks.BLUE_CONCRETE_POWDER, Blocks.BLUE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS_PANE, Blocks.BLUE_BANNER, Blocks.BLUE_WALL_BANNER, Blocks.BLUE_CANDLE, Blocks.BLUE_CANDLE_CAKE, Blocks.BLUE_BED, Blocks.BLUE_SHULKER_BOX, Blocks.BLUE_CARPET));
   public static final RegistryObject<RandomVariationMaterial> BROWN = REGISTRY.register("brown", () -> colour(Blocks.BROWN_TERRACOTTA, Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.BROWN_WOOL, Blocks.BROWN_CONCRETE, Blocks.BROWN_CONCRETE_POWDER, Blocks.BROWN_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS_PANE, Blocks.BROWN_BANNER, Blocks.BROWN_WALL_BANNER, Blocks.BROWN_CANDLE, Blocks.BROWN_CANDLE_CAKE, Blocks.BROWN_BED, Blocks.BROWN_SHULKER_BOX, Blocks.BROWN_CARPET));
   public static final RegistryObject<RandomVariationMaterial> CYAN = REGISTRY.register("cyan", () -> colour(Blocks.CYAN_TERRACOTTA, Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.CYAN_WOOL, Blocks.CYAN_CONCRETE, Blocks.CYAN_CONCRETE_POWDER, Blocks.CYAN_STAINED_GLASS, Blocks.CYAN_STAINED_GLASS_PANE, Blocks.CYAN_BANNER, Blocks.CYAN_WALL_BANNER, Blocks.CYAN_CANDLE, Blocks.CYAN_CANDLE_CAKE, Blocks.CYAN_BED, Blocks.CYAN_SHULKER_BOX, Blocks.CYAN_CARPET));
   public static final RegistryObject<RandomVariationMaterial> GRAY = REGISTRY.register("gray", () -> colour(Blocks.GRAY_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.GRAY_WOOL, Blocks.GRAY_CONCRETE, Blocks.GRAY_CONCRETE_POWDER, Blocks.GRAY_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS_PANE, Blocks.GRAY_BANNER, Blocks.GRAY_WALL_BANNER, Blocks.GRAY_CANDLE, Blocks.GRAY_CANDLE_CAKE, Blocks.GRAY_BED, Blocks.GRAY_SHULKER_BOX, Blocks.GRAY_CARPET));
   public static final RegistryObject<RandomVariationMaterial> GREEN = REGISTRY.register("green", () -> colour(Blocks.GREEN_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.GREEN_WOOL, Blocks.GREEN_CONCRETE, Blocks.GREEN_CONCRETE_POWDER, Blocks.GREEN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS_PANE, Blocks.GREEN_BANNER, Blocks.GREEN_WALL_BANNER, Blocks.GREEN_CANDLE, Blocks.GREEN_CANDLE_CAKE, Blocks.GREEN_BED, Blocks.GREEN_SHULKER_BOX, Blocks.GREEN_CARPET));
   public static final RegistryObject<RandomVariationMaterial> LIGHT_BLUE = REGISTRY.register("light_blue", () -> colour(Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_WOOL, Blocks.LIGHT_BLUE_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, Blocks.LIGHT_BLUE_BANNER, Blocks.LIGHT_BLUE_WALL_BANNER, Blocks.LIGHT_BLUE_CANDLE, Blocks.LIGHT_BLUE_CANDLE_CAKE, Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_BLUE_CARPET));
   public static final RegistryObject<RandomVariationMaterial> LIGHT_GRAY = REGISTRY.register("light_gray", () -> colour(Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_WOOL, Blocks.LIGHT_GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, Blocks.LIGHT_GRAY_BANNER, Blocks.LIGHT_GRAY_WALL_BANNER, Blocks.LIGHT_GRAY_CANDLE, Blocks.LIGHT_GRAY_CANDLE_CAKE, Blocks.LIGHT_GRAY_BED, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIGHT_GRAY_CARPET));
   public static final RegistryObject<RandomVariationMaterial> LIME = REGISTRY.register("lime", () -> colour(Blocks.LIME_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.LIME_WOOL, Blocks.LIME_CONCRETE, Blocks.LIME_CONCRETE_POWDER, Blocks.LIME_STAINED_GLASS, Blocks.LIME_STAINED_GLASS_PANE, Blocks.LIME_BANNER, Blocks.LIME_WALL_BANNER, Blocks.LIME_CANDLE, Blocks.LIME_CANDLE_CAKE, Blocks.LIME_BED, Blocks.LIME_SHULKER_BOX, Blocks.LIME_CARPET));
   public static final RegistryObject<RandomVariationMaterial> MAGENTA = REGISTRY.register("magenta", () -> colour(Blocks.MAGENTA_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.MAGENTA_WOOL, Blocks.MAGENTA_CONCRETE, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.MAGENTA_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS_PANE, Blocks.MAGENTA_BANNER, Blocks.MAGENTA_WALL_BANNER, Blocks.MAGENTA_CANDLE, Blocks.MAGENTA_CANDLE_CAKE, Blocks.MAGENTA_BED, Blocks.MAGENTA_SHULKER_BOX, Blocks.MAGENTA_CARPET));
   public static final RegistryObject<RandomVariationMaterial> ORANGE = REGISTRY.register("orange", () -> colour(Blocks.ORANGE_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.ORANGE_WOOL, Blocks.ORANGE_CONCRETE, Blocks.ORANGE_CONCRETE_POWDER, Blocks.ORANGE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS_PANE, Blocks.ORANGE_BANNER, Blocks.ORANGE_WALL_BANNER, Blocks.ORANGE_CANDLE, Blocks.ORANGE_CANDLE_CAKE, Blocks.ORANGE_BED, Blocks.ORANGE_SHULKER_BOX, Blocks.ORANGE_CARPET));
   public static final RegistryObject<RandomVariationMaterial> PINK = REGISTRY.register("pink", () -> colour(Blocks.PINK_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA, Blocks.PINK_WOOL, Blocks.PINK_CONCRETE, Blocks.PINK_CONCRETE_POWDER, Blocks.PINK_STAINED_GLASS, Blocks.PINK_STAINED_GLASS_PANE, Blocks.PINK_BANNER, Blocks.PINK_WALL_BANNER, Blocks.PINK_CANDLE, Blocks.PINK_CANDLE_CAKE, Blocks.PINK_BED, Blocks.PINK_SHULKER_BOX, Blocks.PINK_CARPET));
   public static final RegistryObject<RandomVariationMaterial> PURPLE = REGISTRY.register("purple", () -> colour(Blocks.PURPLE_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.PURPLE_WOOL, Blocks.PURPLE_CONCRETE, Blocks.PURPLE_CONCRETE_POWDER, Blocks.PURPLE_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS_PANE, Blocks.PURPLE_BANNER, Blocks.PURPLE_WALL_BANNER, Blocks.PURPLE_CANDLE, Blocks.PURPLE_CANDLE_CAKE, Blocks.PURPLE_BED, Blocks.PURPLE_SHULKER_BOX, Blocks.PURPLE_CARPET));
   public static final RegistryObject<RandomVariationMaterial> RED = REGISTRY.register("red", () -> colour(Blocks.RED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA, Blocks.RED_WOOL, Blocks.RED_CONCRETE, Blocks.RED_CONCRETE_POWDER, Blocks.RED_STAINED_GLASS, Blocks.RED_STAINED_GLASS_PANE, Blocks.RED_BANNER, Blocks.RED_WALL_BANNER, Blocks.RED_CANDLE, Blocks.RED_CANDLE_CAKE, Blocks.RED_BED, Blocks.RED_SHULKER_BOX, Blocks.RED_CARPET));
   public static final RegistryObject<RandomVariationMaterial> WHITE = REGISTRY.register("white", () -> colour(Blocks.WHITE_TERRACOTTA, Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.WHITE_WOOL, Blocks.WHITE_CONCRETE, Blocks.WHITE_CONCRETE_POWDER, Blocks.WHITE_STAINED_GLASS, Blocks.WHITE_STAINED_GLASS_PANE, Blocks.WHITE_BANNER, Blocks.WHITE_WALL_BANNER, Blocks.WHITE_CANDLE, Blocks.WHITE_CANDLE_CAKE, Blocks.WHITE_BED, Blocks.WHITE_SHULKER_BOX, Blocks.WHITE_CARPET));
   public static final RegistryObject<RandomVariationMaterial> YELLOW = REGISTRY.register("yellow", () -> colour(Blocks.YELLOW_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.YELLOW_WOOL, Blocks.YELLOW_CONCRETE, Blocks.YELLOW_CONCRETE_POWDER, Blocks.YELLOW_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS_PANE, Blocks.YELLOW_BANNER, Blocks.YELLOW_WALL_BANNER, Blocks.YELLOW_CANDLE, Blocks.YELLOW_CANDLE_CAKE, Blocks.YELLOW_BED, Blocks.YELLOW_SHULKER_BOX, Blocks.YELLOW_CARPET));

   public static final RegistryObject<RandomVariationMaterial> NORMAL_LIGHT = REGISTRY.register("normal_light", () -> light(Blocks.TORCH, Blocks.WALL_TORCH, Blocks.FIRE, Blocks.LANTERN, Blocks.CAMPFIRE));
   public static final RegistryObject<RandomVariationMaterial> SOUL_LIGHT = REGISTRY.register("soul_light", () -> light(Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH, Blocks.SOUL_FIRE, Blocks.SOUL_LANTERN, Blocks.SOUL_CAMPFIRE));

   public static final RegistryObject<RandomVariationMaterial> COAL = REGISTRY.register("coal", () -> ore(Blocks.COAL_BLOCK, Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE));
   public static final RegistryObject<RandomVariationMaterial> DIAMOND = REGISTRY.register("diamond", () -> ore(Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE));
   public static final RegistryObject<RandomVariationMaterial> EMERALD = REGISTRY.register("emerald", () -> ore(Blocks.EMERALD_BLOCK, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE));
   public static final RegistryObject<RandomVariationMaterial> GOLD = REGISTRY.register("gold", () -> ore(Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE));
   public static final RegistryObject<RandomVariationMaterial> IRON = REGISTRY.register("iron", () -> ore(Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE));
   public static final RegistryObject<RandomVariationMaterial> REDSTONE = REGISTRY.register("redstone", () -> ore(Blocks.REDSTONE_BLOCK, Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE));

   public static final RegistryObject<RandomVariationMaterial> ANDESITE = REGISTRY.register("andesite", () -> stone(Blocks.ANDESITE, Blocks.ANDESITE_SLAB, Blocks.ANDESITE_STAIRS, Blocks.ANDESITE_WALL));
   public static final RegistryObject<RandomVariationMaterial> COBBLED_DEEPSLATE = REGISTRY.register("cobbled_deepslate", () -> stone(Blocks.COBBLED_DEEPSLATE, Blocks.COBBLED_DEEPSLATE_SLAB, Blocks.COBBLED_DEEPSLATE_STAIRS, Blocks.COBBLED_DEEPSLATE_WALL));
   public static final RegistryObject<RandomVariationMaterial> COBBLESTONE = REGISTRY.register("cobblestone", () -> stone(Blocks.COBBLESTONE, Blocks.COBBLESTONE_SLAB, Blocks.COBBLESTONE_STAIRS, Blocks.COBBLESTONE_WALL));
   public static final RegistryObject<RandomVariationMaterial> DEEPSLATE_BRICKS = REGISTRY.register("deepslate_bricks", () -> stone(Blocks.DEEPSLATE_BRICKS, Blocks.DEEPSLATE_BRICK_SLAB, Blocks.DEEPSLATE_BRICK_STAIRS, Blocks.DEEPSLATE_BRICK_WALL));
   public static final RegistryObject<RandomVariationMaterial> DEEPSLATE_TILES = REGISTRY.register("deepslate_tiles", () -> stone(Blocks.DEEPSLATE_TILES, Blocks.DEEPSLATE_TILE_SLAB, Blocks.DEEPSLATE_TILE_STAIRS, Blocks.DEEPSLATE_TILE_WALL));
   public static final RegistryObject<RandomVariationMaterial> DIORITE = REGISTRY.register("diorite", () -> stone(Blocks.DIORITE, Blocks.DIORITE_SLAB, Blocks.DIORITE_STAIRS, Blocks.DIORITE_WALL));
   public static final RegistryObject<RandomVariationMaterial> GRANITE = REGISTRY.register("granite", () -> stone(Blocks.GRANITE, Blocks.GRANITE_SLAB, Blocks.GRANITE_STAIRS, Blocks.GRANITE_WALL));
   public static final RegistryObject<RandomVariationMaterial> MOSSY_COBBLESTONE = REGISTRY.register("mossy_cobblestone", () -> stone(Blocks.MOSSY_COBBLESTONE, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.MOSSY_COBBLESTONE_WALL));
   public static final RegistryObject<RandomVariationMaterial> MOSSY_STONE_BRICKS = REGISTRY.register("mossy_stone_bricks", () -> stone(Blocks.MOSSY_STONE_BRICKS, Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.MOSSY_STONE_BRICK_WALL));
   public static final RegistryObject<RandomVariationMaterial> NETHER_BRICKS = REGISTRY.register("nether_bricks", () -> stone(Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_SLAB, Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_BRICK_WALL));
   public static final RegistryObject<RandomVariationMaterial> POLISHED_ANDESITE = REGISTRY.register("polished_andesite", () -> stone(Blocks.POLISHED_ANDESITE, Blocks.POLISHED_ANDESITE_SLAB, Blocks.POLISHED_ANDESITE_STAIRS, Blocks.ANDESITE_WALL));// there is no polished andesite wall
   public static final RegistryObject<RandomVariationMaterial> POLISHED_BLACKSTONE_BRICKS = REGISTRY.register("polished_blackstone_bricks", () -> stone(Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_WALL));
   public static final RegistryObject<RandomVariationMaterial> POLISHED_DEEPSLATE = REGISTRY.register("polished_deepslate", () -> stone(Blocks.POLISHED_DEEPSLATE, Blocks.POLISHED_DEEPSLATE_SLAB, Blocks.POLISHED_DEEPSLATE_STAIRS, Blocks.POLISHED_DEEPSLATE_WALL));
   public static final RegistryObject<RandomVariationMaterial> POLISHED_DIORITE = REGISTRY.register("poslished_diorite", () -> stone(Blocks.POLISHED_DIORITE, Blocks.POLISHED_DIORITE_SLAB, Blocks.POLISHED_DIORITE_STAIRS, Blocks.DIORITE_WALL));// there is no polished diorite wall
   public static final RegistryObject<RandomVariationMaterial> POLISHED_GRANITE = REGISTRY.register("polished_granite", () -> stone(Blocks.POLISHED_GRANITE, Blocks.POLISHED_GRANITE_SLAB, Blocks.POLISHED_GRANITE_STAIRS, Blocks.GRANITE_WALL));// there is no polished granite wall
   public static final RegistryObject<RandomVariationMaterial> RED_NETHER_BRICKS = REGISTRY.register("red_nether_bricks", () -> stone(Blocks.RED_NETHER_BRICKS, Blocks.RED_NETHER_BRICK_SLAB, Blocks.RED_NETHER_BRICK_STAIRS, Blocks.RED_NETHER_BRICK_WALL));
   public static final RegistryObject<RandomVariationMaterial> RED_SANDSTONE = REGISTRY.register("red_sandstone", () -> stone(Blocks.RED_SANDSTONE, Blocks.RED_SANDSTONE_SLAB, Blocks.RED_SANDSTONE_STAIRS, Blocks.RED_SANDSTONE_WALL));
   public static final RegistryObject<RandomVariationMaterial> SANDSTONE = REGISTRY.register("sandstone", () -> stone(Blocks.SANDSTONE, Blocks.SANDSTONE_SLAB, Blocks.SANDSTONE_STAIRS, Blocks.SANDSTONE_WALL));
   public static final RegistryObject<RandomVariationMaterial> STONE_BRICKS = REGISTRY.register("stone_bricks", () -> stone(Blocks.STONE_BRICKS, Blocks.STONE_BRICK_SLAB, Blocks.STONE_BRICK_STAIRS, Blocks.STONE_BRICK_WALL));

   @Contract("_ -> new")
   private static @NotNull RandomVariationMaterial bee(Block bee) {
      return new RandomVariationMaterial(List.of(Pair.of(bee, Shrines.location("bee"))));
   }

   private static RandomVariationMaterial colour(Block terracotta, Block glazedTerracotta, Block wool, Block concrete, Block concrete_powder, Block glass, Block glassPane, Block banner, Block wallBanner, Block candle, Block candleCake, Block bed, Block shulkerBox, Block carpet) {
      return new RandomVariationMaterial(List.of(
            Pair.of(terracotta, Shrines.location("terracotta")),
            Pair.of(glazedTerracotta, Shrines.location("glazed_terracotta")),
            Pair.of(wool, Shrines.location("wool")),
            Pair.of(concrete, Shrines.location("concrete")),
            Pair.of(concrete_powder, Shrines.location("concrete_powder")),
            Pair.of(glass, Shrines.location("glass")),
            Pair.of(glassPane, Shrines.location("glass_pane")),
            Pair.of(banner, Shrines.location("banner")),
            Pair.of(wallBanner, Shrines.location("wall_banner")),
            Pair.of(candle, Shrines.location("candle")),
            Pair.of(candleCake, Shrines.location("candle_cake")),
            Pair.of(bed, Shrines.location("bed")),
            Pair.of(shulkerBox, Shrines.location("shulker_box")),
            Pair.of(carpet, Shrines.location("carpet"))
      ));
   }

   @Contract("_, _, _, _, _ -> new")
   private static @NotNull RandomVariationMaterial light(Block torch, Block wallTorch, Block fire, Block lantern, Block campfire) {
      return new RandomVariationMaterial(List.of(
            Pair.of(torch, Shrines.location("torch")),
            Pair.of(wallTorch, Shrines.location("wall_torch")),
            Pair.of(fire, Shrines.location("fire")),
            Pair.of(lantern, Shrines.location("lantern")),
            Pair.of(campfire, Shrines.location("campfire"))
      ));
   }

   @Contract("_, _, _ -> new")
   private static @NotNull RandomVariationMaterial ore(Block block, Block stone, Block deepslate) {
      return new RandomVariationMaterial(List.of(
            Pair.of(block, Shrines.location("block")),
            Pair.of(stone, Shrines.location("stone")),
            Pair.of(deepslate, Shrines.location("deepslate"))
      ));
   }

   @Contract("_, _, _, _ -> new")
   private static @NotNull RandomVariationMaterial stone(Block stone, Block slab, Block stairs, Block wall) {
      return new RandomVariationMaterial(List.of(
            Pair.of(stone, Shrines.location("block")),
            Pair.of(slab, Shrines.location("slab")),
            Pair.of(stairs, Shrines.location("stairs")),
            Pair.of(wall, Shrines.location("wall"))
      ));
   }

   @Contract("_, _, _, _, _, _, _, _, _, _, _, _, _, _, _ -> new")
   private static @NotNull RandomVariationMaterial wood(Block plank, Block slab, Block button, Block stairs, Block fence, Block fenceGate, Block log, Block strippedLog, Block trapDoor, Block door, Block sign, Block wallSign, Block leaves, Block wood, Block strippedWood) {
      return new RandomVariationMaterial(List.of(
            Pair.of(plank, Shrines.location("block")),
            Pair.of(slab, Shrines.location("slab")),
            Pair.of(button, Shrines.location("button")),
            Pair.of(stairs, Shrines.location("stairs")),
            Pair.of(fence, Shrines.location("fence")),
            Pair.of(fenceGate, Shrines.location("fence_gate")),
            Pair.of(log, Shrines.location("log")),
            Pair.of(strippedLog, Shrines.location("stripped_log")),
            Pair.of(trapDoor, Shrines.location("trapdoor")),
            Pair.of(door, Shrines.location("door")),
            Pair.of(sign, Shrines.location("standing_sign")),
            Pair.of(wallSign, Shrines.location("wall_sign")),
            Pair.of(leaves, Shrines.location("leaves")),
            Pair.of(wood, Shrines.location("wood")),
            Pair.of(strippedWood, Shrines.location("stripped_wood"))
      ));
   }
}