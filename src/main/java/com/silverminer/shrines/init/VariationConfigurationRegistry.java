package com.silverminer.shrines.init;

import com.silverminer.shrines.packages.datacontainer.VariationConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class VariationConfigurationRegistry {
   public static final DeferredRegister<VariationConfiguration> VARIATION_CONFIGURATION = DeferredRegister.create(VariationConfiguration.class, "minecraft");
   public static final Supplier<IForgeRegistry<VariationConfiguration>> VARIATION_CONFIGURATION_CONFIGURATION_REGISTRY = VARIATION_CONFIGURATION.makeRegistry(
         "shrines_variation_configuration", RegistryBuilder::new);
   public static final RegistryObject<VariationConfiguration> MANSION = VARIATION_CONFIGURATION.register("mansion", () -> VariationConfiguration.ALL_ENABLED);
}