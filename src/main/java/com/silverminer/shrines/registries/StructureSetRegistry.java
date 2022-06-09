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
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StructureSetRegistry {
   public static final DeferredRegister<StructureSet> REGISTRY = DeferredRegister.create(Registry.STRUCTURE_SET_REGISTRY, Shrines.MODID);

   public static final RegistryObject<StructureSet> ABANDONED_WITCH_HOUSE = new Builder("abandoned_witch_house", 60, 23, 1721882513).addStructure(StructureRegistry.ABANDONED_WITCH_HOUSE).build();
   public static final RegistryObject<StructureSet> BALLOON = new Builder("balloon", 50, 8, 143665).addStructure(StructureRegistry.BALLOON).build();
   public static final RegistryObject<StructureSet> AZALEA_PAVILION = new Builder("azalea_pavilion", 40, 16, 635166686).addStructure(StructureRegistry.AZALEA_PAVILION).build();
   public static final RegistryObject<StructureSet> BEES = new Builder("bees", 70, 12, 779806245).addStructure(StructureRegistry.BEES).build();
   public static final RegistryObject<StructureSet> END_TEMPLE = new Builder("end_temple", 60, 11, 32 ^ 478392).addStructure(StructureRegistry.END_TEMPLE).build();
   public static final RegistryObject<StructureSet> GUARDIAN_MEETING = new Builder("guardian_meeting", 70, 17, 1498473232).addStructure(StructureRegistry.GUARDIAN_MEETING).build();
   public static final RegistryObject<StructureSet> HARBOUR = new Builder("harbour", 50, 8, 651398043).addStructure(StructureRegistry.HARBOUR).build();
   public static final RegistryObject<StructureSet> TEMPLES = new Builder("temples", 56, 25, 536987987).addStructure(StructureRegistry.SMALL_TEMPLE, 3).addStructure(StructureRegistry.HIGH_TEMPLE, 3).addStructure(StructureRegistry.FLOODED_TEMPLE, 4).addStructure(StructureRegistry.MINERAL_TEMPLE, 4).addStructure(StructureRegistry.MAYAN_TEMPLE, 3).build();
   public static final RegistryObject<StructureSet> JUNGLE_TOWER = new Builder("jungle_tower", 60, 11, 987531843).addStructure(StructureRegistry.JUNGLE_TOWER).build();
   public static final RegistryObject<StructureSet> MODERN_VILLA = new Builder("modern_villa", 55, 30, 688286800).addStructure(StructureRegistry.MODERN_VILLA, 5).addStructure(StructureRegistry.ABANDONED_VILLA, 3).addStructure(StructureRegistry.LUXURY_VILLA, 4).build();
   public static final RegistryObject<StructureSet> NETHER_PYRAMID = new Builder("nether_pyramid", 150, 50, 7428394).addStructure(StructureRegistry.NETHER_PYRAMID_OVERWORLD).addStructure(StructureRegistry.NETHER_PYRAMID_NETHER).build();
   public static final RegistryObject<StructureSet> NETHER_SHRINE = new Builder("nether_shrine", 80, 15, 653267).addStructure(StructureRegistry.NETHER_SHRINE_NETHER).addStructure(StructureRegistry.NETHER_SHRINE_OVERWORLD).build();
   public static final RegistryObject<StructureSet> OASIS_SHRINE = new Builder("oasis_shrine", 40, 32, 2056047070).addStructure(StructureRegistry.OASIS_SHRINE).build();
   public static final RegistryObject<StructureSet> ORIENTAL_HUT = new Builder("oriental_hut", 32, 14, 42384446).addStructure(StructureRegistry.ORIENTAL_HUT).build();
   public static final RegistryObject<StructureSet> ORIENTAL_SANCTUARY = new Builder("oriental_sanctuary", 50, 21, 143665).addStructure(StructureRegistry.ORIENTAL_SANCTUARY).build();
   public static final RegistryObject<StructureSet> PLAYER_HOUSE = new Builder("player_house", 80, 27, 751963298).addStructure(StructureRegistry.SMALL_PLAYER_HOUSE).addStructure(StructureRegistry.TALL_PLAYER_HOUSE).build();
   public static final RegistryObject<StructureSet> INFESTED_PRISON = new Builder("infested_prison", 60, 22, 567483014).addStructure(StructureRegistry.INFESTED_PRISON).build();
   public static final RegistryObject<StructureSet> SHRINE_OF_SAVANNA = new Builder("shrine_of_savanna", 67, 22, 432333099).addStructure(StructureRegistry.SHRINE_OF_SAVANNA).build();
   public static final RegistryObject<StructureSet> TRADER_HOUSE = new Builder("trader_house", 60, 18, 760055678).addStructure(StructureRegistry.TRADER_HOUSE).build();
   public static final RegistryObject<StructureSet> WATCH_TOWER = new Builder("watch_tower", 77, 16, 432189012).addStructure(StructureRegistry.WATCH_TOWER).build();
   public static final RegistryObject<StructureSet> WATER_SHRINE = new Builder("water_shrine", 80, 15, 643168754).addStructure(StructureRegistry.WATER_SHRINE).build();
   public static final RegistryObject<StructureSet> WORLD_TREE_MANOR = new Builder("world_tree_manor", 55, 45, 14944438).addStructure(StructureRegistry.WORLD_TREE_MANOR).build();

   private static class Builder {
      private final String name;
      private final int seed;
      private final List<Pair<Holder<Structure>, Integer>> structures = new ArrayList<>();
      private int spacing = 45;
      private int separation = 23;
      private RandomSpreadType randomSpreadType = RandomSpreadType.LINEAR;

      private Builder(String name, int seed) {
         this.name = name;
         this.seed = seed;
      }

      private Builder(String name, int spacing, int separation, int seed) {
         this.name = name;
         this.spacing = spacing;
         this.separation = separation;
         this.seed = seed;
      }

      private Builder setSpacing(int spacing) {
         this.spacing = spacing;
         return this;
      }

      private Builder setSeparation(int separation) {
         this.separation = separation;
         return this;
      }

      private Builder setRandomSpreadType(RandomSpreadType randomSpreadType) {
         this.randomSpreadType = randomSpreadType;
         return this;
      }

      @Contract("_, _ -> this")
      private Builder addStructure(@NotNull Optional<Holder<Structure>> structureKey, int weight) {
         this.structures.add(Pair.of(structureKey.orElseThrow(), weight));
         return this;
      }

      @Contract("_ -> this")
      private Builder addStructure(Optional<Holder<Structure>> structureKey) {
         return this.addStructure(structureKey, 1);
      }

      @Contract("_, _ -> this")
      private Builder addStructure(@NotNull RegistryObject<Structure> structureRegistryObject, int weight) {
         return this.addStructure(structureRegistryObject.getHolder(), weight);
      }

      @Contract("_ -> this")
      private Builder addStructure(RegistryObject<Structure> structureRegistryObject) {
         return this.addStructure(structureRegistryObject.getHolder());
      }

      private RegistryObject<StructureSet> build() {
         return REGISTRY.register(this.name, () -> new StructureSet(this.structures.stream().map(structure ->
               new StructureSet.StructureSelectionEntry(structure.getFirst(), structure.getSecond())).toList(),
               new RandomSpreadStructurePlacement(spacing, separation, this.randomSpreadType, seed)));
      }
   }
}