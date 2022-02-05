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
               new NewVariationMaterialElement("wall_sign", new ResourceLocation("oak_wall_sign"))
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
               new NewVariationMaterialElement("wall_sign", new ResourceLocation("spruce_wall_sign"))
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
               new NewVariationMaterialElement("wall_sign", new ResourceLocation("dark_oak_wall_sign"))
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
               new NewVariationMaterialElement("wall_sign", new ResourceLocation("birch_wall_sign"))
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
               new NewVariationMaterialElement("wall_sign", new ResourceLocation("acacia_wall_sign"))
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
               new NewVariationMaterialElement("wall_sign", new ResourceLocation("jungle_wall_sign"))
         ), "wood"));
   public static final RegistryObject<NewVariationMaterial> WHITE = VARIATION_MATERIALS.register("white",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("white_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("white_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("white_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("white_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("white_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> ORANGE = VARIATION_MATERIALS.register("orange",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("orange_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("orange_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("orange_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("orange_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("orange_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> MAGENTA = VARIATION_MATERIALS.register("magenta",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("magenta_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("magenta_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("magenta_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("magenta_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("magenta_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> LIGHT_BLUE = VARIATION_MATERIALS.register("light_blue",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("light_blue_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("light_blue_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("light_blue_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("light_blue_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("light_blue_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> YELLOW = VARIATION_MATERIALS.register("yellow",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("yellow_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("yellow_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("yellow_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("yellow_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("yellow_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> LIME = VARIATION_MATERIALS.register("lime",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("lime_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("lime_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("lime_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("lime_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("lime_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> PINK = VARIATION_MATERIALS.register("pink",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("pink_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("pink_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("pink_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("pink_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("pink_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> GRAY = VARIATION_MATERIALS.register("gray",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("gray_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("gray_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("gray_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("gray_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("gray_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> LIGHT_GRAY = VARIATION_MATERIALS.register("light_gray",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("light_gray_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("light_gray_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("light_gray_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("light_gray_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("light_gray_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> BLACK = VARIATION_MATERIALS.register("black",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("black_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("black_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("black_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("black_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("black_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> CYAN = VARIATION_MATERIALS.register("cyan",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("cyan_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("cyan_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("cyan_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("cyan_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("cyan_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> PURPLE = VARIATION_MATERIALS.register("purple",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("purple_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("purple_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("purple_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("purple_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("purple_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> BLUE = VARIATION_MATERIALS.register("blue",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("blue_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("blue_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("blue_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("blue_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("blue_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> BROWN = VARIATION_MATERIALS.register("brown",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("brown_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("brown_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("brown_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("brown_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("brown_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> GREEN = VARIATION_MATERIALS.register("green",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("green_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("green_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("green_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("green_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("green_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> RED = VARIATION_MATERIALS.register("red",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("terracotta", new ResourceLocation("red_terracotta")),
               new NewVariationMaterialElement("glazed_terracotta", new ResourceLocation("red_glazed_terracotta")),
               new NewVariationMaterialElement("wool", new ResourceLocation("red_wool")),
               new NewVariationMaterialElement("concrete", new ResourceLocation("red_concrete")),
               new NewVariationMaterialElement("concrete_powder", new ResourceLocation("red_concrete_powder"))
         ), "color"));
   public static final RegistryObject<NewVariationMaterial> COAL = VARIATION_MATERIALS.register("coal",
         () -> new NewVariationMaterial(List.of(
               new NewVariationMaterialElement("coal", new ResourceLocation("coal_ore"))
         ), "ores"));
   public static final RegistryObject<NewVariationMaterial> IRON = VARIATION_MATERIALS.register("iron",
         () -> new NewVariationMaterial(List.of(
               new NewVariationMaterialElement("iron", new ResourceLocation("iron_ore"))
         ), "ores"));
   public static final RegistryObject<NewVariationMaterial> GOLD = VARIATION_MATERIALS.register("gold",
         () -> new NewVariationMaterial(List.of(
               new NewVariationMaterialElement("gold", new ResourceLocation("gold_ore"))
         ), "ores"));
   public static final RegistryObject<NewVariationMaterial> DIAMOND = VARIATION_MATERIALS.register("diamond",
         () -> new NewVariationMaterial(List.of(
               new NewVariationMaterialElement("diamond", new ResourceLocation("diamond_ore"))
         ), "ores"));
   public static final RegistryObject<NewVariationMaterial> REDSTONE = VARIATION_MATERIALS.register("redstone",
         () -> new NewVariationMaterial(List.of(
               new NewVariationMaterialElement("redstone", new ResourceLocation("redstone_ore"))
         ), "ores"));
   public static final RegistryObject<NewVariationMaterial> EMERALD = VARIATION_MATERIALS.register("emerald",
         () -> new NewVariationMaterial(List.of(
               new NewVariationMaterialElement("emerald", new ResourceLocation("emerald_ore"))
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
               new NewVariationMaterialElement("stair", new ResourceLocation("cobblestone_stair")),
               new NewVariationMaterialElement("wall", new ResourceLocation("cobblestone_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> STONE_BRICKS = VARIATION_MATERIALS.register("stone_bricks",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("stone_bricks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("stone_bricks_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("stone_bricks_stair")),
               new NewVariationMaterialElement("wall", new ResourceLocation("stone_bricks_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> POLISHED_BLACKSTONE_BRICKS = VARIATION_MATERIALS.register("polished_blackstone_bricks",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("polished_blackstone_bricks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("polished_blackstone_bricks_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("polished_blackstone_bricks_stair")),
               new NewVariationMaterialElement("wall", new ResourceLocation("polished_blackstone_bricks_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> MOSSY_COBBLESTONE = VARIATION_MATERIALS.register("mossy_cobblestone",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("mossy_cobblestone")),
               new NewVariationMaterialElement("slab", new ResourceLocation("mossy_cobblestone_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("mossy_cobblestone_stair")),
               new NewVariationMaterialElement("wall", new ResourceLocation("mossy_cobblestone_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> MOSSY_STONE_BRICKS = VARIATION_MATERIALS.register("mossy_stone_bricks",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("mossy_stone_bricks")),
               new NewVariationMaterialElement("slab", new ResourceLocation("mossy_stone_bricks_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("mossy_stone_bricks_stair")),
               new NewVariationMaterialElement("wall", new ResourceLocation("mossy_stone_bricks_wall"))
         ), "stone"));
   public static final RegistryObject<NewVariationMaterial> SANDSTONE = VARIATION_MATERIALS.register("sandstone",
         () -> new NewVariationMaterial(Arrays.asList(
               new NewVariationMaterialElement("stone", new ResourceLocation("sandstone")),
               new NewVariationMaterialElement("slab", new ResourceLocation("sandstone_slab")),
               new NewVariationMaterialElement("stair", new ResourceLocation("sandstone_stair")),
               new NewVariationMaterialElement("wall", new ResourceLocation("sandstone_wall"))
         ), "stone"));
}