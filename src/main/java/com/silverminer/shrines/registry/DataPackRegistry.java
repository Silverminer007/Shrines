package com.silverminer.shrines.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

record DataPackRegistry<T>(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec) {
}