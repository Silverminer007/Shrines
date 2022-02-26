package com.silverminer.shrines.dynamicregistries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * Defines an additional world-gen component to load as part of the RegistryAccess registries.
 * <p>
 * The path of the provided ResourceKey becomes the data folder from which datapack definitions are parsed
 * and the provided Codec handles encoding and decoding to and from the data format.
 * <p>
 * To avoid conflicts with other mods, the path of the ResourceKey must be prefixed with the owning mod's
 * namespace (ie {@code <modid>:<modid>/path}). Static helper methods for creating valid ResourceKeys are
 * provided at {@link #createRegistryKey(String, String)} and {@link #createRegistryKey(ResourceLocation)}.
 * <p>
 * Default or 'built-in' registry entries can be registered via a forge DeferredRegister or IForgeRegistry
 * of the same registry ResourceKey. These elements will populate the resulting RegistryAccess before datapacks
 * are loaded and may be overridden by data-definitions. A static helper method for creating a DeferredRegisters
 * for a RegistryAccessExtension is provided at {@link #createRegister(Class, ResourceKey)}.
 *
 * @param <T> The type of world-gen component.
 */
public final class RegistryAccessExtension<T extends IForgeRegistryEntry<T>> extends ForgeRegistryEntry<RegistryAccessExtension<?>> {
    private final ResourceKey<Registry<T>> registryKey;
    private final Codec<T> directCodec;
    @Nullable
    private final Codec<T> networkCodec;
    private final Lifecycle defaultElementLifecycle;

    public RegistryAccessExtension(ResourceKey<Registry<T>> registryKey, Codec<T> directCodec) {
        this(registryKey, directCodec, null);
    }

    /**
     *
     * @param registryKey the registry key that identifies the dynamic registry
     * @param directCodec used to serialize objects from datapacks
     * @param networkCodec used to serialize and deserialze through network. Use with care. Can cause issues when client is missing codecs
     */
    public RegistryAccessExtension(ResourceKey<Registry<T>> registryKey, Codec<T> directCodec, Codec<T> networkCodec) {
        this(registryKey, directCodec, networkCodec, Lifecycle.experimental());
    }

    public RegistryAccessExtension(ResourceKey<Registry<T>> registryKey, Codec<T> directCodec, Codec<T> networkCodec, Lifecycle defaultElementLifecycle) {
        validateRegistryKey(registryKey);
        setRegistryName(registryKey.location());
        this.registryKey = registryKey;
        this.directCodec = directCodec;
        this.networkCodec = networkCodec;
        this.defaultElementLifecycle = defaultElementLifecycle;
    }

    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return registryKey;
    }

    public Codec<T> getDirectCodec() {
        return directCodec;
    }

    public @Nullable Codec<T> getNetworkCodec() {
        return this.networkCodec;
    }

    public Lifecycle getDefaultElementLifecycle() {
        return defaultElementLifecycle;
    }

    public static <T> ResourceKey<Registry<T>> createRegistryKey(String namespace, String path) {
        var prefix = getPathPrefix(namespace);
        if (!path.startsWith(prefix)) {
            path = prefix + path;
        }

        return ResourceKey.createRegistryKey(new ResourceLocation(namespace, path));
    }

    public static <T> ResourceKey<Registry<T>> createRegistryKey(ResourceLocation location) {
        return createRegistryKey(location.getNamespace(), location.getPath());
    }

    public static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> createRegister(Class<T> type, ResourceKey<Registry<T>> key) {
        validateRegistryKey(key);
        var register = DeferredRegister.create(type, key.location().getNamespace());
        register.makeRegistry(key.location().getPath(), () -> new RegistryBuilder<T>().disableSaving().disableSync());//
        return register;
    }

    private static String getPathPrefix(String namespace) {
        return namespace + '/';
    }

    private static void validateRegistryKey(ResourceKey<? extends Registry<?>> registryKey) {
        var location = registryKey.location();
        var prefix = getPathPrefix(location.getNamespace());

        if (!location.getPath().startsWith(prefix)) {
            throw new IllegalArgumentException(String.format("Registry path must be prefixed with '%s'", prefix));
        }
    }
}