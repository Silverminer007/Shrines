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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.init.NewStructureInit;
import com.silverminer.shrines.utils.client.ClientUtils;
import com.silverminer.shrines.utils.custom_structures.Utils;
import com.silverminer.shrines.utils.functions.ForgeFunctionProvider;
import com.silverminer.shrines.utils.functions.IFunctionProvider;
import com.silverminer.shrines.utils.proxy.ClientProxy;
import com.silverminer.shrines.utils.proxy.ForgeServerProxy;
import com.silverminer.shrines.utils.proxy.IProxy;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;

/**
 * @author Silverminer
 *
 */
@Mod(value = ShrinesMod.MODID)
public class ShrinesMod {
	public static final String MODID = "shrines";
	public static final Logger LOGGER = LogManager.getLogger(ShrinesMod.class);

	protected static IProxy proxy;
	protected static IFunctionProvider functionProvider;

	/**
	 * TODO 2.0.0 Change structure system to jigsaw -> harbour
	 * TODO 2.0.0 Move custom structures to jigsaw (add GUI to define pools and assigne a pool to an structure)
	 * FIXME 2.0.0 Structures generation height fix -> Nether structures
	 * TODO 2.0.0 Use processors to perform Color Structure Piece's work
	 * FIXME 2.0.0 #8(Use a processor?)  & #13
	 * 
	 * TODO 3.0.0 Mc1.17 Update -> Move #isAir to state only version
	 * 
	 * 
	 * Releases:
	 * - 2.0.0 Bug fix update
	 * - 3.0.0 Mc1.17 Update
	 * - 3.0.1 Bugfixes of 3.0.0 and some new features
	 * 
	 * NOTE: Test command: /execute positioned ~10000 ~ ~ run locate 
	 * 
	 */
	public ShrinesMod() {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
				() -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		Utils.loadCustomStructures();
		registerConfig();
	}

	public static IProxy getProxy(){
		if(proxy == null) {
			setProxy();
		}
		return proxy;
	}

	public static void setProxy() {
		proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ForgeServerProxy::new);
	}

	public static IFunctionProvider getFunctionProvider(){
		if(functionProvider == null) {
			setFunctionProvider();
		}
		return functionProvider;
	}

	public static void setFunctionProvider() {
		functionProvider = new ForgeFunctionProvider();
	}

	public static void registerConfig() {
		// Make sure structures are initialized before config will be loaded
		NewStructureInit.initStructures();
		// Config
		Config.register(ModLoadingContext.get());
		// Setup config UI
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
					() -> ClientUtils::getConfigGui);
		});
	}
}