/*
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

import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.utils.StructureLoadUtils;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * @author Silverminer
 */
@Mod(value = ShrinesMod.MODID)
public class ShrinesMod {
    public static final String MODID = "shrines";
    public static final Logger LOGGER = LogManager.getLogger(ShrinesMod.class);
    public static String VERSION = "N/A";

    /**
     * If you're looking for example code on how to make a structure Mod see
     * TelepathicGrunt's Example Mod on how to do that:
     * https://github.com/TelepathicGrunt/StructureTutorialMod
     * <p>
     * TODO 3.0.1 Use processors to perform Color Structure Piece's work (Or maybe PostPlacementProcessors?)
     * <p>
     * FIXME #22 https://github.com/Silverminer007/Shrines/issues/22
     * <p>
     * FIXME Add missing novels @pea_sh0ter
     * <p>
     *     TODO Templates add button doesn't work
     *     TODO Insights Screen looks weird
     *     TODO Create Max recursion depth option for structures
     *     TODO Fix No structure is generating even if it is locatable finally
     *
     * Releases: - 2.0.0 Bug fix update - 3.0.0 Mc1.18 Update - 3.0.1 Bugfixes of
     * 3.0.0 and some new features
     * <p>
     * NOTE: Test command: /execute positioned ~10000 ~ ~ run locate
     */
    public ShrinesMod() {
        ModList.get().getModContainerById(ShrinesMod.MODID)
                .ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());
        LOGGER.info("Shrines " + VERSION + " initialized");

        // ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, TODO
        //        () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        registerConfig();
    }

    public static File getMinecraftDirectory() {
        return FMLPaths.GAMEDIR.get().toFile();
    }

    public static void registerConfig() {
        // Config
        Config.register(ModLoadingContext.get());
        // Setup config UI TODO
        //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
        //        () -> ClientUtils::getConfigGui));

    }
}