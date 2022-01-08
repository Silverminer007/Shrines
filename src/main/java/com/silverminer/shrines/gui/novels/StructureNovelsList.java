/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.novels;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.gui.misc.IconList;
import com.silverminer.shrines.gui.novels.StructureNovelsList.StructureNovelsEntry;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSFetchNovelAmountPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StructureNovelsList extends IconList<StructureNovelsEntry> {
	protected static final Logger LOGGER = LogManager.getLogger(StructureNovelsList.class);

	public StructureNovelsList(Minecraft minecraft, int width, int height, int y0, int y1, int itemHeight,
			Supplier<String> search, List<StructuresPacket> packets) {
		super(minecraft, width, height, y0, y1, itemHeight);
		this.refreshList(search, packets);
	}

	public void refreshList(Supplier<String> filter, List<StructuresPacket> packets) {
		this.clearEntries();
		String s = filter.get().toLowerCase(Locale.ROOT);
		for (StructuresPacket packet : packets) {
			for (StructureData data : packet.getStructures()) {
				if (data.getName().toLowerCase(Locale.ROOT).contains(s)
						|| data.getKey().toLowerCase(Locale.ROOT).contains(s)) {
					if (!data.getNovel().isEmpty()) {
						this.addEntry(new StructureNovelsEntry(data));
					}
				}
			}
		}
	}

	public class StructureNovelsEntry extends IconList.AbstractListEntry<StructureNovelsEntry> {
		private final Minecraft minecraft;
		private final StructureData data;
		private final ResourceLocation iconLocation;

		public StructureNovelsEntry(StructureData data) {
			this.data = data;
			this.minecraft = Minecraft.getInstance();
			this.iconLocation = new ResourceLocation(ShrinesMod.MODID,
					"textures/structures/" + new ResourceLocation(data.getKey()).getPath() + ".png");
		}

		@SuppressWarnings("deprecation")
		@Override
		public void render(MatrixStack matrixStack, int x1, int x, int y, int y2, int p_230432_6_, int p_230432_7_,
				int p_230432_8_, boolean isHovered, float p_230432_10_, int itemSize) {
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.minecraft.getTextureManager().bind(this.iconLocation);
			RenderSystem.enableBlend();
			AbstractGui.blit(matrixStack, y, x, 0.0F, 0.0F, itemSize, itemSize, itemSize, itemSize);
			if (isHovered || StructureNovelsList.this.getSelected() == this) {
				RenderSystem.disableTexture();
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferbuilder = tessellator.getBuilder();
				RenderSystem.color4f(0.0F, 0.0F, 0.0F, 0.75F);
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
				int j = itemSize - itemSize / 4;
				y = y + itemSize;
				bufferbuilder.vertex((double) y - itemSize, (double) x + j, 0.0D).endVertex();// A
				bufferbuilder.vertex((double) y - itemSize, (double) x + itemSize, 0.0D).endVertex();// D
				bufferbuilder.vertex(y, (double) x + itemSize, 0.0D).endVertex();// C
				bufferbuilder.vertex(y, (double) x + j, 0.0D).endVertex();// B
				tessellator.end();
				RenderSystem.enableTexture();
				String name = this.data.getName();
				int stringWidth = this.minecraft.font.width(name);
				if (stringWidth < itemSize || !name.contains(" "))
					this.minecraft.font.draw(matrixStack, name, y - (itemSize + stringWidth) / 2.0F, x + itemSize / 8.0F * 7, 0xffffff);
				else {
					int idx = name.indexOf(" ");
					String s1 = name.substring(0, idx);
					String s2 = name.substring(idx);
					stringWidth = this.minecraft.font.width(s1);
					this.minecraft.font.draw(matrixStack, s1, y - (itemSize + stringWidth) / 2.0F, x + itemSize / 16.0F * 13, 0xffffff);
					stringWidth = this.minecraft.font.width(s2);
					this.minecraft.font.draw(matrixStack, s2, y - (itemSize + stringWidth) / 2.0F, x + itemSize / 16.0F * 15, 0xffffff);
				}
			}
			RenderSystem.disableBlend();
		}

		public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmount) {
			if (StructureNovelsList.this.getSelected() == this) {
				ShrinesPacketHandler.sendToServer(new CTSFetchNovelAmountPacket(this.data));
				return true;
			} else {
				StructureNovelsList.this.setSelected(this);
				return false;
			}
		}
	}
}