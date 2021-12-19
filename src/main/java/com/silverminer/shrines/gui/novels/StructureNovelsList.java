package com.silverminer.shrines.gui.novels;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.gui.misc.IconList;
import com.silverminer.shrines.gui.novels.StructureNovelsList.StructureNovelsEntry;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.load.StructuresPacket;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSFetchNovelAmountPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

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

	@Override
	public NarrationPriority narrationPriority() {
		return NarrationPriority.HOVERED;
	}

	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {

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
		public void render(PoseStack matrixStack, int x1, int x, int y, int y2, int p_230432_6_, int p_230432_7_,
						   int p_230432_8_, boolean isHovered, float p_230432_10_, int itemSize) {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, this.iconLocation);
			RenderSystem.enableBlend();
			blit(matrixStack, y, x, 0.0F, 0.0F, itemSize, itemSize, itemSize, itemSize);
			if (isHovered || StructureNovelsList.this.getSelected() == this) {
				RenderSystem.disableTexture();
				Tesselator tessellator = Tesselator.getInstance();
				BufferBuilder bufferbuilder = tessellator.getBuilder();
				RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 0.75F);
				bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
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