package com.silverminer.shrines.mixins;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.RegistryResourceAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RegistryAccess.class)
public class MixinRegistryAccess {
    @Inject(method = "addBuiltinElements", at = @At("HEAD"), cancellable = true)
    private static <E, T extends IForgeRegistryEntry<T>> void shrines_onAddBuiltinElements(RegistryAccess.RegistryHolder pDestinationRegistryHolder, RegistryResourceAccess.InMemoryStorage pResourceAccess, RegistryAccess.RegistryData<E> pData, CallbackInfo ci) {
        ResourceKey<? extends Registry<E>> resourcekey = pData.key();
        boolean flag = !resourcekey.equals(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY) && !resourcekey.equals(Registry.DIMENSION_TYPE_REGISTRY);
        Registry<E> registry = RegistryAccess.BUILTIN.registryOrThrow(resourcekey);
        if (BuiltinRegistries.REGISTRY.containsKey(resourcekey.location())) {
            registry = ((Registry<Registry<E>>) BuiltinRegistries.REGISTRY).get((ResourceKey<Registry<E>>) resourcekey);
        }
        WritableRegistry<E> writableregistry = pDestinationRegistryHolder.ownedRegistryOrThrow(resourcekey);

        for (Map.Entry<ResourceKey<E>, E> entry : registry.entrySet()) {
            ResourceKey<E> resourcekey1 = entry.getKey();
            E e = entry.getValue();
            if (flag) {
                pResourceAccess.add(RegistryAccess.BUILTIN, resourcekey1, pData.codec(), registry.getId(e), e, registry.lifecycle(e));
            } else {
                writableregistry.registerMapping(registry.getId(e), resourcekey1, e, registry.lifecycle(e));
            }
        }
        ci.cancel();
    }
}