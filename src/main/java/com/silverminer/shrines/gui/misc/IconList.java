package com.silverminer.shrines.gui.misc;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.*;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class IconList<E extends IconList.AbstractListEntry<E>> extends AbstractContainerEventHandler implements Widget, NarratableEntry {
    protected static final Logger LOGGER = LogManager.getLogger(IconList.class);
    protected final Minecraft minecraft;
    protected int itemSize;
    @SuppressWarnings({"unchecked", "rawtypes"})
    private final List<E> children = new IconList.SimpleArrayList();
    protected int width;
    protected int height;
    protected int y0;
    protected int y1;
    protected int x1;
    protected int x0;
    protected int elementsPerRow;
    private double scrollAmount;
    private boolean scrolling;
    private E selected;
    private boolean renderTopAndBottom = true;

    public IconList(Minecraft minecraft, int width, int height, int headerHeight, int footerStart, int itemSize) {
        this.minecraft = minecraft;
        this.width = width;
        this.height = height;
        this.y0 = headerHeight;
        this.y1 = footerStart;
        this.itemSize = itemSize;
        this.x0 = 0;
        this.x1 = width;
        this.elementsPerRow = (this.width - 10) / (this.itemSize + 4);
    }

    public void setHeaderSize(int size) {
        this.y0 = size;
    }

    public void setEntrySize(int size) {
        this.itemSize = size;
        this.elementsPerRow = (this.width - 10) / (this.itemSize + 4);
    }

    public int getEntrySize() {
        return this.itemSize;
    }

    @Nullable
    public E getSelected() {
        return this.selected;
    }

    public void setSelected(@Nullable E p_241215_1_) {
        this.selected = p_241215_1_;
        this.refreshSelection();
    }

    public void setRenderTopAndBottom(boolean p_244606_1_) {
        this.renderTopAndBottom = p_244606_1_;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public E getFocused() {
        return (E) (super.getFocused());
    }

    public final List<E> children() {
        return this.children;
    }

    protected final void clearEntries() {
        this.children.clear();
    }

    protected void replaceEntries(Collection<E> p_230942_1_) {
        this.children.clear();
        this.children.addAll(p_230942_1_);
    }

    protected E getEntry(int p_230953_1_) {
        return this.children().get(p_230953_1_);
    }

    protected int addEntry(E p_230513_1_) {
        this.children.add(p_230513_1_);
        return this.children.size() - 1;
    }

    protected int getItemCount() {
        return this.children().size();
    }

    protected boolean isSelectedItem(int p_230957_1_) {
        return Objects.equals(this.getSelected(), this.children().get(p_230957_1_));
    }

    @Nullable
    protected final E getEntryAtPosition(double x, double y) {
        double xPos = Math.floor(x / (this.getEntrySize() + 4));
        double yPos = Math.floor((y + this.getScrollAmount() - this.y0) / (this.getEntrySize() + 4));
        int element = (int) (this.elementsPerRow * yPos + xPos);
        return element >= this.getItemCount() ? null : this.getEntry(element);
    }

    public void updateSize(int p_230940_1_, int p_230940_2_, int p_230940_3_, int p_230940_4_) {
        this.width = p_230940_1_;
        this.height = p_230940_2_;
        this.y0 = p_230940_3_;
        this.y1 = p_230940_4_;
        this.x0 = 0;
        this.x1 = p_230940_1_;
    }

    public void setLeftPos(int p_230959_1_) {
        this.x0 = p_230959_1_;
        this.x1 = p_230959_1_ + this.width;
    }

    protected int getMaxPosition() {
        int i = this.getItemCount();
        int row = i / this.elementsPerRow;
        if (i % this.elementsPerRow != 0) {
            row++;
        }
        return row * (this.itemSize + 4);
    }

    public void render(PoseStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        int i = this.getScrollbarPosition();
        int j = i + 6;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, GuiComponent.BACKGROUND_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.vertex((double) this.x0, (double) this.y1, 0.0D)
                .uv((float) this.x0 / f, (float) (this.y1 + (int) this.getScrollAmount()) / f).color(32, 32, 32, 255)
                .endVertex();
        bufferbuilder.vertex((double) this.x1, (double) this.y1, 0.0D)
                .uv((float) this.x1 / f, (float) (this.y1 + (int) this.getScrollAmount()) / f).color(32, 32, 32, 255)
                .endVertex();
        bufferbuilder.vertex((double) this.x1, (double) this.y0, 0.0D)
                .uv((float) this.x1 / f, (float) (this.y0 + (int) this.getScrollAmount()) / f).color(32, 32, 32, 255)
                .endVertex();
        bufferbuilder.vertex((double) this.x0, (double) this.y0, 0.0D)
                .uv((float) this.x0 / f, (float) (this.y0 + (int) this.getScrollAmount()) / f).color(32, 32, 32, 255)
                .endVertex();
        tesselator.end();

        int k = this.y0 + 4 - (int) this.getScrollAmount();

        this.renderList(p_230430_1_, k, p_230430_2_, p_230430_3_, p_230430_4_);
        if (this.renderTopAndBottom) {
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            RenderSystem.setShaderTexture(0, GuiComponent.BACKGROUND_LOCATION);
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(519);
            int l = -100;
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.vertex((double) this.x0, (double) this.y0, l).uv(0.0F, (float) this.y0 / f)
                    .color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex((double) (this.x0 + this.width), (double) this.y0, l)
                    .uv((float) this.width / f, (float) this.y0 / f).color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex((double) (this.x0 + this.width), 0.0D, l).uv((float) this.width / f, 0.0F)
                    .color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex((double) this.x0, 0.0D, l).uv(0.0F, 0.0F).color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex((double) this.x0, (double) this.height, l).uv(0.0F, (float) this.height / f)
                    .color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex((double) (this.x0 + this.width), (double) this.height, l)
                    .uv((float) this.width / f, (float) this.height / f).color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex((double) (this.x0 + this.width), (double) this.y1, l)
                    .uv((float) this.width / f, (float) this.y1 / f).color(64, 64, 64, 255).endVertex();
            bufferbuilder.vertex((double) this.x0, (double) this.y1, l).uv(0.0F, (float) this.y1 / f)
                    .color(64, 64, 64, 255).endVertex();
            tesselator.end();
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
            bufferbuilder.vertex((double) this.x0, (double) (this.y0 + m), 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 0)
                    .endVertex();
            bufferbuilder.vertex((double) this.x1, (double) (this.y0 + m), 0.0D).uv(1.0F, 1.0F).color(0, 0, 0, 0)
                    .endVertex();
            bufferbuilder.vertex((double) this.x1, (double) this.y0, 0.0D).uv(1.0F, 0.0F).color(0, 0, 0, 255)
                    .endVertex();
            bufferbuilder.vertex((double) this.x0, (double) this.y0, 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 255)
                    .endVertex();
            bufferbuilder.vertex((double) this.x0, (double) this.y1, 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 255)
                    .endVertex();
            bufferbuilder.vertex((double) this.x1, (double) this.y1, 0.0D).uv(1.0F, 1.0F).color(0, 0, 0, 255)
                    .endVertex();
            bufferbuilder.vertex((double) this.x1, (double) (this.y1 - m), 0.0D).uv(1.0F, 0.0F).color(0, 0, 0, 0)
                    .endVertex();
            bufferbuilder.vertex((double) this.x0, (double) (this.y1 - m), 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 0)
                    .endVertex();
            tesselator.end();
        }

        int k1 = this.getMaxScroll();
        if (k1 > 0) {
            RenderSystem.disableTexture();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            int l1 = (int) ((float) ((this.y1 - this.y0) * (this.y1 - this.y0)) / (float) this.getMaxPosition());
            l1 = Mth.clamp(l1, 32, this.y1 - this.y0 - 8);
            int i2 = (int) this.getScrollAmount() * (this.y1 - this.y0 - l1) / k1 + this.y0;
            if (i2 < this.y0) {
                i2 = this.y0;
            }

            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            bufferbuilder.vertex((double) i, (double) this.y1, 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex((double) j, (double) this.y1, 0.0D).uv(1.0F, 1.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex((double) j, (double) this.y0, 0.0D).uv(1.0F, 0.0F).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex((double) i, (double) this.y0, 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 255).endVertex();
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
            tesselator.end();
        }
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    protected void centerScrollOn(E p_230951_1_) {
        this.setScrollAmount((double) (this.children().indexOf(p_230951_1_) * this.itemSize + this.itemSize / 2
                - (this.y1 - this.y0) / 2));
    }

    protected void ensureVisible(E item) {
        int i = this.getEntryTop(this.children().indexOf(item));
        int j = i - this.y0 - 4 + this.getEntrySize();
        if (j < 0) {
            this.scroll(j);
        }

        int k = this.y1 - i - this.itemSize - this.itemSize;
        if (k < 0) {
            this.scroll(-k);
        }

    }

    private void scroll(int amount) {
        this.setScrollAmount(this.getScrollAmount() + (double) amount);
    }

    public double getScrollAmount() {
        return this.scrollAmount;
    }

    public void setScrollAmount(double p_230932_1_) {
        this.scrollAmount = Mth.clamp(p_230932_1_, 0.0D, (double) this.getMaxScroll());
    }

    public int getMaxScroll() {
        return Math.max(0, this.getMaxPosition() - (this.y1 - this.y0 - 4));
    }

    protected void updateScrollingState(double mouseX, double mouseY, int scrolledAmout) {
        this.scrolling = scrolledAmout == 0 && mouseX >= (double) this.getScrollbarPosition()
                && mouseX < (double) (this.getScrollbarPosition() + 6);
    }

    protected int getScrollbarPosition() {
        return this.width - 5;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmout) {
        this.updateScrollingState(mouseX, mouseY, scrolledAmout);
        if (!this.isMouseOver(mouseX, mouseY)) {
            return false;
        } else {
            E e = this.getEntryAtPosition(mouseX, mouseY);
            if (e != null) {
                if (e.mouseClicked(mouseX, mouseY, scrolledAmout)) {
                    this.setFocused(e);
                    this.setDragging(true);
                    return true;
                }
            } else if (scrolledAmout == 0) {
                return true;
            }

            return this.scrolling;
        }
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
            if (p_231045_3_ < (double) this.y0) {
                this.setScrollAmount(0.0D);
            } else if (p_231045_3_ > (double) this.y1) {
                this.setScrollAmount((double) this.getMaxScroll());
            } else {
                double d0 = (double) Math.max(1, this.getMaxScroll());
                int i = this.y1 - this.y0;
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
        this.setScrollAmount(this.getScrollAmount() - p_231043_5_ * (double) this.itemSize / 2.0D);
        return true;
    }

    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        if (super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_)) {
            return true;
        } else if (p_231046_1_ == 264) {
            this.moveSelection(IconList.Ordering.DOWN);
            return true;
        } else if (p_231046_1_ == 265) {
            this.moveSelection(IconList.Ordering.UP);
            return true;
        } else {
            return false;
        }
    }

    protected void moveSelection(IconList.Ordering p_241219_1_) {
        this.moveSelection(p_241219_1_, (p_241573_0_) -> {
            return true;
        });
    }

    protected void refreshSelection() {
        E e = this.getSelected();
        if (e != null) {
            this.ensureVisible(e);
        }

    }

    protected void moveSelection(IconList.Ordering p_241572_1_, Predicate<E> p_241572_2_) {
        int i = p_241572_1_ == IconList.Ordering.UP ? -1 : 1;
        if (!this.children().isEmpty()) {
            int j = this.children().indexOf(this.getSelected());

            while (true) {
                int k = Mth.clamp(j + i, 0, this.getItemCount() - 1);
                if (j == k) {
                    break;
                }

                E e = this.children().get(k);
                if (p_241572_2_.test(e)) {
                    this.setSelected(e);
                    this.ensureVisible(e);
                    break;
                }

                j = k;
            }
        }

    }

    public boolean isMouseOver(double p_231047_1_, double p_231047_3_) {
        return p_231047_3_ >= (double) this.y0 && p_231047_3_ <= (double) this.y1 && p_231047_1_ >= (double) this.x0
                && p_231047_1_ <= (double) this.x1;
    }

    protected void renderList(PoseStack matrixStack, int scrollAmount, int mouseX, int mouseY, float p_238478_6_) {
        int i = this.getItemCount();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        for (int j = 0; j < i; ++j) {
            int y1 = this.getEntryTop(j);
            int y2 = y1 + this.itemSize + 4;
            E e = this.getEntry(j);
            int x1 = this.getEntryLeft(j);
            int x2 = this.getEntryRight(j);
            if (this.isSelectedItem(j)) {
                int x4 = this.getEntryLeft(j) + this.itemSize + 4;
                int y4 = this.getEntryTop(j) + this.itemSize + 2;
                int x3 = x4 + this.itemSize + 4;
                int y3 = y4 + this.itemSize + 4;
                RenderSystem.disableTexture();
                RenderSystem.setShader(GameRenderer::getPositionShader);
                float f = this.isFocused() ? 1.0F : 0.5F;
                RenderSystem.setShaderColor(f, f, f, 1.0F);
                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
                bufferbuilder.vertex(x3, y4, 0.0D).endVertex();
                bufferbuilder.vertex(x4, y4, 0.0D).endVertex();
                bufferbuilder.vertex(x4, y3, 0.0D).endVertex();
                bufferbuilder.vertex(x3, y3, 0.0D).endVertex();
                tesselator.end();
                RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
                bufferbuilder.vertex(x3 - 1, y4 + 1, 0.0D).endVertex();
                bufferbuilder.vertex(x4 + 1, y4 + 1, 0.0D).endVertex();
                bufferbuilder.vertex(x4 + 1, y3 - 1, 0.0D).endVertex();
                bufferbuilder.vertex(x3 - 1, y3 - 1, 0.0D).endVertex();
                tesselator.end();
                RenderSystem.enableTexture();
            }

            e.render(matrixStack, x1, y2, x2, y1, this.itemSize, mouseX, mouseY,
                    this.isMouseOver(mouseX, mouseY)
                            && Objects.equals(this.getEntryAtPosition(mouseX, mouseY), e),
                    p_238478_6_, this.itemSize);
        }

    }

    public int getEntryLeft(int i) {
        int xPos = i % this.elementsPerRow - 1;
        return xPos * (this.getEntrySize() + 4) + 2;
    }

    public int getEntryRight(int i) {
        return this.getEntryLeft(i) + this.getEntrySize() + 6;
    }

    protected int getEntryTop(int i) {
        i = i - i % this.elementsPerRow;
        int row = i / this.elementsPerRow - 1;
        return (int) ((this.itemSize + 4) * row - this.getScrollAmount() + this.y0 + 4);
    }

    protected boolean isFocused() {
        return false;
    }

    protected E remove(int p_230964_1_) {
        E e = this.children.get(p_230964_1_);
        return (E) (this.removeEntry(this.children.get(p_230964_1_)) ? e : null);
    }

    protected boolean removeEntry(E p_230956_1_) {
        boolean flag = this.children.remove(p_230956_1_);
        if (flag && p_230956_1_ == this.getSelected()) {
            this.setSelected((E) null);
        }

        return flag;
    }

    private void bindEntryToSelf(IconList.AbstractListEntry<E> p_238480_1_) {
        p_238480_1_.list = this;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTop() {
        return this.y0;
    }

    public int getBottom() {
        return this.y1;
    }

    public int getLeft() {
        return this.x0;
    }

    public int getRight() {
        return this.x1;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class AbstractListEntry<E extends IconList.AbstractListEntry<E>>
            implements GuiEventListener {
        @Deprecated
        protected IconList<E> list;

        public abstract void render(PoseStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_,
                                    int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_,
                                    float p_230432_10_, int itemSize);

        public boolean isMouseOver(double p_231047_1_, double p_231047_3_) {
            return Objects.equals(this.list.getEntryAtPosition(p_231047_1_, p_231047_3_), this);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static enum Ordering {
        UP, DOWN;
    }

    @OnlyIn(Dist.CLIENT)
    class SimpleArrayList extends java.util.AbstractList<E> {
        private final List<E> delegate = Lists.newArrayList();

        private SimpleArrayList() {
        }

        public E get(int p_get_1_) {
            return this.delegate.get(p_get_1_);
        }

        public int size() {
            return this.delegate.size();
        }

        public E set(int p_set_1_, E p_set_2_) {
            E e = this.delegate.set(p_set_1_, p_set_2_);
            IconList.this.bindEntryToSelf(p_set_2_);
            return e;
        }

        public void add(int p_add_1_, E p_add_2_) {
            this.delegate.add(p_add_1_, p_add_2_);
            IconList.this.bindEntryToSelf(p_add_2_);
        }

        public E remove(int p_remove_1_) {
            return this.delegate.remove(p_remove_1_);
        }
    }
}