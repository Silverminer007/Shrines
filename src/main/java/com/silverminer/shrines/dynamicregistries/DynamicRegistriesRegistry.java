package com.silverminer.shrines.dynamicregistries;

import com.silverminer.shrines.ShrinesMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = ShrinesMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DynamicRegistriesRegistry {
    public static IForgeRegistry<RegistryAccessExtension<?>> REGISTRY_ACCESS_EXTENSIONS_REGISTRY;

    @SuppressWarnings("unchecked")
    //Ugly hack to let us pass in a typed Class object. Remove when we remove type specific references.
    private static <T> Class<T> c(Class<?> cls) {
        return (Class<T>) cls;
    }

    @SubscribeEvent
    public static void onCreateNewRegistries(RegistryEvent.NewRegistry event) {
        RegistryBuilder<RegistryAccessExtension<?>> registryBuilder = new RegistryBuilder<>();
        registryBuilder.setName(new ResourceLocation(ShrinesMod.MODID, "dynamic_registries"));
        registryBuilder.setType(c(RegistryAccessExtension.class));
        REGISTRY_ACCESS_EXTENSIONS_REGISTRY = registryBuilder.create();
    }
}