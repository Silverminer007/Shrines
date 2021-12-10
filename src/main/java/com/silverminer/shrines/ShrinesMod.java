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

import com.google.common.collect.Lists;
import com.silverminer.shrines.config.Config;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.StructureLoadUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSAddedStructurePacketPacket;
import com.silverminer.shrines.utils.network.stc.STCOpenStructuresPacketEditPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

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
    * TODO #22 https://github.com/Silverminer007/Shrines/issues/22
    * TODO Create Max recursion depth option for structures
    * <p>
    * TODO Add missing novels @pea_sh0ter
    * <p>
    * TODO Test Narration usage of GUI components
    * </p>
    * FIXME Templates add button doesn't work
    */
   public ShrinesMod() {
      ModList.get().getModContainerById(ShrinesMod.MODID)
            .ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());
      LOGGER.info("Shrines " + VERSION + " initialized");
      Config.register(ModLoadingContext.get());
   }

   public static File getMinecraftDirectory() {
      return FMLPaths.GAMEDIR.get().toFile();
   }
}