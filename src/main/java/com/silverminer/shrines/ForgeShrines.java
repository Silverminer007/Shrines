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

import com.silverminer.shrines.client.gui.config.ShrinesStructuresScreen;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.utils.ForgeFunctionProvider;
import com.silverminer.shrines.utils.proxy.ClientProxy;
import com.silverminer.shrines.utils.proxy.ForgeServerProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;

@Mod(value = ShrinesMod.MODID)
public class ForgeShrines extends ShrinesMod {

	public ForgeShrines() {
		super();
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
				() -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
	}

	@Override
	public void setProxy() {
		this.proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ForgeServerProxy::new);
	}

	@Override
	public void setFunctionProvider() {
		this.functionProvider = new ForgeFunctionProvider();
	}

	@Override
	public void registerConfig() {
		// Make sure structures are initialized before config will be loaded
		NewStructureInit.initStructures();
		// Config
		Config.register(ModLoadingContext.get());
		// Setup config UI
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
					() -> ForgeShrines::getConfigGui);
		});
	}

	public static Screen getConfigGui(Minecraft mc, Screen parent) {
		return new ShrinesStructuresScreen(parent);
	}
}