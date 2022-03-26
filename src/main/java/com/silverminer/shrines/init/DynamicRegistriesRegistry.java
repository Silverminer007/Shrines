/*
 * Silverminer007
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.init;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.packages.datacontainer.NewVariationConfiguration;
import com.silverminer.shrines.packages.datacontainer.StructureNovel;
import com.silverminer.shrines.worldgen.structures.variation.NewVariationMaterial;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import silverminer.dynamicregistries.RegistryAccessExtension;

@Mod.EventBusSubscriber(modid = ShrinesMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DynamicRegistriesRegistry {
    public static final DeferredRegister<StructureNovel> NOVELS =
            RegistryAccessExtension.createRegister(StructureNovel.class, StructureNovel.REGISTRY);
    public static final DeferredRegister<NewVariationConfiguration> VARIATION_CONFIGURATION =
            RegistryAccessExtension.createRegister(NewVariationConfiguration.class, NewVariationConfiguration.REGISTRY);
    public static final DeferredRegister<NewVariationMaterial> VARIATION_MATERIAL =
            RegistryAccessExtension.createRegister(NewVariationMaterial.class, NewVariationMaterial.REGISTRY);

    @SubscribeEvent
    public static void registerRegistryAccessExtension(RegistryEvent.Register<RegistryAccessExtension<?>> event) {
        event.getRegistry().registerAll(
                new RegistryAccessExtension<>(StructureNovel.REGISTRY, StructureNovel.CODEC),
                new RegistryAccessExtension<>(NewVariationConfiguration.REGISTRY, NewVariationConfiguration.CODEC),
                new RegistryAccessExtension<>(NewVariationMaterial.REGISTRY, NewVariationMaterial.CODEC)
        );
    }
}