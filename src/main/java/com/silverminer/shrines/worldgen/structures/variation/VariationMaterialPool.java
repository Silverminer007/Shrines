package com.silverminer.shrines.worldgen.structures.variation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.silverminer.shrines.packages.datacontainer.SimpleVariationConfiguration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class VariationMaterialPool {
   private static final NestedVariationMaterial OAK = new NestedVariationMaterial(Blocks.OAK_PLANKS, Blocks.OAK_SLAB, Blocks.OAK_BUTTON, Blocks.OAK_STAIRS, Blocks.OAK_FENCE,
         Blocks.OAK_FENCE_GATE,
         Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG, Blocks.OAK_TRAPDOOR, Blocks.OAK_DOOR, Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN);

   private static final NestedVariationMaterial SPRUCE = new NestedVariationMaterial(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_BUTTON, Blocks.SPRUCE_STAIRS,
         Blocks.SPRUCE_FENCE, Blocks.SPRUCE_FENCE_GATE,
         Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_TRAPDOOR, Blocks.SPRUCE_DOOR, Blocks.SPRUCE_SIGN, Blocks.SPRUCE_WALL_SIGN);

   private static final NestedVariationMaterial DARK_OAK = new NestedVariationMaterial(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_BUTTON,
         Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_FENCE_GATE,
         Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_TRAPDOOR, Blocks.DARK_OAK_DOOR, Blocks.DARK_OAK_SIGN, Blocks.DARK_OAK_WALL_SIGN);

   private static final NestedVariationMaterial BIRCH = new NestedVariationMaterial(Blocks.BIRCH_PLANKS, Blocks.BIRCH_SLAB, Blocks.BIRCH_BUTTON, Blocks.BIRCH_STAIRS,
         Blocks.BIRCH_FENCE, Blocks.SPRUCE_FENCE_GATE,
         Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_TRAPDOOR, Blocks.BIRCH_DOOR, Blocks.BIRCH_SIGN, Blocks.BIRCH_WALL_SIGN);

   private static final NestedVariationMaterial ACACIA = new NestedVariationMaterial(Blocks.ACACIA_PLANKS, Blocks.ACACIA_SLAB, Blocks.ACACIA_BUTTON, Blocks.ACACIA_STAIRS,
         Blocks.ACACIA_FENCE, Blocks.ACACIA_FENCE_GATE,
         Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_TRAPDOOR, Blocks.ACACIA_DOOR, Blocks.ACACIA_SIGN, Blocks.ACACIA_WALL_SIGN);

   private static final NestedVariationMaterial JUNGLE = new NestedVariationMaterial(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_BUTTON, Blocks.JUNGLE_STAIRS,
         Blocks.JUNGLE_FENCE, Blocks.JUNGLE_FENCE_GATE,
         Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_TRAPDOOR, Blocks.JUNGLE_DOOR, Blocks.JUNGLE_SIGN, Blocks.JUNGLE_WALL_SIGN);

   public static final List<? extends VariationMaterial> WOOD = ImmutableList.of(OAK, SPRUCE, DARK_OAK, BIRCH, ACACIA, JUNGLE);

   public static final List<? extends VariationMaterial> TERRACOTTA = createSimpleVariation(SimpleVariationConfiguration::isTerracottaEnabled, Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA,
         Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA,
         Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA,
         Blocks.BLACK_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA,
         Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA);

   public static final List<? extends VariationMaterial> GLAZED_TERRACOTTA = createSimpleVariation(SimpleVariationConfiguration::isGlazedTerracottaEnabled, Blocks.WHITE_GLAZED_TERRACOTTA,
         Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA,
         Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA,
         Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA,
         Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA,
         Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA);

   public static final List<? extends VariationMaterial> WOOL = createWoolVariation(
         Lists.newArrayList(Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL,
               Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.YELLOW_WOOL, Blocks.LIME_WOOL, Blocks.PINK_WOOL,
               Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL, Blocks.BLACK_WOOL, Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL,
               Blocks.BLUE_WOOL, Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL),
         Lists.newArrayList(Blocks.WHITE_CARPET, Blocks.ORANGE_CARPET,
               Blocks.MAGENTA_CARPET, Blocks.LIGHT_BLUE_CARPET, Blocks.YELLOW_CARPET, Blocks.LIME_CARPET, Blocks.PINK_CARPET,
               Blocks.GRAY_CARPET, Blocks.LIGHT_GRAY_CARPET, Blocks.BLACK_CARPET, Blocks.CYAN_CARPET, Blocks.PURPLE_CARPET,
               Blocks.BLUE_CARPET, Blocks.BROWN_CARPET, Blocks.GREEN_CARPET, Blocks.RED_CARPET));

   public static final List<? extends VariationMaterial> CONCRETE = createSimpleVariation(SimpleVariationConfiguration::isConcreteEnabled, Blocks.WHITE_CONCRETE, Blocks.ORANGE_CONCRETE,
         Blocks.MAGENTA_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE, Blocks.YELLOW_CONCRETE, Blocks.LIME_CONCRETE,
         Blocks.PINK_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE, Blocks.BLACK_CONCRETE,
         Blocks.CYAN_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.BLUE_CONCRETE, Blocks.BROWN_CONCRETE,
         Blocks.GREEN_CONCRETE, Blocks.RED_CONCRETE);

   public static final List<? extends VariationMaterial> CONCRETE_POWDER = createSimpleVariation(SimpleVariationConfiguration::isConcretePowderEnabled, Blocks.WHITE_CONCRETE_POWDER,
         Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER,
         Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER,
         Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER,
         Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER,
         Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER);

   public static final List<? extends VariationMaterial> ORE = createSimpleVariation(SimpleVariationConfiguration::areOresEnabled, Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE,
         Blocks.DIAMOND_ORE, Blocks.REDSTONE_ORE, Blocks.EMERALD_ORE);

   public static final List<? extends VariationMaterial> BEES = createSimpleVariation(SimpleVariationConfiguration::areBeesEnabled, Blocks.BEEHIVE, Blocks.BEE_NEST);

   private static final NestedVariationMaterial COBBLESTONE = new NestedVariationMaterial(Blocks.COBBLESTONE, Blocks.COBBLESTONE_SLAB, Blocks.COBBLESTONE_STAIRS, Blocks.COBBLESTONE_WALL);

   private static final NestedVariationMaterial STONE_BRICKS = new NestedVariationMaterial(Blocks.STONE_BRICKS, Blocks.STONE_BRICK_SLAB, Blocks.STONE_BRICK_STAIRS,
         Blocks.STONE_BRICK_WALL);

   private static final NestedVariationMaterial POLISHED_BLACKSTONE_BRICKS = new NestedVariationMaterial(Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB,
         Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_WALL);

   private static final NestedVariationMaterial MOSSY_COBBLESTONE = new NestedVariationMaterial(Blocks.MOSSY_COBBLESTONE, Blocks.MOSSY_COBBLESTONE_SLAB,
         Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.MOSSY_COBBLESTONE_WALL);

   private static final NestedVariationMaterial MOSSY_STONE_BRICKS = new NestedVariationMaterial(Blocks.MOSSY_STONE_BRICKS, Blocks.MOSSY_STONE_BRICK_SLAB,
         Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.MOSSY_STONE_BRICK_WALL);

   private static final NestedVariationMaterial SANDSTONE = new NestedVariationMaterial(Blocks.SANDSTONE, Blocks.SANDSTONE_SLAB,
         Blocks.SANDSTONE_STAIRS, Blocks.SANDSTONE_WALL);

   public static final List<? extends VariationMaterial> STONE = ImmutableList.of(COBBLESTONE, STONE_BRICKS, POLISHED_BLACKSTONE_BRICKS, MOSSY_COBBLESTONE, MOSSY_STONE_BRICKS,
         SANDSTONE);

   private static List<? extends VariationMaterial> createSimpleVariation(Function<SimpleVariationConfiguration, Boolean> property, Block... blocks) {
      return Arrays.stream(blocks).map(block -> new SimpleVariationMaterial(block, property)).toList();
   }

   private static @NotNull List<? extends VariationMaterial> createWoolVariation(@NotNull List<Block> wool, @NotNull List<Block> carpets) {
      if (wool.size() == carpets.size()) {
         List<VariationMaterial> variationMaterialList = new ArrayList<>();
         for (int i = 0; i < wool.size(); i++) {
            variationMaterialList.add(new NestedVariationMaterial(wool.get(i), carpets.get(i)));
         }
         return variationMaterialList;
      }
      return ImmutableList.of();
   }
}