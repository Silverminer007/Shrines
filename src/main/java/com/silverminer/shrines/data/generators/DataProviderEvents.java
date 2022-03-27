/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.data.generators;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.data.ConfiguredStructureFeatures;
import com.silverminer.shrines.data.StructureSets;
import com.silverminer.shrines.data.TemplatePools;
import net.minecraft.data.info.WorldgenRegistryDumpReport;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = ShrinesMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataProviderEvents {
   @SubscribeEvent
   public static void onGatherDataEvent(GatherDataEvent event) {
      event.getGenerator().addProvider(new ShrinesBiomeTagsProvider(event.getGenerator(), event.getExistingFileHelper()));
      event.getGenerator().addProvider(new WorldgenRegistryDumpReport(event.getGenerator()));
   }

   @SubscribeEvent(priority = EventPriority.LOW)
   public static void onRegisterBlocks(RegistryEvent.Register<StructureFeature<?>> event) {
      TemplatePools.bootstrap();
      ConfiguredStructureFeatures.bootstrap();
      StructureSets.bootstrap();
   }
}