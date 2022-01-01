/**
 * Silverminer (and Team)
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the MPL
 * (Mozilla Public License 2.0) for more details.
 * <p>
 * You should have received a copy of the MPL (Mozilla Public License 2.0)
 * License along with this library; if not see here: https://www.mozilla.org/en-US/MPL/2.0/
 */
package com.silverminer.shrines.events;

import com.mojang.blaze3d.platform.InputConstants;
import com.silverminer.shrines.gui.novels.StructureNovelsOverviewScreen;
import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.io.DirectoryStructureAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.utils.ClientUtils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {
   protected static final Logger LOGGER = LogManager.getLogger(ClientEvents.class);

   @EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
   public static class ForgeEventBus {
      @SubscribeEvent
      public static void onClientTick(TickEvent.ClientTickEvent event) {
         Minecraft mc = Minecraft.getInstance();
         if (ClientUtils.editPackagesKeyMapping.isDown()) {
            PackageManagerProvider.CLIENT.joinQueue();
         }
         if (ClientUtils.openNovelsKeyMapping.isDown()) {
            mc.setScreen(new StructureNovelsOverviewScreen(mc.screen));
         }
      }
   }

   @EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
   public static class ModEventBus {
      @SubscribeEvent
      public static void clientSetupEvent(FMLClientSetupEvent event) {
         ClientUtils.openNovelsKeyMapping = new KeyMapping("key.showStructureNovels", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.getKey(InputConstants.KEY_K, 0), "key.categories.shrines");
         ClientRegistry.registerKeyBinding(ClientUtils.openNovelsKeyMapping);
         ClientUtils.editPackagesKeyMapping = new KeyMapping("key.customStructuresScreen", KeyConflictContext.IN_GAME, KeyModifier.ALT, InputConstants.getKey(InputConstants.KEY_K, 0), "key.categories.shrines");
         ClientRegistry.registerKeyBinding(ClientUtils.editPackagesKeyMapping);
      }
      @SubscribeEvent
      public static void addPackFinder(AddPackFindersEvent event) {
         // Register the icons Cache here on client only and the packages in CommonEvents, because we need these on both sides
         PackSource source = PackSource.decorating("pack.source.shrines");
         event.addRepositorySource(new FolderRepositorySource(DirectoryStructureAccessor.RECENT.getImagesCachePath().toFile(), source));
      }
   }
}