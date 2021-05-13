package com.silverminer.shrines.events;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.silverminer.shrines.Shrines;
import com.silverminer.shrines.structures.custom.helper.ResourceData;
import com.silverminer.shrines.utils.Utils;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ClientEvents {
	protected static final Logger LOGGER = LogManager.getLogger(ClientEvents.class);

	@EventBusSubscriber(modid = Shrines.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
	public static class ForgeEventBus {
		@SubscribeEvent
		/**
		 * TODO Fix Structure bounds aren't drawn
		 * @param event
		 */
		public static void renderWorldLast(RenderWorldLastEvent event) {
			MatrixStack ms = event.getMatrixStack();
			RenderTypeBuffers rtb = (RenderTypeBuffers) ObfuscationReflectionHelper
					.getPrivateValue(WorldRenderer.class, event.getContext(), "renderBuffers");
			IRenderTypeBuffer.Impl impl = rtb.bufferSource();
			IRenderTypeBuffer irendertypebuffer1 = impl;
			IVertexBuilder vb = irendertypebuffer1.getBuffer(RenderType.lines());
			for (ArrayList<ResourceData> datas : Utils.BOUNDS_TO_DRAW.values()) {
				for (ResourceData rd : datas) {
					MutableBoundingBox mbb = rd.getBounds();
					WorldRenderer.renderLineBox(ms, vb, AxisAlignedBB.of(mbb), 0.9F, 0.9F, 0.9F, 1.0F);
				}
			}
		}
	}
}