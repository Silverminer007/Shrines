package com.silverminer.shrines.dynamicregistries;

import com.silverminer.shrines.ShrinesMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod.EventBusSubscriber(modid = ShrinesMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEvents {
    @SubscribeEvent
    public static void loadCompleteEvent(FMLLoadCompleteEvent event) {
        RegistryUtils.addExtensions();
    }
}