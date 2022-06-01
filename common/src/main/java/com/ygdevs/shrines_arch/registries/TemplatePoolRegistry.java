/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.ygdevs.shrines_arch.registries;

import com.mojang.datafixers.util.Pair;
import com.ygdevs.shrines_arch.Shrines;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TemplatePoolRegistry {
   public static final DeferredRegister<StructureTemplatePool> REGISTRY = DeferredRegister.create(Shrines.MODID, Registry.TEMPLATE_POOL_REGISTRY);

   public static final RegistrySupplier<StructureTemplatePool> ABANDONED_VILLA = new PoolBuilder().setDirectory("abandoned_villa").addSingleTemplate(d("abandoned_villa")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> ABANDONED_WITCH_HOUSE = new PoolBuilder().setDirectory("abandoned_witch_house").addSingleTemplate(d("abandoned_witch_house")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> AZALEA_PAVILION = new PoolBuilder().setDirectory("azalea_pavilion").addSingleTemplate(d("azalea_pavilion")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> BALLOON = new PoolBuilder().setDirectory("balloon")
         .addLegacyTemplate("balloon/balloon_1")
         .addLegacyTemplate("balloon/balloon_2")
         .addLegacyTemplate("balloon/balloon_3")
         .addLegacyTemplate("balloon/balloon_4")
         .addLegacyTemplate("balloon/balloon_5")
         .addLegacyTemplate("balloon/balloon_6")
         .addLegacyTemplate("balloon/balloon_7")
         .addLegacyTemplate("balloon/balloon_rainbow")
         .addLegacyTemplate("balloon/balloon2_1")
         .addLegacyTemplate("balloon/balloon2_2")
         .addLegacyTemplate("balloon/balloon2_3")
         .addLegacyTemplate("balloon/balloon2_4")
         .buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> BEES = new PoolBuilder().setDirectory("bees").addSingleTemplate(d("bees")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> END_TEMPLE = new PoolBuilder().setDirectory("end_temple").addSingleTemplate(d("end_temple")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> FLOODED_TEMPLE = new PoolBuilder().setDirectory("flooded_temple").addSingleTemplate(d("flooded_temple")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> GUARDIANS_MEETING = new PoolBuilder().setDirectory("guardian_meeting").addSingleTemplate(d("guardian_meeting")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> HARBOUR = new PoolBuilder().setDirectory("harbour").addSingleTemplate("harbour/start").buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> HARBOUR_BUILDINGS = new PoolBuilder().setDirectory("harbour").setFileName("buildings")
         .addSingleTemplate("harbour/buildings/crane/crane_1")
         .addSingleTemplate("harbour/buildings/crane/crane_2")
         .addSingleTemplate("harbour/buildings/houses/house_1")
         .addSingleTemplate("harbour/buildings/warehouses/warehouse_big")
         .addSingleTemplate("harbour/buildings/warehouses/warehouse_small_1")
         .addSingleTemplate("harbour/buildings/warehouses/warehouse_small_2")
         .buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> HARBOUR_STREET = new PoolBuilder().setDirectory("harbour").setFileName("street")
         .addSingleTemplate("harbour/street/curve_1")
         .addSingleTemplate("harbour/street/curve_2")
         .addSingleTemplate("harbour/street/cross")
         .addSingleTemplate("harbour/street/straight_1")
         .addSingleTemplate("harbour/street/straight_2")
         .buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> HARBOUR_WARE = new PoolBuilder().setDirectory("harbour").setFileName("ware")
         .addSingleTemplate("harbour/ware/ware_1", 2)
         .addSingleTemplate("harbour/ware/ware_2", 2)
         .addSingleTemplate("harbour/ware/ware_3")
         .addSingleTemplate("harbour/ware/ware_4")
         .addSingleTemplate("harbour/ware/ware_5", 2)
         .buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> HIGH_TEMPLE = new PoolBuilder().setDirectory("high_temple").addSingleTemplate(d("high_temple")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> JUNGLE_TOWER = new PoolBuilder().setDirectory("jungle_tower").addSingleTemplate(d("jungle_tower")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> LUXURY_VILLA = new PoolBuilder().setDirectory("luxury_villa").addSingleTemplate(d("luxury_villa")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> MINERAL_TEMPLE = new PoolBuilder().setDirectory("mineral_temple").addSingleTemplate(d("mineral_temple")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> OASIS_SHRINE = new PoolBuilder().setDirectory("oasis_shrine").addSingleTemplate(d("oasis_shrine")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> NETHER_PYRAMID = new PoolBuilder().setDirectory("nether_pyramid").addSingleTemplate(d("nether_pyramid")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> NETHER_SHRINE = new PoolBuilder().setDirectory("nether_shrine")
         .addSingleTemplate("nether_shrine/nether_shrine_001")
         .addSingleTemplate("nether_shrine/nether_shrine_002")
         .addSingleTemplate("nether_shrine/nether_shrine_003")
         .addSingleTemplate("nether_shrine/nether_shrine_004")
         .addSingleTemplate("nether_shrine/nether_shrine_005")
         .addSingleTemplate("nether_shrine/nether_shrine_006")
         .addSingleTemplate("nether_shrine/nether_shrine_007")
         .addSingleTemplate("nether_shrine/nether_shrine_008")
         .addSingleTemplate("nether_shrine/nether_shrine_009")
         .addSingleTemplate("nether_shrine/nether_shrine_011")
         .addSingleTemplate("nether_shrine/nether_shrine_sandstone")
         .buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> MODERN_VILLA = new PoolBuilder().setDirectory("modern_villa").addSingleTemplate(d("modern_villa")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> ORIENTAL_HUT = new PoolBuilder().setDirectory("oriental_hut").addSingleTemplate(d("oriental_hut")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> ORIENTAL_SANCTUARY = new PoolBuilder().setDirectory("oriental_sanctuary").addSingleTemplate(d("oriental_sanctuary")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> INFESTED_PRISON = new PoolBuilder().setDirectory("infested_prison").addSingleTemplate(d("infested_prison")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> SHRINE_OF_SAVANNA = new PoolBuilder().setDirectory("shrine_of_savanna").addSingleTemplate(d("shrine_of_savanna")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> SMALL_PLAYER_HOUSE = new PoolBuilder().setDirectory("small_player_house").addLegacyTemplate(d("small_player_house")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> SMALL_TEMPLE = new PoolBuilder().setDirectory("small_temple").addSingleTemplate(d("small_temple")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> TALL_PLAYER_HOUSE = new PoolBuilder().setDirectory("tall_player_house")
         .addSingleTemplate("tall_player_house/tall_player_house_1")
         .addSingleTemplate("tall_player_house/tall_player_house_2")
         .addSingleTemplate("tall_player_house/tall_player_house_3")
         .buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> TRADER_HOUSE = new PoolBuilder().setDirectory("trader_house").addSingleTemplate(d("trader_house")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> WATCH_TOWER = new PoolBuilder().setDirectory("watch_tower").addSingleTemplate(d("watch_tower")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> WATER_SHRINE = new PoolBuilder().setDirectory("water_shrine").addSingleTemplate(d("water_shrine")).buildAndRegister();
   public static final RegistrySupplier<StructureTemplatePool> WORLD_TREE_MANOR = new PoolBuilder().setDirectory("world_tree_manor").addSingleTemplate(d("world_tree_manor")).buildAndRegister();

   /**
    * @return input/input where input is the given string
    */
   @Contract(pure = true)
   private static @NotNull String d(String s) {
      return s + "/" + s;
   }

   private static class PoolBuilder {
      private String namespace = Shrines.MODID;
      private String directory = "";
      private String fileName = "start_pool";
      private final List<Pair<StructurePoolElement, Integer>> poolElements = new ArrayList<>();
      private ResourceLocation fallback = new ResourceLocation("empty");

      private PoolBuilder setNamespace(String namespace) {
         this.namespace = namespace;
         return this;
      }

      private PoolBuilder setDirectory(String directory) {
         this.directory = directory;
         return this;
      }

      private PoolBuilder setFileName(String fileName) {
         this.fileName = fileName;
         return this;
      }

      private PoolBuilder setFallBack(ResourceLocation fallback) {
         this.fallback = fallback;
         return this;
      }

      @Contract("_ -> this")
      private PoolBuilder addLegacyTemplate(@NotNull String template) {
         this.poolElements.add(Pair.of(StructurePoolElement.legacy(new ResourceLocation(namespace, template).toString(), ProcessorLists.EMPTY).apply(StructureTemplatePool.Projection.RIGID), 1));
         return this;
      }

      @Contract("_ -> this")
      private PoolBuilder addLegacyTemplate(@NotNull ResourceLocation template) {
         this.poolElements.add(Pair.of(StructurePoolElement.legacy(template.toString(), ProcessorLists.EMPTY).apply(StructureTemplatePool.Projection.RIGID), 1));
         return this;
      }

      @Contract("_, _ -> this")
      private PoolBuilder addLegacyTemplate(@NotNull ResourceLocation template, int weight) {
         this.poolElements.add(Pair.of(StructurePoolElement.legacy(template.toString(), ProcessorLists.EMPTY).apply(StructureTemplatePool.Projection.RIGID), weight));
         return this;
      }

      @Contract("_, _, _, _ -> this")
      private PoolBuilder addLegacyTemplate(@NotNull ResourceLocation template, Holder<StructureProcessorList> structureProcessorList, StructureTemplatePool.Projection projection, int weight) {
         this.poolElements.add(Pair.of(StructurePoolElement.legacy(template.toString(), structureProcessorList).apply(projection), weight));
         return this;
      }

      @Contract("_ -> this")
      private PoolBuilder addSingleTemplate(@NotNull String template) {
         this.poolElements.add(Pair.of(StructurePoolElement.single(new ResourceLocation(namespace, template).toString(), ProcessorLists.EMPTY).apply(StructureTemplatePool.Projection.RIGID), 1));
         return this;
      }

      @Contract("_, _ -> this")
      private PoolBuilder addSingleTemplate(@NotNull String template, int weight) {
         this.poolElements.add(Pair.of(StructurePoolElement.single(new ResourceLocation(namespace, template).toString(), ProcessorLists.EMPTY).apply(StructureTemplatePool.Projection.RIGID), weight));
         return this;
      }

      @Contract("_ -> this")
      private PoolBuilder addSingleTemplate(@NotNull ResourceLocation template) {
         this.poolElements.add(Pair.of(StructurePoolElement.single(template.toString(), ProcessorLists.EMPTY).apply(StructureTemplatePool.Projection.RIGID), 1));
         return this;
      }

      @Contract("_, _ -> this")
      private PoolBuilder addSingleTemplate(@NotNull ResourceLocation template, int weight) {
         this.poolElements.add(Pair.of(StructurePoolElement.single(template.toString(), ProcessorLists.EMPTY).apply(StructureTemplatePool.Projection.RIGID), weight));
         return this;
      }

      @Contract("_, _, _, _ -> this")
      private PoolBuilder addSingleTemplate(@NotNull ResourceLocation template, Holder<StructureProcessorList> structureProcessorList, StructureTemplatePool.Projection projection, int weight) {
         this.poolElements.add(Pair.of(StructurePoolElement.single(template.toString(), structureProcessorList).apply(projection), weight));
         return this;
      }

      private PoolBuilder addTemplate(StructurePoolElement structurePoolElement, int weight) {
         this.poolElements.add(Pair.of(structurePoolElement, weight));
         return this;
      }

      private RegistrySupplier<StructureTemplatePool> buildAndRegister() {
         if (directory.equals("") && fileName.equals("start_pool")) {
            throw new IllegalArgumentException("Either directory or file must be set for Structure Template Pools");
         }
         ResourceLocation id = new ResourceLocation(namespace, directory.equals("") ? fileName : directory + "/" + fileName);
         return REGISTRY.register(id.getPath(), () -> new StructureTemplatePool(id, fallback, poolElements));
      }
   }
}
