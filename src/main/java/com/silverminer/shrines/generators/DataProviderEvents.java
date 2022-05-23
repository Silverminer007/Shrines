/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.generators;

import com.silverminer.shrines.Shrines;
import net.minecraft.data.info.WorldgenRegistryDumpReport;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Shrines.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataProviderEvents {
   @SubscribeEvent
   public static void onGatherDataEvent(GatherDataEvent event) {
      event.getGenerator().addProvider(new ShrinesBiomeTagsProvider(event.getGenerator(), event.getExistingFileHelper()));
      event.getGenerator().addProvider(new StructureTagProvider(event.getGenerator(), event.getExistingFileHelper()));
   }
}