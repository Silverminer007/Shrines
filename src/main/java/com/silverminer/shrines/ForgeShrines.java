/**
 * Silverminer (and Team)
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * 
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines;

import org.apache.commons.lang3.tuple.Pair;

import com.silverminer.shrines.core.utils.proxy.ClientProxy;
import com.silverminer.shrines.core.utils.proxy.ForgeServerProxy;
import com.silverminer.shrines.forge.config.Config;
import com.silverminer.shrines.forge.utils.ForgeFunctionProvider;
import com.silverminer.shrines.init.StructureInit;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;

@Mod(value = ShrinesMod.MODID)
public class ForgeShrines extends ShrinesMod {

	public ForgeShrines() {
		super();
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
				() -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		StructureInit.STRUCTURES.register(modEventBus);

		// Config
		Config.register(ModLoadingContext.get());
	}

	@Override
	public void setProxy() {
		this.proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ForgeServerProxy::new);
	}

	@Override
	public void setFunctionProvider() {
		this.functionProvider = new ForgeFunctionProvider();
	}
}