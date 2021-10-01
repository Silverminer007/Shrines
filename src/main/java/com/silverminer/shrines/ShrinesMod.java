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

import java.io.File;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.StructureLoadUtils;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.FMLNetworkConstants;

/**
 * @author Silverminer
 *
 */
@Mod(value = ShrinesMod.MODID)
public class ShrinesMod {
	public static final String MODID = "shrines";
	public static String VERSION = "N/A";
	public static final Logger LOGGER = LogManager.getLogger(ShrinesMod.class);

	/**
	 * 
	 * If you're looking for example code on how to make a structure Mod see
	 * TelepathicGrunt's Example Mod on how to do that:
	 * https://github.com/TelepathicGrunt/StructureTutorialMod
	 * 
	 * TODO 2.0.0 add GUI to define pools and assigne a pool to an structure
	 * 
	 * TODO 2.0.0 Use processors to perform Color Structure Piece's work
	 * 
	 * 2.0.0 Test Caves & Cliffs Backport compat
	 * 
	 * TODO Fix screenshots for Novels Screen. Resolution and size. Send them over network to the client
	 * 
	 * TODO 3.0.0 Mc1.17 Update -> Move #isAir to state only version
	 * 
	 * Releases: - 2.0.0 Bug fix update - 3.0.0 Mc1.17 Update - 3.0.1 Bugfixes of
	 * 3.0.0 and some new features
	 * 
	 * NOTE: Test command: /execute positioned ~10000 ~ ~ run locate
	 * 
	 */
	public ShrinesMod() {
		ModList.get().getModContainerById(ShrinesMod.MODID)
				.ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());
		LOGGER.info("Shrines " + VERSION + " initialized");

		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
				() -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		StructureLoadUtils.loadStructures(true);
		registerConfig();
	}

	public static File getMinecraftDirectory() {
		return FMLPaths.GAMEDIR.get().toFile();
	}

	public static void registerConfig() {
		// Config
		Config.register(ModLoadingContext.get());
		// Setup config UI
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
					() -> ClientUtils::getConfigGui);
		});

	}
}