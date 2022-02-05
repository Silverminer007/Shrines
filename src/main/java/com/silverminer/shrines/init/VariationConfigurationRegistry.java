package com.silverminer.shrines.init;

import com.silverminer.shrines.packages.datacontainer.NewVariationConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

public class VariationConfigurationRegistry {
   public static final DeferredRegister<NewVariationConfiguration> VARIATION_CONFIGURATION = DeferredRegister.create(NewVariationConfiguration.class, "minecraft");
   public static final Supplier<IForgeRegistry<NewVariationConfiguration>> VARIATION_CONFIGURATION_CONFIGURATION_REGISTRY = VARIATION_CONFIGURATION.makeRegistry(
         "shrines_variation_configuration", RegistryBuilder::new);
   public static final RegistryObject<NewVariationConfiguration> MANSION = VARIATION_CONFIGURATION.register("mansion", () -> new NewVariationConfiguration(true,
         new ArrayList<>(), new ArrayList<>()));
}