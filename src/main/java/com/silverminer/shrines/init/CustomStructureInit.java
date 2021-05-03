package com.silverminer.shrines.init;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.structures.custom.CustomStructure;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.utils.Utils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = Shrines.MODID, bus = Bus.MOD)
public class CustomStructureInit {
	public static final Logger LOGGER = LogManager.getLogger(CustomStructureInit.class);
	public static final ArrayList<RegistryObject<CustomStructure>> USERS_STRUCTURES = new ArrayList<RegistryObject<CustomStructure>>();
	public static boolean areStructuresRegistered = false;
	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void registerStructures(Register<Structure<?>> event) {
		LOGGER.debug("Registering custom structures");
		for (CustomStructureData csd : Utils.customsStructs) {
			String name = csd.getName().toLowerCase(Locale.ROOT);
			CustomStructure cS = new CustomStructure(NoFeatureConfig.CODEC, name, csd);
			cS.setRegistryName(Shrines.MODID, name);
			if (!Structure.STRUCTURES_REGISTRY.containsValue(cS)) {
				Structure.STRUCTURES_REGISTRY.putIfAbsent(new ResourceLocation(Shrines.MODID, name).toString(), cS);
			}
			if (!Structure.STEP.containsValue(cS.step())) {
				Structure.STEP.putIfAbsent(cS, cS.step());
			}

			Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder().addAll(Structure.NOISE_AFFECTING_FEATURES)
					.add(cS).build();

			StructureInit.STRUCTURES_LIST.add(cS);
			event.getRegistry().register(cS);
			USERS_STRUCTURES.add(RegistryObject.of(new ResourceLocation(Shrines.MODID, name), ForgeRegistries.STRUCTURE_FEATURES));
		}
		areStructuresRegistered = true;
	}
}