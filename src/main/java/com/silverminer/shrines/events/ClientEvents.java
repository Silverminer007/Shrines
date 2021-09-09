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

import java.awt.Color;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.utils.client.ClientUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {
	protected static final Logger LOGGER = LogManager.getLogger(ClientEvents.class);

	@EventBusSubscriber(modid = ShrinesMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
	public static class ForgeEventBus {
		@SubscribeEvent
		/**
		 * Render Bounding box to make issue check more easy
		 * 
		 * @param event
		 */
		public static void renderWorldLast(RenderWorldLastEvent event) {
			MatrixStack ms = event.getMatrixStack();
			renderBounds(ms, event.getPartialTicks());
		}

		public static void renderBounds(MatrixStack ms, float partialTick) {
			// Get Minecraft and store it locally for multiple use
			Minecraft mc = Minecraft.getInstance();

			// Get dimension to draw only bounds of the correct dimension
			RegistryKey<World> dim;
			if (mc.level != null) {
				dim = mc.level.dimension();
			} else {
				dim = null;
			}

			// Translate coordinates from players system to world system
			ActiveRenderInfo activerenderinfo = mc.gameRenderer.getMainCamera();
			Vector3d vec = activerenderinfo.getPosition();

			double renderPosX = vec.x();
			double renderPosY = vec.y();
			double renderPosZ = vec.z();
			ms.pushPose();
			ms.translate(-renderPosX, -renderPosY, -renderPosZ);

			// Get vertex builder
			IRenderTypeBuffer.Impl buffer = mc.renderBuffers().bufferSource();
			IVertexBuilder vb = buffer.getBuffer(RenderType.lines());

			// Color of the bound (White)
			Color c = new Color(255);
			// Split up in red, green and blue and transform it to 0.0 - 1.0
			float red = c.getRed() / 255.0f;
			float green = c.getGreen() / 255.0f;
			float blue = c.getBlue() / 255.0f;

			// Iterate over all bounds to draw//TODO Readd later
			/*for (CustomStructureData data : Utils.getStructures(false)) {
				for (ResourceData rd : data.PIECES_ON_FLY) {
					if (rd.getDimension() == dim) {
						MutableBoundingBox mbb = rd.getBounds();
						// Drawing a line box as in StructureTileEntityRenderer#render
						WorldRenderer.renderLineBox(ms, vb, mbb.x0, mbb.y0, mbb.z0, mbb.x1, mbb.y1, mbb.z1, red, green,
								blue, 1.0f, red, green, blue);
					}
				}
			}*/
			ms.popPose();
			// This line is important. Without it rendering does curious things
			buffer.endBatch(RenderType.lines());
		}

		@SubscribeEvent
		public static void onKeyInput(InputEvent.KeyInputEvent event) {
			int keyCode = event.getKey();
			int scanCode = event.getScanCode();
			if (ClientUtils.structuresScreen.matches(keyCode, scanCode) && Minecraft.getInstance().screen == null
					&& Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasPermissions(2)) {
				
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