package com.silverminer.shrines;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.init.StructureInit;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Shrines.MODID)
@Mod.EventBusSubscriber(modid = Shrines.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Shrines {
	public static final String MODID = "shrines";

	public static final Logger LOGGER = LogManager.getLogger(Shrines.class);

	public Shrines() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		StructureInit.STRUCTURES.register(modEventBus);
	}
}