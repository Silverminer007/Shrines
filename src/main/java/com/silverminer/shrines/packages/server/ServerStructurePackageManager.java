package com.silverminer.shrines.packages.server;

import com.silverminer.shrines.packages.StructurePackageManager;
import com.silverminer.shrines.packages.container.NovelDataContainer;
import com.silverminer.shrines.packages.container.StructurePackageContainer;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import com.silverminer.shrines.packages.io.PackageIOException;
import com.silverminer.shrines.packages.io.StructurePackageIOManager;
import com.silverminer.shrines.utils.CalculationError;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.stc.*;
import com.silverminer.shrines.utils.queue.PlayerQueue;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

public class ServerStructurePackageManager extends StructurePackageManager {
   protected static final Logger LOGGER = LogManager.getLogger(ServerStructurePackageManager.class);
   protected final StructurePackageIOManager ioManager = StructurePackageIOManager.INSTANCE;
   protected StructurePackageContainer initialStructurePackages;
   protected final PlayerQueue playerQueue = new PlayerQueue();

   public StructurePackageContainer getInitialStructurePackages() {
      return initialStructurePackages;
   }

   public void bootstrapPackages() {
      if (this.getPackages() == null || this.getPackages().getSize() == 0) {
         this.earlyLoadPackages();
      }
      this.initialStructurePackages = this.getPackages();
   }

   public void onPlayerJoinQueue(UUID playerID) {
      this.playerQueue.join(playerID);
   }

   public void onPlayerLeaveQueue(UUID playerID) {
      this.playerQueue.leave(playerID);
   }

   public void syncPackagesToClient(UUID playerID) {
      if (this.playerQueue.getQueue().size() > 0 && this.playerQueue.getQueue().get(0).equals(playerID)) {
         ShrinesPacketHandler.sendTo(new STCSyncPackages(this.getPackages()), playerID);
      } else {
         this.onError(new CalculationError("Received invalid save request from client", "Got package save request from client that hadn't had the permission to do that"), playerID);
      }
   }

   public void syncAvailableDimensionsToClient(UUID uuid) {
      List<String> availableDimensions = ServerLifecycleHooks.getCurrentServer().levelKeys().stream().map(ResourceKey::location).map(ResourceLocation::toString).toList();
      ShrinesPacketHandler.sendTo(new STCSyncAvailableDimensions(availableDimensions), uuid);
   }

   public void syncNovelsToClient(UUID uuid) {
      ShrinesPacketHandler.sendTo(new STCSyncNovels(this.getNovels()), uuid);
   }

   public void syncStructureIconsToClient(UUID uuid) {
      try {
         ShrinesPacketHandler.sendTo(new STCSyncStructureIcons(this.ioManager.loadStructureIcons(this.getPackages())), uuid);
      } catch (PackageIOException e) {
         this.onError(e.getReason());
      }
   }

   public void clientSavedPackages(StructurePackageContainer packageContainer, UUID clientID) {
      if (this.playerQueue.getQueue().size() > 0 && this.playerQueue.getQueue().get(0).equals(clientID)) {
         this.packages = packageContainer;
         boolean success = this.savePackages();
         ShrinesPacketHandler.sendTo(new STCSendSaveResult(success), clientID);
      }
   }

   public void importPackage(byte[] packageFile) {
      try {
         StructuresPackageWrapper structuresPackageWrapper = this.ioManager.importPackage(packageFile);
         this.getPackages().add(structuresPackageWrapper);
         if (this.playerQueue.getQueue().size() > 0) {
            this.syncPackagesToClient(this.playerQueue.getQueue().get(0));
         } else {
            this.onError(new CalculationError("Failed to sync packages back to client after import", "No player was found"));
         }
      } catch (PackageIOException e) {
         this.onError(new CalculationError("Failed to import package", "Caused by: %s", e));
      }
   }

   public void exportPackage(UUID packageID) {
      try {
         StructuresPackageWrapper structuresPackageWrapper = this.getPackages().getByKey(packageID);
         if (structuresPackageWrapper != null) {
            byte[] packageFile = this.ioManager.exportPackage(structuresPackageWrapper);
            if (this.playerQueue.getQueue().size() > 0) {
               ShrinesPacketHandler.sendTo(new STCSaveExportedPackage(packageFile), this.playerQueue.getQueue().get(0));
            } else {
               this.onError(new CalculationError("Failed to send exported package to client", "No player was found"));
            }
         }
      } catch (PackageIOException e) {
         this.onError(new CalculationError("Failed to export package", "Caused by: %s", e));
      }
   }

   @Override
   public NovelDataContainer getNovels() {
      return NovelsDataRegistry.INSTANCE.getNovelsData();
   }

   @Override
   public void setNovels(NovelDataContainer novels) {
      NovelsDataRegistry.INSTANCE.setNovelsData(novels);
   }

   @Override
   public void onError(CalculationError error) {
      if (this.playerQueue.getQueue().size() > 0 && this.playerQueue.getQueue().get(0) != null) {
         this.onError(error, this.playerQueue.getQueue().get(0));
      } else {
         this.onError(error, new UUID[0]);
      }
   }

   public void onError(CalculationError error, UUID... clientIDs) {
      for (UUID client : clientIDs) {
         ShrinesPacketHandler.sendTo(new STCError(error), client, false);
      }
      LOGGER.error(error);
   }

   public void loadPackages() {
      try {
         this.packages = this.ioManager.loadPackages();
      } catch (PackageIOException e) {
         this.onError(new CalculationError("Failed to load packages", "Caused by: %s", e.getReason()));
         e.printStackTrace();
      }
   }

   public void earlyLoadPackages() {
      try {
         this.packages = this.ioManager.earlyLoadPackages();
      } catch (PackageIOException e) {
         this.onError(new CalculationError("Failed to load packages", "Caused by: %s", e.getReason()));
         e.printStackTrace();
      }
   }

   public boolean savePackages() {
      try {
         this.ioManager.savePackages(this.packages);
         return true;
      } catch (PackageIOException e) {
         this.onError(new CalculationError("Failed to save packages", "Caused by: %s", e.getReason()));
         e.printStackTrace();
         return false;
      }
   }
}