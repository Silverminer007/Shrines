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
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
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
		 * TODO Something went wrong with matrixstack. everything else is similar to
		 * onBlockHighlight which works perfectly
		 * 
		 * I wish to call my render function here to draw it everytime Here is the frame
		 * drawn wrong: The color is much darker than wished (Dark Green/Black instead
		 * of White) and the bound moves while flying
		 * 
		 * @param event
		 */
		public static void renderWorldLast(RenderWorldLastEvent event) {
			renderBounds(event.getMatrixStack());
		}

		@SubscribeEvent
		/**
		 * This is only a test function. Here is the frame correctly drawn. Color
		 * matches (White - 0xffffff) and the bounds aren't moving while flying
		 * 
		 * @param event
		 */
		public static void renderWorldLast(DrawHighlightEvent.HighlightBlock event) {
			// renderBounds(event.getMatrix());
		}

		public static void renderBounds(MatrixStack ms) {
			// Enable depth test first
			RenderSystem.depthMask(false);

			// Get Minecraft and store it locally for multiple use
			Minecraft mc = Minecraft.getInstance();

			// Get dimension to draw only bounds of the correct dimension
			RegistryKey<World> dim;
			if (mc.level != null) {
				dim = mc.level.dimension();
			} else {
				dim = null;
			}

			// This doesn't work in both ways. What's the correct / working way to do that?
			RenderSystem.lineWidth(30.0f);

			// Translate coordinates from players system to world system
			Vector3d vec = mc.gameRenderer.getMainCamera().getPosition();
			double renderPosX = vec.x();
			double renderPosY = vec.y();
			double renderPosZ = vec.z();
			ms.pushPose();
			ms.translate(-renderPosX, -renderPosY, -renderPosZ);

			// Get vertex builder
			IRenderTypeBuffer irendertypebuffer1 = mc.renderBuffers().bufferSource();
			IVertexBuilder vb = irendertypebuffer1.getBuffer(RenderType.lines());

			// Color of the bound (White)
			Color c = new Color(Utils.properties.bound_color);
			// Split up in red, green and blue and transform it to 0.0 - 1.0
			float red = c.getRed() / 255.0f;
			float green = c.getGreen() / 255.0f;
			float blue = c.getBlue() / 255.0f;

			// Iterate over all bounds to draw
			for (CustomStructureData data : Utils.DATAS_FROM_SERVER) {
				for (ResourceData rd : data.PIECES_ON_FLY) {
					if (rd.getDimension() == dim) {
						MutableBoundingBox mbb = rd.getBounds();
						// Drawing a line box as in StructureTileEntityRenderer#render
						WorldRenderer.renderLineBox(ms, vb, mbb.x0, mbb.y0, mbb.z0, mbb.x1, mbb.y1, mbb.z1, red, green,
								blue, 1.0f, red, green, blue);
					}
				}
			}
			ms.popPose();
		}
	}
}