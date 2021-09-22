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
package com.silverminer.shrines.events;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.network.CTSFetchStructuresPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {
	protected static final Logger LOGGER = LogManager.getLogger(ClientEvents.class);

	@EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
	public static class ForgeEventBus {
		@SubscribeEvent
		public static void onKeyInput(InputEvent.KeyInputEvent event) {
			int keyCode = event.getKey();
			int scanCode = event.getScanCode();
			Minecraft mc = Minecraft.getInstance();
			if (ClientUtils.structuresScreen.matches(keyCode, scanCode) && mc.screen == null && mc.player != null
					&& mc.player.hasPermissions(2)) {
				ShrinesPacketHandler.sendToServer(new CTSFetchStructuresPacket(mc.player, false));
				mc.setScreen(new WorkingScreen());
			}
		}
	}

	@EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
	public static class ModEventBus {
		@SubscribeEvent
		public static void clientSetupEvent(FMLClientSetupEvent event) {
			Minecraft mc = event.getMinecraftSupplier().get();
			KeyBinding[] keyMappings = mc.options.keyMappings;
			ClientUtils.structuresScreen = new KeyBinding("key.customStructuresScreen", 75, "key.categories.shrines");
			keyMappings = ArrayUtils.addAll(keyMappings, ClientUtils.structuresScreen);
			mc.options.keyMappings = keyMappings;
		}
	}
}