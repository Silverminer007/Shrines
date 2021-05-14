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

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.structures.custom.helper.ResourceData;
import com.silverminer.shrines.utils.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class ClientEvents {
	protected static final Logger LOGGER = LogManager.getLogger(ClientEvents.class);

	@EventBusSubscriber(modid = Shrines.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
	public static class ForgeEventBus {
		@SubscribeEvent
		/**
		 * TODO Fix bound is fliccering around with camera rotation
		 * TODO Fix: Bound is using wrong color
		 * TODO Fix multiple bound: They're overlapping
		 * TODO Add properties file with options:
		 * TODO - Bound color
		 * TODO - remove bounds after add
		 * 
		 * @param event
		 */
		public static void renderWorldLast(RenderWorldLastEvent event) {
			RenderSystem.depthMask(false);
			Minecraft mc = Minecraft.getInstance();
			double renderPosX = mc.getEntityRenderDispatcher().camera.getPosition().x();
			double renderPosY = mc.getEntityRenderDispatcher().camera.getPosition().y();
			double renderPosZ = mc.getEntityRenderDispatcher().camera.getPosition().z();
			MatrixStack ms = event.getMatrixStack();
			RenderTypeBuffers rtb = mc.renderBuffers();
			IRenderTypeBuffer.Impl impl = rtb.bufferSource();
			IRenderTypeBuffer irendertypebuffer1 = impl;
			IVertexBuilder vb = irendertypebuffer1.getBuffer(RenderType.lines());
			ms.pushPose();
			ms.translate(-renderPosX, -renderPosY, -renderPosZ);
			for (ArrayList<ResourceData> datas : Utils.BOUNDS_TO_DRAW.values()) {
				for (ResourceData rd : datas) {
					MutableBoundingBox mbb = rd.getBounds();
					WorldRenderer.renderLineBox(ms, vb, mbb.x0, mbb.y0, mbb.z0, mbb.x1, mbb.y1, mbb.z1, 0.1F, 0.1F, 0.1F, 1.0F);
				}
			}
			ms.popPose();
		}
	}
}