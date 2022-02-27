package com.silverminer.shrines.data;

import com.silverminer.shrines.ShrinesMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = ShrinesMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataProviderEvents {
    @SubscribeEvent
    public static void onGatherDataEvent(GatherDataEvent event) {
        event.getGenerator().addProvider(new NovelsProvider(event.getGenerator()));
        event.getGenerator().addProvider(new VariationMaterialProvider(event.getGenerator()));
    }
}