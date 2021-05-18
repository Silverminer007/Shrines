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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.structures.custom.helper.CustomStructureData;
import com.silverminer.shrines.structures.custom.helper.ResourceData;
import com.silverminer.shrines.utils.custom_structures.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
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
		 * TODO Bounds moves with animations (relative to the blocks) flying causes
		 * errors -> standing not -> Seems not to happen at blovkhighlight so may
		 * activeRenderInfo?
		 * 
		 * @param event
		 */
		public static void renderWorldLast(RenderWorldLastEvent event) {
			RenderSystem.depthMask(false);
			Minecraft mc = Minecraft.getInstance();
			RegistryKey<World> dim;
			if (mc.level != null) {
				dim = mc.level.dimension();
			} else {
				dim = null;
			}
			MatrixStack ms = event.getMatrixStack();
			ActiveRenderInfo activerenderinfo = mc.gameRenderer.getMainCamera();
			activerenderinfo.setup(mc.level, (Entity) (mc.getCameraEntity() == null ? mc.player : mc.getCameraEntity()),
					!mc.options.getCameraType().isFirstPerson(), mc.options.getCameraType().isMirrored(),
					event.getPartialTicks());
			Vector3d vec = activerenderinfo.getPosition();
			double renderPosX = vec.x();
			double renderPosY = vec.y();
			double renderPosZ = vec.z();
			IRenderTypeBuffer irendertypebuffer1 = mc.renderBuffers().bufferSource();
			IVertexBuilder vb = irendertypebuffer1.getBuffer(RenderType.lines());
			ms.pushPose();
			ms.translate(-renderPosX, -renderPosY, -renderPosZ);
			Color c = new Color(Utils.properties.bound_color);
			float red = c.getRed() / 255.0f;
			float green = c.getGreen() / 255.0f;
			float blue = c.getBlue() / 255.0f;
			for (CustomStructureData data : Utils.DATAS_FROM_SERVER) {
				for (ResourceData rd : data.PIECES_ON_FLY) {
					if (rd.getDimension() == dim) {
						MutableBoundingBox mbb = rd.getBounds();
						WorldRenderer.renderLineBox(ms, vb, mbb.x0, mbb.y0, mbb.z0, mbb.x1, mbb.y1, mbb.z1, red, green,
								blue, 1.0f);
					}
				}
			}
			ms.popPose();
		}

		// @SubscribeEvent
		/**
		 * 
		 * @param event
		 */
		public static void renderWorldLast(DrawHighlightEvent.HighlightBlock event) {
			RenderSystem.depthMask(false);
			Vector3d vec = event.getInfo().getPosition();
			double renderPosX = vec.x();
			double renderPosY = vec.y();
			double renderPosZ = vec.z();
			MatrixStack ms = event.getMatrix();
			Minecraft mc = Minecraft.getInstance();
			IRenderTypeBuffer irendertypebuffer1 = mc.renderBuffers().bufferSource();
			IVertexBuilder vb = irendertypebuffer1.getBuffer(RenderType.lines());
			ms.pushPose();
			ms.translate(-renderPosX, -renderPosY, -renderPosZ);
			Color c = new Color(Utils.properties.bound_color);
			float red = c.getRed() / 255.0f;
			float green = c.getGreen() / 255.0f;
			float blue = c.getBlue() / 255.0f;
			for (CustomStructureData data : Utils.DATAS_FROM_SERVER) {
				for (ResourceData rd : data.PIECES_ON_FLY) {
					MutableBoundingBox mbb = rd.getBounds();
					WorldRenderer.renderLineBox(ms, vb, mbb.x0, mbb.y0, mbb.z0, mbb.x1, mbb.y1, mbb.z1, red, green,
							blue, 1.0f);
				}
			}
			ms.popPose();
		}
	}
}