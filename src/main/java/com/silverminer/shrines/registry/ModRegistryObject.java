package com.silverminer.shrines.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record ModRegistryObject<T>(ResourceKey<T> key) implements Supplier<T> {
   @Nullable
   public T get() {
      return this.getRegistry().get(this.key());
   }

   @NotNull
   public Holder<T> getAsHolder() {
      return Holder.Reference.createStandAlone(this.getRegistry(), this.key());
   }

   @NotNull
   private Registry<T> getRegistry() {
      return Utils.getRegistry(this.key().registry());
   }
}