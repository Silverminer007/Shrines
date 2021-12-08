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

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.event.TickEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.utils.ClientUtils;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSFetchStructuresPacket;

import net.minecraft.client.Minecraft;
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
            if (ClientUtils.structuresScreen.matches(keyCode, scanCode) && mc.screen == null && mc.player != null) {
                ShrinesPacketHandler.sendToServer(new CTSFetchStructuresPacket(false));
                mc.setScreen(new ProgressScreen(true));
            }
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            Minecraft mc = Minecraft.getInstance();
            if (ClientUtils.structuresScreen.isDown() && mc.screen == null && mc.player != null) {
                ShrinesPacketHandler.sendToServer(new CTSFetchStructuresPacket(false));
                mc.setScreen(new ProgressScreen(true));
            }
        }
    }

    @EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
    public static class ModEventBus {
        @SubscribeEvent
        public static void clientSetupEvent(FMLClientSetupEvent event) {
            ClientUtils.structuresScreen = new KeyMapping("key.customStructuresScreen", 75, "key.categories.shrines");
            ClientRegistry.registerKeyBinding(ClientUtils.structuresScreen);
        }
    }
}