/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.silverminer.shrines.gui.novels;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.silverminer.shrines.packages.PackageManagerProvider;
import com.silverminer.shrines.packages.datacontainer.StructureData;
import com.silverminer.shrines.packages.datacontainer.StructureNovel;
import com.silverminer.shrines.utils.ClientUtils;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class StructureNovelInsightsScreen extends Screen {
    protected static final Logger LOGGER = LogManager.getLogger(StructureNovelInsightsScreen.class);
    private final Screen lastScreen;
    private final List<String> words;
    private double scrollAmount;
    private int bottomHeight;
    private int headerHeight;
    private List<String> lines = Lists.newArrayList();
    private boolean renderInfo = false;
    private boolean scrolling;

    public StructureNovelInsightsScreen(Screen lastScreen, StructureData structure, int unlockedParts) {
        super(new TranslatableComponent("gui.shrines.novel.title", structure.getName()));
        this.lastScreen = lastScreen;
        StructureNovel structureNovel = PackageManagerProvider.CLIENT.getNovelsRegistryData().get(structure.getNovel());
        if(structureNovel != null){
            if (unlockedParts < structureNovel.getParts().size()) {
                this.renderInfo = true;
            }
            this.words = Lists.newArrayList();
            for (int i = 0; i < unlockedParts && i < structureNovel.getParts().size(); i++) {
                String part = structureNovel.getParts().get(i);
                part = new TranslatableComponent(part).getString();// Enable translations of novels even if we don't ship them by default
                String[] wordsInPart = part.replace("\n", " \n ").split(" ");
                this.words.addAll(Arrays.asList(wordsInPart));
            }
        } else {
            this.renderInfo = false;
            String info = "We're sorry, but there is no Novel for this Structure yet :(";
            this.words = Arrays.asList(info.split(" "));
        }
    }

    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
        this.updateScrollingState(p_231044_1_, p_231044_5_);
        return true;
    }

    protected void updateScrollingState(double p_230947_1_, int p_230947_5_) {
        this.scrolling = p_230947_5_ == 0 && p_230947_1_ >= (double) this.getScrollbarPosition()
                && p_230947_1_ < (double) (this.getScrollbarPosition() + 6);
    }

    protected int getScrollbarPosition() {
        return this.width - 5;
    }

    public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
        if (this.getFocused() != null) {
            this.getFocused().mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
        }

        return false;
    }

    public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_,
                                double p_231045_8_) {
        if (super.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_)) {
            return true;
        } else if (p_231045_5_ == 0 && this.scrolling) {
            if (p_231045_3_ < (double) this.headerHeight) {
                this.setScrollAmount(0.0D);
            } else if (p_231045_3_ > (double) this.bottomHeight) {
                this.setScrollAmount(this.getMaxScroll());
            } else {
                double d0 = Math.max(1, this.getMaxScroll());
                int i = this.bottomHeight - this.headerHeight;
                int j = Mth.clamp((int) ((float) (i * i) / (float) this.getMaxPosition()), 32, i - 8);
                double d1 = Math.max(1.0D, d0 / (double) (i - j));
                this.setScrollAmount(this.getScrollAmount() + p_231045_8_ * d1);
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double p_231043_5_) {
        this.setScrollAmount(this.getScrollAmount() - p_231043_5_ * 20.0D);
        return true;
    }

    public double getScrollAmount() {
        return this.scrollAmount;
    }

    public void setScrollAmount(double p_230932_1_) {
        this.scrollAmount = Mth.clamp(p_230932_1_, 0.0D, this.getMaxScroll());
    }

    public int getMaxScroll() {
        return Math.max(0, this.getMaxPosition() - (this.bottomHeight - this.headerHeight - 4));
    }

    private int getMaxPosition() {
        if (this.minecraft == null) {
            return -1;
        }
        return 5 + this.lines.size() * (this.minecraft.font.lineHeight + 1) + this.headerHeight;
    }

    @ParametersAreNonnullByDefault
    public void render(PoseStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        if (this.minecraft == null) {
            return;
        }
        this.bottomHeight = this.renderInfo ? this.height - 20 : this.height;
        this.headerHeight = 26;
        List<String> s = this.words;
        int o = 0;
        lines = Lists.newArrayList();
        while (s.size() > o) {
            if (s.get(o).equals("\n")) {
                lines.add("");
            }
            StringBuilder str = Optional.ofNullable(s.get(o++)).map(StringBuilder::new).orElse(null);
            while (o < s.size() && this.minecraft.font.width(str + " " + s.get(o)) < this.width - 20) {
                if (s.get(o).equals("\n")) {
                    o++;
                    break;
                }
                str = (str == null ? new StringBuilder("null") : str).append(" ").append(s.get(o++));
            }
            lines.add(str == null ? null : str.toString());
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
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, GuiComponent.BACKGROUND_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.vertex(0, this.bottomHeight, 0.0D)
                .uv((float) 0 / f, (float) (this.bottomHeight + (int) this.getScrollAmount()) / f)
                .color(32, 32, 32, 255).endVertex();
        bufferbuilder.vertex(this.width, this.bottomHeight, 0.0D)
                .uv((float) this.width / f, (float) (this.bottomHeight + (int) this.getScrollAmount()) / f)
                .color(32, 32, 32, 255).endVertex();
        bufferbuilder.vertex(this.width, this.headerHeight, 0.0D)
                .uv((float) this.width / f, (float) (this.headerHeight + (int) this.getScrollAmount()) / f)
                .color(32, 32, 32, 255).endVertex();
        bufferbuilder.vertex(0, this.headerHeight, 0.0D)
                .uv((float) 0 / f, (float) (this.headerHeight + (int) this.getScrollAmount()) / f)
                .color(32, 32, 32, 255).endVertex();
        tessellator.end();

        int n = 0;
        for (String line : lines) {
            this.minecraft.font.draw(matrixStack, line, 5,
                    5 + n++ * (this.minecraft.font.lineHeight + 1) + this.headerHeight - (int) this.getScrollAmount(),
                    0xffffff);
        }

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, GuiComponent.BACKGROUND_LOCATION);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(519);
        int l = -100;
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.vertex(0, this.headerHeight, l).uv(0.0F, (float) this.headerHeight / f)
                .color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(this.width, this.headerHeight, l)
                .uv((float) this.width / f, (float) this.headerHeight / f).color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(this.width, 0.0D, l).uv((float) this.width / f, 0.0F).color(64, 64, 64, 255)
                .endVertex();
        bufferbuilder.vertex(0, 0.0D, l).uv(0.0F, 0.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(0, this.height, l).uv(0.0F, (float) this.height / f)
                .color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(this.width, this.height, l)
                .uv((float) this.width / f, (float) this.height / f).color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(this.width, this.bottomHeight, l)
                .uv((float) this.width / f, (float) this.bottomHeight / f).color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(0, this.bottomHeight, l).uv(0.0F, (float) this.bottomHeight / f)
                .color(64, 64, 64, 255).endVertex();
        tessellator.end();
        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO,
                GlStateManager.DestFactor.ONE);
        RenderSystem.disableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        int m = 4;
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(0, this.headerHeight + m, 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 0)
                .endVertex();
        bufferbuilder.vertex(this.width, this.headerHeight + m, 0.0D).uv(1.0F, 1.0F)
                .color(0, 0, 0, 0).endVertex();
        bufferbuilder.vertex(this.width, this.headerHeight, 0.0D).uv(1.0F, 0.0F).color(0, 0, 0, 255)
                .endVertex();
        bufferbuilder.vertex(0, this.headerHeight, 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 255)
                .endVertex();
        bufferbuilder.vertex(0, this.bottomHeight, 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 255)
                .endVertex();
        bufferbuilder.vertex(this.width, this.bottomHeight, 0.0D).uv(1.0F, 1.0F).color(0, 0, 0, 255)
                .endVertex();
        bufferbuilder.vertex(this.width, this.bottomHeight - m, 0.0D).uv(1.0F, 0.0F)
                .color(0, 0, 0, 0).endVertex();
        bufferbuilder.vertex(0, this.bottomHeight - m, 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 0)
                .endVertex();
        tessellator.end();

        int k1 = this.getMaxScroll();
        if (k1 > 0) {
            RenderSystem.disableTexture();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            int l1 = (int) ((float) ((this.bottomHeight - this.headerHeight) * (this.bottomHeight - this.headerHeight))
                    / (float) this.getMaxPosition());
            l1 = Mth.clamp(l1, 32, this.bottomHeight - this.headerHeight - 8);
            int i2 = (int) this.getScrollAmount() * (this.bottomHeight - this.headerHeight - l1) / k1
                    + this.headerHeight;
            if (i2 < this.headerHeight) {
                i2 = this.headerHeight;
            }

            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            bufferbuilder.vertex(i, this.bottomHeight, 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 255)
                    .endVertex();
            bufferbuilder.vertex(j, this.bottomHeight, 0.0D).uv(1.0F, 1.0F).color(0, 0, 0, 255)
                    .endVertex();
            bufferbuilder.vertex(j, this.headerHeight, 0.0D).uv(1.0F, 0.0F).color(0, 0, 0, 255)
                    .endVertex();
            bufferbuilder.vertex(i, this.headerHeight, 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 255)
                    .endVertex();
            bufferbuilder.vertex(i, i2 + l1, 0.0D).uv(0.0F, 1.0F).color(128, 128, 128, 255)
                    .endVertex();
            bufferbuilder.vertex(j, i2 + l1, 0.0D).uv(1.0F, 1.0F).color(128, 128, 128, 255)
                    .endVertex();
            bufferbuilder.vertex(j, i2, 0.0D).uv(1.0F, 0.0F).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex(i, i2, 0.0D).uv(0.0F, 0.0F).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex(i, i2 + l1 - 1, 0.0D).uv(0.0F, 1.0F).color(192, 192, 192, 255)
                    .endVertex();
            bufferbuilder.vertex(j - 1, i2 + l1 - 1, 0.0D).uv(1.0F, 1.0F)
                    .color(192, 192, 192, 255).endVertex();
            bufferbuilder.vertex(j - 1, i2, 0.0D).uv(1.0F, 0.0F).color(192, 192, 192, 255)
                    .endVertex();
            bufferbuilder.vertex(i, i2, 0.0D).uv(0.0F, 0.0F).color(192, 192, 192, 255).endVertex();
            tessellator.end();
        }

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 16777215);
        if (this.renderInfo) {
            Component info = new TranslatableComponent(
                    "gui.shrines.novels.find_more");
            drawCenteredString(matrixStack, this.font, info.getString(), this.width / 2,
                    this.height - this.font.lineHeight - 5, 0xffffff);
        }
        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);
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

    public void onClose() {
        if (this.minecraft == null) {
            return;
        }
        this.minecraft.setScreen(this.lastScreen);
    }

    protected void init() {
        this.addRenderableWidget(new ImageButton(2, 2, 91, 20, 0, 0, 20,
                ClientUtils.BACK_BUTTON_TEXTURE, 256, 256, (button) -> this.onClose(), TextComponent.EMPTY));
    }

    private void scroll(int p_230937_1_) {
        this.setScrollAmount(this.getScrollAmount() + (double) p_230937_1_);
    }
}