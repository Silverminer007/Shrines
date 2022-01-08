/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.datacontainer.StructureData;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * @author Silverminer
 */
@EventBusSubscriber(modid = ShrinesMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class StructureInit {
   protected static final Logger LOGGER = LogManager.getLogger(StructureInit.class);
   public static final ImmutableList<StructureRegistryHolder> STRUCTURES = ImmutableList
         .<StructureRegistryHolder>builder().addAll(initStructures()).build();

   private static ArrayList<StructureRegistryHolder> initStructures() {
      PackageManagerProvider.SERVER.bootstrapPackages();
      ArrayList<StructureRegistryHolder> structures = Lists.newArrayList();
      for (StructuresPackageWrapper packet : PackageManagerProvider.SERVER.getInitialStructurePackages().getAsIterable()) {
         for (StructureData structure : packet.getStructures().getAsIterable()) {
            structures.add(new StructureRegistryHolder(structure.getKey(), structure));
         }
      }
      return structures;
   }

   @SubscribeEvent
   public static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
      LOGGER.info("Registering {} structures of shrines Mod", STRUCTURES.size());
      for (StructureRegistryHolder holder : STRUCTURES) {
         event.getRegistry().register(holder.getStructure());
      }
   }
}