package com.silverminer.shrines;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.StructureInit;
import com.silverminer.shrines.utils.Utils;
import com.silverminer.shrines.utils.network.proxy.ClientProxy;
import com.silverminer.shrines.utils.network.proxy.IProxy;
import com.silverminer.shrines.utils.network.proxy.ServerProxy;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;

@Mod(value = Shrines.MODID)
public class Shrines {
	public static final String MODID = "shrines";

	public static final Logger LOGGER = LogManager.getLogger(Shrines.class);
	@SuppressWarnings("deprecation")
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(),
			() -> () -> new ServerProxy());

	public Shrines() {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
				() -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		StructureInit.STRUCTURES.register(modEventBus);

		// Config
		Config.register(ModLoadingContext.get());
		Utils.loadCustomStructures();
	}
}