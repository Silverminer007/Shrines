/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.registries;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.codec.ReferenceNamedHolderSet;
import com.silverminer.shrines.random_variation.RandomVariationConfig;
import com.silverminer.shrines.random_variation.RandomVariationConfigElement;
import com.silverminer.shrines.random_variation.RandomVariationMaterial;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.silverminer.shrines.registries.RandomVariationMaterialRegistry.*;
import static com.silverminer.shrines.generators.RandomVariationMaterialTagProvider.*;

public class RandomVariationConfigRegistry {
   public static final DeferredRegister<RandomVariationConfig> REGISTRY = DeferredRegister.create(RandomVariationConfig.REGISTRY, Shrines.MODID);
   public static final Supplier<IForgeRegistry<RandomVariationConfig>> FORGE_REGISTRY_SUPPLIER =
         REGISTRY.makeRegistry(() -> new RegistryBuilder<RandomVariationConfig>().dataPackRegistry(RandomVariationConfig.DIRECT_CODEC));

   public static final RegistryObject<RandomVariationConfig> ABANDONED_VILLA = REGISTRY.register("abandoned_villa", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(WOOD), tag(WOOD)),
                     new RandomVariationConfigElement(tag(STONE), tag(STONE))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> ABANDONED_WITCH_HOUSE = REGISTRY.register("abandoned_witch_house", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(list(COBBLESTONE), tag(STONE)),
                     new RandomVariationConfigElement(list(POLISHED_ANDESITE), tag(STONE)),
                     new RandomVariationConfigElement(list(OAK), tag(WOOD)),
                     new RandomVariationConfigElement(list(DARK_OAK), tag(WOOD)),
                     new RandomVariationConfigElement(list(SPRUCE), tag(WOOD))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> AZALEA_PAVILION = REGISTRY.register("azalea_pavilion", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(WOOD), tag(WOOD))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> BALLOON = REGISTRY.register("balloon", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(COLOUR), tag(COLOUR)),
                     new RandomVariationConfigElement(tag(WOOD), tag(WOOD))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> BEES = REGISTRY.register("bees", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(list(OAK), tag(WOOD)),
                     new RandomVariationConfigElement(list(NORMAL_LIGHT), tag(LIGHT))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> END_TEMPLE = REGISTRY.register("end_temple", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(ANY), tag(ANY))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> FLOODED_TEMPLE = REGISTRY.register("flooded_temple", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(STONE), tag(STONE)),
                     new RandomVariationConfigElement(tag(WOOD), tag(WOOD)),
                     new RandomVariationConfigElement(tag(LIGHT), tag(LIGHT))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> GUARDIANS_MEETING = REGISTRY.register("guardians_meeting", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(list(SPRUCE), tag(WOOD))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> HARBOUR = REGISTRY.register("harbour", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(COLOUR), tag(COLOUR)),
                     new RandomVariationConfigElement(tag(WOOD), tag(WOOD)),
                     new RandomVariationConfigElement(tag(STONE), tag(STONE)),
                     new RandomVariationConfigElement(tag(LIGHT), tag(LIGHT)),
                     new RandomVariationConfigElement(tag(ORE), tag(ORE))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> HIGH_TEMPLE = REGISTRY.register("high_temple", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(list(STONE_BRICKS), tag(STONE)),
                     new RandomVariationConfigElement(list(DARK_OAK), tag(WOOD)),
                     new RandomVariationConfigElement(tag(COLOUR), tag(COLOUR))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> INFESTED_PRISON = REGISTRY.register("infested_prison", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(list(OAK), tag(WOOD)),
                     new RandomVariationConfigElement(tag(STONE), tag(STONE))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> JUNGLE_TOWER = REGISTRY.register("jungle_tower", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(WOOD), tag(WOOD))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> LUXURY_VILLA = REGISTRY.register("luxury_villa", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(ANY), tag(ANY))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> MAYAN_TEMPLE = REGISTRY.register("mayan_temple", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(STONE), tag(STONE))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> MINERAL_TEMPLE = REGISTRY.register("mineral_temple", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(STONE), tag(STONE))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> MODERN_VILLA = REGISTRY.register("modern_villa", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(ANY), tag(ANY))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> NETHER_PYRAMID_NETHER = REGISTRY.register("nether_pyramid_nether", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(list(POLISHED_BLACKSTONE_BRICKS), tag(STONE)),
                     new RandomVariationConfigElement(tag(LIGHT), tag(LIGHT))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> NETHER_PYRAMID_OVERWORLD = REGISTRY.register("nether_pyramid_overworld", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(list(POLISHED_BLACKSTONE_BRICKS), tag(STONE)),
                     new RandomVariationConfigElement(tag(LIGHT), tag(LIGHT))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> NETHER_SHRINE_NETHER = REGISTRY.register("nether_shrine_nether", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(list(STONE_BRICKS), tag(STONE))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> NETHER_SHRINE_OVERWORLD = REGISTRY.register("nether_shrine_overworld", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(list(STONE_BRICKS), tag(STONE))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> NONE = REGISTRY.register("none", () -> new RandomVariationConfig(List.of(List.of())));
   public static final RegistryObject<RandomVariationConfig> OASIS_SHRINE = REGISTRY.register("oasis_shrine", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(ANY), tag(ANY))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> ORIENTAL_SANCTUARY = REGISTRY.register("oriental_sanctuary", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(STONE), tag(STONE)),
                     new RandomVariationConfigElement(tag(WOOD), tag(WOOD))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> SHRINE_OF_SAVANNA = REGISTRY.register("shrine_of_savanna", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(WOOD), tag(WOOD))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> SMALL_PLAYER_HOUSE = REGISTRY.register("small_player_house", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(STONE), tag(STONE)),
                     new RandomVariationConfigElement(tag(WOOD), tag(WOOD)),
                     new RandomVariationConfigElement(tag(LIGHT), tag(LIGHT))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> SMALL_TEMPLE = REGISTRY.register("small_temple", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(STONE), tag(STONE)),
                     new RandomVariationConfigElement(tag(WOOD), tag(WOOD)),
                     new RandomVariationConfigElement(tag(LIGHT), tag(LIGHT))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> TALL_PLAYER_HOUSE = REGISTRY.register("tall_player_house", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(STONE), tag(STONE)),
                     new RandomVariationConfigElement(tag(WOOD), tag(WOOD))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> TRADER_HOUSE = REGISTRY.register("trader_house", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(ANY), tag(ANY))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> WATER_SHRINE = REGISTRY.register("water_shrine", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(ANY), tag(ANY))
               )
         )
   ));
   public static final RegistryObject<RandomVariationConfig> WORLD_TREE_MANOR = REGISTRY.register("world_tree_manor", () -> new RandomVariationConfig(
         List.of(
               List.of(
                     new RandomVariationConfigElement(tag(WOOD), tag(WOOD))
               ),
               List.of(
                     new RandomVariationConfigElement(tag(ANY), tag(ANY))
               )
         )
   ));

   @Contract("_ -> new")
   private static @NotNull HolderSet<RandomVariationMaterial> tag(TagKey<RandomVariationMaterial> tagKey) {
      return new ReferenceNamedHolderSet<>(RandomVariationMaterial.REGISTRY, tagKey);
   }

   @Contract("_ -> new")
   @SafeVarargs
   private static @NotNull HolderSet<RandomVariationMaterial> list(RegistryObject<RandomVariationMaterial>... registryObject) {
      return HolderSet.direct(Arrays.stream(registryObject).map(RegistryObject::getHolder).map(Optional::orElseThrow).toList());
   }
}