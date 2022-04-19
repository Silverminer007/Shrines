package com.silverminer.shrines.test;

import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.registry.Utils;
import net.minecraftforge.event.RegisterGameTestsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.NewRegistryEvent;
import org.jetbrains.annotations.NotNull;

public class TestEvents {
   @Mod.EventBusSubscriber(modid = Shrines.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
   static class ModBus {
      @SubscribeEvent
      public static void newRegistry(NewRegistryEvent event) {
         Utils.createRegistry(Test.REGISTRY, Test.CODEC);
         TestRegistry.REGISTRY.register();
      }

      @SubscribeEvent
      public static void onRegisterGameTests(@NotNull RegisterGameTestsEvent event) {
         event.register(DataPackRegistryTest.class);
      }
   }
}