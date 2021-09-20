package com.silverminer.shrines.client.gui.novels;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.client.gui.IconList;
import com.silverminer.shrines.client.gui.novels.StructureNovelsList.StructureNovelsEntry;
import com.silverminer.shrines.new_custom_structures.StructureData;
import com.silverminer.shrines.new_custom_structures.StructuresPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class StructureNovelsList extends IconList<StructureNovelsEntry> {
	protected static final Logger LOGGER = LogManager.getLogger(StructureNovelsList.class);
	private final StructureNovelsScreen screen;

	public StructureNovelsList(Minecraft minecraft, int width, int height, int y0, int y1, int itemHeight,
			Supplier<String> search, List<StructuresPacket> packets, StructureNovelsScreen screen) {
		super(minecraft, width, height, y0, y1, itemHeight);
		this.refreshList(search, packets);
		this.screen = screen;
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
				int i = itemSize;
				int j = i - i / 4;
				y = y + i;
				bufferbuilder.vertex((double) y - i, (double) x + j, 0.0D).endVertex();// A
				bufferbuilder.vertex((double) y - i, (double) x + i, 0.0D).endVertex();// D
				bufferbuilder.vertex((double) y, (double) x + i, 0.0D).endVertex();// C
				bufferbuilder.vertex((double) y, (double) x + j, 0.0D).endVertex();// B
				tessellator.end();
				RenderSystem.enableTexture();
				String name = this.data.getName();
				int stringWidth = this.minecraft.font.width(name);
				if (stringWidth < i || !name.contains(" "))
					this.minecraft.font.draw(matrixStack, name, y - (i + stringWidth) / 2, x + i / 8 * 7, 0xffffff);
				else {
					int idx = name.indexOf(" ");
					String s1 = name.substring(0, idx);
					String s2 = name.substring(idx);
					stringWidth = this.minecraft.font.width(s1);
					this.minecraft.font.draw(matrixStack, s1, y - (i + stringWidth) / 2, x + i / 16 * 13, 0xffffff);
					stringWidth = this.minecraft.font.width(s2);
					this.minecraft.font.draw(matrixStack, s2, y - (i + stringWidth) / 2, x + i / 16 * 15, 0xffffff);
				}
			}
			RenderSystem.disableBlend();
		}

		public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmount) {
			if (StructureNovelsList.this.getSelected() == this) {
				StructureNovelsList.this.minecraft
						.setScreen(new StructureNovelScreen(StructureNovelsList.this.screen, this.data));
				return true;
			} else {
				StructureNovelsList.this.setSelected(this);
				return false;
			}
		}
	}
}