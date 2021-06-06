package com.silverminer.shrines.client.gui.config.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CheckboxButtonEx extends AbstractButton
{
	public boolean value;

	private static final ResourceLocation CHECKBOX_TEXTURE = new ResourceLocation("textures/gui/checkbox.png");

	public CheckboxButtonEx(int x, int y, int width, int height, ITextComponent message, boolean initialValue)
	{
		super(x, y, width, height, message);

		this.value = initialValue;
	}

	@Override
	public void onPress() {
		this.value = !this.value;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks)
	{
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bind(CHECKBOX_TEXTURE);
		RenderSystem.enableDepthTest();
		FontRenderer fontrenderer = minecraft.font;
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		blit(ms, this.x, this.y, 0.0F, this.value ? 20.0F : 0.0F, 10, this.height, 32, 64);
		this.renderBg(ms, minecraft, mouseX, mouseY);
		AbstractGui.drawString(ms, fontrenderer, this.getMessage(), this.x + 24, this.y + (this.height - 8) / 2, 14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
	}

	public boolean getValue() {
		return this.value;
	}
}
