package com.silverminer.shrines;

import com.silverminer.shrines.init.StructureInit;
import com.silverminer.shrines.structures.Generator;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("deprecation")
@Mod(Shrines.MODID)
public class Shrines {
	public static final String MODID = "shrines";

	public Shrines() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(this::setup);
		StructureInit.STRUCTURES.register(modEventBus);
	}

	public void setup(final FMLCommonSetupEvent event) {
		DeferredWorkQueue.runLater(Generator::setupWorldGen);
	}
}