package com.silverminer.shrines.packages;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.packages.client.ClientStructurePackageManager;
import com.silverminer.shrines.packages.server.ServerStructurePackageManager;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSSyncAvailableDimensionsRequest;
import com.silverminer.shrines.utils.network.cts.CTSSyncInitialPackagesRequest;
import com.silverminer.shrines.utils.network.cts.CTSSyncStructureIconsRequest;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = ShrinesMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PackageManagerProvider {
   protected static final Logger LOGGER = LogManager.getLogger(PackageManagerProvider.class);
   public static final ServerStructurePackageManager SERVER = new ServerStructurePackageManager();
   public static final ClientStructurePackageManager CLIENT = new ClientStructurePackageManager();

   @SubscribeEvent
   public static void onPlayerLoggedIn(ClientPlayerNetworkEvent.LoggedInEvent event) {
      if (event.getPlayer() != null) {
         CLIENT.setCurrentStage(ClientStructurePackageManager.Stage.AVAILABLE);
         CLIENT.setPlayerID(event.getPlayer().getUUID());
         ShrinesPacketHandler.sendToServer(new CTSSyncStructureIconsRequest());
         ShrinesPacketHandler.sendToServer(new CTSSyncAvailableDimensionsRequest());
         ShrinesPacketHandler.sendToServer(new CTSSyncInitialPackagesRequest());
      } else {
         LOGGER.error("Failed to initialise CLIENT Package Manager, because no player was available");
      }
   }

   @SubscribeEvent
   public static void onPlayerLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
      // Make sure that we're able to contact the server before we clear everything
      // Don't show an error, because Minecraft logs the player out before a world can be entered and the player is logged in again. At the first log out the connections aren't available
      if (event.getConnection() != null) {
         CLIENT.leaveQueue();
         CLIENT.setCurrentStage(ClientStructurePackageManager.Stage.EMPTY);
         CLIENT.setPlayerID(null);
         CLIENT.setNovels(null);
         CLIENT.setAvailableDimensions(null);
         CLIENT.clearCache();
      }
   }
}
