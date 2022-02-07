/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.init;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.worldgen.structures.variation.NewVariationMaterial;
import com.silverminer.shrines.worldgen.structures.variation.NewVariationMaterialElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class VariationMaterialsRegistry {
   public static final DeferredRegister<NewVariationMaterial> VARIATION_MATERIALS = DeferredRegister.create(NewVariationMaterial.class, ShrinesMod.MODID);
   public static final Supplier<IForgeRegistry<NewVariationMaterial>> VARIATION_MATERIALS_REGISTRY = VARIATION_MATERIALS.makeRegistry(
         "variation_materials", RegistryBuilder::new);
   public static final RegistryObject<NewVariationMaterial> OAK = VARIATION_MATERIALS.register("oak",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("plank", new ResourceLocation("oak_planks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("oak_slab")),
               new NewVariationMaterialElement("button", new ResourceLocation("oak_button")),
               new NewVariationMaterialElement("stairs", new ResourceLocation("oak_stairs")),
               new NewVariationMaterialElement("fence", new ResourceLocation("oak_fence")),
               new NewVariationMaterialElement("fence_gate", new ResourceLocation("oak_fence_gate")),
               new NewVariationMaterialElement("log", new ResourceLocation("oak_log")),
               new NewVariationMaterialElement("stripped_log", new ResourceLocation("stripped_oak_log")),
               new NewVariationMaterialElement("trapdoor", new ResourceLocation("oak_trapdoor")),
               new NewVariationMaterialElement("door", new ResourceLocation("oak_door")),
               new NewVariationMaterialElement("standing_sign", new ResourceLocation("oak_sign")),
               new NewVariationMaterialElement("wall_sign", new ResourceLocation("oak_wall_sign")),
               new NewVariationMaterialElement("leaves", new ResourceLocation("oak_leaves")),
               new NewVariationMaterialElement("wood", new ResourceLocation("oak_wood"))
         ), "wood"));
   public static final RegistryObject<NewVariationMaterial> SPRUCE = VARIATION_MATERIALS.register("spruce",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("plank", new ResourceLocation("spruce_planks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("spruce_slab")),
               new NewVariationMaterialElement("button", new ResourceLocation("spruce_button")),
               new NewVariationMaterialElement("stairs", new ResourceLocation("spruce_stairs")),
               new NewVariationMaterialElement("fence", new ResourceLocation("spruce_fence")),
               new NewVariationMaterialElement("fence_gate", new ResourceLocation("spruce_fence_gate")),
               new NewVariationMaterialElement("log", new ResourceLocation("spruce_log")),
               new NewVariationMaterialElement("stripped_log", new ResourceLocation("stripped_spruce_log")),
               new NewVariationMaterialElement("trapdoor", new ResourceLocation("spruce_trapdoor")),
               new NewVariationMaterialElement("door", new ResourceLocation("spruce_door")),
               new NewVariationMaterialElement("standing_sign", new ResourceLocation("spruce_sign")),
               new NewVariationMaterialElement("wall_sign", new ResourceLocation("spruce_wall_sign")),
               new NewVariationMaterialElement("leaves", new ResourceLocation("spruce_leaves")),
               new NewVariationMaterialElement("wood", new ResourceLocation("spruce_wood"))
         ), "wood"));
   public static final RegistryObject<NewVariationMaterial> DARK_OAK = VARIATION_MATERIALS.register("dark_oak",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("plank", new ResourceLocation("dark_oak_planks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("dark_oak_slab")),
               new NewVariationMaterialElement("button", new ResourceLocation("dark_oak_button")),
               new NewVariationMaterialElement("stairs", new ResourceLocation("dark_oak_stairs")),
               new NewVariationMaterialElement("fence", new ResourceLocation("dark_oak_fence")),
               new NewVariationMaterialElement("fence_gate", new ResourceLocation("dark_oak_fence_gate")),
               new NewVariationMaterialElement("log", new ResourceLocation("dark_oak_log")),
               new NewVariationMaterialElement("stripped_log", new ResourceLocation("stripped_dark_oak_log")),
               new NewVariationMaterialElement("trapdoor", new ResourceLocation("dark_oak_trapdoor")),
               new NewVariationMaterialElement("door", new ResourceLocation("dark_oak_door")),
               new NewVariationMaterialElement("standing_sign", new ResourceLocation("dark_oak_sign")),
               new NewVariationMaterialElement("wall_sign", new ResourceLocation("dark_oak_wall_sign")),
               new NewVariationMaterialElement("leaves", new ResourceLocation("dark_oak_leaves")),
               new NewVariationMaterialElement("wood", new ResourceLocation("dark_oak_wood"))
         ), "wood"));
   public static final RegistryObject<NewVariationMaterial> BIRCH = VARIATION_MATERIALS.register("birch",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("plank", new ResourceLocation("birch_planks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("birch_slab")),
               new NewVariationMaterialElement("button", new ResourceLocation("birch_button")),
               new NewVariationMaterialElement("stairs", new ResourceLocation("birch_stairs")),
               new NewVariationMaterialElement("fence", new ResourceLocation("birch_fence")),
               new NewVariationMaterialElement("fence_gate", new ResourceLocation("birch_fence_gate")),
               new NewVariationMaterialElement("log", new ResourceLocation("birch_log")),
               new NewVariationMaterialElement("stripped_log", new ResourceLocation("stripped_birch_log")),
               new NewVariationMaterialElement("trapdoor", new ResourceLocation("birch_trapdoor")),
               new NewVariationMaterialElement("door", new ResourceLocation("birch_door")),
               new NewVariationMaterialElement("standing_sign", new ResourceLocation("birch_sign")),
               new NewVariationMaterialElement("wall_sign", new ResourceLocation("birch_wall_sign")),
               new NewVariationMaterialElement("leaves", new ResourceLocation("birch_leaves")),
               new NewVariationMaterialElement("wood", new ResourceLocation("birch_wood"))
         ), "wood"));
   public static final RegistryObject<NewVariationMaterial> ACACIA = VARIATION_MATERIALS.register("acacia",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("plank", new ResourceLocation("acacia_planks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("acacia_slab")),
               new NewVariationMaterialElement("button", new ResourceLocation("acacia_button")),
               new NewVariationMaterialElement("stairs", new ResourceLocation("acacia_stairs")),
               new NewVariationMaterialElement("fence", new ResourceLocation("acacia_fence")),
               new NewVariationMaterialElement("fence_gate", new ResourceLocation("acacia_fence_gate")),
               new NewVariationMaterialElement("log", new ResourceLocation("acacia_log")),
               new NewVariationMaterialElement("stripped_log", new ResourceLocation("stripped_acacia_log")),
               new NewVariationMaterialElement("trapdoor", new ResourceLocation("acacia_trapdoor")),
               new NewVariationMaterialElement("door", new ResourceLocation("acacia_door")),
               new NewVariationMaterialElement("standing_sign", new ResourceLocation("acacia_sign")),
               new NewVariationMaterialElement("wall_sign", new ResourceLocation("acacia_wall_sign")),
               new NewVariationMaterialElement("leaves", new ResourceLocation("acacia_leaves")),
               new NewVariationMaterialElement("wood", new ResourceLocation("acacia_wood"))
         ), "wood"));
   public static final RegistryObject<NewVariationMaterial> JUNGLE = VARIATION_MATERIALS.register("jungle",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("plank", new ResourceLocation("jungle_planks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("jungle_slab")),
               new NewVariationMaterialElement("button", new ResourceLocation("jungle_button")),
               new NewVariationMaterialElement("stairs", new ResourceLocation("jungle_stairs")),
               new NewVariationMaterialElement("fence", new ResourceLocation("jungle_fence")),
               new NewVariationMaterialElement("fence_gate", new ResourceLocation("jungle_fence_gate")),
               new NewVariationMaterialElement("log", new ResourceLocation("jungle_log")),
               new NewVariationMaterialElement("stripped_log", new ResourceLocation("stripped_jungle_log")),
               new NewVariationMaterialElement("trapdoor", new ResourceLocation("jungle_trapdoor")),
               new NewVariationMaterialElement("door", new ResourceLocation("jungle_door")),
               new NewVariationMaterialElement("standing_sign", new ResourceLocation("jungle_sign")),
               new NewVariationMaterialElement("wall_sign", new ResourceLocation("jungle_wall_sign")),
               new NewVariationMaterialElement("leaves", new ResourceLocation("jungle_leaves")),
               new NewVariationMaterialElement("wood", new ResourceLocation("jungle_wood"))
         ), "wood"));
   public static final RegistryObject<NewVariationMaterial> CRIMSON = VARIATION_MATERIALS.register("crimson",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("plank", new ResourceLocation("crimson_planks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("crimson_slab")),
               new NewVariationMaterialElement("button", new ResourceLocation("crimson_button")),
               new NewVariationMaterialElement("stairs", new ResourceLocation("crimson_stairs")),
               new NewVariationMaterialElement("fence", new ResourceLocation("crimson_fence")),
               new NewVariationMaterialElement("fence_gate", new ResourceLocation("crimson_fence_gate")),
               new NewVariationMaterialElement("log", new ResourceLocation("crimson_stem")),
               new NewVariationMaterialElement("stripped_log", new ResourceLocation("stripped_crimson_stem")),
               new NewVariationMaterialElement("trapdoor", new ResourceLocation("crimson_trapdoor")),
               new NewVariationMaterialElement("door", new ResourceLocation("crimson_door")),
               new NewVariationMaterialElement("standing_sign", new ResourceLocation("crimson_sign")),
               new NewVariationMaterialElement("wall_sign", new ResourceLocation("crimson_wall_sign")),
               new NewVariationMaterialElement("leaves", new ResourceLocation("crimson_hyphae")),
               new NewVariationMaterialElement("wood", new ResourceLocation("stripped_crimson_hyphae"))
         ), "wood"));
   public static final RegistryObject<NewVariationMaterial> WARPED = VARIATION_MATERIALS.register("warped",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("plank", new ResourceLocation("warped_planks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("warped_slab")),
               new NewVariationMaterialElement("button", new ResourceLocation("warped_button")),
               new NewVariationMaterialElement("stairs", new ResourceLocation("warped_stairs")),
               new NewVariationMaterialElement("fence", new ResourceLocation("warped_fence")),
               new NewVariationMaterialElement("fence_gate", new ResourceLocation("warped_fence_gate")),
               new NewVariationMaterialElement("log", new ResourceLocation("warped_stem")),
               new NewVariationMaterialElement("stripped_log", new ResourceLocation("stripped_warped_stem")),
               new NewVariationMaterialElement("trapdoor", new ResourceLocation("warped_trapdoor")),
               new NewVariationMaterialElement("door", new ResourceLocation("warped_door")),
               new NewVariationMaterialElement("standing_sign", new ResourceLocation("warped_sign")),
               new NewVariationMaterialElement("wall_sign", new ResourceLocation("warped_wall_sign")),
               new NewVariationMaterialElement("leaves", new ResourceLocation("warped_hyphae")),
               new NewVariationMaterialElement("wood", new ResourceLocation("stripped_warped_hyphae"))
         ), "wood"));
   public static final RegistryObject<NewVariationMaterial> WHITE = VARIATION_MATERIALS.register("white",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("white_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("white_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("white_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("white_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("white_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("white_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("white_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("white_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("white_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("white_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("white_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("white_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("white_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("white_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> ORANGE = VARIATION_MATERIALS.register("orange",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("orange_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("orange_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("orange_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("orange_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("orange_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("orange_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("orange_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("orange_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("orange_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("orange_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("orange_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("orange_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("orange_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("orange_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> MAGENTA = VARIATION_MATERIALS.register("magenta",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("magenta_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("magenta_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("magenta_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("magenta_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("magenta_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("magenta_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("magenta_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("magenta_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("magenta_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("magenta_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("magenta_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("magenta_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("magenta_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("magenta_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> LIGHT_BLUE = VARIATION_MATERIALS.register("light_blue",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("light_blue_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("light_blue_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("light_blue_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("light_blue_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("light_blue_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("light_blue_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("light_blue_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("light_blue_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("light_blue_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("light_blue_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("light_blue_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("light_blue_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("light_blue_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("light_blue_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> YELLOW = VARIATION_MATERIALS.register("yellow",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("yellow_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("yellow_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("yellow_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("yellow_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("yellow_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("yellow_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("yellow_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("yellow_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("yellow_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("yellow_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("yellow_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("yellow_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("yellow_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("yellow_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> LIME = VARIATION_MATERIALS.register("lime",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("lime_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("lime_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("lime_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("lime_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("lime_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("lime_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("lime_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("lime_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("lime_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("lime_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("lime_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("lime_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("lime_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("lime_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> PINK = VARIATION_MATERIALS.register("pink",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("pink_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("pink_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("pink_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("pink_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("pink_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("pink_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("pink_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("pink_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("pink_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("pink_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("pink_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("pink_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("pink_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("pink_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> GRAY = VARIATION_MATERIALS.register("gray",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("gray_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("gray_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("gray_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("gray_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("gray_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("gray_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("gray_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("gray_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("gray_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("gray_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("gray_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("gray_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("gray_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("gray_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> LIGHT_GRAY = VARIATION_MATERIALS.register("light_gray",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("light_gray_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("light_gray_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("light_gray_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("light_gray_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("light_gray_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("light_gray_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("light_gray_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("light_gray_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("light_gray_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("light_gray_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("light_gray_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("light_gray_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("light_gray_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("light_gray_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> BLACK = VARIATION_MATERIALS.register("black",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("black_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("black_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("black_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("black_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("black_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("black_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("black_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("black_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("black_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("black_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("black_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("black_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("black_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("black_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> CYAN = VARIATION_MATERIALS.register("cyan",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("cyan_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("cyan_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("cyan_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("cyan_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("cyan_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("cyan_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("cyan_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("cyan_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("cyan_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("cyan_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("cyan_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("cyan_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("cyan_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("cyan_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> PURPLE = VARIATION_MATERIALS.register("purple",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("purple_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("purple_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("purple_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("purple_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("purple_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("purple_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("purple_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("purple_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("purple_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("purple_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("purple_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("purple_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("purple_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("purple_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> BLUE = VARIATION_MATERIALS.register("blue",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("blue_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("blue_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("blue_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("blue_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("blue_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("blue_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("blue_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("blue_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("blue_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("blue_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("blue_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("blue_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("blue_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("blue_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> BROWN = VARIATION_MATERIALS.register("brown",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("brown_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("brown_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("brown_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("brown_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("brown_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("brown_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("brown_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("brown_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("brown_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("brown_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("brown_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("brown_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("brown_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("brown_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> GREEN = VARIATION_MATERIALS.register("green",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("green_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("green_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("green_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("green_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("green_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("green_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("green_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("green_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("green_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("green_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("green_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("green_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("green_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("green_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> RED = VARIATION_MATERIALS.register("red",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("red_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("red_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("red_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("red_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("red_concrete_powder")),
               new NewVariationMaterialElement("glass", new ResourceLocation("red_glass")),
               new NewVariationMaterialElement("glass_pane", new ResourceLocation("red_glass_pane")),
               new NewVariationMaterialElement("banner", new ResourceLocation("red_banner")),
               new NewVariationMaterialElement("wall_banner", new ResourceLocation("red_wall_banner")),
               new NewVariationMaterialElement("candle", new ResourceLocation("red_candle")),
               new NewVariationMaterialElement("candle_cake", new ResourceLocation("red_candle_cake")),
               new NewVariationMaterialElement("bed", new ResourceLocation("red_bed")),
               new NewVariationMaterialElement("shulker_box", new ResourceLocation("red_shulker_box")),
               new NewVariationMaterialElement("carpet", new ResourceLocation("red_carpet"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> COAL = VARIATION_MATERIALS.register("coal",
         () -> new NewVariationMaterial(List.of(
               new NewVariationMaterialElement("normal", new ResourceLocation("coal_ore")),
               new NewVariationMaterialElement("deepslate", new ResourceLocation("deepslate_coal_ore"))
         ), "ores"));
   public static final RegistryObject<NewVariationMaterial> IRON = VARIATION_MATERIALS.register("iron",
         () -> new NewVariationMaterial(List.of(
               new NewVariationMaterialElement("normal", new ResourceLocation("iron_ore")),
               new NewVariationMaterialElement("deepslate", new ResourceLocation("deepslate_iron_ore"))
         ), "ores"));
   public static final RegistryObject<NewVariationMaterial> GOLD = VARIATION_MATERIALS.register("gold",
         () -> new NewVariationMaterial(List.of(
               new NewVariationMaterialElement("normal", new ResourceLocation("gold_ore")),
               new NewVariationMaterialElement("deepslate", new ResourceLocation("deepslate_gold_ore"))
         ), "ores"));
   public static final RegistryObject<NewVariationMaterial> DIAMOND = VARIATION_MATERIALS.register("diamond",
         () -> new NewVariationMaterial(List.of(
               new NewVariationMaterialElement("normal", new ResourceLocation("diamond_ore")),
               new NewVariationMaterialElement("deepslate", new ResourceLocation("deepslate_diamond_ore"))
         ), "ores"));
   public static final RegistryObject<NewVariationMaterial> REDSTONE = VARIATION_MATERIALS.register("redstone",
         () -> new NewVariationMaterial(List.of(
               new NewVariationMaterialElement("normal", new ResourceLocation("redstone_ore")),
               new NewVariationMaterialElement("deepslate", new ResourceLocation("deepslate_redstone_ore"))
         ), "ores"));
   public static final RegistryObject<NewVariationMaterial> EMERALD = VARIATION_MATERIALS.register("emerald",
         () -> new NewVariationMaterial(List.of(
               new NewVariationMaterialElement("normal", new ResourceLocation("emerald_ore")),
               new NewVariationMaterialElement("deepslate", new ResourceLocation("deepslate_emerald_ore"))
         ), "ores"));
   public static final RegistryObject<NewVariationMaterial> BEEHIVE = VARIATION_MATERIALS.register("beehive",
         () -> new NewVariationMaterial(List.of(
               new NewVariationMaterialElement("beehive", new ResourceLocation("beehive"))
         ), "bees"));
   public static final RegistryObject<NewVariationMaterial> BEE_NEST = VARIATION_MATERIALS.register("bee_nest",
         () -> new NewVariationMaterial(List.of(
               new NewVariationMaterialElement("bee_nest", new ResourceLocation("bee_nest"))
         ), "bees"));
   public static final RegistryObject<NewVariationMaterial> COBBLESTONE = VARIATION_MATERIALS.register("cobblestone",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("cobblestone")),
               new NewVariationMaterialElement("slab", new ResourceLocation("cobblestone_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("cobblestone_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("cobblestone_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> STONE_BRICKS = VARIATION_MATERIALS.register("stone_bricks",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("stone_bricks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("stone_brick_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("stone_brick_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("stone_brick_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> POLISHED_BLACKSTONE_BRICKS = VARIATION_MATERIALS.register("polished_blackstone_bricks",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("polished_blackstone_bricks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("polished_blackstone_brick_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("polished_blackstone_brick_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("polished_blackstone_brick_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> MOSSY_COBBLESTONE = VARIATION_MATERIALS.register("mossy_cobblestone",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("mossy_cobblestone")),
               new NewVariationMaterialElement("slab", new ResourceLocation("mossy_cobblestone_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("mossy_cobblestone_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("mossy_cobblestone_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> MOSSY_STONE_BRICKS = VARIATION_MATERIALS.register("mossy_stone_bricks",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("mossy_stone_bricks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("mossy_stone_brick_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("mossy_stone_brick_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("mossy_stone_brick_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> SANDSTONE = VARIATION_MATERIALS.register("sandstone",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("sandstone")),
               new NewVariationMaterialElement("slab", new ResourceLocation("sandstone_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("sandstone_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("sandstone_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> DIORITE = VARIATION_MATERIALS.register("diorite",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("diorite")),
               new NewVariationMaterialElement("slab", new ResourceLocation("diorite_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("diorite_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("diorite_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> POLISHED_DIORITE = VARIATION_MATERIALS.register("polished_diorite",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("polished_diorite")),
               new NewVariationMaterialElement("slab", new ResourceLocation("polished_diorite_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("polished_diorite_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("polished_diorite_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> GRANITE = VARIATION_MATERIALS.register("granite",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("granite")),
               new NewVariationMaterialElement("slab", new ResourceLocation("granite_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("granite_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("granite_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> POLISHED_GRANITE = VARIATION_MATERIALS.register("polished_granite",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("polished_granite")),
               new NewVariationMaterialElement("slab", new ResourceLocation("polished_granite_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("polished_granite_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("polished_granite_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> ANDESITE = VARIATION_MATERIALS.register("andesite",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("andesite")),
               new NewVariationMaterialElement("slab", new ResourceLocation("andesite_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("andesite_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("andesite_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> POLISHED_ANDESITE= VARIATION_MATERIALS.register("polished_andesite",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("polished_andesite")),
               new NewVariationMaterialElement("slab", new ResourceLocation("polished_andesite_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("polished_andesite_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("polished_andesite_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> COBBLED_DEEPSLATE = VARIATION_MATERIALS.register("cobbled_deepslate",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("cobbled_deepslate")),
               new NewVariationMaterialElement("slab", new ResourceLocation("cobbled_deepslate_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("cobbled_deepslate_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("cobbled_deepslate_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> POLISHED_DEEPSLATE = VARIATION_MATERIALS.register("polished_deepslate",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("polished_deepslate")),
               new NewVariationMaterialElement("slab", new ResourceLocation("polished_deepslate_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("polished_deepslate_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("polished_deepslate_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> DEEPSLATE_BRICKS = VARIATION_MATERIALS.register("deepslate_bricks",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("deepslate_bricks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("deepslate_brick_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("deepslate_brick_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("deepslate_brick_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> DEEPSLATE_TILES = VARIATION_MATERIALS.register("deepslate_tiles",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("deepslate_tiles")),
               new NewVariationMaterialElement("slab", new ResourceLocation("deepslate_tile_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("deepslate_tile_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("deepslate_tile_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> RED_NETHER_BRICKS = VARIATION_MATERIALS.register("red_nether_bricks",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("red_nether_bricks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("red_nether_brick_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("red_nether_brick_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("red_nether_brick_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> NETHER_BRICKS = VARIATION_MATERIALS.register("nether_bricks",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("nether_bricks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("nether_brick_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("nether_brick_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("nether_brick_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> RED_SANDSTONE = VARIATION_MATERIALS.register("red_sandstone",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("red_sandstone")),
               new NewVariationMaterialElement("slab", new ResourceLocation("red_sandstone_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("red_sandstone_stairs")),
               new NewVariationMaterialElement("wall", new ResourceLocation("red_sandstone_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> NORMAL_LIGHT = VARIATION_MATERIALS.register("normal_light",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("torch", new ResourceLocation("torch")),
               new NewVariationMaterialElement("wall_torch", new ResourceLocation("wall_torch")),
               new NewVariationMaterialElement("fire", new ResourceLocation("fire")),
               new NewVariationMaterialElement("lantern", new ResourceLocation("lantern")),
               new NewVariationMaterialElement("campfire", new ResourceLocation("campfire"))
         ), "light"));
   public static final RegistryObject<NewVariationMaterial> SOUL_LIGHT = VARIATION_MATERIALS.register("soul_light",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("torch", new ResourceLocation("soul_torch")),
               new NewVariationMaterialElement("wall_torch", new ResourceLocation("soul_wall_torch")),
               new NewVariationMaterialElement("fire", new ResourceLocation("soul_fire")),
               new NewVariationMaterialElement("lantern", new ResourceLocation("soul_lantern")),
               new NewVariationMaterialElement("campfire", new ResourceLocation("soul_campfire"))
         ), "light"));
}