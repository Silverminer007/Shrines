/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.packages.client;

import com.silverminer.shrines.gui.novels.StructureNovelsOverviewScreen;
import com.silverminer.shrines.gui.packets.SkipableScreen;
import com.silverminer.shrines.gui.packets.StructuresPacketsScreen;
import com.silverminer.shrines.gui.packets.WaitInQueueScreen;
import com.silverminer.shrines.gui.packets.WorkingScreen;
import com.silverminer.shrines.packages.container.NovelDataContainer;
import com.silverminer.shrines.packages.container.StructureIconContainer;
import com.silverminer.shrines.packages.container.StructurePackageContainer;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import com.silverminer.shrines.packages.io.DirectoryStructureAccessor;
import com.silverminer.shrines.packages.io.PackageIOException;
import com.silverminer.shrines.utils.CalculationError;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class ClientStructurePackageManager {
   protected static final Logger LOGGER = LogManager.getLogger(ClientStructurePackageManager.class);
   private Stage currentStage = Stage.EMPTY;
   private int queuePosition = -1;
   private UUID playerID = null;
   private NovelDataContainer novelDataContainer;
   private List<String> availableDimensions;
   private StructurePackageContainer initialPackages;
   protected StructurePackageContainer packages = new StructurePackageContainer();

   public StructurePackageContainer getPackages() {
      return this.packages;
   }

   private Minecraft getMinecraft() {
      return Minecraft.getInstance();
   }

   public Stage getCurrentStage() {
      return currentStage;
   }

   public void setCurrentStage(Stage currentStage) {
      this.currentStage = currentStage;
   }

   public UUID getPlayerID() {
      return playerID;
   }

   public void setPlayerID(UUID playerID) {
      this.playerID = playerID;
   }

   public void joinQueue() {
      // You can only join the queue if you aren't in it yet or were already allowed to edit packages. You should also only join the queue if the packages are actually available
      if (this.getCurrentStage().equals(Stage.AVAILABLE)) {
         this.setCurrentStage(Stage.QUEUE);
         ShrinesPacketHandler.sendToServer(new CTSPlayerJoinQueue(this.getPlayerID()));
         this.getMinecraft().setScreen(new WaitInQueueScreen(this.getMinecraft().screen));
      }
   }

   public int getQueuePosition() {
      if (this.getCurrentStage() == Stage.QUEUE) {
         return this.queuePosition;
      } else {
         return -1;
      }
   }

   public void updateQueuePosition(int queuePosition) {
      this.queuePosition = queuePosition;
      if (this.queuePosition == 0 && this.getCurrentStage() != Stage.EDIT) {
         this.loadPackages();
      }
   }

   public void leaveQueue() {
      if (this.getCurrentStage().equals(Stage.QUEUE)) {
         this.setCurrentStage(Stage.AVAILABLE);
      }
      this.updateQueuePosition(-1);
      ShrinesPacketHandler.sendToServer(new CTSPlayerLeaveQueue(this.getPlayerID()));
   }

   public void importPackage() {
      TranslatableComponent title = new TranslatableComponent("gui.shrines.import.select");
      String s = TinyFileDialogs.tinyfd_openFileDialog(title.getString(), null, null, null, false);
      if (s == null) {
         return;
      }
      try {
         byte[] archive = Files.readAllBytes(Paths.get(s));
         ShrinesPacketHandler.sendToServer(new CTSImportPackage(archive, this.getPackages()));
         this.getMinecraft().setScreen(new ProgressScreen(true));
      } catch (IOException e) {
         this.onError(new CalculationError("Failed to import package", "Caused by IO Error: %s", e));
      }
   }

   public void exportPackage(StructuresPackageWrapper packageWrapper) {
      ShrinesPacketHandler.sendToServer(new CTSExportPackage(packageWrapper.getPackageID(), this.getPackages()));
   }

   public void saveExportedPackage(byte[] packageFile) {
      TranslatableComponent title = new TranslatableComponent("gui.shrines.export.select");
      String s = TinyFileDialogs.tinyfd_selectFolderDialog(title.getString(), System.getProperty("user.home"));
      if (s == null) {
         return;
      }
      try {
         Files.write(Paths.get(s, "Structures-Package.zip"), packageFile);
      } catch (IOException e) {
         this.onError(new CalculationError("Failed to export package", "Failed to save exported package. Caused by: %s", e));
      }
   }

   public void loadPackages() {
      ShrinesPacketHandler.sendToServer(new CTSSyncPackagesRequest());
   }

   public void setPackages(StructurePackageContainer packages) {
      this.packages = packages;
      this.setCurrentStage(Stage.EDIT);
      Screen lastScreen = this.getMinecraft().screen;
      while (lastScreen instanceof SkipableScreen skipableScreen) {
         lastScreen = skipableScreen.getLastScreen();
      }
      this.getMinecraft().setScreen(new StructuresPacketsScreen(lastScreen));
   }

   public void savePackages() {
      ShrinesPacketHandler.sendToServer(new CTSSavePackages(this.getPackages()));
      this.setCurrentStage(Stage.AVAILABLE);
   }

   public void stopEditing() {
      this.setCurrentStage(Stage.AVAILABLE);
   }

   public void handleSaveResult(boolean success) {
      if (this.getMinecraft().screen instanceof WorkingScreen savingScreen) {
         this.getMinecraft().setScreen(success ? savingScreen.getLastScreen() : savingScreen.getScreenOnFail());
         if (success) {
            this.stopEditing();
         }
      } else {
         this.getMinecraft().setScreen(null);
         this.stopEditing();
      }
   }

   public List<String> getAvailableDimensions() {
      return availableDimensions;
   }

   public void setAvailableDimensions(List<String> availableDimensions) {
      this.availableDimensions = availableDimensions;
   }

   public void showNovelsOverview() {
      ShrinesPacketHandler.sendToServer(new CTSSyncNovelsRequest());
   }

   public void openNovelsOverviewScreen() {
      this.getMinecraft().setScreen(new StructureNovelsOverviewScreen(this.getMinecraft().screen));
   }

   public StructurePackageContainer getInitialPackages() {
      return initialPackages;
   }

   public void setInitialPackages(StructurePackageContainer initialPackages) {
      this.initialPackages = initialPackages;
   }

   public NovelDataContainer getNovels() {
      return this.novelDataContainer;
   }

   public void setNovels(NovelDataContainer novels) {
      this.novelDataContainer = novels;
   }

   public void cacheStructureIcons(StructureIconContainer structureIconContainer) {
      try {
         Files.delete(DirectoryStructureAccessor.RECENT.getImagesCachePath());
      } catch (IOException e) {
         this.onError(new CalculationError("Unable to cache structure icons", "Caused by: %s", e));
      }
      for (ResourceLocation key : structureIconContainer.getKeys()) {
         Path iconPath = DirectoryStructureAccessor.RECENT.getStructureIconPath(key, DirectoryStructureAccessor.RECENT.getImagesCachePath(), true);
         try {
            if (!Files.exists(iconPath.getParent())) {
               Files.createDirectories(iconPath.getParent());
            }
            Files.write(iconPath, structureIconContainer.getByKey(key));
         } catch (IOException e) {
            this.onError(new CalculationError("Unable to cache structure icons", "Phase: save icons to cache. Caused by: %s", e));
         }
      }
   }

   public void clearCache() {
      try {
         DirectoryStructureAccessor.RECENT.clearCache();
      } catch (PackageIOException e) {
         this.onError(e.getReason());
      }
   }

   public void onError(CalculationError error) {
      ClientUtils.showErrorToast(error);
      LOGGER.error(error);
   }

   public enum Stage {
      EMPTY, AVAILABLE, QUEUE, EDIT
   }
}