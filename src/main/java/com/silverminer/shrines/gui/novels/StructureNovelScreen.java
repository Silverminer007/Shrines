package com.silverminer.shrines.gui.novels;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.silverminer.shrines.ShrinesMod;
import com.silverminer.shrines.structures.load.StructureData;
import com.silverminer.shrines.structures.novels.NovelsDataRegistry;
import com.silverminer.shrines.utils.network.ShrinesPacketHandler;
import com.silverminer.shrines.utils.network.cts.CTSFetchStructuresPacket;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StructureNovelScreen extends Screen {
	protected static final Logger LOGGER = LogManager.getLogger(StructureNovelScreen.class);
	private double scrollAmount;
	private int bottomHeight;
	private int headerHeight;
	private List<String> lines = Lists.newArrayList();
	private List<String> words = Lists.newArrayList();
	private boolean renderInfo = false;
	private boolean scrolling;

	public StructureNovelScreen(StructureData structure, double novelAmount) {
		super(new TranslationTextComponent("What is " + structure.getName() + "?", structure));// TRANSLATION
		String novel = structure.getNovel();
		double amount = NovelsDataRegistry.getNovelAmount(structure.getKey());
		if (amount < 1.0D) {
			this.renderInfo = true;
		}
		this.words = Arrays.asList(novel.replace("\n", " \n ").split(" "));
		this.words = this.words.subList(0, (int) (this.words.size() * amount));
	}

	public void onClose() {
		ShrinesPacketHandler.sendToServer(new CTSFetchStructuresPacket(this.minecraft.player, false));
	}

	public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double p_231043_5_) {
		this.setScrollAmount(this.getScrollAmount() - p_231043_5_ * 20.0D);
		return true;
	}

	public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_,
			double p_231045_8_) {
		if (super.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_)) {
			return true;
		} else if (p_231045_5_ == 0 && this.scrolling) {
			if (p_231045_3_ < (double) this.headerHeight) {
				this.setScrollAmount(0.0D);
			} else if (p_231045_3_ > (double) this.bottomHeight) {
				this.setScrollAmount((double) this.getMaxScroll());
			} else {
				double d0 = (double) Math.max(1, this.getMaxScroll());
				int i = this.bottomHeight - this.headerHeight;
				int j = MathHelper.clamp((int) ((float) (i * i) / (float) this.getMaxPosition()), 32, i - 8);
				double d1 = Math.max(1.0D, d0 / (double) (i - j));
				this.setScrollAmount(this.getScrollAmount() + p_231045_8_ * d1);
			}

			return true;
		} else {
			return false;
		}
	}

	private void scroll(int p_230937_1_) {
		this.setScrollAmount(this.getScrollAmount() + (double) p_230937_1_);
	}

	public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		if (super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_)) {
			return true;
		} else if (p_231046_1_ == 264) {
			this.scroll(10);
			return true;
		} else if (p_231046_1_ == 265) {
			this.scroll(-10);
			return true;
		} else {
			return false;
		}
	}

	protected void init() {
		this.addButton(new ImageButton(2, 2, 91, 20, 0, 0, 20,
				new ResourceLocation(ShrinesMod.MODID, "textures/gui/widgets.png"), 256, 256, (button) -> {
					this.onClose();
				}, StringTextComponent.EMPTY));
	}

	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
		this.updateScrollingState(p_231044_1_, p_231044_3_, p_231044_5_);
		return true;
	}

	protected void updateScrollingState(double p_230947_1_, double p_230947_3_, int p_230947_5_) {
		this.scrolling = p_230947_5_ == 0 && p_230947_1_ >= (double) this.getScrollbarPosition()
				&& p_230947_1_ < (double) (this.getScrollbarPosition() + 6);
	}

	public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
		if (this.getFocused() != null) {
			this.getFocused().mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
		}

		return false;
	}

	@SuppressWarnings("deprecation")
	public void render(MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.bottomHeight = this.renderInfo ? this.height - 20 : this.height;
		this.headerHeight = 26;
		List<String> s = this.words;
		int o = 0;
		lines = Lists.newArrayList();
		while (s.size() > o) {
			if (s.get(o).equals("\n")) {
				lines.add("");
			}
			String str = s.get(o++);
			while (o < s.size() && this.minecraft.font.width(str + " " + s.get(o)) < this.width - 20) {
				if (s.get(o).equals("\n")) {
					o++;
					break;
				}
				str = str + " " + s.get(o++);
			}
			lines.add(str);
		}
		if (this.renderInfo) {
			if (lines.size() > 0) {
				lines.set(lines.size() - 1, lines.get(lines.size() - 1) + " ...");
			} else {
				lines = Lists.newArrayList("...");
			}
		}

		int i = this.getScrollbarPosition();
		int j = i + 6;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		this.minecraft.getTextureManager().bind(AbstractGui.BACKGROUND_LOCATION);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32.0F;
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		bufferbuilder.vertex((double) 0, (double) this.bottomHeight, 0.0D)
				.uv((float) 0 / f, (float) (this.bottomHeight + (int) this.getScrollAmount()) / f)
				.color(32, 32, 32, 255).endVertex();
		bufferbuilder.vertex((double) this.width, (double) this.bottomHeight, 0.0D)
				.uv((float) this.width / f, (float) (this.bottomHeight + (int) this.getScrollAmount()) / f)
				.color(32, 32, 32, 255).endVertex();
		bufferbuilder.vertex((double) this.width, (double) this.headerHeight, 0.0D)
				.uv((float) this.width / f, (float) (this.headerHeight + (int) this.getScrollAmount()) / f)
				.color(32, 32, 32, 255).endVertex();
		bufferbuilder.vertex((double) 0, (double) this.headerHeight, 0.0D)
				.uv((float) 0 / f, (float) (this.headerHeight + (int) this.getScrollAmount()) / f)
				.color(32, 32, 32, 255).endVertex();
		tessellator.end();

		int n = 0;
		for (String line : lines) {
			this.minecraft.font.draw(matrixStack, line, 5,
					5 + n++ * (this.minecraft.font.lineHeight + 1) + this.headerHeight - (int) this.getScrollAmount(),
					0xffffff);
		}

		this.minecraft.getTextureManager().bind(AbstractGui.BACKGROUND_LOCATION);
		RenderSystem.enableDepthTest();
		RenderSystem.depthFunc(519);
		int l = -100;
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		bufferbuilder.vertex((double) 0, (double) this.headerHeight, l).uv(0.0F, (float) this.headerHeight / f)
				.color(64, 64, 64, 255).endVertex();
		bufferbuilder.vertex((double) (0 + this.width), (double) this.headerHeight, l)
				.uv((float) this.width / f, (float) this.headerHeight / f).color(64, 64, 64, 255).endVertex();
		bufferbuilder.vertex((double) (0 + this.width), 0.0D, l).uv((float) this.width / f, 0.0F).color(64, 64, 64, 255)
				.endVertex();
		bufferbuilder.vertex((double) 0, 0.0D, l).uv(0.0F, 0.0F).color(64, 64, 64, 255).endVertex();
		bufferbuilder.vertex((double) 0, (double) this.height, l).uv(0.0F, (float) this.height / f)
				.color(64, 64, 64, 255).endVertex();
		bufferbuilder.vertex((double) (0 + this.width), (double) this.height, l)
				.uv((float) this.width / f, (float) this.height / f).color(64, 64, 64, 255).endVertex();
		bufferbuilder.vertex((double) (0 + this.width), (double) this.bottomHeight, l)
				.uv((float) this.width / f, (float) this.bottomHeight / f).color(64, 64, 64, 255).endVertex();
		bufferbuilder.vertex((double) 0, (double) this.bottomHeight, l).uv(0.0F, (float) this.bottomHeight / f)
				.color(64, 64, 64, 255).endVertex();
		tessellator.end();
		RenderSystem.depthFunc(515);
		RenderSystem.disableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO,
				GlStateManager.DestFactor.ONE);
		RenderSystem.disableAlphaTest();
		RenderSystem.shadeModel(7425);
		RenderSystem.disableTexture();
		int m = 4;
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		bufferbuilder.vertex((double) 0, (double) (this.headerHeight + m), 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 0)
				.endVertex();
		bufferbuilder.vertex((double) this.width, (double) (this.headerHeight + m), 0.0D).uv(1.0F, 1.0F)
				.color(0, 0, 0, 0).endVertex();
		bufferbuilder.vertex((double) this.width, (double) this.headerHeight, 0.0D).uv(1.0F, 0.0F).color(0, 0, 0, 255)
				.endVertex();
		bufferbuilder.vertex((double) 0, (double) this.headerHeight, 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 255)
				.endVertex();
		bufferbuilder.vertex((double) 0, (double) this.bottomHeight, 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 255)
				.endVertex();
		bufferbuilder.vertex((double) this.width, (double) this.bottomHeight, 0.0D).uv(1.0F, 1.0F).color(0, 0, 0, 255)
				.endVertex();
		bufferbuilder.vertex((double) this.width, (double) (this.bottomHeight - m), 0.0D).uv(1.0F, 0.0F)
				.color(0, 0, 0, 0).endVertex();
		bufferbuilder.vertex((double) 0, (double) (this.bottomHeight - m), 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 0)
				.endVertex();
		tessellator.end();

		int k1 = this.getMaxScroll();
		if (k1 > 0) {
			RenderSystem.disableTexture();
			int l1 = (int) ((float) ((this.bottomHeight - this.headerHeight) * (this.bottomHeight - this.headerHeight))
					/ (float) this.getMaxPosition());
			l1 = MathHelper.clamp(l1, 32, this.bottomHeight - this.headerHeight - 8);
			int i2 = (int) this.getScrollAmount() * (this.bottomHeight - this.headerHeight - l1) / k1
					+ this.headerHeight;
			if (i2 < this.headerHeight) {
				i2 = this.headerHeight;
			}

			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			bufferbuilder.vertex((double) i, (double) this.bottomHeight, 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 255)
					.endVertex();
			bufferbuilder.vertex((double) j, (double) this.bottomHeight, 0.0D).uv(1.0F, 1.0F).color(0, 0, 0, 255)
					.endVertex();
			bufferbuilder.vertex((double) j, (double) this.headerHeight, 0.0D).uv(1.0F, 0.0F).color(0, 0, 0, 255)
					.endVertex();
			bufferbuilder.vertex((double) i, (double) this.headerHeight, 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 255)
					.endVertex();
			bufferbuilder.vertex((double) i, (double) (i2 + l1), 0.0D).uv(0.0F, 1.0F).color(128, 128, 128, 255)
					.endVertex();
			bufferbuilder.vertex((double) j, (double) (i2 + l1), 0.0D).uv(1.0F, 1.0F).color(128, 128, 128, 255)
					.endVertex();
			bufferbuilder.vertex((double) j, (double) i2, 0.0D).uv(1.0F, 0.0F).color(128, 128, 128, 255).endVertex();
			bufferbuilder.vertex((double) i, (double) i2, 0.0D).uv(0.0F, 0.0F).color(128, 128, 128, 255).endVertex();
			bufferbuilder.vertex((double) i, (double) (i2 + l1 - 1), 0.0D).uv(0.0F, 1.0F).color(192, 192, 192, 255)
					.endVertex();
			bufferbuilder.vertex((double) (j - 1), (double) (i2 + l1 - 1), 0.0D).uv(1.0F, 1.0F)
					.color(192, 192, 192, 255).endVertex();
			bufferbuilder.vertex((double) (j - 1), (double) i2, 0.0D).uv(1.0F, 0.0F).color(192, 192, 192, 255)
					.endVertex();
			bufferbuilder.vertex((double) i, (double) i2, 0.0D).uv(0.0F, 0.0F).color(192, 192, 192, 255).endVertex();
			tessellator.end();
		}

		RenderSystem.enableTexture();
		RenderSystem.shadeModel(7424);
		RenderSystem.enableAlphaTest();
		RenderSystem.disableBlend();

		drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 16777215);
		if (this.renderInfo) {
			ITextComponent info = new TranslationTextComponent(// TRANSLATION
					"Find more structures like this one to unlock the whole story");
			drawCenteredString(matrixStack, this.font, info.getString(), this.width / 2,
					this.height - this.font.lineHeight - 5, 0xffffff);
		}
		super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);
	}

	public double getScrollAmount() {
		return this.scrollAmount;
	}

	public void setScrollAmount(double p_230932_1_) {
		this.scrollAmount = MathHelper.clamp(p_230932_1_, 0.0D, (double) this.getMaxScroll());
	}

	public int getMaxScroll() {
		return Math.max(0, this.getMaxPosition() - (this.bottomHeight - this.headerHeight - 4));
	}

	private int getMaxPosition() {
		return 5 + this.lines.size() * (this.minecraft.font.lineHeight + 1) + this.headerHeight;
	}

	protected int getScrollbarPosition() {
		return this.width - 5;
	}
}